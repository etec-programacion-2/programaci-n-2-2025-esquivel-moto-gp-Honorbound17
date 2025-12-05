package org.example.motogp.carrera

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * DTO serializable (plano) para guardar/cargar el estado de la temporada.
 * Evita serializar el grafo completo de objetos de dominio.
 */
@Serializable
data class EstadoTemporadaSerializable(
    val nombrePilotoJugador: String,
    val nacionalidadPiloto: String,
    val edadPiloto: Int,
    val carreraActual: Int,
    val totalCarreras: Int,
    val puntosPilotos: Map<String, Int>,
    val puntosEquipos: Map<String, Int>,
    val nombresCircuitos: List<String>,
    val dificultad: Int,
    val historialCarreras: Int = 0
) {
    fun toJson(): String {
        val json = Json { prettyPrint = true }
        return json.encodeToString(this)
    }

    companion object {
        fun fromJson(jsonString: String): EstadoTemporadaSerializable {
            val parser = Json { ignoreUnknownKeys = true }
            return parser.decodeFromString<EstadoTemporadaSerializable>(jsonString)
        }
    }
}