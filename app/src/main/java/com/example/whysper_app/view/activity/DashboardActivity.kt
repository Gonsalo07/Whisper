package com.example.whysper_app.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.whysper_app.R
import com.example.whysper_app.view.fragment.CrearDenunciaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_publicaciones -> {
                    Toast.makeText(this, "Publicaciones (Próximamente)", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_publicar -> {
                    cargarFragment(CrearDenunciaFragment())
                    true
                }
                R.id.nav_perfil -> {
                    Toast.makeText(this, "Perfil (Próximamente)", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}