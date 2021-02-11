package com.lllic.immersionutil

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class AActivity : AppCompatActivity() {
    private var insetTop = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.immersiveStatusBar(50)
//        WindowInsetsManager(this)
        setContentView(R.layout.activity_main)
//        addWindowInsetListener()
//        bt.setOnClickListener {
//            Dialog(this).apply {
//                setContentView(R.layout.dialog_main)
//            }.show()
//        }
    }


    private fun addWindowInsetListener() {
        tvHeader.setOnApplyWindowInsetsListener { v, insets ->
//            val defaultInsets = v.onApplyWindowInsets(insets)
            val params: ViewGroup.MarginLayoutParams = tvHeader.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = params.topMargin - insetTop + insets.systemWindowInsetTop
            insetTop = insets.systemWindowInsetTop
//            tvHeader.tag
//            val params: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
//            if (params.height == 0) {
//                post {
//                    params.height = params.height + getStatusBarHeight()
//                    setPadding(
//                        paddingLeft,
//                        paddingTop + getStatusBarHeight(),
//                        paddingRight,
//                        paddingBottom
//                    )
//                }
//            } else {
//                params.height = params.height + getStatusBarHeight()
//                setPadding(
//                    paddingLeft,
//                    paddingTop + getStatusBarHeight(),
//                    paddingRight,
//                    paddingBottom
//                )
//            }


            return@setOnApplyWindowInsetsListener insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                0,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                defaultInsets.
//                val systemWindowInsets = Insets.of(0,0,0,0)
//                WindowInsets.Builder(defaultInsets).setSystemWindowInsets(systemWindowInsets).build()
//            } else {
//                defaultInsets.replaceSystemWindowInsets(
//                    defaultInsets.systemWindowInsetLeft,
//                    defaultInsets.systemWindowInsetTop,
//                    defaultInsets.systemWindowInsetRight,
//                    defaultInsets.systemWindowInsetBottom
//                )
//            }
        }
    }
}