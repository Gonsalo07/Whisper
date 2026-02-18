package com.example.whysper_app.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    private lateinit var btnLoginShowPass : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginDni = findViewById(R.id.etLoginDni)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLoginSend = findViewById(R.id.btnLoginSend)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)
        btnLoginShowPass = findViewById(R.id.btnLoginShowPass)



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
                if(dni.length != 8)
                {
                    enviarMensaje("El DNI que ingresaste no es valido, vuelve a revisarlo ...")
                    return@setOnClickListener
                }
                //Validar cuenta si existe ...


            } else
            {
                enviarMensaje("Completa todos los campos para continuar ...")
                return@setOnClickListener
            }

        }

        btnLoginShowPass.setOnClickListener {
            if (etLoginPassword.inputType == InputType.TYPE_CLASS_TEXT) {
                etLoginPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                etLoginPassword.inputType = InputType.TYPE_CLASS_TEXT
            }
            etLoginPassword.setSelection(etLoginPassword.text.length)
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
