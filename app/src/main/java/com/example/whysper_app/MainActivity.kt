package com.example.whysper_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.whysper_app.view.activity.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnWelcomeApp : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        btnWelcomeApp = findViewById(R.id.btnWelcomeApp)

        btnWelcomeApp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}

//https://www.figma.com/community/file/1300432160609727073