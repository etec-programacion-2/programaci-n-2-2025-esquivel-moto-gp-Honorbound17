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

    @Test fun testCreacionEquipo() {
        val moto = crearMotoDucatiGP24()
        val equipo = Equipo("Ducati Team", moto)
    
        assertNotNull(equipo)
        assertEquals("Ducati Team", equipo.nombre)
        assertEquals(0, equipo.numeroPilotos())
    }

    @Test fun testFicharPiloto() {
        val equipo = crearEquipoDucatiLenovo()
        val piloto = crearPilotoElite("Francesco Bagnaia", Nacionalidad.ITALIA, 26)
    
        equipo.ficharPiloto(piloto)
    
        assertEquals(1, equipo.numeroPilotos())
        assertEquals("Francesco Bagnaia", equipo.obtenerPilotoPrincipal()?.nombre)
    }

    @Test fun testFicharPilotoEquipoCompleto() {
        val equipo = crearEquipoRepsolHonda()
        val piloto1 = crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30)
        val piloto2 = crearPilotoExcelente("Joan Mir", Nacionalidad.ESPANA, 26)
        val piloto3 = crearPilotoBueno("Test Piloto", Nacionalidad.USA, 25)
    
        // Fichar dos pilotos (máximo permitido)
        equipo.ficharPiloto(piloto1)
        equipo.ficharPiloto(piloto2)
    
        assertTrue(equipo.estaCompleto())
    
        // Intentar fichar un tercer piloto debería fallar
        assertFailsWith<IllegalStateException> {
            equipo.ficharPiloto(piloto3)
        }
    }

    @Test fun testDarDeBajaPiloto() {
        val equipo = crearEquipoYamaha()
        val piloto = crearPilotoExcelente("Fabio Quartararo", Nacionalidad.FRANCIA, 24)
    
        equipo.ficharPiloto(piloto)
        assertEquals(1, equipo.numeroPilotos())
    
        val eliminado = equipo.darDeBajaPiloto(piloto)
        assertTrue(eliminado)
        assertEquals(0, equipo.numeroPilotos())
    }

    @Test fun testObtenerRendimientoMedio() {
        val equipo = crearEquipoDucatiLenovo()
        val piloto = crearPilotoElite("Francesco Bagnaia", Nacionalidad.ITALIA, 26)
    
        equipo.ficharPiloto(piloto)
    
        val rendimiento = equipo.obtenerRendimientoMedio()
        assertTrue(rendimiento > 0.0, "El rendimiento debería ser mayor a 0")
        assertTrue(rendimiento <= 100.0, "El rendimiento debería ser menor o igual a 100")
    }

    @Test fun testEquipoSinPilotos() {
        val equipo = crearEquipoRepsolHonda()
    
        assertEquals(0, equipo.numeroPilotos())
        assertFalse(equipo.estaCompleto())
        assertNull(equipo.obtenerPilotoPrincipal())
    }
}
