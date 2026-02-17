package com.example.whysper_app.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whysper_app.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etLoginDni : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnLoginSend : Button
    private lateinit var btnGoToRegister : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginDni = findViewById(R.id.etLoginDni)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLoginSend = findViewById(R.id.btnLoginSend)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)

        // Estoy creando una funciona para returilizar los Toast

        fun enviarMensaje(mensaje: String) {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }


        //Voy a valiar los inputs entrados en el formulario
        btnLoginSend.setOnClickListener {
            val dni = etLoginDni.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            if(dni.isNotEmpty() && password.isNotEmpty())
            {
            } else
            {
                enviarMensaje("Debes ingresar datos validos para continuar ...")
            }

        }



        //Aqui estoy cambiando a la visa de Registro
        btnGoToRegister.setOnClickListener {
            finish()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
/*

ppt
justificacion
objetivos smart (Sobre producto final. porceaje se quantifica)

recomendaciones: para otros estudiantes que quieran realizar algo similar
conclusiones

informa de participacion en un TXT, que hizo cada uno y que porcentaje hizo cada uno


        presentacion: conocimiento, codigo, y malograr el codigo

        */
