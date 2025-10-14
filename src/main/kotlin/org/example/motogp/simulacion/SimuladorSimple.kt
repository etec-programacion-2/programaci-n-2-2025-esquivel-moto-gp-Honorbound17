package org.example.motogp.simulacion

import org.example.motogp.models.Circuito
import org.example.motogp.models.Piloto
import kotlin.random.Random

/**
 * OBJETIVO: Cumplir el contrato de la interfaz y permitir pruebas del flujo de la aplicación,
 * no ser realista en la simulación.
 */

class SimuladorCarreraSimple : SimuladorCarrera {
    
    private val random = Random.Default
    
    override fun simular(pilotos: List<Piloto>, circuito: Circuito): ResultadoCarrera {
        require(pilotos.size >= 2) { "Se necesitan al menos 2 pilotos para simular una carrera" }
        
        // Calcular puntajes para cada piloto
        val puntajes = pilotos.associateWith { piloto ->
            calcularPuntajePiloto(piloto, circuito)
        }
        
        // Ordenar pilotos por puntaje descendente
        val posiciones = puntajes.entries
            .sortedByDescending { it.value }
            .map { it.key }
        
        // Simular algunas vueltas rápidas y abandonos para hacerlo más interesante
        val vueltasRapidas = simularVueltasRapidas(puntajes)
        val abandonos = simularAbandonos(pilotos)
        
        return ResultadoCarrera(
            posiciones = posiciones,
            vueltasRapidas = vueltasRapidas,
            abandonos = abandonos
        )
    }
   
    private fun calcularPuntajePiloto(piloto: Piloto, circuito: Circuito): Double {
        val habilidadesPiloto = calcularPuntajeHabilidades(piloto)
        val rendimientoMoto = calcularPuntajeMoto(piloto)
        val factorCircuito = calcularFactorCircuito(piloto, circuito)
        val factorAleatorio = random.nextDouble(0.8, 1.2) // ±20% de variación
        
        return (habilidadesPiloto * 0.6 + rendimientoMoto * 0.4) * factorCircuito * factorAleatorio
    }
    
    /**
     * Calcula el puntaje basado en las habilidades del piloto.
    */
    private fun calcularPuntajeHabilidades(piloto: Piloto): Double {
        val promedioHabilidades = piloto.habilidades.promedioHabilidades()
        
        // Bonificación por rango del piloto
        val bonificacionRango = when (piloto.rango) {
            org.example.motogp.enums.RangoHabilidad.S -> 1.3
            org.example.motogp.enums.RangoHabilidad.A -> 1.15
            org.example.motogp.enums.RangoHabilidad.B -> 1.0
            org.example.motogp.enums.RangoHabilidad.C -> 0.9
            org.example.motogp.enums.RangoHabilidad.D -> 0.8
        }
        
        return promedioHabilidades * bonificacionRango
    }
    
    /**
     * Calcula el puntaje basado en la moto del piloto.
     * Si el piloto no tiene equipo, usa valores por defecto.
    */
    private fun calcularPuntajeMoto(piloto: Piloto): Double {
        val moto = piloto.equipo?.moto
        
        return if (moto != null) {
            // Usar el rendimiento de la moto del equipo
            moto.rendimiento.promedio() * 100.0 // Escalar a un rango similar a habilidades
        } else {
            // Valor por defecto para pilotos sin equipo
            5000.0
        }
    }
    
    /**
     * Calcula cómo afecta el circuito al rendimiento del piloto.
     * Considera las características del piloto vs las del circuito.
     */
    private fun calcularFactorCircuito(piloto: Piloto, circuito: Circuito): Double {
        val factorBase = 1.0
        
        // Pilotos con buena velocidad se benefician en circuitos con rectas largas
        val factorRecta = if (circuito.rectaPrincipal > 800) {
            piloto.habilidades.velocidadFinal / 15000.0
        } else {
            1.0
        }
        
        // Pilotos con buen paso por curva se benefician en circuitos técnicos
        val factorCurvas = if (circuito.curvas > 12) {
            piloto.habilidades.pasoPorCurvaFinal / 15000.0
        } else {
            1.0
        }
        
        // Ajustar por dificultad del circuito
        val factorDificultad = 1.0 + (circuito.dificultad / 200.0)
        
        return factorBase * (factorRecta * 0.4 + factorCurvas * 0.6) * factorDificultad
    }
    
    /**
     * Simula tiempos de vuelta rápida para algunos pilotos.
     */
    private fun simularVueltasRapidas(puntajes: Map<Piloto, Double>): Map<Piloto, Double> {
        return puntajes.entries
            .shuffled()
            .take(random.nextInt(1, 4)) // 1-3 pilotos con vuelta rápida
            .associate { (piloto, puntaje) ->
                // Tiempo base + ajuste por puntaje (mejor puntaje = mejor tiempo)
                val tiempoBase = 90.0 + random.nextDouble(-5.0, 5.0)
                val ajustePuntaje = (15000 - puntaje) / 1000.0 // Ajuste inverso
                piloto to (tiempoBase + ajustePuntaje).coerceIn(85.0, 95.0)
            }
    }
    
    /**
     * Simula abandonos aleatorios de pilotos.
     */
    private fun simularAbandonos(pilotos: List<Piloto>): List<Piloto> {
        // Probabilidad de abandono: 5% para pilotos rank S, 10% para A, 15% para B, etc.
        return pilotos.filter { piloto ->
            val probabilidadAbandono = when (piloto.rango) {
                org.example.motogp.enums.RangoHabilidad.S -> 0.05
                org.example.motogp.enums.RangoHabilidad.A -> 0.10
                org.example.motogp.enums.RangoHabilidad.B -> 0.15
                org.example.motogp.enums.RangoHabilidad.C -> 0.20
                org.example.motogp.enums.RangoHabilidad.D -> 0.25
            }
            random.nextDouble() < probabilidadAbandono
        }
    }
    
    /**
     * Versión alternativa del simulador que permite ajustar los factores de peso.
    */
    fun simularConPesos(
        pilotos: List<Piloto>,
        circuito: Circuito,
        pesoHabilidades: Double = 0.6,
        pesoMoto: Double = 0.4
    ): ResultadoCarrera {
        require(pilotos.size >= 2) { "Se necesitan al menos 2 pilotos para simular una carrera" }
        require((pesoHabilidades + pesoMoto) in 0.9..1.1) { "Los pesos deben sumar aproximadamente 1.0" }
        
        val puntajes = pilotos.associateWith { piloto ->
            val habilidadesPiloto = calcularPuntajeHabilidades(piloto)
            val rendimientoMoto = calcularPuntajeMoto(piloto)
            val factorCircuito = calcularFactorCircuito(piloto, circuito)
            val factorAleatorio = random.nextDouble(0.8, 1.2)
            
            (habilidadesPiloto * pesoHabilidades + rendimientoMoto * pesoMoto) * factorCircuito * factorAleatorio
        }
        
        val posiciones = puntajes.entries
            .sortedByDescending { it.value }
            .map { it.key }
        
        return ResultadoCarrera(
            posiciones = posiciones,
            vueltasRapidas = simularVueltasRapidas(puntajes),
            abandonos = simularAbandonos(pilotos)
        )
    }
}
