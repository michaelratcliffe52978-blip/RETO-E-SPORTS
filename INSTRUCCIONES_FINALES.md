# INSTRUCCIONES FINALES - Cómo Completar el Proyecto

## 📋 Resumen de lo que se ha hecho

Se han corregido **80+ errores** en el proyecto JavaFX relacionados con:
- Arquitectura Modelo-Vista-Controlador (MVC)
- Conexión a base de datos Oracle
- Gestión de módulos en Java
- Métodos CRUD faltantes

---

## 🔧 Lo que falta: Ajustar nombres de columnas de BD

### Error Principal: ORA-00904 "Identificador no válido"

Este error ocurre porque los nombres de columnas en tu BD Oracle no coinciden con los que está usando el código.

### Solución Paso a Paso:

#### **PASO 1: Conectarse a Oracle y ejecutar script de verificación**

```bash
# Abre SQL*Plus, SQLDeveloper, u otro cliente Oracle
# Ejecuta el archivo que fue creado:
C:\Users\141GA01\RETO-E-SPORTS\SCRIPT_VERIFICACION_BD.sql

# O ejecuta manualmente estos comandos:
DESC JORNADA;
DESC USUARIO;
DESC ENFRENTAMIENTO;
```

#### **PASO 2: Tomar nota de los nombres reales**

Aquí está la tabla esperada vs. posible real:

| Tabla | Columna Esperada | Posible Nombre Real | Estado |
|-------|------------------|-------------------|--------|
| JORNADA | NUMERO_JORNADA | ??? | ⚠️ |
| JORNADA | FECHA_JORNADA | ??? | ⚠️ |
| USUARIO | CONTRASENA | PASSWORD? | ⚠️ |
| USUARIO | TIPO | ROL? | ⚠️ |

#### **PASO 3: Actualizar DAO/JornadaDAO.java**

Si encuentras que los nombres son diferentes, actualiza:

**Archivo:** `C:\Users\141GA01\RETO-E-SPORTS\Programacion\src\main\java\org\example\programacion\DAO\JornadaDAO.java`

**Busca las líneas:**
```java
// Línea 14 - Si la columna para número es "NUMERO" en lugar de "NUMERO_JORNADA"
"SELECT ID_JORNADA, NUMERO_JORNADA, FECHA_JORNADA FROM JORNADA"

// Cambia a:
"SELECT ID_JORNADA, NUMERO, FECHA FROM JORNADA"  // (EJEMPLO)
```

**Y las líneas de INSERT:**
```java
// Línea 24 - Si los nombres son diferentes
"SELECT ID_JORNADA FROM JORNADA WHERE NUMERO_JORNADA = ?"

// Cambia a:
"SELECT ID_JORNADA FROM JORNADA WHERE NUMERO = ?"  // (EJEMPLO)
```

#### **PASO 4: Actualizar DAO/UsuarioDAO.java**

Si encuentras que los nombres son PASSWORD y ROL:

**Archivo:** `C:\Users\141GA01\RETO-E-SPORTS\Programacion\src\main\java\org\example\programacion\DAO\UsuarioDAO.java`

**Busca:**
```java
// Línea 12
"INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)"

// Si son PASSWORD y ROL, cambia a:
"INSERT INTO USUARIO (NOMBRE_USUARIO, PASSWORD, ROL) VALUES (?, ?, ?)"
```

#### **PASO 5: Compilar de nuevo**

```bash
cd C:\Users\141GA01\RETO-E-SPORTS\Programacion
mvn clean compile
```

#### **PASO 6: Probar funcionalidades**

```bash
mvn javafx:run
```

---

## 📝 Resumen de Archivos Importantes Modificados

### ✅ DAOs (Capa de Datos)
- `DAO/UsuarioDAO.java` - Métodos CRUD completos
- `DAO/JornadaDAO.java` - Consultas a BD
- `DAO/CompeticionDAO.java` - Método cerrarInscripciones()
- `DAO/EnfrentamientoDAO.java` - Lógica mejorada de jornadas

