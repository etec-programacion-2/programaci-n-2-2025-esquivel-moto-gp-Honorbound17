package org.example.motogp.models

import org.example.motogp.enums.Nacionalidad
import org.example.motogp.enums.RangoHabilidad
import kotlin.math.max
import kotlin.math.min

/**
 * Data class que representa un piloto de MotoGP.
 * @property nombre Nombre completo del piloto
 * @property nacionalidad Nacionalidad del piloto
 * @property edad Edad del piloto (debe ser mayor o igual a 18)
 * @property habilidades Objeto encapsulado con las habilidades del piloto
 */
data class Piloto(
    val nombre: String,
    val nacionalidad: Nacionalidad,
    val edad: Int,
    val habilidades: Habilidades,
    val equipo: Equipo? = null
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

/**
 * Función de extensión para ajustar valores al rango 1-100
 */
fun Int.coerceInHabilidadRange(): Int {
    return max(1, min(100, this))
}

// Funciones de fábrica para crear pilotos de diferentes rangos
fun crearPilotoElite(nombre: String, nacionalidad: Nacionalidad, edad: Int): Piloto {
    return Piloto(
        nombre = nombre,
        nacionalidad = nacionalidad,
        edad = edad,
        habilidades = Habilidades(
            velocidadBase = (90..100).random(),
            frenadaBase = (85..95).random(),
            pasoPorCurvaBase = (95..100).random(),
            rango = RangoHabilidad.S
        )
    )
}

fun crearPilotoExcelente(nombre: String, nacionalidad: Nacionalidad, edad: Int): Piloto {
    return Piloto(
        nombre = nombre,
        nacionalidad = nacionalidad,
        edad = edad,
        habilidades = Habilidades(
            velocidadBase = (80..90).random(),
            frenadaBase = (75..85).random(),
            pasoPorCurvaBase = (85..95).random(),
            rango = RangoHabilidad.A
        )
    )
}

fun crearPilotoBueno(nombre: String, nacionalidad: Nacionalidad, edad: Int): Piloto {
    return Piloto(
        nombre = nombre,
        nacionalidad = nacionalidad,
        edad = edad,
        habilidades = Habilidades(
            velocidadBase = (70..84).random(),
            frenadaBase = (60..75).random(),
            pasoPorCurvaBase = (60..74).random(),
            rango = RangoHabilidad.B
        )
    )
}