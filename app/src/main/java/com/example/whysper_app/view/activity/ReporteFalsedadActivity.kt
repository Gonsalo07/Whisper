package com.example.whysper_app.view.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.whysper_app.R
import com.example.whysper_app.data.model.*
import com.example.whysper_app.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReporteFalsedadActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_falsedad)

        // Recuperar el ID de la denuncia que pasamos desde el Login (o luego desde el Adapter)
        val denunciaIdRecibido = intent.getLongExtra("DENUNCIA_ID", -1L)

        val spinner = findViewById<Spinner>(R.id.spinnerMotivo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionReporte)
        val btnEnviar = findViewById<Button>(R.id.btnEnviarReporte)

        // Configurar Spinner
        val motivos = arrayOf("Información falsa", "Spam", "Odio o acoso", "Foto engañosa", "Otro")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motivos)

        btnEnviar.setOnClickListener {
            val motivoSeleccionado = spinner.selectedItem.toString()
            val descripcion = etDescripcion.text.toString().trim()

            if (descripcion.isEmpty()) {
                Toast.makeText(this, "Por favor, añade una descripción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            println("DEBUG_APP: Enviando motivo: $motivoSeleccionado a denuncia: $denunciaIdRecibido")

            val nuevoReporte = ReporteFalsedad(
                denunciaId = DenunciaRef(id = denunciaIdRecibido),
                usuarioId = UsuarioRef(id = 1L),
                motivo = motivoSeleccionado,
                descripcion = descripcion
            )

            enviarReporteAlServidor(nuevoReporte)
        }
    }

    private fun enviarReporteAlServidor(reporte: ReporteFalsedad) {
        ApiClient.apiService.crearReporteFalsedad(reporte).enqueue(object : Callback<ReporteFalsedad> {
            override fun onResponse(call: Call<ReporteFalsedad>, response: Response<ReporteFalsedad>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ReporteFalsedadActivity, "Reporte enviado con éxito", Toast.LENGTH_LONG).show()
                    finish() // Regresa a la pantalla anterior
                } else {
                    Toast.makeText(this@ReporteFalsedadActivity, "Error en el servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReporteFalsedad>, t: Throwable) {
                Toast.makeText(this@ReporteFalsedadActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}