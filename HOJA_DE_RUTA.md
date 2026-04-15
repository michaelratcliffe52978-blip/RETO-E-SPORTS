# 🗺️ HOJA DE RUTA - Cómo Continuar

## 🎯 Tu Misión (Si la aceptas)

Completar el 9% faltante del proyecto siguiendo estos pasos simples.

---

## 📍 PASO 1: Verificar Base de Datos (5 minutos)

### ¿Qué hacer?
Abre Oracle SQL Developer o SQL*Plus y ejecuta:

```sql
DESC JORNADA;
DESC USUARIO;
DESC ENFRENTAMIENTO;
```

### ¿Qué buscar?
```
COLUMNA ESPERADA          →  POSIBLE NOMBRE REAL
─────────────────────────────────────────────────
ID_JORNADA               →  ID_JORNADA ✓
NUMERO_JORNADA           →  NUMERO ⚠️
FECHA_JORNADA            →  FECHA ⚠️
ID_USUARIO               →  ID_USUARIO ✓
NOMBRE_USUARIO           →  NOMBRE_USUARIO ✓
CONTRASENA               →  PASSWORD ⚠️
TIPO                     →  ROL ⚠️
ID_JORNADA (en FK)       →  ID_JORNADA ✓
```

### 📝 Mi Checklist
- [ ] Ejecuté DESC JORNADA
- [ ] Ejecuté DESC USUARIO
- [ ] Ejecuté DESC ENFRENTAMIENTO
- [ ] Anoté los nombres reales

---

## 📍 PASO 2: Actualizar Archivos (Si es Necesario)

### Si NUMERO_JORNADA se llama "NUMERO":

**Archivo:** `Programacion\src\main\java\org\example\programacion\DAO\JornadaDAO.java`

**Busca la línea ~38:**
```java
String sql = "SELECT ID_JORNADA, NUMERO_JORNADA, FECHA_JORNADA FROM JORNADA";
```

**Cámbiala a:**
```java
String sql = "SELECT ID_JORNADA, NUMERO, FECHA FROM JORNADA";
```

**También busca la línea ~24:**
```java
String sqlSelect = "SELECT ID_JORNADA FROM JORNADA WHERE NUMERO_JORNADA = ?";
```

**Cámbiala a:**
```java
String sqlSelect = "SELECT ID_JORNADA FROM JORNADA WHERE NUMERO = ?";
```

---

### Si CONTRASENA se llama "PASSWORD":

**Archivo:** `Programacion\src\main\java\org\example\programacion\DAO\UsuarioDAO.java`

**Busca todas las líneas con CONTRASENA:**
```java
"INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO) VALUES (?, ?, ?)"
"WHERE NOMBRE_USUARIO = ? AND CONTRASENA = ?"
```

**Y cámbialo todo por:**
```java
"INSERT INTO USUARIO (NOMBRE_USUARIO, PASSWORD, TIPO) VALUES (?, ?, ?)"
"WHERE NOMBRE_USUARIO = ? AND PASSWORD = ?"
```

---

### Si TIPO se llama "ROL":

**En el mismo archivo UsuarioDAO.java:**

**Busca:**
```java
"AND TIPO = 'admin'"
"INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, TIPO)"
"UPDATE USUARIO SET ... TIPO = ?"
```

**Cámbialo por:**
```java
"AND ROL = 'admin'"
"INSERT INTO USUARIO (NOMBRE_USUARIO, CONTRASENA, ROL)"
"UPDATE USUARIO SET ... ROL = ?"
```

### 📝 Mi Checklist
- [ ] Identifiqué los nombres reales
- [ ] Actualicé JornadaDAO.java (si fue necesario)
- [ ] Actualicé UsuarioDAO.java (si fue necesario)
- [ ] Guardé los archivos

---

## 📍 PASO 3: Compilar Proyecto (2 minutos)

### ¿Qué hacer?

Abre PowerShell/CMD en la carpeta del proyecto:

```bash
cd C:\Users\141GA01\RETO-E-SPORTS\Programacion
mvn clean compile
```

### ¿Qué esperar?

✅ **CORRECTO:**
```
[INFO] BUILD SUCCESS
```

