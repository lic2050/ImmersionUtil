package com.lllic.immersionutil

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var insetTop = 0
    private var insetBotton = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.immersiveStatusBar(50)
//        WindowInsetsManager(this)
        setContentView(R.layout.activity_main)
//        addWindowInsetListener()
        bt.setOnClickListener {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            Dialog(this).apply {
                setContentView(R.layout.dialog_main)
                setOnDismissListener {
                window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                    this@MainActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }
            }.show()
        }
        btA.setOnClickListener {
            startActivity(Intent(this, AActivity::class.java))
        }
    }


    private fun addWindowInsetListener() {
        et.setOnApplyWindowInsetsListener { v, insets ->
            if (insetBotton != insets.systemWindowInsetBottom) {
                val params: ViewGroup.MarginLayoutParams =
                    et.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin =
                    params.bottomMargin - insetBotton + insets.systemWindowInsetBottom
                insetBotton = insets.systemWindowInsetBottom
                et.layoutParams = params
            }
            return@setOnApplyWindowInsetsListener insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                0
            )
        }

        tvHeader.setOnApplyWindowInsetsListener { v, insets ->
//            val defaultInsets = v.onApplyWindowInsets(insets)
            if (insetTop != insets.systemWindowInsetTop) {
                val params: ViewGroup.MarginLayoutParams =
                    tvHeader.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = params.topMargin - insetTop + insets.systemWindowInsetTop
                insetTop = insets.systemWindowInsetTop
            }
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
    private var insetBotton1 = 0
    private fun addWindowInsetListener1() {
        et.setOnApplyWindowInsetsListener { v, insets ->
            if (insetBotton1 != insets.systemWindowInsetBottom) {
                val params: ViewGroup.MarginLayoutParams =
                    et.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin =
                    params.bottomMargin - insetBotton1 + insets.systemWindowInsetBottom
                insetBotton1 = insets.systemWindowInsetBottom
                et.layoutParams = params
            }
            return@setOnApplyWindowInsetsListener insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                0
            )
        }

    }
}