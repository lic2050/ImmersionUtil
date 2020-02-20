package com.lllic.immersion

import android.graphics.Color
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window


/**
 * Created by Lic.
 * Date: 2020/1/10 14:56
 * Info: 沉浸式UI管理工具
 */
fun Window.immersion(statusBarAlpha: Int = 0, navigationBarAlpha: Int = 0) {
    immersionStatusBar(statusBarAlpha)
    immersionNavigationBar(navigationBarAlpha)
}

fun Window.immersionStatusBar(alpha: Int = 0) {
    setStatusBarTranslucent(this, alpha)
}

fun Window.immersionNavigationBar(alpha: Int = 0) {
    setNavigationBarTranslucent(this, alpha)
}

/**
 * View 添加StatusBar高度的topMargin
 * 适用于背景图片延伸到状态栏，Toolbar在状态栏下方这类场景 toolbar.fitStatusBar(window)
 */
fun View.fitStatusBar() {
    (layoutParams as? MarginLayoutParams)?.let { layoutParams ->
        val offsetMargin = layoutParams.topMargin
        // todo 使用PopupWindow监听systemWindowInsetBottom 解决adjustResize无效bug
        setOnApplyWindowInsetsListener { _, insets ->
            layoutParams.topMargin = offsetMargin + insets.systemWindowInsetTop
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

/**
 * View 同时添加StatusBar高度的height和paddingTop
 * 适用于控件延申到状态栏并且高度固定，但是内容从状态栏下方开始绘制的场景
 */
fun View.fitStatusBarOverlay() {
    (layoutParams as? MarginLayoutParams)?.let { layoutParams ->
        val offsetHeight = layoutParams.height
        val offsetPadding = paddingTop
        setOnApplyWindowInsetsListener { _, insets ->
            layoutParams.height = offsetHeight + insets.systemWindowInsetTop
            setPaddingRelative(
                paddingStart,
                offsetPadding + insets.systemWindowInsetTop,
                paddingEnd,
                paddingBottom
            )
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

fun View.fitNavigationBar() {
    (layoutParams as? MarginLayoutParams)?.let { layoutParams ->
        val offsetMargin = layoutParams.bottomMargin
        setOnApplyWindowInsetsListener { _, insets ->
            layoutParams.bottomMargin = offsetMargin + insets.systemWindowInsetBottom
            return@setOnApplyWindowInsetsListener insets
        }
    }
}

/**
 * 设置状态栏半透明度
 *
 * @param window
 */
private fun setStatusBarTranslucent(window: Window, alpha: Int = 0) {
    window.statusBarColor = Color.argb(alpha, 0, 0, 0)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

/**
 * 设置导航栏半透明度
 *
 * @param window
 */
private fun setNavigationBarTranslucent(window: Window, alpha: Int) {
    window.navigationBarColor = Color.argb(alpha, 0, 0, 0)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
}