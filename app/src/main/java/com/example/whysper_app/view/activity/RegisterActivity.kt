package com.example.whysper_app.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whysper_app.R
import com.example.whysper_app.api.RetrofitClient
import com.example.whysper_app.api.UsuarioService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var btnRegistrar: Button
    private lateinit var etAlias: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDni: EditText
    private lateinit var etContrasena: EditText
    private lateinit var tvTerminoCondiciones : TextView
    private lateinit var btnGoToLogin : Button
    private lateinit var cbTerminos : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        db = Firebase.firestore
        etAlias = findViewById(R.id.etAlias)
        etEmail = findViewById(R.id.etEmail)
        etDni = findViewById(R.id.etDni)
        etContrasena = findViewById(R.id.etContrasena)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        tvTerminoCondiciones = findViewById(R.id.tvTerminoCondiciones)
        btnGoToLogin = findViewById(R.id.btnGoToLogin)
        cbTerminos = findViewById(R.id.cbTerminosCondiciones)

        tvTerminoCondiciones.setOnClickListener {
                val intent = Intent(this, TerminosCondicionesActivity::class.java)
                startActivity(intent)
            }

        btnGoToLogin.setOnClickListener {
                finish()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
        }

        //SOLO SON VALIDACIONES SIMPLES
        btnRegistrar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etContrasena.text.toString().trim()
            val alias = etAlias.text.toString().trim()
            val dni = etDni.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || alias.isEmpty() || dni.isEmpty()) {
                Toast.makeText(this, R.string.completar_campos, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!cbTerminos.isChecked) {
                Toast.makeText(this, R.string.acepta_tyc, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dni.length != 8) {
                Toast.makeText(this, R.string.validar_dni, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, R.string.validar_contrasena, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            verificarDuplicadosYCrear(email, password, alias, dni)
        }

        //DETALLE PARA QUE EL BOTON DE REGISTRAR SE ACTIVE CUANDO TODO EN EL FORMULARIO ESTE READY
        cbTerminos.setOnCheckedChangeListener { _, isChecked ->
            btnRegistrar.isEnabled = isChecked

            if (isChecked) {
                btnRegistrar.alpha = 1.0f
            } else {
                btnRegistrar.alpha = 0.5f
            }
        }
    }
    //AQUI SE VERIFICAN DUPLICACDOS EN LOS CAMPOS MEDIANTE EL FIREBASE
    private fun verificarDuplicadosYCrear(email: String, pass: String, alias: String, dni: String) {
        db.collection("usuarios").whereEqualTo("dni", dni).get() //Aqui le dices al firebase que busque en esa coleccion y busque y comapre ese campo
            .addOnSuccessListener { documentosDni -> //VALIDO NO REPETIR DNI
                if (!documentosDni.isEmpty) {
                    Toast.makeText(this, R.string.dni_en_uso, Toast.LENGTH_SHORT).show()
                } else {
                    db.collection("usuarios").whereEqualTo("email", email).get()
                        .addOnSuccessListener { documentosEmail -> //VALIDO NO REPETIR EMAIL
                            if (!documentosEmail.isEmpty) {
                                Toast.makeText(this, R.string.email_en_uso, Toast.LENGTH_SHORT).show()
                            } else {
                                db.collection("alias_publicos").whereEqualTo("alias", alias).get()
                                    .addOnSuccessListener { documentosAlias -> //VALIDO NO REPETIR ALIAS
                                        if (!documentosAlias.isEmpty) {
                                            Toast.makeText(this, R.string.alias_en_uso, Toast.LENGTH_SHORT).show()
                                        } else {
                                            crearCuenta(email, pass, alias, dni)
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, R.string.error_validar_alias, Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, R.string.error_validar_dni, Toast.LENGTH_SHORT).show()
            }
    }

    private fun crearCuenta(email: String, pass: String, alias: String, dni: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    val datosParaApi = hashMapOf(
                        "email" to email,
                        "password" to pass,
                        "dni" to dni,
                        "rol" to "USUARIO",
                        "estado" to "ACTIVO",
                        "firebase_id" to uid,
                        "aliasTemporal" to alias
                    )
                    consumirMiApiUsuarios(datosParaApi, email, alias, dni, uid)
                } else {
                    Toast.makeText(this, "Error Auth: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun consumirMiApiUsuarios(datos: HashMap<String, String>, email: String, alias: String, dni: String, uid: String) {
        RetrofitClient.usuarioService.registrarUsuario(datos).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    guardarEnFirestore(email, alias, dni, uid)
                } else {
                    Toast.makeText(this@RegisterActivity, R.string.error_conexion, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun guardarEnFirestore(email: String, alias: String, dni: String, uid: String) {
        val datosUsuario = hashMapOf(
            "uid" to uid,
            "email" to email,
            "dni" to dni,
            "rol" to "USUARIO",
            "estado" to "ACTIVO"
        )
        val datosAlias = hashMapOf(
            "usuario_id" to uid,
            "alias" to alias,
            "creado_en" to FieldValue.serverTimestamp()
        )

        val grupo = db.batch()
        val userRef = db.collection("usuarios").document(uid)
        val aliasRef = db.collection("alias_publicos").document()

        grupo.set(userRef, datosUsuario)
        grupo.set(aliasRef, datosAlias)

        grupo.commit().addOnSuccessListener {
            Toast.makeText(this, R.string.registroexitoso, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}