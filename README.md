# ImmersionUtil
沉浸式UI管理工具
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
		implementation "com.github.lic2050:ImmersionUtil:2.0"
	}


只支持5.0+

1.  window.immersiveStatusBar()

    设置状态栏透明，并且将跟布局延申到状态栏上

2.  view.fitStatusBar()

    给view或toolbar添加状态栏高度的marginTop
    

    view.fitStatusBarOverlay()
    
    给view(toolbar)同时添加状态栏高度的 height和paddingTop
