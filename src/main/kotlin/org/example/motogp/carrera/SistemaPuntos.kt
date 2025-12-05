package org.example.motogp.carrera

import org.example.motogp.models.Piloto
import org.example.motogp.simulacion.ResultadoCarrera

/**
 * Sistema de puntos de MotoGP (responsabilidad única: cálculo de puntos).
 */
object SistemaPuntos {
    private val PUNTUACIONES = mapOf(
        1 to 25,
        2 to 20,
        3 to 16,
        4 to 13,
        5 to 11,
        6 to 10,
        7 to 9,
        8 to 8,
        9 to 7,
        10 to 6,
        11 to 5,
        12 to 4,
        13 to 3,
        14 to 2,
        15 to 1
    )

    fun obtenerPuntos(posicion: Int): Int = PUNTUACIONES[posicion] ?: 0

    /**
     * Calcula los puntos por piloto a partir del ResultadoCarrera.
     * Los pilotos que aparecen en abandonos reciben 0 puntos.
     */
    fun calcularPuntosCarrera(resultado: ResultadoCarrera): Map<Piloto, Int> {
        return resultado.posiciones.mapIndexed { index, piloto ->
            if (resultado.abandonos.contains(piloto)) {
                piloto to 0
            } else {
                piloto to obtenerPuntos(index + 1)
            }
        }.toMap()
    }
}