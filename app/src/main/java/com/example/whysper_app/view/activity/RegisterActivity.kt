package com.example.whysper_app.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whysper_app.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var tvTerminoCondiciones : TextView
    private lateinit var btnGoToLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvTerminoCondiciones = findViewById(R.id.tvTerminoCondiciones)
        btnGoToLogin = findViewById(R.id.btnGoToLogin)

        tvTerminoCondiciones.setOnClickListener {
            val intent = Intent(this, TerminosCondicionesActivity::class.java)
            startActivity(intent)
        }

        btnGoToLogin.setOnClickListener {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}