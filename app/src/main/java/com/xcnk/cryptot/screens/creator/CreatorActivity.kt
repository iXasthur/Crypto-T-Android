package com.xcnk.cryptot.screens.creator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.xcnk.cryptot.R
import com.xcnk.cryptot.screens.MyActivity


class CreatorActivity : MyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.new_crypto)
    }
}