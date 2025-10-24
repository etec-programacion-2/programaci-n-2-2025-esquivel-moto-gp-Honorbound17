plugins {
    kotlin("jvm") version "1.9.22"
    application
    kotlin("plugin.serialization") version "1.9.22" // ← AGREGAR ESTA LÍNEA
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    
    // 🔥 NUEVAS DEPENDENCIAS PARA SERIALIZACIÓN JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("org.example.motogp.MainKt")
}