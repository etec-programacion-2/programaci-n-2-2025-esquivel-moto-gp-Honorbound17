package org.example.motogp

import org.example.motogp.enums.FabricanteMoto
import org.example.motogp.enums.Nacionalidad
import org.example.motogp.enums.RangoHabilidad
import org.example.motogp.models.Habilidades
import org.example.motogp.models.Moto
import org.example.motogp.models.Piloto
import org.example.motogp.models.Rendimiento
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
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
    
    @Test fun testValidacionEdadMinima() {
        val habilidades = Habilidades(
            velocidadBase = 80,
            frenadaBase = 75,
            pasoPorCurvaBase = 70,
            rango = RangoHabilidad.A
        )
        
        assertFailsWith<IllegalArgumentException> {
            Piloto(
                nombre = "Piloto Joven",
                nacionalidad = Nacionalidad.ESPANA,
                edad = 17,  // Edad inválida
                habilidades = habilidades
            )
        }
    }
    
    @Test fun testCreacionMotoValida() {
        val rendimiento = Rendimiento(
            velocidadMaxima = 95,
            aceleracion = 88,
            maniobrabilidad = 85
        )
        
        val moto = Moto(
            fabricante = FabricanteMoto.DUCATI,
            modelo = "Desmosedici GP24",
            rendimiento = rendimiento
        )
        
        assertNotNull(moto, "La moto debería crearse correctamente")
        assertNotNull(moto.rendimiento, "El rendimiento debería existir")
        assertEquals("Ducati", moto.marca)
    }
    
    @Test fun testValidacionRendimiento() {
        assertFailsWith<IllegalArgumentException> {
            Rendimiento(
                velocidadMaxima = 150,  // Inválido
                aceleracion = 80,
                maniobrabilidad = 75
            )
        }
    }
    
    @Test fun testPuntajeTotalRendimiento() {
        val rendimiento = Rendimiento(
            velocidadMaxima = 90,
            aceleracion = 85,
            maniobrabilidad = 80
        )
        
        assertEquals(255, rendimiento.puntajeTotal())
        assertEquals(85.0, rendimiento.promedio())
    }
}
