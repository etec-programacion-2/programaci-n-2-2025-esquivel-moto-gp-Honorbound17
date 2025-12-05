# MotoGP Legacy — Simulador Profesional de Carreras

Autor: Esquivel Coronel Ignacio

## Resumen
MotoGP Legacy es un simulador de carreras escrito en Kotlin. Permite crear un piloto, configurar una temporada, simular carreras, guardar/cargar partidas en JSON y ejecutar pruebas automáticas.

---

## Requisitos previos (obligatorios)
- Java JDK 17 o superior (recomendado: OpenJDK 17)
  - Verificar: `java -version`
  - Si usas SDKMAN: `sdk use java 17.x.x`
  - Si usas Ubuntu/Debian: `sudo apt install openjdk-17-jdk`
- Gradle se incluye mediante Gradle Wrapper; NO es necesario instalar Gradle a nivel sistema (pero si lo haces, que sea ≥ 7.0).
  - Usar preferiblemente el wrapper: `./gradlew` (Linux/macOS) o `gradlew.bat` (Windows)
- Sistema operativo: Windows, macOS o Linux

---

## Instalación y ejecución (paso a paso)

1. Clonar el proyecto
   - `git clone <url-del-repo>`
   - `cd programaci-n-2-2025-esquivel-moto-gp-Honorbound17`

2. Compilar el proyecto (incluye ejecución de tests)
   - Linux / macOS: `./gradlew clean build`
   - Windows (PowerShell/CMD): `gradlew.bat clean build`

   Si sólo quieres compilar rápido (sin tests): `./gradlew assemble`

3. Ejecutar la aplicación (modo interactivo)
   - `./gradlew run`
   - Windows: `gradlew.bat run`

   Nota: la tarea `run` está configurada para reenviar la entrada estándar, por lo que la interacción por consola debe funcionar correctamente.

4. Ejecutar pruebas
   - `./gradlew test`
   - Para ver más detalle: `./gradlew test --info` o ejecutar sólo una prueba concreta:
     `./gradlew test --tests "org.example.motogp.AppTest.testGuardadoYCargaManager" --info`

5. Archivos guardados
   - Las partidas se guardan como JSON con extensión `.motojson` en el directorio actual (p. ej. `partida.motojson`).

---

## Cómo jugar (flujo principal)

1. Al ejecutar la aplicación verás el menú principal:
   - `1. Nueva Partida` — crear piloto y configurar temporada.
   - `2. Cargar Partida` — cargar una partida guardada (`.motojson`).
   - `3. Créditos` — información del proyecto.
   - `4. Salir` — cerrar la aplicación.

2. Creación de piloto:
   - Introduce nombre y selecciona la nacionalidad.
   - Selecciona nivel de experiencia (Élite / Profesional / Semiprofesional / Novato).

3. Configuración de temporada:
   - Elige dificultad (Fácil / Normal / Difícil / Élite / Personalizada).
   - Elige duración de temporada (5 / 10 / 15 / Personalizada).

4. Menú de partida en curso:
   - `Simular siguiente carrera` — ejecuta la simulación para la próxima carrera.
   - `Ver estado actual` — muestra posición y puntos del jugador.
   - `Ver clasificación` — clasificación de pilotos y constructores.
   - `Guardar partida` — guarda el estado actual (archivo `.motojson`).
   - `Gestión de equipo` — funcionalidad en desarrollo.
   - `Volver` — regresa al menú principal.

---

## Sistema de juego y características

- Sistema de rangos (afecta el rendimiento del piloto):
  - S (Élite): multiplicador 3.0 (rango visual/valoración destacada)
  - A (Excelente): multiplicador 2.0
  - B (Bueno): multiplicador 1.5
  - C (Promedio): multiplicador 1.25
  - D (Novato): multiplicador 1.1

- Circuitos incluidos (constantes internas):
  - Jerez — "Circuito de Jerez-Ángel Nieto" (ESPANA)
  - Mugello — "Mugello Circuit" (ITALIA)
  - Assen — "TT Circuit Assen" (PAISES_BAJOS)
  - Silverstone — "Silverstone" (REINO_UNIDO)

- Sistema de puntos (MotoGP):
  - 1º: 25 pts, 2º: 20 pts, 3º: 16 pts, 4º–15º: puntos decrecientes según el estándar MotoGP.

- Persistencia:
  - DTO plano `EstadoTemporadaSerializable` usado para guardar el estado en JSON (kotlinx.serialization).
  - Utilidad simple `GestorArchivos` para leer/escribir archivos `.motojson`.

---

## Solución de problemas comunes

- Gradle Wrapper no ejecuta (`./gradlew: 108: uname: not found` o `xargs is not available`):
  - Instala las utilidades del sistema: en Debian/Ubuntu `sudo apt install coreutils findutils`.
  - Asegúrate de que `/bin` y `/usr/bin` estén en `$PATH`.

- Java no encontrado:
  - Instala Java 17 y exporta `JAVA_HOME` si es necesario:
    - `export JAVA_HOME=/ruta/a/jdk`
    - `export PATH=$JAVA_HOME/bin:$PATH`
  - Verifica: `java -version`, `javac -version`.

- Problemas con stdin al ejecutar `./gradlew run` (EOF / ReadAfterEOFException):
  - La app usa una lectura segura de consola. Si ejecutas en CI o en un entorno sin stdin la app terminará de forma ordenada. Para ejecución interactiva local usa un terminal estándar.

- Permisos en Linux:
  - Si `./gradlew` no es ejecutable: `chmod +x gradlew`

- Tests fallan o dependencias faltantes:
  - Limpia y reconstruye: `./gradlew clean build`
  - Ejecuta tests con información adicional: `./gradlew test --info`

---

## Notas para desarrolladores / Contribuciones

- Usar Kotlin 1.9.x con plugin de serialización (kotlinx.serialization).
- Mantener la enum `Nacionalidad` con valores ASCII (p. ej. `ESPANA`) para evitar problemas con identificadores y serialización.
- Para ejecutar interactivamente desde Gradle la tarea `run` está configurada para reenviar stdin (`standardInput = System.`in``).

Sugerencia de flujo para commits:
- Crear rama: `git checkout -b fix/serialization-mode-carrera`
- Añadir cambios: `git add .`
- Commit: usar un mensaje claro (p. ej. `fix: sync mode-carrera, add serialization DTO and safe console I/O; tests passing`)
- Push: `git push origin HEAD`

---

## Estado actual del proyecto (resumen)
- La aplicación está en funcionamiento y los tests unitarios proporcionados se ejecutan correctamente en mi entorno.
- La persistencia usa JSON y un DTO plano para estabilidad y compatibilidad futura.
- Algunas funcionalidades (como gestión avanzada de equipo) están marcadas como "en desarrollo".
