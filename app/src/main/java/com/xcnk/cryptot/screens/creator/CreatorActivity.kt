package com.xcnk.cryptot.screens.creator

import android.content.Intent
import android.os.Bundle
import com.xcnk.cryptot.R
import com.xcnk.cryptot.screens.MyActivity


class CreatorActivity : MyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.new_crypto)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}