package com.example.whysper_app.view.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whysper_app.R

class TerminosCondicionesActivity : AppCompatActivity() {

    private lateinit var btnTerminosRegresar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminos_condiciones)

        btnTerminosRegresar = findViewById(R.id.btnTerminosRegresar)

        btnTerminosRegresar.setOnClickListener {
            finish()
        }
    }
}