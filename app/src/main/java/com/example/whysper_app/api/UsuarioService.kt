package com.example.whysper_app.api

import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("usuarios")
    fun registrarUsuario(@Body datos: HashMap<String, String>): Call<ResponseBody>
}