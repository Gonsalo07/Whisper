package com.example.whysper_app.data.network

import com.example.whysper_app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface WhisperApiService {


    @GET("api/categorias/dropdown")
    fun obtenerCategoriasDropdown(): Call<List<CategoriaDropdown>>

    @GET("api/categorias")
    fun obtenerCategorias(): Call<List<Categoria>>


    @GET("api/alias/dropdown")
    fun obtenerAliasDropdown(): Call<List<AliasDropdown>>

    @GET("api/alias")
    fun obtenerAlias(): Call<List<AliasPublico>>


    @GET("api/denuncia")
    fun obtenerDenuncias(): Call<List<Denuncia>>

    @GET("api/denuncia/{id}")
    fun obtenerDenunciaPorId(@Path("id") id: Long): Call<Denuncia>

    @POST("api/denuncia")
    fun crearDenuncia(@Body denuncia: DenunciaRequest): Call<Denuncia>

    @PUT("api/denuncia/{id}")
    fun actualizarDenuncia(
        @Path("id") id: Long,
        @Body denuncia: DenunciaRequest
    ): Call<Denuncia>

    @DELETE("api/denuncia/{id}")
    fun eliminarDenuncia(@Path("id") id: Long): Call<Void>


    @Multipart
    @POST("api/evidencia/upload/{denunciaId}")
    fun subirEvidencia(
        @Path("denunciaId") denunciaId: Long,
        @Part file: MultipartBody.Part
    ): Call<Evidencia>

    @GET("api/evidencia/por-denuncia/{id}")
    fun obtenerEvidenciasPorDenuncia(
        @Path("id") id: Long
    ): Call<List<Evidencia>>


    @POST("api/usuarios")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>

    @GET("api/usuarios/email/{email}")
    fun obtenerUsuarioPorEmail(@Path("email") email: String): Call<Usuario>
}

// Objeto singleton para obtener el servicio
object ApiClient {
    val apiService: WhisperApiService by lazy {
        RetrofitClient.instance.create(WhisperApiService::class.java)
    }
}