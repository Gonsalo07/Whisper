package com.example.whysper_app.utils

import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter

fun tiempoRelativo(fechaString: String): String {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val fecha = LocalDateTime.parse(fechaString, formatter)

    val ahora = LocalDateTime.now()
    val duracion = Duration.between(fecha, ahora)

    val minutos = duracion.toMinutes()
    val horas = duracion.toHours()
    val dias = duracion.toDays()

    return when {
        minutos < 1 -> "hace unos segundos"
        minutos < 60 -> "hace $minutos minutos"
        horas < 24 -> "hace $horas horas"
        dias < 7 -> "hace $dias días"
        else -> fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}