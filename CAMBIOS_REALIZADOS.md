# Cambios Realizados - Corrección de Errores

## Resumen de Correcciones

Se han corregido múltiples errores en el proyecto JavaFX relacionados con la arquitectura Modelo-Vista-Controlador (MVC) y la conexión a base de datos Oracle.

---

## 1. **Module-Info.java** - Exportación de módulos

### Problema
- La clase `JornadaController` no era accesible desde FXML debido a problemas de módulo
- Error: `IllegalAccessException: module org.example.programacion does not export org.example.programacion.Controladores`

### Solución
- Agregadas las exportaciones y opens para los paquetes `DAO` y `Util`
- Ahora el módulo exporta correctamente todos los paquetes necesarios

**Archivo:** `module-info.java`

---

## 2. **UsuarioDAO.java** - Expansión de métodos CRUD

### Problema
- UsuarioDAO solo tenía métodos de inserción y validación, faltaban update, delete y getAllUsuarios

### Solución
- Agregados métodos:
  - `getAllUsuarios()` - Obtiene todos los usuarios de la BD
  - `updateUsuario(int id, String username, String password, String rol)` - Actualiza usuarios
  - `deleteUsuario(int idUsuario)` - Elimina usuarios

**Archivo:** `DAO/UsuarioDAO.java`

---

## 3. **UsuarioController.java** - Limpieza de código duplicado

### Problema
- El archivo tenía código duplicado y mal formado con métodos fuera de la clase
- Se detectó que había dos definiciones duplicadas de métodos

### Solución
- Eliminado código duplicado
- Mantenida la versión limpia con solo los métodos necesarios

**Archivo:** `Controladores/UsuarioController.java`

---

## 4. **UsuarioControllerFX.java** - Nuevo controlador para FXML

### Problema
- Faltaba un controlador que implementara `Initializable` para trabajar con FXML

### Solución
- Creado nuevo `UsuarioControllerFX` que implementa `Initializable`
- Métodos incluidos:
  - `initialize()` - Configuración inicial
  - `cargarDatosUsuarios()` - Carga datos de la BD
  - `onGuardar()` - Guarda/actualiza usuarios
  - `onEliminar()` - Elimina usuarios
  - `onLimpiar()` - Limpia formulario
  - `onVolver()` - Vuelve al menú anterior

**Archivo:** `Controladores/UsuarioControllerFX.java` (NUEVO)

---

## 5. **CRUDUsuarios.fxml** - Actualización del controlador

### Problema
- El FXML apuntaba a `org.example.programacion.Vista.CRUDUsuarios` que no existe como controlador FXML

### Solución
- Cambiado `fx:controller` a `org.example.programacion.Controladores.UsuarioControllerFX`

**Archivo:** `resources/CRUDUsuarios.fxml`

---

## 6. **MenuAdmin.java** - Corrección de métodos estáticos

### Problema
- `MenuAdmin` intentaba llamar a métodos estáticos que no existen:
  - `EquiposDAO.validarMinimoJugadores()`
  - `EquiposDAO.getAllEquipos()`
  - `EnfrentamientoDAO.generarYGuardarCalendario()`

### Solución
- Creadas instancias de los DAOs
- Llamadas a métodos de instancia (no estáticos)
- Se agregó `EquiposDAO equiposDAO = new EquiposDAO()`
- Se agregó `EnfrentamientoDAO enfrentamientoDAO = new EnfrentamientoDAO()`

**Archivo:** `Vista/MenuAdmin.java`

---

## 7. **EnfrentamientoDAO.java** - Corrección del FK violado

### Problema
- Error: `ORA-02291: restricción de integridad (FK_ENFRENTAMIENTO_JOR) violada`
- Se insertaba en ENFRENTAMIENTO con un ID de jornada que no existía
- El método usaba `pstmt.setInt(5, (i + 1))` que era el número de jornada, no el ID

### Solución
- Se reescribió `generarYGuardarCalendario()` para:
  1. Primero crear la jornada en la BD
  2. Obtener el `ID_JORNADA` generado
  3. Usar ese ID al insertar los enfrentamientos
  4. Si la jornada ya existe, se obtiene su ID existente

**Archivo:** `DAO/EnfrentamientoDAO.java`

---

## 8. **CompeticionDAO.java** - Método faltante

### Problema
- `MenuAdmin.onGenerarCalendario()` llama a `competicionDAO.cerrarInscripciones()` que no existía

### Solución
- Agregado método `cerrarInscripciones()` que actualiza el estado de la competición

**Archivo:** `DAO/CompeticionDAO.java`

---

## Problemas Identificados pero No Completamente Resueltos

### Error: ORA-00904 "FECHA", "NUMERO", "PASSWORD", "ROL" - Identificadores no válidos

**Causa:** Los nombres de columnas en la BD Oracle no coinciden con los usados en el código

**Posibles soluciones:**
- Verificar esquema real de la BD ejecutando: `DESC JORNADA;` `DESC USUARIO;`
- Comparar nombres de columnas reales vs. esperados
- Actualizar los SQL en los DAOs si los nombres son diferentes

**Archivos afectados:**
- `DAO/JornadaDAO.java` - usa `NUMERO_JORNADA`, `FECHA_JORNADA`
- `DAO/UsuarioDAO.java` - usa `NOMBRE_USUARIO`, `CONTRASENA`, `TIPO`

---

## Resumen de Archivos Modificados

| Archivo | Tipo | Cambio |
|---------|------|--------|
| `module-info.java` | Modificado | Agregadas exportaciones de módulos |
| `DAO/UsuarioDAO.java` | Modificado | Expandidos métodos CRUD |
| `Controladores/UsuarioController.java` | Modificado | Eliminado código duplicado |
| `Controladores/UsuarioControllerFX.java` | NUEVO | Controlador FXML para CRUDUsuarios |
| `Vista/MenuAdmin.java` | Modificado | Corregidas llamadas de métodos estáticos |
| `DAO/EnfrentamientoDAO.java` | Modificado | Corregida lógica de inserción de jornadas |
| `DAO/CompeticionDAO.java` | Modificado | Agregado método cerrarInscripciones() |
| `resources/CRUDUsuarios.fxml` | Modificado | Actualizado fx:controller |

---

## Próximos Pasos Recomendados

1. **Verificar esquema de BD:**
   ```sql
   DESC JORNADA;
   DESC USUARIO;
   DESC ENFRENTAMIENTO;
   ```

2. **Actualizar nombres de columnas** en los DAOs si no coinciden

3. **Compilar y probar:**
   ```bash
   mvn clean compile
   mvn javafx:run
   ```

4. **Validar funcionalidades:**
   - Crear usuarios
   - Crear equipos y jornadas
   - Generar calendario
   - Ver que no haya errores de FK

---

**Fecha de cambios:** 15 de Abril de 2026
**Responsable:** GitHub Copilot

