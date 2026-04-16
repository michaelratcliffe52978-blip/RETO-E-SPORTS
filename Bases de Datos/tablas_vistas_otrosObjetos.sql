--BORRADO Y CREACION DE TABLAS

-- 1. LIMPIEZA INICIAL (Borramos todo para evitar duplicados)
DROP TABLE Equipo_Enfrentamiento CASCADE CONSTRAINTS;
DROP TABLE Enfrentamiento CASCADE CONSTRAINTS;
DROP TABLE Jugador CASCADE CONSTRAINTS;
DROP TABLE Jornada CASCADE CONSTRAINTS;
DROP TABLE Equipo CASCADE CONSTRAINTS;
DROP TABLE Competicion CASCADE CONSTRAINTS;
DROP TABLE Usuario CASCADE CONSTRAINTS;

-- 2. CREACIÓN DE TABLAS
CREATE TABLE Competicion (
    id_competicion NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_competicion VARCHAR2(100),
    estado VARCHAR2(20),
    CONSTRAINT ck_estado_comp CHECK (estado IN ('abierto', 'cerrado', 'encurso', 'finalizado'))
);

CREATE TABLE Jornada (
    id_jornada NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_jornada NUMBER,
    fecha_jornada DATE,
    id_competicion NUMBER,
    CONSTRAINT fk_jornada_comp FOREIGN KEY (id_competicion) REFERENCES Competicion(id_competicion)
);

CREATE TABLE Equipo (
    id_equipo NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_equipo VARCHAR2(100),
    fecha_fundacion DATE
);

CREATE TABLE Jugador (
    id_jugador NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_jugador VARCHAR2(100),
    apellido VARCHAR2(100),
    nacionalidad VARCHAR2(50),
    fecha_nacimiento DATE,
    nickname VARCHAR2(50),
    rol VARCHAR2(20),
    sueldo NUMBER(10, 2),
    id_equipo NUMBER,
    CONSTRAINT ck_rol_jug CHECK (rol IN ('duelista', 'controlador', 'iniciador', 'centinela')),
    CONSTRAINT fk_jugador_equipo FOREIGN KEY (id_equipo) REFERENCES Equipo(id_equipo)
);

CREATE TABLE Enfrentamiento (
    id_partido NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_enfrentamiento DATE,
    equipo1 VARCHAR2(50),
    equipo2 VARCHAR2(50),
    hora VARCHAR2(10),
    id_jornada NUMBER,
    CONSTRAINT fk_enfrentamiento_jor FOREIGN KEY (id_jornada) REFERENCES Jornada(id_jornada)
);

CREATE TABLE Equipo_Enfrentamiento (
    id_equipo NUMBER,
    id_partido NUMBER,
    resultado VARCHAR2(50),
    PRIMARY KEY (id_equipo, id_partido),
    CONSTRAINT fk_inter_equipo FOREIGN KEY (id_equipo) REFERENCES Equipo(id_equipo),
    CONSTRAINT fk_inter_partido FOREIGN KEY (id_partido) REFERENCES Enfrentamiento(id_partido)
);

CREATE TABLE Usuario (
    id_usuario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario VARCHAR2(50),
    contrasena VARCHAR2(255),
    tipo VARCHAR2(10),
    CONSTRAINT ck_tipo_usuario_check CHECK (tipo IN ('user', 'admin'))
);

-- 3. INSERCIÓN DE DATOS (Ahora que las tablas EXISTEN)
INSERT INTO Competicion (nombre_competicion, estado) VALUES ('VALORANT Masters Madrid', 'encurso');



INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES ('Fnatic', TO_DATE('2004-07-23', 'YYYY-MM-DD'));
INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES ('Sentinels', TO_DATE('2017-06-01', 'YYYY-MM-DD'));
INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES ('LOUD', TO_DATE('2022-02-03', 'YYYY-MM-DD'));
INSERT INTO Equipo (nombre_equipo, fecha_fundacion) VALUES ('Team Liquid', TO_DATE('2000-01-01', 'YYYY-MM-DD'));

