package com.example.whysper_app.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AliasService {

    @POST("alias")
    fun registrarAlias(@Body datosAlias: HashMap<String, Any>): Call<ResponseBody>
}