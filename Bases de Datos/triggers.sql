--AUTOR: EQUIPO 4
--FECHA: 20/04/2026

-- 1. TRIGGER SUELDO MINIMO
CREATE OR REPLACE TRIGGER trg_sueldo_minimo
BEFORE INSERT OR UPDATE ON Jugadores
FOR EACH ROW
BEGIN
    IF :NEW.sueldo IS NOT NULL AND :NEW.sueldo < 1221 THEN
        :NEW.sueldo := 1221;
    END IF;
END;
/


-- 2. TRIGGER 6 JUGADORES MAXIMO
CREATE OR REPLACE TRIGGER trg_max_jugadores_equipo
FOR INSERT ON Jugadores
COMPOUND TRIGGER
    TYPE t_equipos IS TABLE OF NUMBER INDEX BY PLS_INTEGER;
    v_equipos t_equipos;
    v_idx PLS_INTEGER := 0;

    AFTER EACH ROW IS
    BEGIN
        v_idx := v_idx + 1;
        v_equipos(v_idx) := :NEW.id_equipo;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
        v_total NUMBER;
    BEGIN
        FOR i IN 1 .. v_equipos.COUNT LOOP
            SELECT COUNT(*) INTO v_total FROM Jugadores WHERE id_equipo = v_equipos(i);
            IF v_total > 6 THEN
                RAISE_APPLICATION_ERROR(-20012, 'Un equipo no puede tener mas de 6 jugadores');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END trg_max_jugadores_equipo;
/

-- 3. TRIGGER VALIDAR ESTADO (Tabla Competiciones)
CREATE OR REPLACE TRIGGER tr_validar_estado
BEFORE INSERT OR UPDATE OR DELETE ON Competiciones
FOR EACH ROW
BEGIN
    IF UPDATING OR DELETING THEN
        IF :OLD.estado IN ('cerrado') THEN
            RAISE_APPLICATION_ERROR(-20020, 'Competición bloqueada');
        END IF;
    END IF;
    IF INSERTING THEN
        IF :NEW.estado IN ('cerrado') THEN
            RAISE_APPLICATION_ERROR(-20021, 'No se puede crear una competición ya cerrada');
        END IF;
    END IF;
END;
/

-- 4. TRIGGER 1 JORNADA POR SEMANA (Tabla Jornadas)
CREATE OR REPLACE TRIGGER tr_jornada
FOR INSERT ON Jornadas
COMPOUND TRIGGER
    TYPE t_jornada_rec IS RECORD (id_comp NUMBER, fecha DATE);
    TYPE t_jornadas IS TABLE OF t_jornada_rec INDEX BY PLS_INTEGER;
    v_jornadas t_jornadas;
    v_idx PLS_INTEGER := 0;

    AFTER EACH ROW IS
    BEGIN
        v_idx := v_idx + 1;
        v_jornadas(v_idx).id_comp := :NEW.id_competicion;
        v_jornadas(v_idx).fecha := :NEW.fecha_jornada;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
        v_total NUMBER;
    BEGIN
        FOR i IN 1 .. v_jornadas.COUNT LOOP
            SELECT COUNT(*) INTO v_total FROM Jornadas
            WHERE id_competicion = v_jornadas(i).id_comp
              AND TRUNC(fecha_jornada, 'IW') = TRUNC(v_jornadas(i).fecha, 'IW');
            IF v_total > 1 THEN
                RAISE_APPLICATION_ERROR(-20030, 'Solo se permite una jornadas por semana');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END tr_jornada;
/

-- 5. TRIGGER FECHA RESULTADO (Tabla Equipos_Enfrentamientos)
CREATE OR REPLACE TRIGGER tr_fecha_resultado
BEFORE INSERT OR UPDATE ON Equipos_Enfrentamientos
FOR EACH ROW
DECLARE
    v_fecha DATE;
BEGIN
    -- Nota: Usamos Enfrentamientos porque es donde esta la fecha del partido
    SELECT fecha_enfrentamiento INTO v_fecha
    FROM Enfrentamientos
    WHERE id_partido = :NEW.id_partido;

    IF v_fecha > SYSDATE AND :NEW.resultado IS NOT NULL THEN
        RAISE_APPLICATION_ERROR(-20040, 'No se puede asignar un resultado antes de la fecha');
    END IF;
END;
/
