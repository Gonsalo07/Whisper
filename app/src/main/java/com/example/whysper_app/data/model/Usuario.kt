package com.example.whysper_app.data.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val id: Long? = null,
    val email: String,
    val password: String,
    val rol: String = "USUARIO",
    val estado: String = "ACTIVO"
)

data class AliasPublico(
    val id: Long? = null,
    val usuarioId: Usuario? = null,
    val alias: String,
    val creadoEn: String? = null
)

data class AliasDropdown(
    val id: Long,
    val label: String
)

data class Categoria(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String? = null
)

data class CategoriaDropdown(
    val id: Long,
    val label: String
)

data class Denuncia(
    val id: Long? = null,
    val usuarioId: Usuario,
    val aliasId: AliasPublico,
    val categoriaId: Categoria,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val estado: String = "EN_EVALUACION",
    @SerializedName("creadaEn")
    val creadaEn: String,
    val evidencias: List<Evidencia>
)

data class DenunciaUser(
    val id: Long? = null,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val estado: String = "EN_EVALUACION",
    @SerializedName("creadaEn")
    val creadaEn: String,
    val categoriaNombre: String,
)

data class ReporteFalsedad(
    val id: Long? = null,

    @SerializedName("denunciaId")
    val denunciaId: DenunciaRef,

    @SerializedName("usuarioId")
    val usuarioId: UsuarioRef,

    @SerializedName("motivo")
    val motivo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("estado")
    val estado: String = "VISIBLE"
)

data class DenunciaRequest(
    val usuarioId: UsuarioRef,
    val aliasId: AliasRef,
    val categoriaId: CategoriaRef,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String
)

data class UsuarioRef(val id: Long)
data class AliasRef(val id: Long)
data class CategoriaRef(val id: Long)


data class Evidencia(
    val id: Long? = null,
    val denunciaId: DenunciaRef,
    val url: String,
    val tipo: String,
    val creadoEn: String? = null,
    val estado: String = "VISIBLE"
)

data class DenunciaRef(val id: Long)
