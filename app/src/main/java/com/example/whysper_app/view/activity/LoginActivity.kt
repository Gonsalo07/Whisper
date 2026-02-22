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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var etLoginDni: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var btnLoginSend: Button
    private lateinit var btnGoToRegister: Button
    private lateinit var btnLoginShowPass: ImageButton
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginDni = findViewById(R.id.etLoginDni)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLoginSend = findViewById(R.id.btnLoginSend)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)
        btnLoginShowPass = findViewById(R.id.btnLoginShowPass)

        btnLoginSend.setOnClickListener {
            val dni = etLoginDni.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            if (dni.isNotEmpty() && password.isNotEmpty()) {
                if (dni.length != 8) {
                    Toast.makeText(this, R.string.dni_invalido, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                verificarUsuarioYEntrar(dni, password)
            } else {
                Toast.makeText(this, R.string.completar_campos, Toast.LENGTH_SHORT).show()
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

            //Aqui estoy cambiando a la vista de Registro
            btnGoToRegister.setOnClickListener {
                finish()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
    }
    private fun verificarUsuarioYEntrar(dni: String, pass: String) {
        db.collection("usuarios").whereEqualTo("dni",dni).get()
            .addOnSuccessListener { documentos ->
                if(documentos.isEmpty){
                    Toast.makeText(this, R.string.dni_no_registrado, Toast.LENGTH_SHORT).show()
                }else{
                    val email = documentos.documents[0].getString("email")

                    if(email != null){
                        iniciarSesionFirebase(email,pass)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_SHORT).show()
            }
    }
    private fun iniciarSesionFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task -> //VERIFICO QUE AMBAS ESTEN CORRECTAS
                if (task.isSuccessful) {
                    Toast.makeText(this, R.string.bienvenida, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, R.string.contra_error, Toast.LENGTH_SHORT).show()
                }
            }
    }
}

