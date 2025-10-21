package org.example.motogp

import org.example.motogp.carrera.ModoCarreraManager
import org.example.motogp.enums.Nacionalidad
import org.example.motogp.models.crearPilotoElite
import org.example.motogp.simulacion.SimuladorCarreraSimple

/**
 * Función principal del simulador de MotoGP
 */
fun main() {
    println("🏍️ SIMULADOR DE MOTOGP - MODO CARRERA 🏁")
    println("=" * 45)
    
    demoModoCarreraCompleto()
}

// === NUEVA FUNCIÓN QUE DEBES AGREGAR ===
fun demoModoCarreraCompleto() {
    println("\n🎮 DEMO COMPLETO - MODO CARRERA MANAGER")
    println("=" * 50)
    
    // Crear el gestor con inyección de dependencias
    val simulador = SimuladorCarreraSimple()
    val manager = ModoCarreraManager(simulador)
    
    // PASO 1: Crear piloto jugador
    val pilotoJugador = crearPilotoElite("Alex Rins", Nacionalidad.ESPANA, 27)
    
    // PASO 2: Configurar e iniciar temporada
    manager.configurarTemporada(4) // 4 carreras
    manager.iniciarNuevaCarrera(pilotoJugador, 75)
    
    println("🎯 TEMPORADA INICIADA")
    println(manager.obtenerEstadoJugador())
    
    // PASO 3: Simular todas las carreras
    while (manager.temporadaEnCurso() && !manager.temporadaFinalizada()) {
        val proximaCarrera = manager.obtenerProximaCarrera()
        println("\n🏁 PRÓXIMA CARRERA: ${proximaCarrera?.nombre ?: "Final"}")
        
        val resultado = manager.simularSiguienteCarrera()
        println(resultado.resumen())
        
        // Mostrar clasificación actualizada
        println("\n📊 CLASIFICACIÓN ACTUAL:")
        manager.obtenerClasificacionGeneral().forEach { (piloto, puntos) ->
            val emoji = if (piloto == pilotoJugador) "🎯" else "👤"
            println("$emoji ${piloto.nombre}: $puntos pts")
        }
    }
    
    // PASO 4: Finalizar temporada
    println("\n🏆 FIN DE TEMPORADA")
    val resumen = manager.finalizarTemporada()
    println(resumen)
}

// === MANTÉN TODAS ESTAS FUNCIONES EXISTENTES (las puedes comentar si quieres) ===

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
fun Double.format(digits: Int) = "%.${digits}f".format(this)

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

fun demoSIMULADOR() {
    println("\n🎮 DEMO DEL SIMULADOR DE CARRERAS")
    println("=" * 45)
    
    val simulador = SimuladorCarreraSimple()
    
    // Crear equipos y pilotos
    val ducati = crearEquipoDucatiLenovo()
    val honda = crearEquipoRepsolHonda()
    val yamaha = crearEquipoYamaha()
    
    val bagnaia = crearPilotoElite("Francesco Bagnaia", Nacionalidad.ITALIA, 26).apply {
        ducati.ficharPiloto(this)
    }
    val bastianini = crearPilotoExcelente("Enea Bastianini", Nacionalidad.ITALIA, 25).apply {
        ducati.ficharPiloto(this)
    }
    val marquez = crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30).apply {
        honda.ficharPiloto(this)
    }
    val quartararo = crearPilotoExcelente("Fabio Quartararo", Nacionalidad.FRANCIA, 24).apply {
        yamaha.ficharPiloto(this)
    }
    
    val pilotos = listOf(bagnaia, bastianini, marquez, quartararo)
    
    // Simular carreras en diferentes circuitos
    val circuitos = listOf(
        CIRCUITO_JEREZ to "Jerez",
        CIRCUITO_MUGELO to "Mugello", 
        CIRCUITO_ASSEN to "Assen"
    )
    
    circuitos.forEach { (circuito, nombre) ->
        println("\n🏁 CARRERA EN: ${circuito.nombre}")
        println("📏 ${circuito.descripcion()}")
        
        val resultado = simulador.simular(pilotos, circuito)
        println(resultado.resumen())
        
        // Mostrar puntuación del campeonato simplificada
        println("\n📊 PUNTUACIONES:")
        resultado.posiciones.forEachIndexed { index, piloto ->
            val puntos = when (index) {
                0 -> 25
                1 -> 20
                2 -> 16
                3 -> 13
                4 -> 11
                else -> 0
            }
            println("${index + 1}. ${piloto.nombre} - $puntos pts")
        }
    }
    
    // Demo de simulación con pesos personalizados
    println("\n⚖️  SIMULACIÓN CON PESOS PERSONALIZADOS (70% habilidades, 30% moto)")
    val resultadoPersonalizado = simulador.simularConPesos(
        pilotos = pilotos,
        circuito = CIRCUITO_SILVERSTONE,
        pesoHabilidades = 0.7,
        pesoMoto = 0.3
    )
    println(resultadoPersonalizado.resumen())
}
