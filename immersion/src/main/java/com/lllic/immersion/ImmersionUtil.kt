package com.lllic.immersion

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi


/**
 * Created by Lic.
 * Date: 2020/1/10 14:56
 * Info: 沉浸式UI管理工具
 */

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.immersive(statusBarAlpha: Int = 0, navigationBarAlpha: Int = 0) {
    immersiveStatusBar(statusBarAlpha)
    immersiveNavigationBar(navigationBarAlpha)
}

/**
* 透明状态栏，并将跟布局延申到状态栏下
*/
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.immersiveStatusBar(alpha: Int = 0) {
    setStatusBarTranslucent(alpha)
    overlayStatusBar()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.immersiveNavigationBar(alpha: Int = 0) {
    setNavigationBarTranslucent(this, alpha)
}

/**
* 设置状态栏透明度
*/
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun Window.setStatusBarTranslucent(alpha: Int = 0) {
    colorStatusBar(Color.argb(alpha, 0, 0, 0))
}

/**
* 设置状态栏颜色
*/
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.colorStatusBar(color: Int) {
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = color

}

/**
* 将跟布局延申到状态栏下
*/
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Window.overlayStatusBar(overlay: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        //刘海屏允许内容绘制到刘海区域
        attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
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
}

/**
* 将view添加状态栏高度的topMargin
*/
fun View.fitStatusBar() {
    val params: MarginLayoutParams = layoutParams as MarginLayoutParams
    params.topMargin = params.topMargin + getStatusBarHeight()
}

/**
* 将view的增加状态栏高度的 height和paddingTop
*/
fun View.fitStatusBarOverlay() {
    val params: MarginLayoutParams = layoutParams as MarginLayoutParams
    if (params.height == 0) {
        post {
            params.height = params.height + getStatusBarHeight()
            setPadding(
                    paddingLeft,
                    paddingTop + getStatusBarHeight(),
                    paddingRight,
                    paddingBottom
            )
        }
    } else {
        params.height = params.height + getStatusBarHeight()
        setPadding(
                paddingLeft,
                paddingTop + getStatusBarHeight(),
                paddingRight,
                paddingBottom
        )
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun setNavigationBarTranslucent(window: Window, alpha: Int) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    window.navigationBarColor = Color.argb(alpha, 0, 0, 0)
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.fitNavigationBar() {
    val offset = paddingBottom
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

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.fitAdjustResize() {
    fitNavigationBar()
}

fun Window.setStatusBarThem(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = decorView
        decorView.systemUiVisibility = if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
    }
}

fun getStatusBarHeight(): Int {
    var statusBarHeight = 0
    // 获得状态栏高度
    val resources = Resources.getSystem()
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        //根据资源ID获取响应的尺寸值
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}
