package com.lllic.immersion


import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.view.*
import androidx.core.view.ViewCompat

/**
 * Created by Lic.
 * Date: 2020/1/20 13:45
 * Info:
 */

fun Window.immersive(statusBarAlpha: Int = 0, navigationBarAlpha: Int = 0) {
    immersiveStatusBar(statusBarAlpha)
    immersiveNavigationBar(navigationBarAlpha)
}

fun Window.immersiveStatusBar(alpha: Int = 0) {
    setStatusBarTranslucent(alpha)
    overlayStatusBar()
}

fun Window.immersiveNavigationBar(alpha: Int = 0) {
    setNavigationBarTranslucent(this, alpha)
}

fun Window.setStatusBarTranslucent(alpha: Int = 0) {
    colorStatusBar(Color.argb(alpha, 0, 0, 0))
}

fun Window.colorStatusBar(color: Int) {
    statusBarColor = color
}

/**
 * 调整控件高度和PaddingTop，适应状态栏
 * @param overlay 是否将控件延伸到状态栏 充当状态栏背景色(true:同时添加高度和paddingTop; false: 只添加marginTop)
 * @param consume 是否消费掉insetTop
 */
fun View.fitTopInset(overlay: Boolean = false, consume: Boolean = false) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val oldTopInset = getTag(R.id.window_inset_manager_inset_top) as Int? ?: 0
        if (oldTopInset != insets.systemWindowInsetTop) {
            val params: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
            val offsetTopInset = insets.systemWindowInsetTop - oldTopInset
            if (overlay) {
                if (params.height >= 0) params.height = params.height + offsetTopInset
                setPadding(paddingLeft, paddingTop + offsetTopInset, paddingRight, paddingBottom)
            } else {
                params.topMargin = params.topMargin + offsetTopInset
            }
            v.layoutParams = params
            requestLayout()
            setTag(R.id.window_inset_manager_inset_top, insets.systemWindowInsetTop)
            return@setOnApplyWindowInsetsListener if (consume) {
                insets.replaceSystemWindowInsets(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
            } else {
                insets
            }
        } else {
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

fun Window.addInsetListener(
    topInsetListener: ((topInset: Int) -> Unit)? = null,
    bottomInsetListener: ((bottomInset: Int) -> Unit)? = null
) {
    val decorView = decorView as ViewGroup
    val insetListenerView = View(context)
    ViewCompat.setOnApplyWindowInsetsListener(insetListenerView) { v, insets ->
        topInsetListener?.invoke(insets.systemWindowInsetTop)
        bottomInsetListener?.invoke(insets.systemWindowInsetBottom)
        insets
    }
    decorView.addView(insetListenerView, 0, ViewGroup.LayoutParams(0, 0))
}

/**
 * 响应虚拟导航栏和键盘变化
 * @param overlay 是否将控件延伸到（虚拟导航栏和键盘） 充当占位背景色
 * @param consumeTop 是否启用过渡动画
 * @param animDuration 是过渡动画时间
 */
fun View.fitBottomInset(
    overlay: Boolean = false,
    enableAnim: Boolean = true,
    animDuration: Long = 150L,
    bottomInsetListener: ((bottomInsetHeight: Int) -> Unit)? = null
) {
    setOnApplyWindowInsetsListener { v, insets ->
        val oldBottomInset = getTag(R.id.window_inset_manager_inset_bottom) as Int? ?: 0
        if (oldBottomInset != insets.systemWindowInsetBottom) {
            val offsetBottomInset = insets.systemWindowInsetBottom - oldBottomInset
            val params: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
            if (enableAnim) {
                ValueAnimator().apply {
                    setIntValues(0, offsetBottomInset)
                    duration = animDuration
                    addUpdateListener {
                        updateView(overlay, it.animatedValue as Int, params)
                    }
                }
            } else {
                updateView(overlay, offsetBottomInset, params)
            }
            bottomInsetListener?.invoke(insets.systemWindowInsetBottom)
            setTag(R.id.window_inset_manager_inset_bottom, insets.systemWindowInsetBottom)
        }
        return@setOnApplyWindowInsetsListener insets.replaceSystemWindowInsets(
            insets.systemWindowInsetLeft,
            insets.systemWindowInsetTop,
            insets.systemWindowInsetRight,
            0
        )
    }
}


/**
 * 同一个控件同时响应topInset和bottomInset
 * 通常只给根布局设置
 */
fun View.fitRootInset() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val oldTopInset = getTag(R.id.window_inset_manager_inset_top) as Int? ?: 0
        val oldBottomInset = getTag(R.id.window_inset_manager_inset_bottom) as Int? ?: 0
        if (oldTopInset != insets.systemWindowInsetTop || oldBottomInset != insets.systemWindowInsetBottom) {
            val offsetTopInset = insets.systemWindowInsetTop - oldTopInset
            val offsetBottomInset = insets.systemWindowInsetBottom - oldBottomInset
            setPadding(paddingLeft, offsetTopInset, paddingRight, offsetBottomInset)
            setTag(R.id.window_inset_manager_inset_top, insets.systemWindowInsetTop)
            setTag(R.id.window_inset_manager_inset_bottom, insets.systemWindowInsetBottom)
            return@setOnApplyWindowInsetsListener insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                0,
                insets.systemWindowInsetRight,
                0
            )
        } else {
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

/**
 * 不消费WindowInset
 * CollapsingToolbarLayout 等
 * 某些View会自动消费掉inset，导致后面的view无法正确的获取到inset
 */
fun View.unconsumeWindowInset() {
    setOnApplyWindowInsetsListener { v, insets -> insets }
}

private fun View.updateView(overlay: Boolean, offsetBottomInset: Int, params: ViewGroup.MarginLayoutParams) {
    if (overlay) {
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + offsetBottomInset)
        params.height = params.height + offsetBottomInset
    } else {
        params.bottomMargin = params.bottomMargin + offsetBottomInset
    }
    requestLayout()
}

private fun setNavigationBarTranslucent(window: Window, alpha: Int) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    window.navigationBarColor = Color.argb(alpha, 0, 0, 0)
}

/**
 * 根布局延伸到状态栏
 */
private fun Window.overlayStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        //刘海屏允许内容绘制到刘海区域
        attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
    decorView.fitsSystemWindows = false
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}


fun Window.setStatusBarThem(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = decorView
        decorView.systemUiVisibility = if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
    }
}
