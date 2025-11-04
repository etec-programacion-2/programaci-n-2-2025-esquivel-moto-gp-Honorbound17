package org.example.motogp

import org.example.motogp.carrera.ModoCarreraManager
import org.example.motogp.enums.Nacionalidad
import org.example.motogp.models.crearPilotoElite
import org.example.motogp.models.crearPilotoExcelente
import org.example.motogp.models.crearPilotoBueno
import org.example.motogp.simulacion.SimuladorCarreraSimple
import kotlin.system.exitProcess

/**
 * PUNTO DE ENTRADA PRINCIPAL DE LA APLICACIÓN
 */
fun main() {
    println("🏍️".repeat(5) + " MOTOGP LEGACY " + "🏍️".repeat(5))
    println("=" * 50)
    
    val gestorCarrera = ModoCarreraManager(SimuladorCarreraSimple())
    val interfazUsuario = InterfazUsuario(gestorCarrera)
    
    // Bucle principal de la aplicación
    interfazUsuario.ejecutar()
}

/**
 * Clase que maneja exclusivamente la interfaz de usuario.
 */
class InterfazUsuario(private val gestorCarrera: org.example.motogp.carrera.GestionModoCarrera) {
    
    /**
     * BUCLE PRINCIPAL DE LA APLICACIÓN
     */
    fun ejecutar() {
        var ejecutando = true
        
        while (ejecutando) {
            mostrarMenuPrincipal()
            
            when (obtenerOpcionUsuario(1, 4)) {
                1 -> iniciarNuevaPartida()
                2 -> cargarPartida()
                3 -> mostrarCreditos()
                4 -> {
                    println("👋 ¡Gracias por jugar al MotoGP Simulator!")
                    ejecutando = false
                }
            }
        }
    }
    
    /**
     * Muestra el menú principal de la aplicación
     */
    private fun mostrarMenuPrincipal() {
        println("\n" + "🎮".repeat(20))
        println("          MENÚ PRINCIPAL")
        println("🎮".repeat(20))
        println("1. 🆕 Nueva Partida")
        println("2. 📂 Cargar Partida") 
        println("3. ℹ️  Créditos")
        println("4. ❌ Salir")
        println("🎮".repeat(20))
        print("Selecciona una opción (1-4): ")
    }
    
    /**
     * Maneja el flujo de creación de una nueva partida
     */
    private fun iniciarNuevaPartida() {
        println("\n" + "🚀".repeat(25))
        println("       NUEVA PARTIDA")
        println("🚀".repeat(25))
        
        // Crear piloto jugador
        val pilotoJugador = crearPilotoJugador()
        
        // Configurar dificultad
        val dificultad = configurarDificultad()
        
        // Configurar temporada
        val numeroCarreras = configurarTemporada()
        
        // Iniciar la partida
        gestorCarrera.configurarTemporada(numeroCarreras)
        gestorCarrera.iniciarNuevaCarrera(pilotoJugador, dificultad)
        
        println("\n✅ ¡Partida creada exitosamente!")
        println("👤 Piloto: ${pilotoJugador.nombre}")
        println("📅 Temporada: $numeroCarreras carreras")
        println("⚡ Dificultad: $dificultad/100")
        
        // Entrar al menú de partida en curso
        menuPartidaEnCurso()
    }
    
    /**
     * Crea el piloto del jugador mediante interacción por consola
     */
    private fun crearPilotoJugador(): org.example.motogp.models.Piloto {
        println("\n👤 CREACIÓN DE PILOTO")
        println("-".repeat(25))
        
        print("Nombre de tu piloto: ")
        val nombre = readln().trim().takeIf { it.isNotBlank() } ?: "Piloto Novato"
        
        println("\n🏳️  Selecciona tu nacionalidad:")
        org.example.motogp.enums.Nacionalidad.values().forEachIndexed { index, nacionalidad ->
            println("${index + 1}. ${nacionalidad.name}")
        }
        val nacionalidadIndex = obtenerOpcionUsuario(1, org.example.motogp.enums.Nacionalidad.values().size) - 1
        val nacionalidad = org.example.motogp.enums.Nacionalidad.values()[nacionalidadIndex]
        
        println("\n🎯 Selecciona tu nivel de experiencia:")
        println("1. 🥇 Élite (Rango S) - Para expertos")
        println("2. 🥈 Profesional (Rango A) - Para jugadores avanzados") 
        println("3. 🥉 Semiprofesional (Rango B) - Para jugadores intermedios")
        println("4. 🔰 Novato (Rango C) - Para principiantes")
        
        return when (obtenerOpcionUsuario(1, 4)) {
            1 -> crearPilotoElite(nombre, nacionalidad, 25)
            2 -> crearPilotoExcelente(nombre, nacionalidad, 24)
            3 -> crearPilotoBueno(nombre, nacionalidad, 23)
            4 -> crearPilotoBueno(nombre, nacionalidad, 22).copy(
                habilidades = crearPilotoBueno(nombre, nacionalidad, 22).habilidades.copy(
                    velocidadBase = 60,
                    frenadaBase = 55,
                    pasoPorCurvaBase = 58
                )
            )
            else -> crearPilotoBueno(nombre, nacionalidad, 23)
        }
    }
    
