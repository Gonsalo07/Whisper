package com.example.whysper_app.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whysper_app.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnGoToLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnGoToLogin = findViewById(R.id.btnGoToLogin)

        btnGoToLogin.setOnClickListener {
            finish()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}