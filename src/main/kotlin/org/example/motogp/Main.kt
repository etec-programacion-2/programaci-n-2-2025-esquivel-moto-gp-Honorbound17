package org.example.motogp

import org.example.motogp.enums.Nacionalidad
import org.example.motogp.enums.RangoHabilidad
import org.example.motogp.models.Habilidades
import org.example.motogp.models.Piloto
import org.example.motogp.models.crearPilotoElite
import org.example.motogp.models.crearMotoDucatiGP24

/**
 * Función principal del simulador de MotoGP
 */
fun main() {
    println("🏍️ SIMULADOR DE MOTOGP 🏁")
    println("=" * 30)
    
    // Crear un piloto de ejemplo
    val piloto = crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30)
    val moto = crearMotoDucatiGP24()
    
    // Mostrar información
    mostrarInformacionPiloto(piloto)
    println()
    mostrarInformacionMoto(moto)
}

fun mostrarInformacionPiloto(piloto: Piloto) {
    println("👤 PILOTO:")
    println("Nombre: ${piloto.nombre}")
    println("Nacionalidad: ${piloto.nacionalidad}")
    println("Edad: ${piloto.edad} años")
    println("Rango: ${piloto.rango}")
    println("Habilidades:")
    println("  - Velocidad: ${piloto.habilidades.velocidadFinal.format(2)}")
    println("  - Frenada: ${piloto.habilidades.frenadaFinal.format(2)}")
    println("  - Curvas: ${piloto.habilidades.pasoPorCurvaFinal.format(2)}")
    println("  - Promedio: ${piloto.habilidades.promedioHabilidades().format(2)}")
}

fun mostrarInformacionMoto(moto: Moto) {
    println("🏍️ MOTO:")
    println("Fabricante: ${moto.marca}")
    println("Modelo: ${moto.modelo}")
    println("Rendimiento:")
    println("  - Velocidad máxima: ${moto.rendimiento.velocidadMaxima}")
    println("  - Aceleración: ${moto.rendimiento.aceleracion}")
    println("  - Maniobrabilidad: ${moto.rendimiento.maniobrabilidad}")
    println("  - Puntaje total: ${moto.rendimiento.puntajeTotal()}")
}

// Función de extensión para formatear números
fun Double.format(digits: Int) = "% .${digits}f".format(this)

// Función de extensión para repetir strings (útil para separadores)
operator fun String.times(n: Int) = this.repeat(n)
