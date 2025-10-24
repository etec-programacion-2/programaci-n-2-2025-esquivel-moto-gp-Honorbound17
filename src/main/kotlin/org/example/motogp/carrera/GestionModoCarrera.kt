package org.example.motogp.carrera

import org.example.motogp.models.Circuito
import org.example.motogp.models.Piloto
import org.example.motogp.simulacion.ResultadoCarrera

/**
 * Interfaz que define la gestión del modo carrera de MotoGP.
 * Esta interfaz actúa como una "fachada" que oculta la complejidad interna del sistema
 * y proporciona una interfaz simplificada para el cliente.
 */

interface GestionModoCarrera {
    
    // --- CONFIGURACIÓN INICIAL ---
    
    /**
     * Inicia una nueva temporada/carrera con el piloto seleccionado por el jugador.
     * 
     * @param pilotoJugador Piloto controlado por el jugador
     * @param dificultad Nivel de dificultad (1~100)
     * @throws IllegalStateException si ya hay una temporada en curso
     */
    fun iniciarNuevaCarrera(pilotoJugador: Piloto, dificultad: Int = 50)
    
    /**
     * Configura las opciones de la temporada.
     * 
     * @param numeroCarreras Número total de carreras en la temporada
     * @param equiposParticipantes Lista de equipos que participan
     */
    fun configurarTemporada(numeroCarreras: Int, equiposParticipantes: List<String> = emptyList())
    
    // --- SIMULACIÓN Y PROGRESO ---
    
    /**
     * Simula la siguiente carrera del calendario.
     * 
     * @return Resultado de la carrera simulada
     * @throws IllegalStateException si no hay temporada en curso o ya se completó la temporada
     */
    fun simularSiguienteCarrera(): ResultadoCarrera
    
    /**
     * Avanza al siguiente evento del calendario sin simular.
     * Útil para saltar carreras o para modo espectador.
     */
    fun avanzarSiguienteEvento()
    
    /**
     * Simula una carrera específica fuera del calendario.
     * 
     * @param circuito Circuito donde se realiza la carrera
     * @param pilotos Lista de pilotos participantes
     * @return Resultado de la carrera
     */
    fun simularCarreraPersonalizada(circuito: Circuito, pilotos: List<Piloto>): ResultadoCarrera
    
    // --- INFORMACIÓN Y CONSULTAS ---
    
    /**
     * Obtiene la clasificación general actual del campeonato.
     * 
     * @return Mapa con pilotos y sus puntos acumulados, ordenado por puntos descendente
     */
    fun obtenerClasificacionGeneral(): Map<Piloto, Int>
    
    /**
     * Obtiene el estado actual del jugador.
     * 
     * @return String descriptivo con información del jugador, equipo, posición, etc.
     */
    fun obtenerEstadoJugador(): String
    
    /**
     * Obtiene la clasificación por equipos.
     * 
     * @return Mapa con equipos y sus puntos acumulados
     */
    fun obtenerClasificacionConstructores(): Map<String, Int>
    
    /**
     * Obtiene el calendario de la temporada actual.
     * 
     * @return Lista de circuitos en el orden del calendario
     */
    fun obtenerCalendario(): List<Circuito>
    
    /**
     * Obtiene la próxima carrera del calendario.
     * 
     * @return Próximo circuito o null si no hay más carreras
     */
    fun obtenerProximaCarrera(): Circuito?
    
    /**
     * Obtiene el progreso actual de la temporada.
     * 
     * @return Par con (carrera actual, total de carreras)
     */
    fun obtenerProgresoTemporada(): Pair<Int, Int>
    
    // --- GESTIÓN DE EQUIPO Y CONTRATOS ---
    
    /**
     * Intenta fichar por un equipo mejor.
     * 
     * @param equipoDestino Nombre del equipo al que se quiere fichar
     * @return true si el fichaje fue exitoso, false en caso contrario
     */
    fun intentarFichaje(equipoDestino: String): Boolean
    
    /**
     * Mejora las habilidades del piloto jugador.
     * 
     * @param tipoHabilidad Tipo de habilidad a mejorar ("velocidad", "frenada", "curvas")
     * @param puntos Puntos a añadir (limitado por presupuesto/experiencia)
     * @return true si la mejora fue exitosa
     */
    fun mejorarHabilidad(tipoHabilidad: String, puntos: Int): Boolean
    
