/*
 * Tests para el simulador de MotoGP
 */
package org.example.motogp  // Cambia el package para que coincida

import org.example.motogp.enums.Nacionalidad
import org.example.motogp.enums.RangoHabilidad
import org.example.motogp.models.Habilidades
import org.example.motogp.models.Piloto
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class AppTest {
    
    @Test fun testCreacionPilotoValido() {
        val habilidades = Habilidades(
            velocidadBase = 95,
            frenadaBase = 88,
            pasoPorCurvaBase = 92,
            rango = RangoHabilidad.S
        )
        
        val piloto = Piloto(
            nombre = "Marc Márquez",
            nacionalidad = Nacionalidad.ESPANA,
            edad = 30,
            habilidades = habilidades
        )
        
        assertNotNull(piloto, "El piloto debería crearse correctamente")
        assertNotNull(piloto.habilidades, "Las habilidades deberían existir")
    }
    
    @Test fun testHabilidadesConRangoS() {
        val habilidades = Habilidades(
            velocidadBase = 95,
            frenadaBase = 88,
            pasoPorCurvaBase = 92,
            rango = RangoHabilidad.S
        )
        
        // Verificar que las habilidades finales están dentro del rango esperado
        assertTrue(habilidades.velocidadFinal >= 95 * 3.0, "Velocidad debería estar multiplicada por 3.0+")
        assertTrue(habilidades.velocidadFinal <= 95 * 300.0, "Velocidad debería estar multiplicada por 300.0-")
    }
    
    @Test fun testValidacionEdadMinima() {
        val habilidades = Habilidades(
            velocidadBase = 80,
            frenadaBase = 75,
            pasoPorCurvaBase = 70,
            rango = RangoHabilidad.A
        )
        
        // Debería fallar porque la edad es menor de 18
        assertFailsWith<IllegalArgumentException> {
            Piloto(
                nombre = "Piloto Joven",
                nacionalidad = Nacionalidad.ESPANA,
                edad = 17,  // Edad inválida
                habilidades = habilidades
            )
        }
    }
    
    @Test fun testValidacionHabilidadesBase() {
        // Debería fallar porque la habilidad base es mayor de 100
        assertFailsWith<IllegalArgumentException> {
            Habilidades(
                velocidadBase = 150,  // Inválido
                frenadaBase = 75,
                pasoPorCurvaBase = 60,
                rango = RangoHabilidad.B
            )
        }
    }
    
    @Test fun testPromedioHabilidades() {
        val habilidades = Habilidades(
            velocidadBase = 100,
            frenadaBase = 100,
            pasoPorCurvaBase = 100,
            rango = RangoHabilidad.S
        )
        
        val promedio = habilidades.promedioHabilidades()
        assertTrue(promedio >= 300.0, "El promedio debería ser al menos 300.0")
        assertTrue(promedio <= 30000.0, "El promedio debería ser máximo 30000.0")
    }
    
    @Test fun testPropiedadRangoEnPiloto() {
        val habilidades = Habilidades(
            velocidadBase = 85,
            frenadaBase = 80,
            pasoPorCurvaBase = 75,
            rango = RangoHabilidad.A
        )
        
        val piloto = Piloto(
            nombre = "Fabio Quartararo",
            nacionalidad = Nacionalidad.FRANCIA,
            edad = 24,
            habilidades = habilidades
        )
        
        assertTrue(piloto.rango == RangoHabilidad.A, "El rango del piloto debería ser A")
    }
}