    /**
     * Configura la dificultad de la partida
     */
    private fun configurarDificultad(): Int {
        println("\n⚡ CONFIGURAR DIFICULTAD")
        println("-".repeat(25))
        println("1. 🟢 Fácil (25) - Para principiantes")
        println("2. 🟡 Normal (50) - Jugadores ocasionales")
        println("3. 🟠 Difícil (75) - Jugadores experimentados")
        println("4. 🔴 Élite (90) - Solo para expertos")
        println("5. 🎯 Personalizada - Elige tu nivel")
        
        return when (obtenerOpcionUsuario(1, 5)) {
            1 -> 25
            2 -> 50
            3 -> 75
            4 -> 90
            5 -> {
                print("Introduce el nivel de dificultad (1-100): ")
                obtenerOpcionUsuario(1, 100)
            }
            else -> 50
        }
    }
    
    /**
     * Configura la duración de la temporada
     */
    private fun configurarTemporada(): Int {
        println("\n📅 CONFIGURAR TEMPORADA")
        println("-".repeat(25))
        println("1. 🏁 Corta (5 carreras) - Para partidas rápidas")
        println("2. 🏆 Normal (10 carreras) - Experiencia estándar")
        println("3. 🏅 Larga (15 carreras) - Para jugadores dedicados")
        println("4. 📊 Personalizada - Elige el número de carreras")
        
        return when (obtenerOpcionUsuario(1, 4)) {
            1 -> 5
            2 -> 10
            3 -> 15
            4 -> {
                print("Introduce el número de carreras (3-20): ")
                obtenerOpcionUsuario(3, 20)
            }
            else -> 10
        }
    }
    
    /**
     * Maneja el flujo de carga de partida
     */
    private fun cargarPartida() {
        println("\n" + "📂".repeat(25))
        println("       CARGAR PARTIDA")
        println("📂".repeat(25))

        print("Nombre de la partida a cargar: ")
        val nombreArchivo = readln().trim().takeIf { it.isNotBlank() } ?: "partida_guardada"
        
        println("\n⏳ Cargando partida...")
        val exito = gestorCarrera.cargarPartida(nombreArchivo)
        
        if (exito) {
            println("✅ Partida cargada exitosamente!")
            menuPartidaEnCurso()
        } else {
            println("❌ No se pudo cargar la partida '$nombreArchivo'")
            println("💡 Asegúrate de que el archivo existe y es válido")
        }
    }
    
    /**
     * Menú principal cuando hay una partida en curso
     */
    private fun menuPartidaEnCurso() {
        var enPartida = true
        
        while (enPartida && gestorCarrera.temporadaEnCurso()) {
            mostrarMenuPartida()
            
            when (obtenerOpcionUsuario(1, 6)) {
                1 -> simularSiguienteCarrera()    // Opción 1: Simular carrera
                2 -> mostrarEstadoActual()        // Opción 2: Ver estado
                3 -> mostrarClasificacion()       // Opción 3: Ver clasificación
                4 -> guardarPartidaActual()       // Opción 4: Guardar
                5 -> menuGestionEquipo()          // Opción extra
                6 -> {
                    println("← Volviendo al menú principal")
                    enPartida = false
                }
            }
        }
        
        // Si salimos porque terminó la temporada
        if (gestorCarrera.temporadaFinalizada()) {
            println("\n🏆 TEMPORADA FINALIZADA!")
            val resumen = gestorCarrera.finalizarTemporada()
            println(resumen)
        }
    }
    
