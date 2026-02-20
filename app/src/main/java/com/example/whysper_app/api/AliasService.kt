package com.example.whysper_app.api

import com.example.whysper_app.data.model.AliasPublicoResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AliasService {

    @POST("alias")
    fun registrarAlias(@Body datosAlias: HashMap<String, Any>): Call<ResponseBody>

    @PUT("alias/firebase/{firebaseId}")
    fun actualizarAliasMySQL(@Path("firebaseId") firebaseId: String, @Body datos: Map<String, String>): Call<AliasPublicoResponse>
}