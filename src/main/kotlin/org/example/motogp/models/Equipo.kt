package org.example.motogp.models

/**
 * Clase que representa un equipo de MotoGP.
 * @property nombre Nombre del equipo
 * @property moto Moto oficial del equipo
 * @property pilotos Lista de pilotos del equipo (máximo 2)
 * 
 * Objetivo de aprendizaje: Composición y manejo de colecciones
 */
class Equipo(
    val nombre: String,
    val moto: Moto,
    pilotos: List<Piloto> = emptyList()
) {
    private val _pilotos = pilotos.toMutableList()
    
    /**
     * Lista inmutable de pilotos del equipo
     */
    val pilotos: List<Piloto>
        get() = _pilotos.toList()

    init {
        require(nombre.isNotBlank()) { "El nombre del equipo no puede estar vacío" }
        require(_pilotos.size <= MAX_PILOTOS) { "Un equipo no puede tener más de $MAX_PILOTOS pilotos" }
    }

    /**
     * Ficha un piloto al equipo
     * @param piloto Piloto a fichar
     * @throws IllegalStateException si el equipo ya está completo
     */
    fun ficharPiloto(piloto: Piloto) {
        if (_pilotos.size >= MAX_PILOTOS) {
            throw IllegalStateException("El equipo $nombre ya tiene $MAX_PILOTOS pilotos. No se puede fichar a ${piloto.nombre}")
        }
        _pilotos.add(piloto)
    }

    /**
     * Da de baja a un piloto del equipo
     * @param piloto Piloto a dar de baja
     * @return true si se eliminó el piloto, false si no estaba en el equipo
     */
    fun darDeBajaPiloto(piloto: Piloto): Boolean {
        return _pilotos.remove(piloto)
    }

    /**
     * Calcula el rendimiento medio del equipo combinando:
     * - Promedio de habilidades de todos los pilotos
     * - Rendimiento de la moto
     * @return Rendimiento medio del equipo (0.0 - 100.0)
     */
    fun obtenerRendimientoMedio(): Double {
        if (_pilotos.isEmpty()) {
            return moto.rendimiento.promedio() / 100.0 * 50.0 // Solo moto = 50% del máximo
        }

        val promedioPilotos = _pilotos.map { it.habilidades.promedioHabilidades() }.average()
        val promedioMoto = moto.rendimiento.promedio()

        // Normalizar y combinar (50% pilotos, 50% moto)
        val rendimientoPilotos = (promedioPilotos / 30000.0) * 50.0 // Normalizar a 0-50
        val rendimientoMoto = (promedioMoto / 100.0) * 50.0 // Normalizar a 0-50

        return rendimientoPilotos + rendimientoMoto
    }

    /**
     * Obtiene el piloto principal (primero de la lista)
     * @return Piloto principal o null si no hay pilotos
     */
    fun obtenerPilotoPrincipal(): Piloto? {
        return _pilotos.firstOrNull()
    }

    /**
     * Obtiene el segundo piloto
     * @return Segundo piloto o null si no hay segundo piloto
     */
    fun obtenerSegundoPiloto(): Piloto? {
        return _pilotos.getOrNull(1)
    }

    /**
     * Verifica si el equipo está completo
     * @return true si tiene 2 pilotos, false en caso contrario
     */
    fun estaCompleto(): Boolean {
        return _pilotos.size == MAX_PILOTOS
    }

    /**
     * Obtiene el número de pilotos actuales
     */
    fun numeroPilotos(): Int {
        return _pilotos.size
    }

    /**
     * Descripción completa del equipo
     */
    fun descripcion(): String {
        val infoPilotos = if (_pilotos.isEmpty()) {
            "Sin pilotos"
        } else {
            _pilotos.joinToString(", ") { it.nombre }
        }
        
        return """
        🏁 EQUIPO: $nombre
        🏍️  Moto: ${moto.descripcion()}
        👥 Pilotos: $infoPilotos
        📊 Rendimiento: ${obtenerRendimientoMedio().format(2)}%
        🔧 Estado: ${if (estaCompleto()) "Completo ✅" else "Vacantes: ${MAX_PILOTOS - _pilotos.size}"}
        """.trimIndent()
    }

    companion object {
        const val MAX_PILOTOS = 2
    }
}

fun crearEquipoRepsolHonda(): Equipo {
    val moto = Moto(
        fabricante = FabricanteMoto.HONDA,
        modelo = "RC213V",
        rendimiento = Rendimiento(
            velocidadMaxima = 94,
            aceleracion = 88,
            maniobrabilidad = 92
        )
    )
    
    return Equipo(
        nombre = "Repsol Honda Team",
        moto = moto
    )
}

fun crearEquipoDucatiLenovo(): Equipo {
    val moto = Moto(
        fabricante = FabricanteMoto.DUCATI,
        modelo = "Desmosedici GP24",
        rendimiento = Rendimiento(
            velocidadMaxima = 98,
            aceleracion = 95,
            maniobrabilidad = 90
        )
    )
    
    return Equipo(
        nombre = "Ducati Lenovo Team",
        moto = moto
    )
}

fun crearEquipoYamaha(): Equipo {
    val moto = Moto(
        fabricante = FabricanteMoto.YAMAHA,
        modelo = "YZR-M1",
        rendimiento = Rendimiento(
            velocidadMaxima = 95,
            aceleracion = 92,
            maniobrabilidad = 96
        )
    )
    
    return Equipo(
        nombre = "Monster Energy Yamaha",
        moto = moto
    )
}