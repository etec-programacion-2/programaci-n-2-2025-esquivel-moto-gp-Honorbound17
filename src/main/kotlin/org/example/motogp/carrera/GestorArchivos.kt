package org.example.motogp.carrera

import java.io.File

/**
 * Utilidades simples para guardar/leer strings (JSON) en disco.
 */
object GestorArchivos {
    /**
     * Guarda un string en un archivo.
     * Devuelve true si se escribió correctamente.
     */
    fun guardarString(contenido: String, nombreArchivo: String): Boolean {
        return try {
            File(nombreArchivo).writeText(contenido)
            println("✅ Guardado en $nombreArchivo")
            true
        } catch (e: Exception) {
            println("❌ Error al guardar archivo '$nombreArchivo': ${e.message}")
            false
        }
    }

    /**
     * Lee y devuelve el contenido del archivo, o null en caso de error.
     */
    fun cargarString(nombreArchivo: String): String? {
        return try {
            File(nombreArchivo).readText()
        } catch (e: Exception) {
            println("❌ Error al leer archivo '$nombreArchivo': ${e.message}")
            null
        }
    }

    /**
     * Lista archivos en el directorio actual con extensión .motojson
     */
    fun listarPartidasGuardadas(directorio: String = "."): List<String> {
        return try {
            File(directorio).listFiles { f -> f.isFile && f.name.endsWith(".motojson") }
                ?.map { it.name } ?: emptyList()
        } catch (e: Exception) {
            println("❌ Error al listar archivos en '$directorio': ${e.message}")
            emptyList()
        }
    }
}