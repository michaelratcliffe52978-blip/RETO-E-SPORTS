SET SERVEROUTPUT ON;

-- 1. TRIGGER: trg_sueldo_minimo
/* Este trigger se ejecuta antes de insertar o actualizar registros en la tabla Jugador.
Su función es verificar que el sueldo introducido no sea inferior al mínimo establecido (1.221€).
En caso de que el valor sea menor, se ajusta automáticamente al salario mínimo permitido,
garantizando así la integridad de la regla de negocio definida.*/

-- 1. TRIGGER: trg_sueldo_minimo

CREATE OR REPLACE TRIGGER trg_sueldo_minimo
BEFORE INSERT OR UPDATE ON Jugador
FOR EACH ROW
BEGIN
    IF :NEW.sueldo IS NOT NULL AND :NEW.sueldo < 1221 THEN
        :NEW.sueldo := 1221;
    END IF;
END;


-- 2. TRIGGER: trg_equipos_pares
/*Este trigger se encarga de controlar que el número de equipos en la tabla Equipo
sea siempre par. Antes de insertar o eliminar un equipo, comprueba cuántos equipos
existen y, en caso de que el resultado sea impar, muestra un aviso e impide la
operación para mantener la condición de paridad.
*/
CREATE OR REPLACE TRIGGER trg_equipos_pares
AFTER INSERT OR DELETE ON Equipo
DECLARE
    v_total NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_total FROM Equipo;

    IF MOD(v_total, 2) <> 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'El número de equipos debe ser par');
    END IF;
END;


-- 3. TRIGGER: trg_min_jugadores_equipo
/*Este trigger se ejecuta antes de insertar o eliminar jugadores en la tabla Jugador.
Su función es asegurar que ningún equipo tenga menos de 2 jugadores, impidiendo 
la operación si al realizarla se incumple esta condición.
*/

CREATE OR REPLACE TRIGGER trg_min_jugadores_equipo
BEFORE INSERT OR DELETE ON Jugador
FOR EACH ROW
DECLARE
    v_total NUMBER;
BEGIN
    IF INSERTING THEN
        SELECT COUNT(*) INTO v_total
        FROM Jugador
        WHERE id_equipo = :NEW.id_equipo;

        IF v_total + 1 < 2 THEN
            RAISE_APPLICATION_ERROR(-20010, 'Un equipo debe tener al menos 2 jugadores');
        END IF;

    ELSIF DELETING THEN
        SELECT COUNT(*) INTO v_total
        FROM Jugador
        WHERE id_equipo = :OLD.id_equipo;

        IF v_total - 1 < 2 THEN
            RAISE_APPLICATION_ERROR(-20011, 'Un equipo debe tener al menos 2 jugadores');
        END IF;
    END IF;
END;


-- 4. TRIGGER: trg_max_jugadores_equipo
/*Este trigger controla que un equipo no pueda tener más de 6 jugadores. Antes de
insertar un nuevo jugador, comprueba cuántos jugadores tiene su equipo y bloquea
la inserción si al añadirlo se supera ese límite.
*/

CREATE OR REPLACE TRIGGER trg_max_jugadores_equipo
BEFORE INSERT ON Jugador
FOR EACH ROW
DECLARE
    v_total NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_total
    FROM Jugador
    WHERE id_equipo = :NEW.id_equipo;

    IF v_total + 1 > 6 THEN
        RAISE_APPLICATION_ERROR(-20012, 'Un equipo no puede tener más de 6 jugadores');
    END IF;
END;


-- 5. TRIGGER: tr_validar_estado
/*Este trigger impide modificar datos de la tabla Competicion cuando su estado es
'cerrado' o 'finalizado'. De esta forma se asegura la integridad de la competición,
evitando cambios una vez que ya no está activa.

Este trigger se ejecuta antes de actualizar o eliminar una competición y su función
es impedir cualquier modificación cuando el estado de la competición es “cerrado”
o “finalizado”, garantizando así la integridad de los datos una vez que la 
competición ha terminado.
*/

CREATE OR REPLACE TRIGGER tr_validar_estado
BEFORE INSERT OR UPDATE OR DELETE ON Competicion
FOR EACH ROW
BEGIN
    IF :OLD.estado IN ('cerrado', 'finalizado') THEN
        RAISE_APPLICATION_ERROR(-20020, 'Competición bloqueada');
    END IF;
END;
/


-- 6. TRIGGER: tr_jornada
/*Este trigger controla la creación de jornadas en la tabla Jornada, asegurando
que solo se pueda registrar una jornada por semana dentro de una misma competición.
Si ya existe una jornada en la misma semana, la inserción se bloquea.
*/

CREATE OR REPLACE TRIGGER tr_jornada
BEFORE INSERT ON Jornada
FOR EACH ROW
DECLARE
    v_total NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_total
    FROM Jornada
    WHERE id_competicion = :NEW.id_competicion
      AND TRUNC(fecha_jornada, 'IW') = TRUNC(:NEW.fecha_jornada, 'IW');

    IF v_total > 0 THEN
        RAISE_APPLICATION_ERROR(-20030, 'Solo se permite una jornada por semana en la misma competición');
    END IF;
END;


-- 7. TRIGGER: tr_fecha_resultado
/*Este trigger evita que se introduzca un resultado en un enfrentamiento si la 
fecha del partido todavía no ha ocurrido. De esta forma, solo se pueden registrar
resultados una vez que el partido ya se ha jugado.
*/

CREATE OR REPLACE TRIGGER tr_fecha_resultado
BEFORE INSERT OR UPDATE ON Equipo_Enfrentamiento
FOR EACH ROW
DECLARE
    v_fecha DATE;
BEGIN
    SELECT fecha_enfrentamiento INTO v_fecha
    FROM Enfrentamiento
    WHERE id_partido = :NEW.id_partido;

    IF v_fecha > SYSDATE AND :NEW.resultado IS NOT NULL THEN
        RAISE_APPLICATION_ERROR(-20040, 'No se puede asignar un resultado antes de la fecha del enfrentamiento');
    END IF;
END;


