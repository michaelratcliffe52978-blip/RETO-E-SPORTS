--BORRADO Y CREACION DE TABLAS

-- 1. LIMPIEZA INICIAL (Borramos todo para evitar duplicados)
DROP TABLE Equipos_Enfrentamientos CASCADE CONSTRAINTS;
DROP TABLE Enfrentamientos CASCADE CONSTRAINTS;
DROP TABLE Jugadores CASCADE CONSTRAINTS;
DROP TABLE Jornadas CASCADE CONSTRAINTS;
DROP TABLE Equipos CASCADE CONSTRAINTS;
DROP TABLE Competiciones CASCADE CONSTRAINTS;
DROP TABLE Usuarios CASCADE CONSTRAINTS;

-- 2. CREACI?N DE TABLAS
CREATE TABLE Competiciones (
    id_competicion NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_competicion VARCHAR2(100),
    estado VARCHAR2(20) default 'abierto',
    CONSTRAINT comp_estado_ck CHECK (estado IN ('abierto', 'cerrado', 'encurso', 'finalizado'))
);


CREATE TABLE Jornadas (
    id_jornada NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_jornada NUMBER,
    fecha_jornada DATE,
    id_competicion NUMBER,
    CONSTRAINT  comp_jornada_fk FOREIGN KEY (id_competicion) REFERENCES Competiciones(id_competicion)
);

CREATE TABLE Equipos (
    id_equipo NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_equipo VARCHAR2(100),
    fecha_fundacion DATE
);

CREATE TABLE Jugadores (
    id_jugador NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_jugador VARCHAR2(100),
    apellido VARCHAR2(100),
    nacionalidad VARCHAR2(50),
    fecha_nacimiento DATE,
    nickname VARCHAR2(50),
    rol VARCHAR2(20),
    sueldo NUMBER(10, 2),
    id_equipo NUMBER,
    CONSTRAINT jug_rol_ck CHECK (rol IN ('duelista', 'controlador', 'iniciador', 'centinela')),
    CONSTRAINT  equipo_jugador_fk FOREIGN KEY (id_equipo) REFERENCES Equipos(id_equipo)
);

CREATE TABLE Enfrentamientos (
    id_partido NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_enfrentamiento DATE,
    equipo1 VARCHAR2(50),
    equipo2 VARCHAR2(50),
    hora VARCHAR2(10),
    id_jornada NUMBER,
    CONSTRAINT jor_enfrentamientos_fk FOREIGN KEY (id_jornada) REFERENCES Jornadas(id_jornada)
);

CREATE TABLE Equipos_Enfrentamientos (
    id_equipo NUMBER,
    id_partido NUMBER,
    resultado VARCHAR2(50),
    PRIMARY KEY (id_equipo, id_partido),
    CONSTRAINT equipo_inter_fk FOREIGN KEY (id_equipo) REFERENCES Equipos(id_equipo),
    CONSTRAINT partido_inter_fk FOREIGN KEY (id_partido) REFERENCES Enfrentamientos(id_partido)
);

CREATE TABLE Usuarios (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario VARCHAR2(50),
    contrasena VARCHAR2(255),
    tipo VARCHAR2(10),
    CONSTRAINT tipo_usuario_ck CHECK (tipo IN ('user', 'admin'))
);


-- ==========================================
-- 4. INSERCIÓN DE DATOS (Equipos y Jugadores)
-- ==========================================

--Competicion
INSERT INTO Competiciones (nombre_competicion, estado) 
VALUES ('UFC', 'abierto');

-- INSERTAR 4 EQUIPOS
INSERT INTO Equipos (nombre_equipo, fecha_fundacion) 
VALUES ('KOI', TO_DATE('2021-12-15', 'YYYY-MM-DD'));

INSERT INTO Equipos (nombre_equipo, fecha_fundacion) 
VALUES ('Team Heretics', TO_DATE('2016-08-24', 'YYYY-MM-DD'));

