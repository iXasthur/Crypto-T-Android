package com.xcnk.cryptot.utils

import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.xcnk.cryptot.screens.MyActivity

fun Fragment.startAnimation(progressBar: ProgressBar) {
    val activity: MyActivity = activity as MyActivity
    activity.blockBackButton = true
    activity.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar.visibility = View.VISIBLE
}

fun Fragment.stopAnimation(progressBar: ProgressBar) {
    val activity: MyActivity = activity as MyActivity
    activity.blockBackButton = false
    activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar.visibility = View.INVISIBLE
}
