package org.example.motogp.models

import org.example.motogp.enums.RangoHabilidad

/**
 * Data class que representa las habilidades de un piloto de MotoGP con sistema de rangos.
 * @property velocidadBase Habilidad base en rectas (1-100)
 * @property frenadaBase Habilidad base en frenadas (1-100)
 * @property pasoPorCurvaBase Habilidad base en curvas (1-100)
 * @property rango Rango del piloto (S, A, B, C, D)
 */
data class Habilidades(
    val velocidadBase: Int,
    val frenadaBase: Int,
    val pasoPorCurvaBase: Int,
    val rango: RangoHabilidad
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
