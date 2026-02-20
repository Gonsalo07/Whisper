package com.example.whysper_app.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.whysper_app.R
import com.example.whysper_app.api.RetrofitClient
import com.example.whysper_app.data.model.AliasPublicoResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var isEditing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore
        val uid = auth.currentUser?.uid ?: ""
        val tvAlias = view.findViewById<TextView>(R.id.tvProfileAlias)
        val etAlias = view.findViewById<EditText>(R.id.etProfileAlias)
        val btnEdit = view.findViewById<ImageButton>(R.id.btnEditAlias)
        val btnSave = view.findViewById<Button>(R.id.btnSaveAlias)

        cargarDatosPerfil(uid, tvAlias, view.findViewById(R.id.tvProfileEmail))

        btnEdit.setOnClickListener {
            isEditing = !isEditing
            if (isEditing) {
                tvAlias.visibility = View.GONE
                etAlias.visibility = View.VISIBLE
                etAlias.setText(tvAlias.text)
                btnSave.visibility = View.VISIBLE
            } else {
                cancelarEdicion(tvAlias, etAlias, btnSave)
            }
        }

        btnSave.setOnClickListener {
            val nuevoAlias = etAlias.text.toString().trim()
            if (nuevoAlias.isNotEmpty()) {
                actualizarAlias(uid, nuevoAlias, tvAlias, etAlias, btnSave)
            }
        }
    }
    private fun cargarDatosPerfil(uid: String, tvAlias: TextView, tvEmail: TextView) {
        // 1. Cargamos el Email directamente de Firebase Auth (es lo más seguro y rápido)
        tvEmail.text = auth.currentUser?.email

        // 2. Buscamos el Alias en la colección "alias_publicos" filtrando por usuario_id
        db.collection("alias_publicos")
            .whereEqualTo("usuario_id", uid)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Tomamos el primer documento encontrado
                    val document = documents.documents[0]
                    tvAlias.text = document.getString("alias")
                } else {
                    tvAlias.text = "Sin alias"
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al cargar alias", Toast.LENGTH_SHORT).show()
            }
    }
    private fun actualizarAlias(uid: String, nuevo: String, tv: TextView, et: EditText, btn: Button) {
        db.collection("alias_publicos")
            .whereEqualTo("usuario_id", uid)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id

                    // Ahora actualizamos ese documento específico
                    db.collection("alias_publicos").document(docId)
                        .update("alias", nuevo)
                        .addOnSuccessListener {
                            tv.text = nuevo
                            cancelarEdicion(tv, et, btn)
                            Toast.makeText(context, "Alias actualizado en la nube", Toast.LENGTH_SHORT).show()

                            actualizarEnMySQL(uid, nuevo, tv, et, btn)
                        }
                }
            }
    }
    private fun actualizarEnMySQL(uid: String, nuevo: String, tv: TextView, et: EditText, btn: Button) {
        val datos = mapOf("alias" to nuevo)
        RetrofitClient.aliasService.actualizarAliasMySQL(uid, datos).enqueue(object : Callback<AliasPublicoResponse> {
            override fun onResponse(call: Call<AliasPublicoResponse>, response: Response<AliasPublicoResponse>) {
                if (response.isSuccessful) {
                    tv.text = nuevo
                    cancelarEdicion(tv, et, btn)
                    Toast.makeText(context, "Alias sincronizado en todo el sistema", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Firebase OK, pero MySQL falló", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AliasPublicoResponse>, t: Throwable) {
                Toast.makeText(context, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun cancelarEdicion(tv: TextView, et: EditText, btn: Button) {
        isEditing = false
        tv.visibility = View.VISIBLE
        et.visibility = View.GONE
        btn.visibility = View.GONE
    }
}