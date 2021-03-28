package com.xcnk.cryptot.screens

import androidx.appcompat.app.AppCompatActivity

open class MyActivity: AppCompatActivity() {

    var blockBackButton: Boolean = false

    override fun onBackPressed() {
        if (!blockBackButton) {
            super.onBackPressed()
        } else {
            println("Blocked back press due to running animation")
        }
    }

}