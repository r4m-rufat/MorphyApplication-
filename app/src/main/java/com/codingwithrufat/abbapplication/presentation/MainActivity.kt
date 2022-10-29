package com.codingwithrufat.abbapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingwithrufat.abbapplication.R
import com.codingwithrufat.abbapplication.presentation.home_page.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, HomeFragment())
            .commit()

    }
}