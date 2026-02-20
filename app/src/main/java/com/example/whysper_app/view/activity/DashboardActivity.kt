package com.example.whysper_app.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.whysper_app.R
import com.example.whysper_app.view.fragment.CrearDenunciaFragment

import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        loadFragment(ProfileFragment())

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_publicaciones -> loadFragment(PublicacionesFragment())
                R.id.nav_publicar -> loadFragment(CrearDenunciaFragment())
                R.id.nav_perfil -> loadFragment(ProfileFragment())
            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()




    }
}