INSERT INTO Equipos (nombre_equipo, fecha_fundacion) 
VALUES ('Giants', TO_DATE('2008-07-01', 'YYYY-MM-DD'));

INSERT INTO Equipos (nombre_equipo, fecha_fundacion) 
VALUES ('Karmine Corp', TO_DATE('2020-03-30', 'YYYY-MM-DD'));


-- INSERTAR 8 JUGADORES (2 por equipo, respetando los roles del CONSTRAINT)

--Competicion


-- Jugadores de KOI (id_equipo = 1)
INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('José Luis', 'Aranguren', 'España', TO_DATE('1998-05-12', 'YYYY-MM-DD'), 'koldamenta', 'controlador', 50000.00, 1);

INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Bogdan', 'Naumov', 'Bulgaria', TO_DATE('2001-09-22', 'YYYY-MM-DD'), 'sheydos', 'iniciador', 55000.00, 1);


-- Jugadores de Team Heretics (id_equipo = 2)
INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Wassim', 'Cista', 'Francia', TO_DATE('2003-04-14', 'YYYY-MM-DD'), 'keloqz', 'duelista', 60000.00, 2);

INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Auni', 'Chahade', 'Dinamarca', TO_DATE('1999-11-05', 'YYYY-MM-DD'), 'AvovA', 'controlador', 48000.00, 2);


-- Jugadores de Giants (id_equipo = 3)
INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Adolfo', 'Gallego', 'España', TO_DATE('2000-02-18', 'YYYY-MM-DD'), 'Fit1nho', 'duelista', 52000.00, 3);

INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Kirill', 'Nekrasov', 'Rusia', TO_DATE('1998-10-30', 'YYYY-MM-DD'), 'Cloud', 'iniciador', 49000.00, 3);


-- Jugadores de Karmine Corp (id_equipo = 4)
INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Ryad', 'Ensaad', 'Francia', TO_DATE('2002-07-21', 'YYYY-MM-DD'), 'Shin', 'centinela', 45000.00, 4);

INSERT INTO Jugadores (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Alexis', 'Guitard', 'Francia', TO_DATE('2001-01-10', 'YYYY-MM-DD'), 'Newzera', 'iniciador', 47000.00, 4);


--Jornadas
INSERT INTO Jornadas (numero_jornada, fecha_jornada, id_competicion)
VALUES (1, TO_DATE('24/04/2025','DD/MM/YYYY'), NULL);

INSERT INTO Jornadas (numero_jornada, fecha_jornada, id_competicion)
VALUES (2, TO_DATE('01/05/2025','DD/MM/YYYY'), NULL);

INSERT INTO Jornadas (numero_jornada, fecha_jornada, id_competicion)
VALUES (3, TO_DATE('08/05/2025','DD/MM/YYYY'), NULL);


--Enfrentamientos
INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('24/04/2025','DD/MM/YYYY'), 'KOI', 'Karmine Corp', '10:00', 1);

INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('24/04/2025','DD/MM/YYYY'), 'Team Heretics', 'Giants', '12:00', 1);

INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('01/05/2025','DD/MM/YYYY'), 'Team Heretics', 'Karmine Corp', '10:00', 2);

INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('01/05/2025','DD/MM/YYYY'), 'Giants', 'KOI', '12:00', 2);

INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('08/05/2025','DD/MM/YYYY'), 'Giants', 'Karmine Corp', '10:00', 3);

INSERT INTO Enfrentamientos (fecha_enfrentamiento, equipo1, equipo2, hora, id_jornada)
VALUES (TO_DATE('08/05/2025','DD/MM/YYYY'), 'KOI', 'Team Heretics', '12:00', 3);


--Usuarios
INSERT INTO Usuarios (nombre_usuario, contrasena, tipo) 
VALUES ('user', 'user123', 'user');

INSERT INTO Usuarios (nombre_usuario, contrasena, tipo) 
VALUES ('admin', 'admin123', 'admin');

-- GUARDAR CAMBIOS DE LOS INSERTS
COMMIT;

