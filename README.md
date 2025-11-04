Esquivel Coronel Ignacio

--Prerrequisitos Obligatorios--:
  
  Java JDK 17 o superior
  
  Gradle 7.0 o superior (se incluye Gradle Wrapper)
  
  Sistema operativo: Windows, macOS o Linux
  
-Verificar instalación de Java: java -version
-Verificar instalación de Gradle: gradle --version

--Instalación y Ejecución--:

Paso 1: Descargar el Proyecto = git clone <url-del-repositorio>
Luego en una terminal ejecutar: cd programaci-n-2-2025-esquivel-moto-gp-Honorbound17

Paso 2: Compilar el Proyecto = ./gradlew build
En Windows sería: gradlew.bat build

Paso 3: Ejecutar la Aplicación = ./gradlew run
En Windows sería: gradlew.bat run

Paso 4: Ejecutar las Pruebas = ./gradlew test
Ver reporte de pruebas: ./gradlew test --info


-----------------------------------------------------------------------------------

🎮 Cómo Jugar?

Flujo Principal del Juego

--Menú Principal--
    Selecciona "Nueva Partida" para comenzar
    Elige "Cargar Partida" para continuar una existente

--Creación de Piloto--
    Ingresa nombre y nacionalidad
    Selecciona nivel de experiencia (Élite, Profesional, Semiprofesional, Novato)

--Configuración de Temporada--
    Elige dificultad (Fácil, Normal, Difícil, Élite o Personalizada)
    Selecciona duración de temporada (5, 10, 15 o carreras personalizadas)

--Menú de Partida--
  Simular siguiente carrera: Avanza en el calendario
    
  Ver estado actual: Consulta tu posición y puntos
    
  Ver clasificación: Tabla completa de pilotos
    
  Guardar partida: Guarda el progreso actual
  
  Gestión de equipo: Opciones de equipo (en desarrollo)


-----------------------------------------------------------------------------------

🔧 Solución de Problemas

--Error: Java no encontrado--
  Instalar Java 17 (Ubuntu/Debian) ==> sudo apt install openjdk-17-jdk

  Verificar instalación ==> java -version

--Error: Permisos en Linux--
  Dar permisos de ejecución a Gradle Wrapper ==> chmod +x gradlew

--Error: Dependencias faltantes--
  Limpiar y reinstalar dependencias ==> ./gradlew clean build


-----------------------------------------------------------------------------------

📊 Características del Simulador

--Sistema de Rangos--
 
  S (Élite): Multiplicador 3.0-300.0
  A (Excelente): Multiplicador 2.0-200.0
  B (Bueno): Multiplicador 1.5-150.0
  C (Promedio): Multiplicador 1.25-125.0
  D (Novato): Multiplicador 1.1-110.0

--Circuitos Incluidos--
 
  🏁 Jerez-Ángel Nieto (España)
  🏁 Mugello (Italia)
  🏁 Assen (Países Bajos)
  🏁 Silverstone (Reino Unido)

--Sistema de Puntos MotoGP--
 
  🥇 1º: 25 puntos
  🥈 2º: 20 puntos
  🥉 3º: 16 puntos
  4º-15º: Puntos decrecientes
