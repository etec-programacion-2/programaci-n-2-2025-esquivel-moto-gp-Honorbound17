#pragma once
#include "TiposMotoGP.h"

/**
 * @enum Nacionalidad
 * @brief Representa las nacionalidades de los pilotos.
 * 
 * Usamos un enum en lugar de strings para:
 * - Evitar errores tipográficos (ej: "ESPAÑA" vs "España")
 * - Mejorar rendimiento en comparaciones
 * - Facilitar el autocompletado en el IDE
 * - Validar valores en tiempo de compilación
 */
enum class Nacionalidad {
    ESPANA,     // España
    ITALIA,     // Italia
    FRANCIA,    // Francia
    ALEMANIA,   // Alemania
    PORTUGAL,   // Portugal
    USA,        // Estados Unidos
    ARGENTINA,  // Argentina
    JAPON,      // Japón
    AUSTRALIA,  // Australia
    BRASIL      // Brasil
};

/**
 * @enum FabricanteMoto
 * @brief Representa los fabricantes de motos.
 * 
 * Un enum es preferible a string porque:
 * - Limita los valores posibles a opciones válidas
 * - Oculta implementaciones internas (ej: podría usar códigos numéricos)
 * - Reduce memory footprint (usa integers en lugar de strings)
 */
enum class FabricanteMoto {
    DUCATI,     // Ducati Motor Holding
    YAMAHA,     // Yamaha Motor Company
    HONDA,      // Honda Motor Co., Ltd
    SUZUKI,     // Suzuki Motor Corporation
    KAWASAKI    // Kawasaki Heavy Industries
};

