package com.lllic.immersion

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.core.view.ViewCompat


/**
 * Created by Lic.
 * Date: 2020/1/10 14:56
 * Info: 沉浸式UI管理工具
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

/**
 * 设置状态栏半透明度
 *
 * @param window
 */
private fun Window.setStatusBarTranslucent(alpha: Int = 0) {
    colorStatusBar(Color.argb(alpha, 0, 0, 0))
}

/**
 * 设置状态栏半透明度
 *
 * @param window
 */
fun Window.colorStatusBar(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val decorViewGroup: ViewGroup = decorView as ViewGroup
        var statusBarView: View? = decorViewGroup.findViewWithTag("custom_status_bar_tag")
        if (statusBarView == null) {
            statusBarView = View(context)
            statusBarView.tag = "custom_status_bar_tag"
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(context)
            )
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            decorViewGroup.addView(statusBarView)
        }
        statusBarView.setBackgroundColor(color)
    }
}


/**
 * 根布局延申到状态栏
 */
fun Window.overlayStatusBar(overlay: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (overlay) {
            decorView.setOnApplyWindowInsetsListener { v, insets ->
                val defaultInsets = v.onApplyWindowInsets(insets)
                return@setOnApplyWindowInsetsListener defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.systemWindowInsetLeft,
                    0,
                    defaultInsets.systemWindowInsetRight,
                    defaultInsets.systemWindowInsetBottom
                )
            }
        } else {
            decorView.setOnApplyWindowInsetsListener(null)
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (overlay) {
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        ViewCompat.requestApplyInsets(decorView)
    }
}

/**
 * View 添加StatusBar高度的topMargin
 * 适用于背景图片延伸到状态栏，Toolbar在状态栏下方这类场景 toolbar.fitStatusBar(window)
 */
fun View.fitStatusBar() {
    val params: MarginLayoutParams = layoutParams as MarginLayoutParams
    params.topMargin = params.topMargin + getStatusBarHeight(context)
}

/**
 * View 同时添加StatusBar高度的height和paddingTop
 * 适用于控件(ToolBar)延申到状态栏
 */
fun View.fitStatusBarOverlay() {
    val params: MarginLayoutParams = layoutParams as MarginLayoutParams
    params.height = params.height + getStatusBarHeight(context)
    setPadding(paddingLeft, paddingTop + getStatusBarHeight(context), paddingRight, paddingBottom)
}

/**
 * 设置导航栏半透明度
 * 根布局延申到导航栏下面
 * @param window
 */
private fun setNavigationBarTranslucent(window: Window, alpha: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.navigationBarColor = Color.argb(alpha, 0, 0, 0)
    } else {
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}

/**
 * 自适应导航栏高度，给控件添加导航栏高度的bottomMargin
 * 高度会随导航栏显示隐藏自动改变
 * 同时能响应键盘显示隐藏，adjustResize模式也会正常工作
 */
fun View.fitNavigationBar() {
    val offset = paddingBottom
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        setOnApplyWindowInsetsListener { _, insets ->
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                offset + insets.systemWindowInsetBottom
            )
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

/**
 * 修复adjustResize失效
 */
fun View.fitAdjustResize() {
    fitNavigationBar()
}

/**
 * 设置状态黑色样式
 */
fun Window.setStatusBarThem(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = decorView
        decorView.systemUiVisibility = if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
    }
}

private fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight: Int = 0
    //获取status_bar_height资源的ID
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        //根据资源ID获取响应的尺寸值
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}