    /**
     * Obtiene las opciones de equipo disponibles para fichar.
     * 
     * @return Lista de nombres de equipos disponibles
     */
    fun obtenerEquiposDisponibles(): List<String>
    
    // --- CONTROL DE ESTADO ---
    
    /**
     * Verifica si la temporada está en curso.
     * 
     * @return true si hay una temporada activa, false en caso contrario
     */
    fun temporadaEnCurso(): Boolean
    
    /**
     * Verifica si la temporada ha finalizado.
     * 
     * @return true si se completaron todas las carreras, false en caso contrario
     */
    fun temporadaFinalizada(): Boolean
    
    /**
     * Guarda el estado actual del modo carrera.
     * 
     * @param nombreArchivo Nombre del archivo donde guardar
     * @return true si se guardó correctamente
     */
    fun guardarProgreso(nombreArchivo: String): Boolean
    
    /**
     * Carga un estado guardado del modo carrera.
     * 
     * @param nombreArchivo Nombre del archivo a cargar
     * @return true si se cargó correctamente
     */
    fun cargarProgreso(nombreArchivo: String): Boolean
    
    /**
     * Finaliza la temporada actual y calcula los resultados finales.
     * 
     * @return Información del resumen de la temporada
     */
    fun finalizarTemporada(): String
}

/**
 * Data class que representa el estado completo de una temporada.
 */
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
    /**
     * Calcula la posición actual del jugador en el campeonato.
     */
    fun obtenerPosicionJugador(): Int {
        val puntosJugador = puntosPilotos[pilotoJugador] ?: 0
        return puntosPilotos.entries
            .sortedByDescending { it.value }
            .indexOfFirst { it.key == pilotoJugador } + 1
    }
    
    /**
     * Verifica si la temporada está completada.
     */
    fun temporadaCompletada(): Boolean {
        return carreraActual > totalCarreras
    }
}

/**
 * Sistema de puntos de MotoGP.
 */
object SistemaPuntos {
    private val PUNTUACIONES = mapOf(
        1 to 25,   // 1er puesto
        2 to 20,   // 2do puesto  
        3 to 16,   // 3er puesto
        4 to 13,   // 4to puesto
        5 to 11,   // 5to puesto
        6 to 10,   // 6to puesto
        7 to 9,    // 7mo puesto
        8 to 8,    // 8vo puesto
        9 to 7,    // 9no puesto
        10 to 6,   // 10mo puesto
        11 to 5,   // 11vo puesto
        12 to 4,   // 12vo puesto
        13 to 3,   // 13vo puesto
        14 to 2,   // 14vo puesto
        15 to 1    // 15vo puesto
    )
    
    /**
     * Obtiene los puntos para una posición dada.
     * 
     * @param posicion Posición de llegada (1-based)
     * @return Puntos asignados, 0 si está fuera de los puntos
     */
    fun obtenerPuntos(posicion: Int): Int {
        return PUNTUACIONES[posicion] ?: 0
    }
    
    /**
     * Calcula los puntos para un resultado de carrera.
     * 
     * @param resultado Resultado de la carrera
     * @return Mapa con pilotos y puntos obtenidos
     */
    fun calcularPuntosCarrera(resultado: ResultadoCarrera): Map<Piloto, Int> {
        return resultado.posiciones.mapIndexed { index, piloto ->
            // Los abandonos no obtienen puntos
            if (resultado.abandonos.contains(piloto)) {
                piloto to 0
            } else {
                piloto to obtenerPuntos(index + 1)
            }
        }.toMap()
    }

    /**
    * Guarda el estado actual de la partida en un archivo.
    * 
    *  @param fichero Ruta del archivo donde guardar
    * @return true si se guardó correctamente, false en caso de error
    */
    fun guardarPartida(fichero: String): Boolean

    /**
    * Carga el estado de una partida desde un archivo.
    * 
    * @param fichero Ruta del archivo a cargar
    * @return true si se cargó correctamente, false en caso de error
    */
    fun cargarPartida(fichero: String): Boolean
}
