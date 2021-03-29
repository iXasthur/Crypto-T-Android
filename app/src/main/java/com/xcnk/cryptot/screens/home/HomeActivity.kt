package com.xcnk.cryptot.screens.home

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xcnk.cryptot.R
import com.xcnk.cryptot.screens.MyActivity

class HomeActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.activity_home_bottom_navigation_view)
        val navController = findNavController(R.id.activity_home_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.cryptosFragment,
                R.id.mapFragment,
                R.id.settingsFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

}