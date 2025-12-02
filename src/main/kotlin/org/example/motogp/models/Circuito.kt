package org.example.motogp.models

import org.example.motogp.enums.Nacionalidad

/**
 * Data class que representa un circuito de MotoGP.
 * 
 * @property nombre Nombre oficial del circuito
 * @property pais País donde se encuentra el circuito
 * @property longitud Longitud del circuito en metros
 * @property curvas Numero de curvas del circuito
 * @property rectaPrincipal Longitud de la recta principal en metros
 * @property dificultad Nivel de dificultad del circuito (1-100)
 */
data class Circuito(
    val nombre: String,
    val pais: Nacionalidad,
    val longitud: Double,
    val curvas: Int,
    val rectaPrincipal: Double,
    val dificultad: Int
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del circuito no puede estar vacío" }
        require(longitud > 0) { "La longitud debe ser mayor a 0" }
        require(curvas > 0) { "El circuito debe tener al menos 1 curva" }
        require(rectaPrincipal > 0) { "La recta principal debe tener longitud positiva" }
        require(dificultad in 1..100) { "La dificultad debe estar entre 1 y 100" }
    }
    
    /**
     * Calcula un factor de dificultad para pilotos basado en las características del circuito.
     */
    fun factorDificultad(): Double {
        return (dificultad / 100.0) * (curvas / 20.0) * (longitud / 5000.0)
    }
    
    /**
     * Descripción del circuito.
     */
    fun descripcion(): String {
        return "$nombre (${pais}) - ${longitud.toInt()}m, $curvas curvas, Recta: ${rectaPrincipal.toInt()}m"
    }
}

// Circuitos famosos de MotoGP
val CIRCUITO_JEREZ = Circuito(
    nombre = "Circuito de Jerez",
    pais = Nacionalidad.ESPAÑA,
    longitud = 4.423,
    curvas = 13,
    dificultad = 7,
    rectaPrincipal = 600.0
)

val CIRCUITO_MUGELO = Circuito(
    nombre = "Mugello Circuit",
    pais = Nacionalidad.ITALIA,
    longitud = 5245.0,
    curvas = 15,
    rectaPrincipal = 1141.0,
    dificultad = 90
)

val CIRCUITO_ASSEN = Circuito(
    nombre = "TT Circuit Assen",
    pais = Nacionalidad.ITALIA,
    longitud = 4542.0,
    curvas = 18,
    rectaPrincipal = 560.0,
    dificultad = 88
)

val CIRCUITO_SILVERSTONE = Circuito(
    nombre = "Silverstone Circuit",
    pais = Nacionalidad.USA,
    longitud = 5900.0,
    curvas = 18,
    rectaPrincipal = 770.0,
    dificultad = 87
)
