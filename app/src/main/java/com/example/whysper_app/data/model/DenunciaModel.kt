package com.example.whysper_app.data.model

import com.google.gson.annotations.SerializedName

data class DenunciaModel(
    val id: Long? = null,
    val usuarioId: Usuario,
    val aliasId: AliasPublico,
    val categoriaId: Categoria,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val estado: String = "EN_EVALUACION",
    @SerializedName("creadaEn")
    val creadaEn: String? = null
)