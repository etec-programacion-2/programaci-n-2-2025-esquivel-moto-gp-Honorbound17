package org.example.motogp.enums

import kotlin.random.Random

/**
 * @enum RangoHabilidad
 * @brief Representa el nivel de habilidad de un piloto basado en estadísticas reales.
 * 
 * Cada rango tiene multiplicadores diferentes para las habilidades base:
 * - S: Élite (top 5%)
 * - A: Excelente (top 15%) 
 * - B: Bueno (top 30%)
 * - C: Promedio (top 60%) 
 * - D: Novato (resto)
 */
enum class RangoHabilidad(val multiplicadorMin: Double, val multiplicadorMax: Double) {
    S(3.0, 300.0),   // Élite: multiplica habilidades base por 3.0-300.0
    A(2.0, 200.0),   // Excelente: multiplica por 2.0-200.0
    B(1.5, 150.0),   // Bueno: multiplica por 1.5-150.0
    C(1.25, 125.0),  // Promedio: multiplica por 1.25-125.0
    D(1.1, 110.0);   // Novato: multiplica por 1.1-110.0

    /**
     * Calcula el valor final de una habilidad basado en el rango
     */
    fun calcularHabilidadFinal(habilidadBase: Int): Double {
        require(habilidadBase in 1..100) { "Habilidad base debe estar entre 1-100" }
        
        val multiplicador = Random.nextDouble(multiplicadorMin, multiplicadorMax)
        return habilidadBase * multiplicador
    }
}