❌ **ERROR:**
```
[ERROR] COMPILATION ERROR
```

Si hay error, revisa el mensaje y ajusta nuevamente.

### 📝 Mi Checklist
- [ ] Ejecuté mvn clean compile
- [ ] El resultado fue BUILD SUCCESS
- [ ] No hay errores en consola

---

## 📍 PASO 4: Ejecutar la Aplicación (1 minuto)

### ¿Qué hacer?

```bash
mvn javafx:run
```

### ¿Qué debería pasar?

1. Se abre una ventana JavaFX
2. Ves una interfaz gráfica
3. Puedes hacer login
4. No hay errores de base de datos

### 📝 Mi Checklist
- [ ] Ejecuté mvn javafx:run
- [ ] Se abrió la aplicación
- [ ] Puedo ver la interfaz
- [ ] Puedo hacer login

---

## 📍 PASO 5: Verificar Funcionalidades (5 minutos)

### Prueba estas cosas:

**1. Login:**
- [ ] Intenta login con admin (usuario existente)
- [ ] Intenta login con usuario normal (usuario existente)

**2. Crear Usuario:**
- [ ] Ve a CRUD → Usuarios
- [ ] Intenta crear un nuevo usuario
- [ ] Debería guardarse sin error

**3. Crear Equipo:**
- [ ] Ve a CRUD → Equipos
- [ ] Intenta crear un nuevo equipo
- [ ] Debería guardarse sin error

**4. Ver Datos:**
- [ ] Ve a CRUD → Jornadas
- [ ] Deberías ver la tabla con datos
- [ ] Si no hay datos, crea algunos nuevos

**5. Generar Calendario:**
- [ ] (Solo admin) Ve a MenuAdmin
- [ ] Haz clic en "Generar Calendario"
- [ ] Debería crearse sin error

### 📝 Mi Checklist
- [ ] El login funciona
- [ ] Puedo crear usuarios
- [ ] Puedo ver la tabla de jornadas
- [ ] No hay errores ORA-00904

---

## 📊 Resumen de Progreso

```
░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
ANTES (Inicio)
════════════════════════════════════════════════

PASO 1: Verificar BD              ████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
PASO 2: Actualizar DAOs           ██████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
PASO 3: Compilar                  ████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
PASO 4: Ejecutar                  ██████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
PASO 5: Verificar                 ████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░

DESPUÉS (Fin)
════════════════════════════════════════════════
PROYECTO 100% FUNCIONAL          ████████████████████████████████████████████
```

---

## ⏱️ Tiempo Total Estimado

| Paso | Tiempo | Acumulado |
|------|--------|-----------|
| 1. Verificar BD | 5 min | 5 min |
| 2. Actualizar archivos | 5 min | 10 min |
| 3. Compilar | 2 min | 12 min |
| 4. Ejecutar | 1 min | 13 min |
| 5. Verificar | 5 min | **18 min** |

**Total:** ~20 minutos (sin contar descargas)

---

## 🎁 Bonus: Si todo funciona

Podrás:
- ✅ Crear y editar usuarios
- ✅ Crear y editar equipos
- ✅ Crear y editar jugadores
- ✅ Crear y editar jornadas
- ✅ Generar calendario automático
- ✅ Registrar resultados
- ✅ Ver informes

---

## 🆘 Si algo no funciona

1. Revisa el error que sale en consola
2. Si es "ORA-00904", ve a [`GUIA_ERRORES_BD.md`](GUIA_ERRORES_BD.md)
3. Si es otro error, busca en [`INSTRUCCIONES_FINALES.md`](INSTRUCCIONES_FINALES.md)
4. Si no lo encuentras, lee [`CAMBIOS_REALIZADOS.md`](CAMBIOS_REALIZADOS.md)

---

## ✨ Último paso: ¡Celebra!

```
    ___
   (o o)
  ( =^= )
   ")_(")
    
HAS COMPLETADO 100% DEL PROYECTO ✅
```

**¡Enhorabuena! Ahora tienes un proyecto JavaFX completamente funcional con arquitectura MVC correcta.**

---

**Próximo:** Hacer lo que planees con la aplicación 🚀

*Documento actualizado: 15 de Abril de 2026*

