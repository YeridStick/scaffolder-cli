# Scaffolder-CLI

Generador de microservicios basado en Spring WebFlux y Arquitectura Hexagonal (Clean Architecture). Diseñado para acelerar el desarrollo de aplicaciones reactivas con R2DBC.

## Requisitos

- Java 17 o superior.
- Gradle (incluido mediante el wrapper).

## Instalación

Para instalar y preparar la herramienta en tu máquina local:

```bash
# 1. Clonar el repositorio
git clone https://github.com/YeridStick/scaffolder-cli.git
cd scaffolder-cli

# 2. Compilar el proyecto
# En Unix/macOS:
./gradlew build
# En Windows:
gradlew.bat build
# O si tienes gradle instalado globalmente:
gradle build
```

## Uso vía Gradle

Puedes ejecutar el motor de generación directamente usando Gradle:

### 1. Inicializar un nuevo proyecto
Este comando crea la estructura base, build.gradle y la configuración de adaptadores reactivos.

```bash
# Unix/macOS:
./gradlew run --args="init --group com.tuempresa --name mi-microservicio"

# Windows:
gradlew.bat run --args="init --group com.tuempresa --name mi-microservicio"
```

### 2. Generar una entidad CRUD
Este comando genera los 7 componentes de la arquitectura hexagonal para una entidad específica.

```bash
# Unix/macOS:
./gradlew run --args="add-entity --group com.tuempresa --project mi-microservicio --name Product"

# Windows:
gradlew.bat run --args="add-entity --group com.tuempresa --project mi-microservicio --name Product"
```

## Arquitectura Generada

Cada entidad generada sigue el patrón de Puertos y Adaptadores:

- **Domain Model**: POJOs puros (Records).
- **Domain Ports**: Interfaces de entrada (Use Cases) y salida (Repositories).
- **Domain Services**: Lógica de negocio agnóstica de frameworks.
- **Adapters In**: Controladores funcionales (WebFlux Handlers y Routers).
- **Adapters Out**: Implementaciones de persistencia con R2DBC.

---
Creado por Yerid para optimizar desarrollos reactivos.
