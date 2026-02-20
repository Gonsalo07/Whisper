package com.example.whysper_app.view.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.whysper_app.R
import com.example.whysper_app.data.model.*
import com.example.whysper_app.data.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearDenunciaFragment : Fragment() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var btnSeleccionarArchivo: Button
    private lateinit var btnPublicar: Button

    private lateinit var layoutArchivoSeleccionado: LinearLayout
    private lateinit var ivPreview: ImageView
    private lateinit var tvNombreArchivo: TextView
    private lateinit var tvTipoArchivo: TextView
    private lateinit var btnEliminarArchivo: ImageButton

    private val categorias = mutableListOf<CategoriaDropdown>()
    private var categoriaSeleccionadaId: Long? = null

    private var archivoUri: Uri? = null
    private var tipoArchivo: String? = null

    // TEMPORAL
    private val usuarioId: Long = 1
    private val aliasId: Long = 1

    private val seleccionarArchivoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                archivoUri = it
                mostrarPreviewArchivo(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear_denuncia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarVistas(view)
        configurarListeners()
        cargarCategorias()
    }

    private fun inicializarVistas(view: View) {
        etTitulo = view.findViewById(R.id.etTitulo)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria)
        btnSeleccionarArchivo = view.findViewById(R.id.btnSeleccionarArchivo)
        btnPublicar = view.findViewById(R.id.btnPublicar)

        layoutArchivoSeleccionado = view.findViewById(R.id.layoutArchivoSeleccionado)
        ivPreview = view.findViewById(R.id.ivPreview)
        tvNombreArchivo = view.findViewById(R.id.tvNombreArchivo)
        tvTipoArchivo = view.findViewById(R.id.tvTipoArchivo)
        btnEliminarArchivo = view.findViewById(R.id.btnEliminarArchivo)
    }

    private fun configurarListeners() {
        btnSeleccionarArchivo.setOnClickListener {
            seleccionarArchivoLauncher.launch("*/*")
        }

        btnEliminarArchivo.setOnClickListener {
            archivoUri = null
            tipoArchivo = null
            layoutArchivoSeleccionado.visibility = View.GONE
        }

        btnPublicar.setOnClickListener {
            publicarDenuncia()
        }

        spinnerCategoria.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    categoriaSeleccionadaId = categorias[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun cargarCategorias() {
        ApiClient.apiService.obtenerCategoriasDropdown()
            .enqueue(object : Callback<List<CategoriaDropdown>> {

                override fun onResponse(
                    call: Call<List<CategoriaDropdown>>,
                    response: Response<List<CategoriaDropdown>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { lista ->
                            categorias.clear()
                            categorias.addAll(lista)

                            val labels = categorias.map { it.label }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                labels
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerCategoria.adapter = adapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<CategoriaDropdown>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun mostrarPreviewArchivo(uri: Uri) {
        val mimeType = requireContext().contentResolver.getType(uri)

        tipoArchivo = when {
            mimeType?.startsWith("image/") == true -> "IMAGEN"
            mimeType?.startsWith("video/") == true -> "VIDEO"
            mimeType == "application/pdf" -> "PDF"
            else -> "DOCUMENTO"
        }

        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        val nombreArchivo = cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: "archivo"

        tvNombreArchivo.text = nombreArchivo
        tvTipoArchivo.text = "Tipo: $tipoArchivo"

        if (tipoArchivo == "IMAGEN") {
            Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(ivPreview)
        } else {
            ivPreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        layoutArchivoSeleccionado.visibility = View.VISIBLE
    }

    private fun publicarDenuncia() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val ubicacion = etUbicacion.text.toString().trim()

        if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty()) {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (categoriaSeleccionadaId == null) {
            Toast.makeText(requireContext(), "Seleccione una categoría", Toast.LENGTH_SHORT).show()
            return
        }

        val denunciaRequest = DenunciaRequest(
            usuarioId = UsuarioRef(usuarioId),
            aliasId = AliasRef(aliasId),
            categoriaId = CategoriaRef(categoriaSeleccionadaId!!),
            titulo = titulo,
            descripcion = descripcion,
            ubicacion = ubicacion
        )

        btnPublicar.isEnabled = false
        btnPublicar.text = "Publicando..."

        ApiClient.apiService.crearDenuncia(denunciaRequest)
            .enqueue(object : Callback<Denuncia> {

                override fun onResponse(call: Call<Denuncia>, response: Response<Denuncia>) {
                    if (response.isSuccessful) {
                        val denunciaCreada = response.body()

                        if (archivoUri != null && denunciaCreada != null) {
                            subirArchivo(denunciaCreada.id!!, archivoUri!!)
                        } else {
                            finalizarPublicacion()
                        }
                    } else {
                        errorPublicacion()
                    }
                }

                override fun onFailure(call: Call<Denuncia>, t: Throwable) {
                    errorPublicacion()
                }
            })
    }

    private fun subirArchivo(denunciaId: Long, uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: return

        val mimeType = requireContext().contentResolver.getType(uri) ?: "application/octet-stream"

        val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", "archivo", requestFile)

        ApiClient.apiService.subirEvidencia(denunciaId, body)
            .enqueue(object : Callback<Evidencia> {

                override fun onResponse(call: Call<Evidencia>, response: Response<Evidencia>) {
                    finalizarPublicacion()
                }

                override fun onFailure(call: Call<Evidencia>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error subiendo archivo", Toast.LENGTH_LONG).show()
                    finalizarPublicacion()
                }
            })
    }

    private fun finalizarPublicacion() {
        Toast.makeText(requireContext(), "Denuncia publicada correctamente", Toast.LENGTH_SHORT).show()

        // Limpiar formulario
        etTitulo.text.clear()
        etDescripcion.text.clear()
        etUbicacion.text.clear()
        archivoUri = null
        layoutArchivoSeleccionado.visibility = View.GONE

        btnPublicar.isEnabled = true
        btnPublicar.text = "Publicar Denuncia"
    }

    private fun errorPublicacion() {
        btnPublicar.isEnabled = true
        btnPublicar.text = "Publicar Denuncia"
        Toast.makeText(requireContext(), "Error al publicar", Toast.LENGTH_SHORT).show()
    }
}