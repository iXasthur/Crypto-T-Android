package com.xcnk.cryptot.screens

import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

open class MyActivity: LocaleAwareCompatActivity() {

    var blockBackButton: Boolean = false

    override fun onBackPressed() {
        if (!blockBackButton) {
            super.onBackPressed()
        } else {
            println("Blocked back press due to running animation")
        }
    }

}