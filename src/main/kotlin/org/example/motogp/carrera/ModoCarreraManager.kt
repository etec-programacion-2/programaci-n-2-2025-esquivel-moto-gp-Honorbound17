package org.example.motogp.carrera

import org.example.motogp.models.Circuito
import org.example.motogp.models.Piloto
import org.example.motogp.models.Equipo
import org.example.motogp.models.crearEquipoDucatiLenovo
import org.example.motogp.models.crearEquipoRepsolHonda
import org.example.motogp.models.crearEquipoYamaha
import org.example.motogp.models.crearPilotoElite
import org.example.motogp.models.crearPilotoExcelente
import org.example.motogp.models.crearPilotoBueno
import org.example.motogp.enums.Nacionalidad
import org.example.motogp.simulacion.ResultadoCarrera
import org.example.motogp.simulacion.SimuladorCarrera
import org.example.motogp.simulacion.SimuladorCarreraSimple
import kotlin.random.Random

class ModoCarreraManager(
    private val simulador: SimuladorCarrera = SimuladorCarreraSimple()
) : GestionModoCarrera {

    // --- ESTADO INTERNO DEL MODO CARRERA ---
    private var estadoTemporada: EstadoTemporada? = null
    private var pilotoJugador: Piloto? = null
    private var dificultad: Int = 50

    // Calendario de circuitos (mutable interno)
    private val calendario: MutableList<Circuito> = mutableListOf()

    // Clasificaciones
    private val puntosPilotos: MutableMap<Piloto, Int> = mutableMapOf()
    private val puntosEquipos: MutableMap<String, Int> = mutableMapOf()

    // Equipos participantes
    private val equipos: MutableList<Equipo> = mutableListOf()
    private val pilotosCPU: MutableList<Piloto> = mutableListOf()

    // Historial de carreras
    private val historialCarreras: MutableList<ResultadoCarrera> = mutableListOf()

    // --- IMPLEMENTACIÓN DE LA INTERFAZ ---
    override fun iniciarNuevaCarrera(pilotoJugador: Piloto, dificultad: Int) {
        require(dificultad in 1..100) { "La dificultad debe estar entre 1 y 100" }

        if (temporadaEnCurso()) {
            throw IllegalStateException("Ya hay una temporada en curso. Finaliza la actual antes de iniciar una nueva.")
        }

        this.pilotoJugador = pilotoJugador
        this.dificultad = dificultad

        // Inicializar equipos y pilotos CPU
        inicializarEquiposYCalendario()

        // Inicializar puntos/estado
        puntosPilotos.clear()
        puntosEquipos.clear()
        historialCarreras.clear()

        // Agregar al Piloto Jugador a los puntos
        puntosPilotos[pilotoJugador] = 0

        // Estado inicial de temporada
        estadoTemporada = EstadoTemporada(
            pilotoJugador = pilotoJugador,
            carreraActual = 1,
            totalCarreras = calendario.size,
            puntosPilotos = puntosPilotos.toMap(),
            puntosEquipos = puntosEquipos.toMap(),
            calendario = calendario.toList(),
            dificultad = dificultad
        )

        println("✅ Nueva temporada iniciada con ${calendario.size} carreras")
        println("🎯 Piloto: ${pilotoJugador.nombre}")
        println("⚡ Dificultad: $dificultad")
    }

    override fun configurarTemporada(numeroCarreras: Int, equiposParticipantes: List<String>) {
        require(numeroCarreras > 0) { "El número de carreras debe ser mayor a 0" }

        val circuitosDisponibles = listOf(
            org.example.motogp.models.CIRCUITO_JEREZ,
            org.example.motogp.models.CIRCUITO_MUGELO,
            org.example.motogp.models.CIRCUITO_ASSEN,
            org.example.motogp.models.CIRCUITO_SILVERSTONE
        )

        calendario.clear()
        repeat(numeroCarreras) {
            calendario.add(circuitosDisponibles.random())
        }

        println("📅 Temporada configurada con $numeroCarreras carreras")
    }

    override fun simularSiguienteCarrera(): ResultadoCarrera {
        val estado = estadoTemporada ?: throw IllegalStateException("No hay temporada en curso")

        if (temporadaFinalizada()) {
            throw IllegalStateException("La temporada ya ha finalizado")
        }

        val carreraActual = estado.carreraActual
        val circuito = calendario.getOrNull(carreraActual - 1)
            ?: throw IllegalStateException("No hay más carreras en el calendario")

        println("\n🏁 Simulando carrera $carreraActual/${calendario.size}")
        println("📍 Circuito: ${circuito.nombre}")

        val pilotosCarrera = mutableListOf<Piloto>()
        pilotoJugador?.let { pilotosCarrera.add(it) }
        pilotosCarrera.addAll(pilotosCPU)

        val resultado = simulador.simular(pilotosCarrera, circuito)

        actualizarPuntosCarrera(resultado)
        historialCarreras.add(resultado)

        // Actualizar estadoTemporada (creamos copy para inmutabilidad)
        estadoTemporada = estado.copy(
            carreraActual = carreraActual + 1,
            puntosPilotos = puntosPilotos.toMap(),
            puntosEquipos = puntosEquipos.toMap(),
            historialCarreras = historialCarreras.toList()
        )

        println("✅ Carrera simulada - Ganador: ${resultado.obtenerGanador().nombre}")
        return resultado
    }

    override fun avanzarSiguienteEvento() {
        val estado = estadoTemporada ?: throw IllegalStateException("No hay temporada en curso")
        if (temporadaFinalizada()) throw IllegalStateException("La temporada ya ha finalizado")

        estadoTemporada = estado.copy(
            carreraActual = estado.carreraActual + 1
        )

        println("⏭️  Evento avanzado. Carrera actual: ${estado.carreraActual + 1}/${calendario.size}")
    }

    override fun simularCarreraPersonalizada(circuito: Circuito, pilotos: List<Piloto>): ResultadoCarrera {
        require(pilotos.size >= 2) { "Se necesitan al menos 2 pilotos para una carrera" }
        println("🎮 Carrera personalizada en ${circuito.nombre}")
        println("👥 Pilotos: ${pilotos.joinToString { it.nombre }}")
        return simulador.simular(pilotos, circuito)
    }

    override fun obtenerClasificacionGeneral(): Map<Piloto, Int> {
        return puntosPilotos.entries.sortedByDescending { it.value }.associate { it.toPair() }
    }

    override fun obtenerEstadoJugador(): String {
        val piloto = pilotoJugador ?: return "❌ No hay piloto jugador activo"
        val estado = estadoTemporada ?: return "❌ No hay temporada en curso"

        val posicion = estado.obtenerPosicionJugador()
        val puntos = puntosPilotos[piloto] ?: 0
        val equipo = piloto.equipo?.nombre ?: "Sin equipo"
        val carreraActual = estado.carreraActual.coerceAtMost(calendario.size)

        return """
        🎯 ESTADO DEL JUGADOR
        👤 Piloto: ${piloto.nombre}
        🏁 Posición: $posicion
        📊 Puntos: $puntos
        🏍️  Equipo: $equipo
        📅 Carrera: $carreraActual/${calendario.size}
        ⚡ Dificultad: $dificultad
        """.trimIndent()
    }

    override fun obtenerClasificacionConstructores(): Map<String, Int> {
        return puntosEquipos.entries.sortedByDescending { it.value }.associate { it.toPair() }
    }

    override fun obtenerCalendario(): List<Circuito> = calendario.toList()

    override fun obtenerProximaCarrera(): Circuito? {
        val estado = estadoTemporada ?: return null
        return calendario.getOrNull(estado.carreraActual - 1)
    }

    override fun obtenerProgresoTemporada(): Pair<Int, Int> {
        val estado = estadoTemporada ?: return Pair(0, 0)
        return Pair(estado.carreraActual - 1, calendario.size)
    }

    override fun intentarFichaje(equipoDestino: String): Boolean {
        val piloto = pilotoJugador ?: return false
        val equipo = equipos.find { it.nombre == equipoDestino } ?: return false

        val exitoFichaje = Random.nextDouble() < 0.3 + (dificultad / 200.0)
        if (exitoFichaje) {
            piloto.equipo?.darDeBajaPiloto(piloto)
            equipo.ficharPiloto(piloto)
            println("✅ ¡Fichaje exitoso! ${piloto.nombre} se une a $equipoDestino")
            return true
        } else {
            println("❌ Fichaje fallido. $equipoDestino no aceptó la oferta")
            return false
        }
    }

    override fun mejorarHabilidad(tipoHabilidad: String, puntos: Int): Boolean {
        println("🛠️  Mejora de habilidad '$tipoHabilidad' en $puntos puntos (simulado)")
        return true
    }

    override fun obtenerEquiposDisponibles(): List<String> = equipos.map { it.nombre }

    override fun temporadaEnCurso(): Boolean = estadoTemporada != null && !temporadaFinalizada()

    override fun temporadaFinalizada(): Boolean {
        val estado = estadoTemporada ?: return false
        // Basar la comprobación en totalCarreras cargado en el estado (más fiable)
        return estado.carreraActual > estado.totalCarreras
    }

    // Implementación requerida: finalizarTemporada
    override fun finalizarTemporada(): String {
        val estado = estadoTemporada ?: throw IllegalStateException("No hay temporada en curso")
        val ganador = obtenerClasificacionGeneral().entries.firstOrNull()
        val ganadorConstructores = obtenerClasificacionConstructores().entries.firstOrNull()

        val resumen = """
        🏆 TEMPORADA FINALIZADA 🏆

        🥇 Campeón de Pilotos: ${ganador?.key?.nombre ?: "N/A"} - ${ganador?.value ?: 0} puntos
        🏍️  Campeón de Constructores: ${ganadorConstructores?.key ?: "N/A"} - ${ganadorConstructores?.value ?: 0} puntos

        📊 Posición del jugador: ${estado.obtenerPosicionJugador()}
        📈 Puntos del jugador: ${puntosPilotos[pilotoJugador] ?: 0}

        🏁 Carreras disputadas: ${historialCarreras.size}
        """.trimIndent()

        estadoTemporada = null
        pilotoJugador = null
        puntosPilotos.clear()
        puntosEquipos.clear()
        historialCarreras.clear()

        return resumen
    }

    override fun guardarProgreso(nombreArchivo: String): Boolean {
        val estado = estadoTemporada ?: return false
        return try {
            val nombre = if (nombreArchivo.endsWith(".motojson")) nombreArchivo else "$nombreArchivo.motojson"
            val serializable = EstadoTemporadaSerializable(
                nombrePilotoJugador = estado.pilotoJugador.nombre,
                nacionalidadPiloto = estado.pilotoJugador.nacionalidad.name,
                edadPiloto = estado.pilotoJugador.edad,
                carreraActual = estado.carreraActual,
                totalCarreras = estado.totalCarreras,
                puntosPilotos = estado.puntosPilotos.mapKeys { it.key.nombre },
                puntosEquipos = estado.puntosEquipos,
                nombresCircuitos = estado.calendario.map { it.nombre },
                dificultad = estado.dificultad,
                historialCarreras = estado.historialCarreras.size
            )
            GestorArchivos.guardarString(serializable.toJson(), nombre)
        } catch (e: Exception) {
            println("❌ Error al guardar progreso: ${e.message}")
            false
        }
    }

    override fun cargarProgreso(nombreArchivo: String): Boolean {
        return try {
            val nombre = if (nombreArchivo.endsWith(".motojson")) nombreArchivo else "$nombreArchivo.motojson"
            val json = GestorArchivos.cargarString(nombre) ?: return false
            val s = EstadoTemporadaSerializable.fromJson(json)

            // Reconstruir piloto jugador
            val nacionalidad = try { Nacionalidad.valueOf(s.nacionalidadPiloto) } catch (e: Exception) { Nacionalidad.ESPANA }
            val piloto = crearPilotoBueno(s.nombrePilotoJugador, nacionalidad, s.edadPiloto)

            // Reconstruir calendario con circuitos conocidos, o fallback aleatorio
            val conocidos = listOf(
                org.example.motogp.models.CIRCUITO_JEREZ,
                org.example.motogp.models.CIRCUITO_MUGELO,
                org.example.motogp.models.CIRCUITO_ASSEN,
                org.example.motogp.models.CIRCUITO_SILVERSTONE
            )

            // Mapear nombres a objetos Circuito, manteniendo lista de Circuito correcta
            val calendarioReconstruido: List<Circuito> = s.nombresCircuitos.map { nombreCircuito ->
                conocidos.find { it.nombre == nombreCircuito } ?: conocidos.random()
            }

            // Sincronizar la propiedad interna 'calendario' para que otras funciones que la usan estén alineadas
            calendario.clear()
            calendario.addAll(calendarioReconstruido)

            // Reconstruir mapa de pilotos desde nombres (usamos factories simples)
            val puntosPilotosReconstruidos: MutableMap<Piloto, Int> = mutableMapOf()
            for ((nombrePiloto, puntos) in s.puntosPilotos) {
                val p = if (nombrePiloto == s.nombrePilotoJugador) piloto
                else crearPilotoBueno(nombrePiloto, Nacionalidad.ESPANA, 25)
                puntosPilotosReconstruidos[p] = puntos
            }

            // Reconstruir puntos de equipos (se mantienen por nombre)
            val puntosEquiposReconstruidos = s.puntosEquipos.toMutableMap()

            // Asignar estado reconstruido (usamos el calendarioReconstruido para el estado)
            estadoTemporada = EstadoTemporada(
                pilotoJugador = piloto,
                carreraActual = s.carreraActual,
                totalCarreras = s.totalCarreras,
                puntosPilotos = puntosPilotosReconstruidos.toMap(),
                puntosEquipos = puntosEquiposReconstruidos.toMap(),
                calendario = calendarioReconstruido,
                dificultad = s.dificultad,
                historialCarreras = emptyList()
            )

            // Actualizar estructuras internas (mutable) para que el manager pueda continuar
            puntosPilotos.clear()
            puntosPilotos.putAll(puntosPilotosReconstruidos)
            puntosEquipos.clear()
            puntosEquipos.putAll(puntosEquiposReconstruidos)
            pilotosCPU.clear()
            pilotoJugador = piloto

            println("✅ Partida cargada correctamente desde: $nombre")
            true
        } catch (e: Exception) {
            println("❌ Error al cargar progreso: ${e.message}")
            false
        }
    }

    fun listarPartidasGuardadas(): List<String> {
        return GestorArchivos.listarPartidasGuardadas()
    }

    // --- MÉTODOS PRIVADOS DE APOYO ---

    private fun inicializarEquiposYCalendario() {
        equipos.clear()
        equipos.addAll(listOf(
            crearEquipoDucatiLenovo(),
            crearEquipoRepsolHonda(),
            crearEquipoYamaha()
        ))

        pilotosCPU.clear()
        pilotosCPU.addAll(listOf(
            crearPilotoElite("Francesco Bagnaia", Nacionalidad.ITALIA, 26).apply { equipos[0].ficharPiloto(this) },
            crearPilotoExcelente("Enea Bastianini", Nacionalidad.ITALIA, 25).apply { equipos[0].ficharPiloto(this) },
            crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30).apply { equipos[1].ficharPiloto(this) },
            crearPilotoExcelente("Fabio Quartararo", Nacionalidad.FRANCIA, 24).apply { equipos[2].ficharPiloto(this) }
        ))

        if (calendario.isEmpty()) {
            configurarTemporada(5)
        }

        puntosEquipos.clear()
        equipos.forEach { equipo -> puntosEquipos[equipo.nombre] = 0 }

        puntosPilotos.clear()
        pilotosCPU.forEach { piloto -> puntosPilotos[piloto] = 0 }
    }

    private fun actualizarPuntosCarrera(resultado: ResultadoCarrera) {
        val puntosCarrera: Map<Piloto, Int> = SistemaPuntos.calcularPuntosCarrera(resultado)
        for ((piloto, puntos) in puntosCarrera) {
            puntosPilotos[piloto] = (puntosPilotos[piloto] ?: 0) + puntos
        }
        for (equipo in equipos) {
            val puntosEquipo = equipo.pilotos.sumOf { p -> puntosCarrera[p] ?: 0 }
            puntosEquipos[equipo.nombre] = (puntosEquipos[equipo.nombre] ?: 0) + puntosEquipo
        }
    }
}