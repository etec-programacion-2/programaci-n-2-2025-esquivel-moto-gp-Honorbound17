import kotlin.math.max
import kotlin.math.min

data class Habilidades(
    val velocidad: Int,
    val frenada: Int,
    val pasoPorCurva: Int
) {
    init {
        require(velocidad in 1..100) { "Velocidad debe estar entre 1 y 100" }
        require(frenada in 1..100) { "Frenada debe estar entre 1 y 100" }
        require(pasoPorCurva in 1..100) { "Paso por curva debe estar entre 1 y 100" }
    }
}
