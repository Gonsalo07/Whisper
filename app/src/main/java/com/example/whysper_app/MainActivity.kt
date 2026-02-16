package com.example.whysper_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_welcome)

        /*val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_publicaciones -> {
                    true
                }
                R.id.nav_publicar -> {
                    true
                }
                R.id.nav_perfil -> {
                    true
                }
                else -> false
            }
        }*/
    }
}

//https://www.figma.com/community/file/1300432160609727073