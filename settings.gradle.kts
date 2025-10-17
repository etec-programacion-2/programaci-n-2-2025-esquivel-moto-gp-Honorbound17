rootProject.name = "programaci-n-2-2025-esquivel-moto-gp-Honorbound17"

 /**
 * BENEFICIOS DEL PATRÓN FACHADA:
 * 1. OCULTAMIENTO DE COMPLEJIDAD:
 *    - El cliente no necesita conocer los detalles internos de simulación, gestión de equipos, 
 *      cálculos de puntuación, etc.
 *    - Proporciona una interfaz unified y simple para operaciones complejas
 * 
 * 2. SEPARACIÓN DE RESPONSABILIDADES:
 *    - Cada subsistema (simulación, equipos, circuitos, puntuaciones) tiene su propia responsabilidad
 *    - La fachada coordina entre estos subsistemas sin acoplarlos
 * 
 * 3. FACILITA EL MANTENIMIENTO:
 *    - Los cambios internos no afectan al cliente
 *    - Se pueden modificar subsistemas sin cambiar la interfaz pública
 * 
 * 4. MEJORA LA TESTABILIDAD:
 *    - Se pueden mockear fácilmente las dependencias internas
 *    - El cliente puede testearse de forma aislada
 * 
 * 5. REDUCE EL ACOPLAMIENTO:
 *    - El cliente solo depende de esta interfaz, no de múltiples subsistemas
 *    - Permite cambiar implementaciones internas fácilmente
*/