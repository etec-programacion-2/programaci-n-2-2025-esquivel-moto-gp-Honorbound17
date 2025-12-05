package org.example.motogp.carrera

import org.example.motogp.models.Circuito
import org.example.motogp.models.Piloto
import org.example.motogp.simulacion.ResultadoCarrera

data class EstadoTemporada(
    val pilotoJugador: Piloto,
    val carreraActual: Int,
    val totalCarreras: Int,
    val puntosPilotos: Map<Piloto, Int>,
    val puntosEquipos: Map<String, Int>,
    val calendario: List<Circuito>,
    val dificultad: Int,
    val historialCarreras: List<ResultadoCarrera> = emptyList()
) {
    fun obtenerPosicionJugador(): Int {
        return puntosPilotos.entries
            .sortedByDescending { it.value }
            .indexOfFirst { it.key == pilotoJugador } + 1
    }

    fun temporadaCompletada(): Boolean = carreraActual > totalCarreras
}