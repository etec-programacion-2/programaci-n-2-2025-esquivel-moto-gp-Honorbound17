package org.example.motogp

// Extensión para formatear números
fun Double.format(digits: Int) = "%.${digits}f".format(this)

// Operador para repetir strings (útil para separadores)
operator fun String.times(n: Int) = this.repeat(n)