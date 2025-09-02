package org.example.motogp.models

import org.example.motogp.enums.RangoHabilidad
import kotlin.math.max
import kotlin.math.min

/**
 * Data class que representa las habilidades de un piloto de MotoGP con sistema de rangos.
 */
data class Habilidades(
    val velocidadBase: Int,      // Cambiado de "velocidad" a "velocidadBase"
    val frenadaBase: Int,        // Cambiado de "frenada" a "frenadaBase"
    val pasoPorCurvaBase: Int,   // Cambiado de "pasoPorCurva" a "pasoPorCurvaBase"
    val rango: RangoHabilidad    // Añadido el rango
) {
    init {
        require(velocidadBase in 1..100) { "Velocidad base debe estar entre 1 y 100" }
        require(frenadaBase in 1..100) { "Frenada base debe estar entre 1 y 100" }
        require(pasoPorCurvaBase in 1..100) { "Paso por curva base debe estar entre 1 y 100" }
    }

    // Propiedades calculadas (valores finales con multiplicador del rango)
    val velocidadFinal: Double get() = rango.calcularHabilidadFinal(velocidadBase)
    val frenadaFinal: Double get() = rango.calcularHabilidadFinal(frenadaBase)
    val pasoPorCurvaFinal: Double get() = rango.calcularHabilidadFinal(pasoPorCurvaBase)

    /**
     * Calcula el promedio de habilidades finales
     */
    fun promedioHabilidades(): Double {
        return (velocidadFinal + frenadaFinal + pasoPorCurvaFinal) / 3.0
    }
}