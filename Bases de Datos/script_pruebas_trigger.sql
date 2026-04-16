--PRUEBA TRIGGER SUELDO M�NIMO
-- Prueba: Insertar un jugador con sueldo de 1000� (deber�a subir a 1221�)
INSERT INTO Jugador (nombre_jugador, sueldo, id_equipo) 
VALUES ('Prueba Sueldo', 1000, 1);

-- Verificaci�n
SELECT sueldo FROM Jugador WHERE lower(nombre_jugador)= lower('Prueba Sueldo'); 
-- El resultado debe ser 1221.



--PRUEBA TRIGGER EQUIPOS PARES 
-- Si tienes un n�mero par de equipos, al intentar insertar uno (quedando impar) fallar�:
INSERT INTO Equipo (nombre_equipo) VALUES ('Equipo Impar');
-- Deber�a lanzar: ORA-20002: El n�mero de equipos debe ser par



--PRUEBA TRIGGER 6 JUGADORES MAXIMO
-- Intenta insertar un 7� jugador en un equipo que ya tiene 6
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('Jugador Extra', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 3', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 4', 1);
INSERT INTO Jugador (nombre_jugador, id_equipo) VALUES ('jUGADOR 5', 1);

-- Deber�a lanzar: ORA-20012: Un equipo no puede tener m�s de 6 jugadores



--PRUEBA TRIGGER 2 JUGADORES MINIMO 
-- Intenta borrar un jugador de un equipo que solo tiene 2
DELETE FROM Jugador 
WHERE lower(nombre_jugador)= lower('jugador prueba');
-- Deber�a lanzar: ORA-20011: Un equipo debe tener al menos 2 jugadores


--PRUEBA TRIGGER VALIDAR ESTADO
-- Primero, aseg�rate de tener una competici�n cerrada
UPDATE Competicion SET estado = 'cerrado' WHERE id_competicion = 1;

-- Intenta cambiar el nombre de esa competici�n
UPDATE Competicion SET nombre_competicion = 'Nuevo Nombre' WHERE id_competicion = 1;
-- Deber�a lanzar: ORA-20020: Competici�n bloqueada




---PRUEBA TRIGGER JORNADA
-- Insertamos una jornadas para el pr�ximo lunes
INSERT INTO Jornada (id_competicion, fecha_jornada) 
VALUES (1, TO_DATE('2026-05-04', 'YYYY-MM-DD'));
-- Intentamos insertar otra jornadas en la misma semana (ej. el mi�rcoles de esa misma semana)
INSERT INTO Jornada (id_competicion, fecha_jornada) 
VALUES (1, TO_DATE('2026-05-06', 'YYYY-MM-DD'));
-- Deber�a lanzar: ORA-20030: Solo se permite una jornadas por semana...



---PRUEBA TRIGGER FECHA
-- Insertamos un partido para el a�o 2027 (claramente a futuro)
INSERT INTO Enfrentamiento (fecha_enfrentamiento, id_jornada) 
VALUES (TO_DATE('2027-01-01', 'YYYY-MM-DD'),1);

INSERT INTO Equipo_Enfrentamiento (id_partido, id_equipo, resultado) 
VALUES (21, 1, NULL); -- Lo insertamos sin resultado (esto el trigger lo permite)

UPDATE Equipo_Enfrentamiento 
SET resultado = '3-0' 
WHERE id_partido = 21;
 

