package com.example.whysper_app.data.model

import com.google.gson.annotations.SerializedName

// ═══════════════════════════════════════════════════════════════════
// USUARIO
// ═══════════════════════════════════════════════════════════════════
data class Usuario(
    val id: Long? = null,
    val email: String,
    val password: String,
    val rol: String = "USUARIO",
    val estado: String = "ACTIVO"
)

// ═══════════════════════════════════════════════════════════════════
// ALIAS PÚBLICO
// ═══════════════════════════════════════════════════════════════════
data class AliasPublico(
    val id: Long? = null,
    val usuarioId: Usuario? = null,
    val alias: String,
    val creadoEn: String? = null
)

// Para dropdown (solo id y label)
data class AliasDropdown(
    val id: Long,
    val label: String
)

// ═══════════════════════════════════════════════════════════════════
// CATEGORÍA
// ═══════════════════════════════════════════════════════════════════
data class Categoria(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String? = null
)

// Para dropdown (solo id y label)
data class CategoriaDropdown(
    val id: Long,
    val label: String
)

// ═══════════════════════════════════════════════════════════════════
// DENUNCIA
// ═══════════════════════════════════════════════════════════════════
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
    val creadaEn: String? = null
)

// ═══════════════════════════════════════════════════════════════════
// REPORTE DE FALSEDAD
// ═══════════════════════════════════════════════════════════════════

// Modifica esta parte en tu archivo Usuario.kt
data class ReporteFalsedad(
    val id: Long? = null,

    @SerializedName("denunciaId") // Debe coincidir con el nombre de la variable en Java
    val denunciaId: DenunciaRef,

    @SerializedName("usuarioId") // Debe coincidir con el nombre de la variable en Java
    val usuarioId: UsuarioRef,

    @SerializedName("motivo")
    val motivo: String,

    // Agregamos la descripción si la usas, aunque en tu Java no la veo (ojo aquí)
    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("estado")
    val estado: String = "VISIBLE"
)

// Request para crear denuncia (sin el timestamp, lo pone el backend)
data class DenunciaRequest(
    val usuarioId: UsuarioRef,
    val aliasId: AliasRef,
    val categoriaId: CategoriaRef,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String
)

// Referencias simples para enviar solo el ID
data class UsuarioRef(val id: Long)
data class AliasRef(val id: Long)
data class CategoriaRef(val id: Long)


data class Evidencia(
    val id: Long? = null,
    val denunciaId: DenunciaRef,
    val url: String,
    val tipo: String, // PDF, IMAGEN, VIDEO, AUDIO
    val creadoEn: String? = null,
    val estado: String = "VISIBLE"
)

data class DenunciaRef(val id: Long)

data class EvidenciaRequest(
    val denunciaId: DenunciaRef,
    val url: String,
    val tipo: String
)