# Guía para Resolver Errores de Base de Datos Oracle

## Error: ORA-00904 "XXX": identificador no válido

Este error significa que estás intentando acceder a una columna que no existe en la tabla.

### Errores Reportados:
1. `ORA-00904: "FECHA": identificador no válido`
2. `ORA-00904: "NUMERO": identificador no válido`
3. `ORA-00904: "PASSWORD": identificador no válido`
4. `ORA-00904: "ROL": identificador no válido`

---

## Paso 1: Verificar el Esquema Real de la BD

Conectate a tu BD Oracle y ejecuta:

```sql
-- Ver estructura de JORNADA
DESC JORNADA;

-- Ver estructura de USUARIO
DESC USUARIO;

-- Ver estructura de ENFRENTAMIENTO
DESC ENFRENTAMIENTO;
```

---

## Paso 2: Comparar Nombres de Columnas

Basado en los errores, es probable que:

| Tabla | Nombre Esperado en Código | Posible Nombre Real en BD |
|-------|---------------------------|--------------------------|
| JORNADA | `NUMERO_JORNADA` | `NUMERO` |
| JORNADA | `FECHA_JORNADA` | `FECHA` |
| USUARIO | `CONTRASENA` | `PASSWORD` |
| USUARIO | `TIPO` | `ROL` |

---

## Paso 3: Actualizar los DAOs

Si los nombres no coinciden, actualiza los archivos SQL:

### Archivo: `DAO/JornadaDAO.java`

**Busca:**
```java
"SELECT ID_JORNADA, NUMERO_JORNADA, FECHA_JORNADA FROM JORNADA"
"INSERT INTO JORNADA (NUMERO_JORNADA, FECHA_JORNADA) VALUES (?, ?)"
```

**Si tus columnas son NUMERO y FECHA, cambia a:**
```java
"SELECT ID_JORNADA, NUMERO, FECHA FROM JORNADA"
"INSERT INTO JORNADA (NUMERO, FECHA) VALUES (?, ?)"
```

---

### Archivo: `DAO/UsuarioDAO.java`

**Busca:**
```java
"INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)"
"WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ? AND TIPO = 'admin'"
```

**Si tus columnas usan PASSWORD y ROL, cambia a:**
```java
"INSERT INTO USUARIO (NOMBRE_USUARIO, PASSWORD, ROL) VALUES (?, ?, ?)"
"WHERE NOMBRE_USUARIO = ? AND PASSWORD = ? AND ROL = 'admin'"
```

---

## Paso 4: Error de FK Violada

```
ORA-02291: restricción de integridad (FK_ENFRENTAMIENTO_JOR) violada - clave principal no encontrada
```

**Causa:** Se intenta insertar una fila en ENFRENTAMIENTO con un `ID_JORNADA` que no existe en JORNADA

**Solución:** El código ya ha sido actualizado en `EnfrentamientoDAO.java` para:
1. Crear la jornada primero
2. Obtener su ID
3. Usar ese ID en los enfrentamientos

Si sigue fallando, verifica que:
```sql
-- Ver si existen jornadas
SELECT * FROM JORNADA;

-- Ver constrains
DESC ENFRENTAMIENTO;
```

---

## Paso 5: Verificar Nombres en ENFRENTAMIENTO

El error también puede estar en cómo se referencia `ID_JORNADA` en la tabla ENFRENTAMIENTO:

```sql
-- Verifica la estructura
DESC ENFRENTAMIENTO;

-- Verifica las claves foráneas
SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME='ENFRENTAMIENTO';
```

---

## Solución Rápida: Ver todas las columnas

Ejecuta esto para ver exactamente qué columnas tienes:

```sql
SELECT COLUMN_NAME, DATA_TYPE 
FROM USER_TAB_COLUMNS 
WHERE TABLE_NAME IN ('JORNADA', 'USUARIO', 'ENFRENTAMIENTO')
ORDER BY TABLE_NAME, COLUMN_ID;
```

---

## Troubleshooting

### Si sigue habiendo errores después de actualizar los DAOs:

1. **Limpia y compila de nuevo:**
   ```bash
   mvn clean compile
   ```

2. **Borra el directorio target:**
   ```bash
   rmdir /s /q target
   mvn clean
   ```

3. **Recarga el proyecto en el IDE:**
   - En IntelliJ: File → Invalidate Caches → Restart

---

**Nota:** Todos estos cambios están relacionados con la configuración específica de tu BD Oracle. Una vez que identifiques los nombres correctos, actualiza los DAOs y todo debería funcionar.

