package org.example.motogp.models

import org.example.motogp.enums.FabricanteMoto

/**
 * Data class que representa una moto de MotoGP.
 * @property fabricante Fabricante de la moto
 * @property modelo Nombre del modelo de la moto
 * @property rendimiento Objeto encapsulado con el rendimiento de la moto
 */
data class Moto(
    val fabricante: FabricanteMoto,
    val modelo: String,
    val rendimiento: Rendimiento
) {
    init {
        require(modelo.isNotBlank()) { "El modelo no puede estar vacío" }
    }

    /**
     * Obtiene el nombre del fabricante de la moto
     */
    val marca: String
        get() = when (fabricante) {
            FabricanteMoto.DUCATI -> "Ducati"
            FabricanteMoto.YAMAHA -> "Yamaha"
            FabricanteMoto.HONDA -> "Honda"
            FabricanteMoto.SUZUKI -> "Suzuki"
            FabricanteMoto.KAWASAKI -> "Kawasaki"
        }

    /**
     * Descripción completa de la moto
     */
    fun descripcion(): String {
        return "$marca $modelo - Vel: ${rendimiento.velocidadMaxima} | " +
               "Acel: ${rendimiento.aceleracion} | Man: ${rendimiento.maniobrabilidad}"
    }
}

// Funciones de fábrica para crear motos de diferentes fabricantes
fun crearMotoDucatiGP24(): Moto {
    return Moto(
        fabricante = FabricanteMoto.DUCATI,
        modelo = "Desmosedici GP24",
        rendimiento = Rendimiento(
            velocidadMaxima = 98,
            aceleracion = 95,
            maniobrabilidad = 90
        )
    )
}

fun crearMotoYamahaM1(): Moto {
    return Moto(
        fabricante = FabricanteMoto.YAMAHA,
        modelo = "YZR-M1",
        rendimiento = Rendimiento(
            velocidadMaxima = 95,
            aceleracion = 92,
            maniobrabilidad = 96
        )
    )
}
