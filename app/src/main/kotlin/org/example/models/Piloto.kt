package org.example.motogp.models

import org.example.motogp.enums.Nacionalidad
import kotlin.math.max
import kotlin.math.min

/**
 * Data class que representa un piloto de MotoGP.
 */
data class Piloto(
    val nombre: String,
    val nacionalidad: Nacionalidad,
    val edad: Int,
    val habilidades: Habilidades
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(edad >= 18) { "El piloto debe tener al menos 18 años" }
    }

    /**
     * Obtiene el rango del piloto (delegado a habilidades)
     */
    val rango: RangoHabilidad
        get() = habilidades.rango
}

// Función de extensión para ajustar valores al rango 1-100
fun Int.coerceInHabilidadRange(): Int {
    return max(1, min(100, this))
}
