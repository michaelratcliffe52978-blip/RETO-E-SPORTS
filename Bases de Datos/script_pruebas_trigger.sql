--PRUEBA TRIGGER SUELDO MÍNIMO
-- Prueba: Insertar un jugador con sueldo de 1000€ (debería subir a 1221€)
INSERT INTO Jugador (nombre_jugador, sueldo, id_equipo) 
VALUES ('Prueba Sueldo', 1000, 1);

-- Verificación
SELECT sueldo FROM Jugador WHERE lower(nombre_jugador)= lower('Prueba Sueldo'); 
-- El resultado debe ser 1221.



--PRUEBA TRIGGER EQUIPOS PARES 
-- Si tienes un número par de equipos, al intentar insertar uno (quedando impar) fallará:
INSERT INTO Equipo (nombre_equipo) VALUES ('Equipo Impar');
-- Debería lanzar: ORA-20002: El número de equipos debe ser par



--PRUEBA TRIGGER 6 JUGADORES MAXIMO
-- Intenta insertar un 7º jugador en un equipo que ya tiene 6
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('Jugador Extra', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 3', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 4', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 5', 1);

-- Debería lanzar: ORA-20012: Un equipo no puede tener más de 6 jugadores



--PRUEBA TRIGGER 2 JUGADORES MINIMO 
-- Intenta borrar un jugador de un equipo que solo tiene 2
DELETE FROM Jugador 
WHERE lower(nombre_jugador)= lower('jugador prueba');
-- Debería lanzar: ORA-20011: Un equipo debe tener al menos 2 jugadores


--PRUEBA TRIGGER VALIDAR ESTADO
-- Primero, asegúrate de tener una competición cerrada
UPDATE Competicion SET estado = 'cerrado' WHERE id_competicion = 1;

-- Intenta cambiar el nombre de esa competición
UPDATE Competicion SET nombre_competicion = 'Nuevo Nombre' WHERE id_competicion = 1;
-- Debería lanzar: ORA-20020: Competición bloqueada




---PRUEBA TRIGGER JORNADA
-- Insertamos una jornada para el próximo lunes
INSERT INTO Jornada (id_competicion, fecha_jornada) 
VALUES (1, TO_DATE('2026-05-04', 'YYYY-MM-DD'));
-- Intentamos insertar otra jornada en la misma semana (ej. el miércoles de esa misma semana)
INSERT INTO Jornada (id_competicion, fecha_jornada) 
VALUES (1, TO_DATE('2026-05-06', 'YYYY-MM-DD'));
-- Debería lanzar: ORA-20030: Solo se permite una jornada por semana...



---PRUEBA TRIGGER FECHA
-- Insertamos un partido para el año 2027 (claramente a futuro)
INSERT INTO Enfrentamiento (fecha_enfrentamiento, id_jornada) 
VALUES (TO_DATE('2027-01-01', 'YYYY-MM-DD'),1);

INSERT INTO Equipo_Enfrentamiento (id_partido, id_equipo, resultado) 
VALUES (21, 1, NULL); -- Lo insertamos sin resultado (esto el trigger lo permite)

UPDATE Equipo_Enfrentamiento 
SET resultado = '3-0' 
WHERE id_partido = 21;
 

