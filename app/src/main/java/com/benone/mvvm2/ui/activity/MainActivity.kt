package com.benone.mvvm2.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benone.mvvm2.R
import com.benone.mvvm2.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,HomeFragment())
        transaction.commit()
    }
}