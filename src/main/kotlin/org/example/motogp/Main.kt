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

fun demoEquipos() {
    println("\n🏁 DEMO DE EQUIPOS MOTOGP")
    println("=" * 40)
    
    // Crear equipos
    val ducati = crearEquipoDucatiLenovo()
    val honda = crearEquipoRepsolHonda()
    val yamaha = crearEquipoYamaha()
    
    // Crear pilotos
    val bagnaia = crearPilotoElite("Francesco Bagnaia", Nacionalidad.ITALIA, 26)
    val bastianini = crearPilotoExcelente("Enea Bastianini", Nacionalidad.ITALIA, 25)
    val marquez = crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30)
    val quartararo = crearPilotoExcelente("Fabio Quartararo", Nacionalidad.FRANCIA, 24)
    
    // Fichar pilotos
    ducati.ficharPiloto(bagnaia)
    ducati.ficharPiloto(bastianini)
    honda.ficharPiloto(marquez)
    yamaha.ficharPiloto(quartararo)
    
    // Mostrar información de equipos
    listOf(ducati, honda, yamaha).forEach { equipo ->
        println(equipo.descripcion())
        println()
    }
}

// Llamar a la demo desde main()
fun main() {
    println("🏍️ SIMULADOR DE MOTOGP 🏁")
    println("=" * 30)
    
    demoEquipos()
}
