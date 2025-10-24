package org.example.motogp.carrera

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Data class que representa el estado completo de una temporada.
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
    /**
     * Convierte a string JSON
     */
    fun toJson(): String {
        return Json.encodeToString(this)
    }
    
    companion object {
        /**
         * Crea desde string JSON
         */
        fun fromJson(json: String): EstadoTemporadaSerializable {
            return Json.decodeFromString(json)
        }
    }
}

/**
 * Utilidades para guardar y cargar archivos JSON
 */
object GestorArchivos {
    /**
     * Guarda un string en un archivo
     */
    fun guardarString(contenido: String, nombreArchivo: String): Boolean {
        return try {
            File(nombreArchivo).writeText(contenido)
            true
        } catch (e: Exception) {
            println("❌ Error al guardar archivo: ${e.message}")
            false
        }
    }
    
    /**
     * Carga un string desde un archivo
     */
    fun cargarString(nombreArchivo: String): String? {
        return try {
            File(nombreArchivo).readText()
        } catch (e: Exception) {
            println("❌ Error al cargar archivo: ${e.message}")
            null
        }
    }
    
    /**
     * Lista los archivos de partida guardados
     */
    fun listarPartidasGuardadas(directorio: String = "."): List<String> {
        return File(directorio).listFiles { file -> 
            file.name.endsWith(".motojson") 
        }?.map { it.name } ?: emptyList()
    }
}
