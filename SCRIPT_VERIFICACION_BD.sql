-- Script de Verificación de Base de Datos Oracle
-- Ejecuta estos comandos para diagnosticar problemas

-- ============================================
-- 1. VER ESTRUCTURA DE TABLA JORNADA
-- ============================================
DESC JORNADA;

-- Debería mostrar columnas. Busca nombres como:
-- - NUMERO_JORNADA o NUMERO
-- - FECHA_JORNADA o FECHA
-- - ID_JORNADA

-- ============================================
-- 2. VER ESTRUCTURA DE TABLA USUARIO
-- ============================================
DESC USUARIO;

-- Debería mostrar columnas. Busca nombres como:
-- - NOMBRE_USUARIO
-- - CONTRASENA o PASSWORD
-- - TIPO o ROL
-- - ID_USUARIO

-- ============================================
-- 3. VER ESTRUCTURA DE TABLA ENFRENTAMIENTO
-- ============================================
DESC ENFRENTAMIENTO;

-- Debería mostrar columnas. Busca nombres como:
-- - ID_JORNADA
-- - EQUIPO1, EQUIPO2
-- - FECHA_ENFRENTAMIENTO o similar
-- - HORA

-- ============================================
-- 4. VER TODAS LAS COLUMNAS CON TIPOS
-- ============================================
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, NULLABLE
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME IN ('JORNADA', 'USUARIO', 'ENFRENTAMIENTO', 'EQUIPO', 'JUGADOR')
ORDER BY TABLE_NAME, COLUMN_ID;

-- ============================================
-- 5. VER RESTRICCIONES (CONSTRAINTS)
-- ============================================
SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE, TABLE_NAME, STATUS
FROM USER_CONSTRAINTS
WHERE TABLE_NAME IN ('JORNADA', 'USUARIO', 'ENFRENTAMIENTO', 'EQUIPO', 'JUGADOR');

-- ============================================
-- 6. VER CLAVES FORÁNEAS ESPECÍFICAMENTE
-- ============================================
SELECT
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    R_CONSTRAINT_NAME,
    STATUS
FROM USER_CONS_COLUMNS
WHERE TABLE_NAME = 'ENFRENTAMIENTO';

-- ============================================
-- 7. DATOS DE PRUEBA - Ver contenido
-- ============================================
SELECT * FROM JORNADA;
SELECT * FROM USUARIO;
SELECT * FROM EQUIPO;
SELECT * FROM JUGADOR;

-- ============================================
-- 8. VER DEFINICIÓN EXACTA DE ENFRENTAMIENTO
-- ============================================
SELECT CONSTRAINT_NAME, R_TABLE_NAME, DELETE_RULE
FROM USER_CONSTRAINTS
WHERE TABLE_NAME = 'ENFRENTAMIENTO' AND CONSTRAINT_TYPE = 'R';

-- ============================================
-- RESULTADOS ESPERADOS SEGÚN CÓDIGO ACTUAL
-- ============================================
/*
JORNADA debería tener:
- ID_JORNADA (PK)
- NUMERO_JORNADA
- FECHA_JORNADA

USUARIO debería tener:
- ID_USUARIO (PK)
- NOMBRE_USUARIO
- CONTRASENA
- TIPO

ENFRENTAMIENTO debería tener:
- ID_PARTIDO o similar (PK)
- FECHA_ENFRENTAMIENTO
- EQUIPO1 (nombre del equipo o FK)
- EQUIPO2 (nombre del equipo o FK)
- HORA
- ID_JORNADA (FK a JORNADA.ID_JORNADA)
*/

-- ============================================
-- Si encuentras nombres diferentes,
-- ANOTA los nombres reales aquí:
-- ============================================
/*
CAMBIOS NECESARIOS:
1. Nombre de columna en JORNADA para número: __________
2. Nombre de columna en JORNADA para fecha: __________
3. Nombre de columna en USUARIO para contraseña: __________
4. Nombre de columna en USUARIO para rol: __________

Una vez identificados, actualiza los archivos DAO:
- C:\Users\141GA01\RETO-E-SPORTS\Programacion\src\main\java\org\example\programacion\DAO\JornadaDAO.java
- C:\Users\141GA01\RETO-E-SPORTS\Programacion\src\main\java\org\example\programacion\DAO\UsuarioDAO.java
*/

