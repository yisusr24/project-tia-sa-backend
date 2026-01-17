# Sistema de Inventario y Ventas

**Stack:** Spring Boot 3.2 (Java 21) + Next.js 14 (TypeScript) + PostgreSQL 14

---

## Requisitos Previos

- **Java 21** (JDK)
- **Maven 3.8+**
- **Node.js 18+**
- **PostgreSQL 14+**

---

## Instalación y Configuración

### 1. Configurar Base de Datos

```bash

# Crear la base de datos
CREATE DATABASE inventario_db;

```

#### Ejecutar Scripts SQL
```bash
# - Conectar a inventario_db
# - Ejecutar src/main/resources/database/01_schema.sql
# - Ejecutar src/main/resources/database/02_seed_data.sql

```

### 3. Configurar application.yml

Editar `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/inventario_db
    username: postgres
    password: TU_CONTRASEÑA_POSTGRESQL
```

### 4. Ejecutar Backend

### 3. Ejecutar Backend

```bash
cd project-tia-sa-backend
mvn spring-boot:run
```

**Backend:** http://localhost:8101/api  
**Swagger:** http://localhost:8101/api/swagger-ui.html

## Autor

**Jesús Rosales Reyes**  
Proyecto Técnico - TIA SA

---
