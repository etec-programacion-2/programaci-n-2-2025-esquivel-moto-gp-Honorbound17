package org.example.motogp.simulacion

import org.example.motogp.models.Circuito
import org.example.motogp.models.Piloto

/**
 * Interfaz que define el contrato para cualquier motor de simulación de carreras de MotoGP.
 * 
 * POR QUÉ UNA INTERFAZ ES CRUCIAL AQUÍ:
 * 
 * 1. PRINCIPIO DE INVERSIÓN DE DEPENDENCIAS (SOLID - D):
 *    - Los módulos de alto nivel (UI, lógica de negocio) no deben depender de módulos de bajo nivel (implementación específica)
 *    - Ambos deben depender de abstracciones (esta interfaz)
 * 
 * 2. MANTENIBILIDAD:
 *    - Permite cambiar la implementación del simulador sin afectar al resto del sistema
 *    - Facilita la adición de nuevos algoritmos de simulación
 * 
 * 3. TESTEO:
 *    - Podemos crear implementaciones mock para tests unitarios
 *    - Permite testing aislado de componentes que usan el simulador
 * 
 * 4. EXTENSIBILIDAD:
 *    - Podemos tener múltiples implementaciones (Simple, Avanzada, con IA, etc.)
 *    - Fomenta el desarrollo basado en contratos
 * 
 * 5. FLEXIBILIDAD:
 *    - El sistema puede usar diferentes simuladores en tiempo de ejecución
 *    - Soporte para estrategias de simulación intercambiables
 */
interface SimuladorCarrera {
    
    /**
     * Simula una carrera completa entre los pilotos proporcionados en un circuito específico.
     * 
     * @param pilotos Lista de pilotos que participan en la carrera
     * @param circuito Circuito donde se realiza la carrera
     * @return ResultadoCarrera con el orden de llegada de los pilotos
     * @throws IllegalArgumentException si la lista de pilotos está vacía o tiene menos de 2 pilotos
     */
    fun simular(pilotos: List<Piloto>, circuito: Circuito): ResultadoCarrera
}

/**
 * Data class que representa el resultado de una carrera de MotoGP.
 * 
 * @property posiciones Lista ordenada de pilotos por posición de llegada (primero = ganador)
 * @property vueltasRapidas Map que contiene el piloto y su tiempo de vuelta rápida (opcional)
 * @property abandonos Lista de pilotos que no terminaron la carrera (opcional)
 */
data class ResultadoCarrera(
    val posiciones: List<Piloto>,
    val vueltasRapidas: Map<Piloto, Double> = emptyMap(),
    val abandonos: List<Piloto> = emptyList()
) {
    
    /**
     * Obtiene el piloto ganador de la carrera.
     * 
     * @return El piloto en primera posición
     * @throws NoSuchElementException si no hay posiciones
     */
    fun obtenerGanador(): Piloto {
        return posiciones.first()
    }
    
    /**
     * Obtiene el podio (primeros 3 pilotos).
     * 
     * @return Lista con los 3 primeros pilotos, o menos si hay menos de 3
     */
    fun obtenerPodio(): List<Piloto> {
        return posiciones.take(3)
    }
    
    /**
     * Obtiene la posición de un piloto específico.
     * 
     * @param piloto Piloto a buscar
     * @return Posición (1-based) o null si no terminó o no participó
     */
    fun obtenerPosicion(piloto: Piloto): Int? {
        return posiciones.indexOf(piloto).takeIf { it != -1 }?.plus(1)
    }
    
    /**
     * Verifica si un piloto terminó en puntos (posiciones 1-15 en MotoGP).
     * 
     * @param piloto Piloto a verificar
     * @return true si terminó entre los primeros 15, false en caso contrario
     */
    fun pilotoEnPuntos(piloto: Piloto): Boolean {
        return obtenerPosicion(piloto) in 1..15
    }
    
    /**
     * Genera un resumen legible del resultado de la carrera.
     */
    fun resumen(): String {
        val builder = StringBuilder()
        builder.appendLine("🏁 RESULTADO DE CARRERA 🏁")
        builder.appendLine("Podio:")
        
        obtenerPodio().forEachIndexed { index, piloto ->
            val posicion = when (index) {
                0 -> "🥇 1st"
                1 -> "🥈 2nd" 
                2 -> "🥉 3rd"
                else -> "${index + 1}th"
            }
            builder.appendLine("$posicion: ${piloto.nombre} (${piloto.equipo?.nombre ?: "Sin equipo"})")
        }
        
        if (vueltasRapidas.isNotEmpty()) {
            val vueltaRapida = vueltasRapidas.minByOrNull { it.value }
            builder.appendLine("⏱️  Vuelta rápida: ${vueltaRapida?.key?.nombre} - ${vueltaRapida?.value?.format(3)}s")
        }
        
        if (abandonos.isNotEmpty()) {
            builder.appendLine("🚩 Abandonos: ${abandonos.joinToString { it.nombre }}")
        }
        
        return builder.toString()
    }
}

// Extensión para formatear tiempos
private fun Double.format(digits: Int) = "%.${digits}f".format(this)