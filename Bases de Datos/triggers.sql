SET SERVEROUTPUT ON;

-- 1. TRIGGER SUELDO MÍNIMO
CREATE OR REPLACE TRIGGER trg_sueldo_minimo
BEFORE INSERT OR UPDATE ON Jugadores -- Antes decía Jugador
FOR EACH ROW
BEGIN
    IF :NEW.sueldo IS NOT NULL AND :NEW.sueldo < 1221 THEN
        :NEW.sueldo := 1221;
    END IF;
END;
/

-- 2. TRIGGER EQUIPO PARES
CREATE OR REPLACE TRIGGER trg_equipos_pares
AFTER INSERT OR DELETE ON Equipos -- Antes decía Equipo
DECLARE
    v_total NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_total FROM Equipos;

    IF MOD(v_total, 2) <> 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'El número de equipos debe ser par (actualmente hay ' || v_total || ')');
    END IF;
END;
/

-- 3. TRIGGER 2 JUGADORES MÍNIMO (Compound Trigger)
CREATE OR REPLACE TRIGGER trg_min_jugadores_equipo
FOR INSERT OR DELETE ON Jugadores
COMPOUND TRIGGER
    TYPE t_equipos IS TABLE OF NUMBER INDEX BY PLS_INTEGER;
    v_equipos t_equipos;
    v_idx PLS_INTEGER := 0;

    AFTER EACH ROW IS
    BEGIN
        v_idx := v_idx + 1;
        IF INSERTING THEN v_equipos(v_idx) := :NEW.id_equipo;
        ELSIF DELETING THEN v_equipos(v_idx) := :OLD.id_equipo;
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT IS
        v_total NUMBER;
    BEGIN
        FOR i IN 1 .. v_equipos.COUNT LOOP
            SELECT COUNT(*) INTO v_total FROM Jugadores WHERE id_equipo = v_equipos(i);
            IF v_total < 2 THEN
                RAISE_APPLICATION_ERROR(-20010, 'Un equipo debe tener al menos 2 jugadores');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END trg_min_jugadores_equipo;
/

-- 4. TRIGGER 6 JUGADORES MÁXIMO (Compound Trigger)
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
                RAISE_APPLICATION_ERROR(-20012, 'Un equipo no puede tener más de 6 jugadores');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END trg_max_jugadores_equipo;
/

-- 5. TRIGGER VALIDAR ESTADO (Tabla Competiciones)
CREATE OR REPLACE TRIGGER tr_validar_estado
BEFORE INSERT OR UPDATE OR DELETE ON Competiciones
FOR EACH ROW
BEGIN
    IF :OLD.estado IN ('cerrado', 'finalizado') THEN
        RAISE_APPLICATION_ERROR(-20020, 'Competición bloqueada');
    END IF;
END;
/

-- 6. TRIGGER 1 JORNADA POR SEMANA (Tabla Jornadas)
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
                RAISE_APPLICATION_ERROR(-20030, 'Solo se permite una jornada por semana');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END tr_jornada;
/

-- 7. TRIGGER FECHA RESULTADO (Tabla Equipos_Enfrentamientos)
CREATE OR REPLACE TRIGGER tr_fecha_resultado
BEFORE INSERT OR UPDATE ON Equipos_Enfrentamientos
FOR EACH ROW
DECLARE
    v_fecha DATE;
BEGIN
    -- Nota: Usamos Enfrentamientos porque es donde está la fecha del partido
    SELECT fecha_enfrentamiento INTO v_fecha
    FROM Enfrentamientos
    WHERE id_partido = :NEW.id_partido;

    IF v_fecha > SYSDATE AND :NEW.resultado IS NOT NULL THEN
        RAISE_APPLICATION_ERROR(-20040, 'No se puede asignar un resultado antes de la fecha');
    END IF;
END;
/