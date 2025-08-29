import kotlin.math.max
import kotlin.math.min

data class Piloto(
    val nombre: String,
    val nacionalidad: Nacionalidad,
    val edad: Int,
    val habilidades: Habilidades
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(edad >= 18) { "El piloto debe tener al menos 18 años" }
    }
}

fun Int.coerceInHabilidadRange(): Int {
    return max(1, min(100, this))
}
