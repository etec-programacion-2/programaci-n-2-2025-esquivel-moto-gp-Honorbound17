package org.example.motogp.constants

import org.example.motogp.models.Circuito
import org.example.motogp.enums.Nacionalidad

object CircuitosConstants {
    val CIRCUITO_JEREZ = Circuito(
        nombre = "Circuito de Jerez",
        pais = Nacionalidad.ESPAÑOL,
        longitud = 4.423,
        curvas = 13,
        dificultad = 7
    )
    
    val CIRCUITO_MUGELO = Circuito(
        nombre = "Mugello Circuit",
        pais = Nacionalidad.ITALIANO,
        longitud = 5.245,
        curvas = 15,
        dificultad = 9
    )
    
    val CIRCUITO_ASSEN = Circuito(
        nombre = "TT Circuit Assen",
        pais = Nacionalidad.HOLANDÉS,
        longitud = 4.542,
        curvas = 18,
        dificultad = 8
    )
    
    val CIRCUITO_SILVERSTONE = Circuito(
        nombre = "Silverstone Circuit",
        pais = Nacionalidad.INGLÉS,
        longitud = 5.891,
        curvas = 18,
        dificultad = 9
    )
}