INSERT INTO Jugador (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Boaster', 'Jake Howlett', 'UK', TO_DATE('1995-05-25', 'YYYY-MM-DD'), 'Boaster', 'controlador', 5000, 1);

INSERT INTO Jugador (nombre_jugador, apellido, nacionalidad, fecha_nacimiento, nickname, rol, sueldo, id_equipo) 
VALUES ('Tyson', 'Ngo', 'Canada', TO_DATE('2001-05-05', 'YYYY-MM-DD'), 'TenZ', 'duelista', 8000, 2);

INSERT INTO Competicion (nombre_competicion, estado) 
VALUES ('VCT', 'abierto');


INSERT INTO Usuario (nombre_usuario, contrasena, tipo) VALUES ('admin', 'admin123', 'admin');

-- 4. GUARDAR CAMBIOS
COMMIT;




--VISTAS
//vistas jugadores y equipos 
create or replace view v_detalle_jugadores as
select j.nickname, j.nombre_jugador || ' ' || j.apellido as nombre_completo,
    j.rol, e.nombre_equipo, j.nacionalidad, j.sueldo
from jugador j join equipo e 
on j.id_equipo = e.id_equipo;



//vista calendario 
create or replace view v_calendario_partidos as
select c.nombre_competicion, jo.numero_jornada, e.fecha_enfrentamiento,
    e.hora, e.equipo1, e.equipo2
from enfrentamiento e join jornada jo
on e.id_jornada = jo.id_jornada
join competicion c 
on jo.id_competicion = c.id_competicion;



//vista resultados 
create or replace view v_resultado_partidos as
select p.id_partido, p.fecha_enfrentamiento, eq.nombre_equipo, ee.resultado
from equipo_enfrentamiento ee join equipo eq 
on ee.id_equipo = eq.id_equipo
join enfrentamiento p 
on ee.id_partido = p.id_partido;


//vista resumen competiciones
create or replace view v_resumen_competiciones as
select c.nombre_competicion, c.estado, count(j.id_jornada) as total_jornadas
from competicion c left join jornada j 
on c.id_competicion = j.id_competicion
group by c.nombre_competicion, c.estado;



----   OTROS OBJETOS    ----


---SINONIMOS---
//para las tablas principales
create or replace synonym jug for jugador;

        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='jug';

create or replace synonym eq for equipo;

        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='eq';


create or replace synonym com for competicion;

        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='com';


//para la tabla con nombre largo
create or replace synonym detalle_partido for equipo_enfrentamiento;
        
        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='detalle_partido';


//para las vistas 
create or replace synonym calendario for v_calendario_partidos;
        
        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='calendario';


create or replace synonym plantillas for v_detalle_jugadores;
        
        --comprobacion
        select synonym_name, table_owner, table_name
        from user_synonyms
        where lower(synonym_name)='plantillas';       


---INDICES---

//Para buscar por Nickname 
create index idx_jugador_nickname on jugador(nickname);
        --comprobacion 
        select *
        from user_indexes ui join user_ind_columns uc
        on ui.index_name=uc.index_name
        where lower(ui.index_name)='idx_jugador_nickname';

//Para buscar enfrentamientos por fecha 
create index idx_fecha_enfrentamiento on enfrentamiento(fecha_enfrentamiento);
        --comprobacion 
        select *
        from user_indexes ui join user_ind_columns uc
        on ui.index_name=uc.index_name
        where lower(ui.index_name)='idx_fecha_enfrentamiento';


// para mejorar la unión entre Jugador y Equipo
create index idx_jugador_equipo on Jugador(id_equipo);
        --comprobacion 
        select *
        from user_indexes ui join user_ind_columns uc
        on ui.index_name=uc.index_name
        where lower(ui.index_name)='idx_jugador_equipo';

//para mejorar la unión entre Jornada y Competición
create index idx_jornada_competicion on Jornada(id_competicion);
        --comprobacion 
        select *
        from user_indexes ui join user_ind_columns uc
        on ui.index_name=uc.index_name
        where lower(ui.index_name)='idx_jornada_competicion';


