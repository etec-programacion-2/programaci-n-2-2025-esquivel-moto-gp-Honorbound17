package org.example.motogp.models

/**
 * Data class que representa el rendimiento de una moto de MotoGP.
 * @property velocidadMaxima Velocidad máxima de la moto (1-100)
 * @property aceleracion Capacidad de aceleración (1-100)
 * @property maniobrabilidad Facilidad para maniobrar (1-100)
 */
data class Rendimiento(
    val velocidadMaxima: Int,
    val aceleracion: Int,
    val maniobrabilidad: Int
) {
    init {
        require(velocidadMaxima in 1..100) { "Velocidad máxima debe estar entre 1 y 100" }
        require(aceleracion in 1..100) { "Aceleración debe estar entre 1 y 100" }
        require(maniobrabilidad in 1..100) { "Maniobrabilidad debe estar entre 1 y 100" }
    }

    /**
     * Calcula el puntaje total de rendimiento
     */
    fun puntajeTotal(): Int {
        return velocidadMaxima + aceleracion + maniobrabilidad
    }

    /**
     * Calcula el promedio de rendimiento
     */
    fun promedio(): Double {
        return puntajeTotal() / 3.0
    }
}