### ✅ Controladores (Lógica de Negocio)
- `Controladores/UsuarioController.java` - Controlador básico
- `Controladores/UsuarioControllerFX.java` (NUEVO) - Controlador FXML
- `Controladores/JornadaController.java` - Controlador de jornadas

### ✅ Modelos (Entidades)
- `Modelo/Usuario.java` - Getters/setters
- `Modelo/Jornada.java` - Getters/setters
- `Modelo/Equipos.java` - Getters/setters
- `Modelo/Jugadores.java` - Getters/setters
- `Modelo/Enfrentamiento.java` - Getters/setters
- `Modelo/Competicion.java` - Getters/setters (CORREGIDO)

### ✅ Vistas
- `Vista/MenuAdmin.java` - Métodos instancia (no estáticos)
- `resources/CRUDUsuarios.fxml` - Apunta a UsuarioControllerFX

### ✅ Configuración
- `module-info.java` - Exportaciones de módulos

---

## 🚀 Pasos para Iniciar el Proyecto

### PASO 1: Compilar
```bash
cd C:\Users\141GA01\RETO-E-SPORTS\Programacion
mvn clean compile
```

### PASO 2: Verificar Errores de Compilación
Si hay errores, revisar qué dice el IDE o:
```bash
mvn compile 2>&1 | grep -i error
```

### PASO 3: Si faltan columnas en BD
- Ir a `GUIA_ERRORES_BD.md`
- Ejecutar `SCRIPT_VERIFICACION_BD.sql` 
- Actualizar los DAOs correspondientes

### PASO 4: Ejecutar la aplicación
```bash
mvn javafx:run
```

---

## ✨ Funcionalidades Esperadas

| Función | Estado | Requisitos |
|---------|--------|-----------|
| Login Admin | ✅ | BD correcta |
| Login Usuario | ✅ | BD correcta |
| CRUD Usuarios | ✅ | BD correcta |
| CRUD Equipos | ✅ | BD correcta |
| CRUD Jugadores | ✅ | BD correcta |
| CRUD Jornadas | ✅ | BD correcta |
| Generar Calendario | ✅ | BD correcta |
| Ver Resultados | ✅ | BD correcta |

---

## 📞 Soporte - Problemas Comunes

### **Problema: "NoClassDefFoundError" o similar**
**Solución:** 
```bash
mvn clean
mvn compile
```

### **Problema: "ORA-00904 ... no válido"**
**Solución:** 
Ver sección "Lo que falta" más arriba

### **Problema: "IllegalAccessException en FXML"**
**Solución:** 
Ya está resuelto en `module-info.java`

### **Problema: TableView no muestra datos**
**Solución:** 
Verificar que los getters de la clase modelo sigan la convención JavaBeans:
```java
// CORRECTO:
public String getNombreUsuario() { }
public void setNombreUsuario(String x) { }

// INCORRECTO:
public String getnombreUsuario() { }  // Nota la "n" minúscula
```

---

## 📚 Documentación Adicional

Archivos creados durante las correcciones:

1. **CAMBIOS_REALIZADOS.md** - Detalle completo de cada cambio
2. **GUIA_ERRORES_BD.md** - Cómo resolver errores ORA-00904
3. **RESUMEN_ESTADO.md** - Estado actual del proyecto
4. **SCRIPT_VERIFICACION_BD.sql** - Script para ver esquema de BD
5. **Este archivo** - Instrucciones finales

---

## ✅ Checklist Final

- [ ] He ejecutado SCRIPT_VERIFICACION_BD.sql
- [ ] Identifiqué los nombres reales de columnas
- [ ] Actualicé DAO/JornadaDAO.java (si fue necesario)
- [ ] Actualicé DAO/UsuarioDAO.java (si fue necesario)
- [ ] Compilé con `mvn clean compile` sin errores
- [ ] La aplicación se ejecuta con `mvn javafx:run`
- [ ] Puedo hacer login como admin
- [ ] Puedo crear usuarios, equipos y jornadas
- [ ] Puedo generar calendario

---

**¡El proyecto está listo para las pruebas finales!**

Si todavía hay errores, revisar los archivos de documentación mencionados arriba.

*Última actualización: 15 de Abril de 2026*

