package org.example.motogp

import org.example.motogp.enums.FabricanteMoto
import org.example.motogp.enums.Nacionalidad
import org.example.motogp.enums.RangoHabilidad
import org.example.motogp.models.Habilidades
import org.example.motogp.models.Moto
import org.example.motogp.models.Piloto
import org.example.motogp.models.Rendimiento
import org.example.motogp.simulacion.SimuladorCarrera
import org.example.motogp.simulacion.SimuladorCarreraSimple
import org.example.motogp.simulacion.ResultadoCarrera
import org.example.motogp.carrera.GestionModoCarrera
import org.example.motogp.carrera.SistemaPuntos
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

    @Test fun testResultadoCarrera() {
        val piloto1 = crearPilotoElite("Piloto 1", Nacionalidad.ESPANA, 25)
        val piloto2 = crearPilotoExcelente("Piloto 2", Nacionalidad.ITALIA, 26)
        val piloto3 = crearPilotoBueno("Piloto 3", Nacionalidad.FRANCIA, 27)
    
        val resultado = ResultadoCarrera(
            posiciones = listOf(piloto1, piloto2, piloto3),
            vueltasRapidas = mapOf(piloto1 to 89.456),
            abandonos = emptyList()
        )
    
        assertEquals(piloto1, resultado.obtenerGanador())
        assertEquals(3, resultado.obtenerPodio().size)
        assertEquals(1, resultado.obtenerPosicion(piloto1))
        assertTrue(resultado.pilotoEnPuntos(piloto1))
    }

    @Test fun testCircuitoCreacion() {
        val circuito = CIRCUITO_JEREZ
    
        assertNotNull(circuito)
        assertEquals("Circuito de Jerez-Ángel Nieto", circuito.nombre)
        assertEquals(Nacionalidad.ESPANA, circuito.pais)
        assertTrue(circuito.dificultad in 1..100)
    }

    // Test Mock para demostrar el uso de la interfaz
    class SimuladorMock : SimuladorCarrera {
        override fun simular(pilotos: List<Piloto>, circuito: Circuito): ResultadoCarrera {
            return ResultadoCarrera(posiciones = pilotos.reversed())
        }
    }

    @Test fun testInterfazSimulador() {
        val simuladorMock = SimuladorMock()
        val pilotos = listOf(
            crearPilotoElite("Piloto A", Nacionalidad.ESPANA, 25),
            crearPilotoExcelente("Piloto B", Nacionalidad.ITALIA, 26)
        )
    
        val resultado = simuladorMock.simular(pilotos, CIRCUITO_MUGELO)
    
        assertEquals(2, resultado.posiciones.size)
        assertEquals("Piloto B", resultado.obtenerGanador().nombre)
    }

    @Test fun testSimuladorCarreraSimple() {
        val simulador = SimuladorCarreraSimple()
    
        val pilotos = listOf(
            crearPilotoElite("Marc Márquez", Nacionalidad.ESPANA, 30),
            crearPilotoExcelente("Fabio Quartararo", Nacionalidad.FRANCIA, 24),
            crearPilotoBueno("Jack Miller", Nacionalidad.AUSTRALIA, 28)
        )
    
        val resultado = simulador.simular(pilotos, CIRCUITO_JEREZ)
    
        assertEquals(3, resultado.posiciones.size)
        assertTrue(resultado.posiciones.isNotEmpty())
        // Verificar que todos los pilotos están en el resultado (pueden estar en diferente orden)
        assertEquals(pilotos.toSet(), resultado.posiciones.toSet())
    }

    @Test fun testSimuladorConMenosDe2Pilotos() {
        val simulador = SimuladorCarreraSimple()
        val pilotoUnico = listOf(crearPilotoElite("Solo Piloto", Nacionalidad.ESPANA, 25))
    
            assertFailsWith<IllegalArgumentException> {
            simulador.simular(pilotoUnico, CIRCUITO_MUGELO)
        }
    }

    @Test fun testSimuladorConPesosPersonalizados() {
        val simulador = SimuladorCarreraSimple()
    
        val pilotos = listOf(
            crearPilotoElite("Piloto Elite", Nacionalidad.ESPANA, 25),
            crearPilotoBueno("Piloto Bueno", Nacionalidad.ITALIA, 26)
        )
    
        val resultado = simulador.simularConPesos(
            pilotos = pilotos,
            circuito = CIRCUITO_ASSEN,
            pesoHabilidades = 0.7,
            pesoMoto = 0.3
        )
    
        assertEquals(2, resultado.posiciones.size)
        assertTrue(resultado.obtenerGanador() in pilotos)
    }

    @Test fun testResultadoEsDiferenteCadaVez() {
        val simulador = SimuladorCarreraSimple()
    
        val pilotos = listOf(
            crearPilotoElite("Piloto A", Nacionalidad.ESPANA, 25),
            crearPilotoElite("Piloto B", Nacionalidad.ITALIA, 26),
            crearPilotoElite("Piloto C", Nacionalidad.FRANCIA, 27)
        )
    
        val resultado1 = simulador.simular(pilotos, CIRCUITO_JEREZ)
        val resultado2 = simulador.simular(pilotos, CIRCUITO_JEREZ)
    
        // Los resultados pueden ser diferentes debido al factor aleatorio
        // Pero deben contener los mismos pilotos
        assertEquals(pilotos.toSet(), resultado1.posiciones.toSet())
        assertEquals(pilotos.toSet(), resultado2.posiciones.toSet())
    }

    @Test fun testSistemaPuntos() {
        assertEquals(25, SistemaPuntos.obtenerPuntos(1))
        assertEquals(20, SistemaPuntos.obtenerPuntos(2))
        assertEquals(1, SistemaPuntos.obtenerPuntos(15))
        assertEquals(0, SistemaPuntos.obtenerPuntos(16))
        assertEquals(0, SistemaPuntos.obtenerPuntos(0))
    }

    @Test fun testEstadoTemporada() {
        val piloto = crearPilotoElite("Jugador", Nacionalidad.ESPANA, 25)
        val estado = EstadoTemporada(
            pilotoJugador = piloto,
            carreraActual = 5,
            totalCarreras = 20,
            puntosPilotos = mapOf(piloto to 100),
            puntosEquipos = mapOf("Ducati" to 150),
            calendario = listOf(CIRCUITO_JEREZ, CIRCUITO_MUGELO),
            dificultad = 75
        )
    
        assertEquals(5, estado.carreraActual)
        assertEquals(20, estado.totalCarreras)
        assertEquals(1, estado.obtenerPosicionJugador())
        assertFalse(estado.temporadaCompletada())
    }

    // Mock para testing de la interfaz
    class GestionModoCarreraMock : GestionModoCarrera {
        private var temporadaIniciada = false
    
        override fun iniciarNuevaCarrera(pilotoJugador: Piloto, dificultad: Int) {
            emporadaIniciada = true
        }
    
        override fun configurarTemporada(numeroCarreras: Int, equiposParticipantes: List<String>) {
            // Mock implementation
        }
    
        override fun simularSiguienteCarrera(): ResultadoCarrera {
            return ResultadoCarrera(emptyList())
        }
    
        override fun avanzarSiguienteEvento() {
            // Mock implementation
        }
    
        override fun simularCarreraPersonalizada(circuito: Circuito, pilotos: List<Piloto>): ResultadoCarrera {
            return ResultadoCarrera(pilotos)
        }
    
        override fun obtenerClasificacionGeneral(): Map<Piloto, Int> {
            return emptyMap()
        }
    
        override fun obtenerEstadoJugador(): String {
            return "Estado mock del jugador"
        }
    
        override fun obtenerClasificacionConstructores(): Map<String, Int> {
            return emptyMap()
        }
    
        override fun obtenerCalendario(): List<Circuito> {
            return emptyList()
        }
    
        override fun obtenerProximaCarrera(): Circuito? {
            return null
        }
    
        override fun obtenerProgresoTemporada(): Pair<Int, Int> {
            return Pair(0, 0)
        }
    
        override fun intentarFichaje(equipoDestino: String): Boolean {
            return false
        }
    
        override fun mejorarHabilidad(tipoHabilidad: String, puntos: Int): Boolean {
            return false
        }
    
        override fun obtenerEquiposDisponibles(): List<String> {
            return emptyList()
        }
    
        override fun temporadaEnCurso(): Boolean {
            return temporadaIniciada
        }
    
        override fun temporadaFinalizada(): Boolean {
            return false
        }
    
        override fun guardarProgreso(nombreArchivo: String): Boolean {
            return true
        }
    
        override fun cargarProgreso(nombreArchivo: String): Boolean {
            return true
        }
    
        override fun finalizarTemporada(): String {
            return "Resumen mock de temporada"
        }
    }

    @Test fun testInterfazGestionModoCarrera() {
        val gestion = GestionModoCarreraMock()
        val piloto = crearPilotoElite("Jugador Test", Nacionalidad.ESPANA, 25)
    
        // Test de inicio de temporada
        gestion.iniciarNuevaCarrera(piloto, 75)
        assertTrue(gestion.temporadaEnCurso())
    
        // Test de métodos de consulta
        assertNotNull(gestion.obtenerEstadoJugador())
        assertNotNull(gestion.obtenerClasificacionGeneral())
        assertNotNull(gestion.obtenerCalendario())
    }
}