    /**
     * Muestra el menú de partida en curso
     */
    private fun mostrarMenuPartida() {
        val progreso = gestorCarrera.obtenerProgresoTemporada()
        val proximaCarrera = gestorCarrera.obtenerProximaCarrera()
        
        println("\n" + "🏁".repeat(25))
        println("     PARTIDA EN CURSO")
        println("🏁".repeat(25))
        println("📊 Progreso: ${progreso.first}/${progreso.second} carreras")
        proximaCarrera?.let { println("📍 Próxima: ${it.nombre}") }
        println("🏁".repeat(25))
        println("1. ▶️  Simular siguiente carrera")
        println("2. 📈 Ver estado actual")
        println("3. 🏆 Ver clasificación")
        println("4. 💾 Guardar partida")
        println("5. 🏍️  Gestión de equipo")
        println("6. ← Volver al menú principal")
        println("🏁".repeat(25))
        print("Selecciona una opción (1-6): ")
    }
    
    /**
     * Simula la siguiente carrera del calendario
     */
    private fun simularSiguienteCarrera() {
        println("\n⏳ Simulando carrera...")
        val resultado = gestorCarrera.simularSiguienteCarrera()
        
        println("\n" + "🏁".repeat(40))
        println("         RESULTADO DE CARRERA")
        println("🏁".repeat(40))
        println(resultado.resumen())  // ← Muestra resultado claro y formateado
        println("🏁".repeat(40))
        
        // Pausa para que el usuario pueda leer los resultados
        println("\n⏎ Presiona Enter para continuar...")
        readln()
    }
    
    /**
     * Muestra el estado actual del jugador
     */
    private fun mostrarEstadoActual() {
        println("\n" + "📊".repeat(25))
        println("       ESTADO ACTUAL")
        println("📊".repeat(25))
        println(gestorCarrera.obtenerEstadoJugador())
        println("📊".repeat(25))
    }
    
    /**
     * Muestra la clasificación actual
     */
    private fun mostrarClasificacion() {
        println("\n" + "🏆".repeat(25))
        println("     CLASIFICACIÓN ACTUAL")
        println("🏆".repeat(25))
        
        val clasificacion = gestorCarrera.obtenerClasificacionGeneral()
        if (clasificacion.isEmpty()) {
            println("No hay datos de clasificación disponibles")
        } else {
            clasificacion.forEachIndexed { index, (piloto, puntos) ->
                val posicion = index + 1
                val emoji = when (posicion) {
                    1 -> "🥇"
                    2 -> "🥈" 
                    3 -> "🥉"
                    else -> "🔸"
                }
                println("$emoji $posicion. ${piloto.nombre} - $puntos pts")
            }
        }
        println("🏆".repeat(25))
    }
    
    /**
     * Guarda la partida actual
     */
    private fun guardarPartidaActual() {
        print("\n💾 Nombre para guardar la partida: ")
        val nombreArchivo = readln().trim().takeIf { it.isNotBlank() } ?: "partida_guardada"
        
        val exito = gestorCarrera.guardarPartida(nombreArchivo)
        if (exito) {
            println("✅ Partida guardada como '$nombreArchivo.motojson'")
        } else {
            println("❌ Error al guardar la partida")
        }
    }
    
    /**
     * Menú de gestión de equipo
     */
    private fun menuGestionEquipo() {
        println("\n🏍️ Gestión de equipo - En desarrollo")
        println("💡 Próximamente: fichajes, mejoras de moto, etc.")
        println("⏎ Presiona Enter para continuar...")
        readln()
    }
    
    /**
     * Muestra los créditos del juego
     */
    private fun mostrarCreditos() {
        println("\n" + "⭐".repeat(25))
        println("         CRÉDITOS")
        println("⭐".repeat(25))
        println("🏍️  MotoGP Simulator")
        println("🎮 Desarrollado con Kotlin")
        println("💡 Arquitectura: Patrón Fachada")
        println("📊 Simulador: Sistema de rangos S-A-B-C-D")
        println("💾 Persistencia: JSON con kotlinx.serialization")
        println("⭐".repeat(25))
        println("⏎ Presiona Enter para continuar...")
        readln()
    }
    
    /**
     * Obtiene una opción válida del usuario
     */
    private fun obtenerOpcionUsuario(min: Int, max: Int): Int {
        while (true) {
            try {
                val input = readln().toInt()
                if (input in min..max) {
                    return input
                } else {
                    println("❌ Por favor, introduce un número entre $min y $max: ")
                }
            } catch (e: NumberFormatException) {
                println("❌ Entrada inválida. Por favor, introduce un número: ")
            }
        }
    }
}

// Función de extensión para formatear números
fun Double.format(digits: Int) = "%.${digits}f".format(this)

// Función de extensión para repetir strings (útil para separadores)
operator fun String.times(n: Int) = this.repeat(n)