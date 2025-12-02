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
     * @param nombreArchivo Nombre del archivo donde guardar (se puede añadir extensión .motojson)
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