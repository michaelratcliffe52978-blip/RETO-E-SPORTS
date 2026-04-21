--AUTOR: EQUIPO 4
--FECHA: 20/04/2026


-- 1. LIMPIEZA INICIAL (Borramos todo para evitar duplicados)
DROP TABLE Equipos_Enfrentamientos CASCADE CONSTRAINTS;
DROP TABLE Enfrentamientos CASCADE CONSTRAINTS;
DROP TABLE Jugadores CASCADE CONSTRAINTS;
DROP TABLE Jornadas CASCADE CONSTRAINTS;
DROP TABLE Equipos CASCADE CONSTRAINTS;
DROP TABLE Competiciones CASCADE CONSTRAINTS;
DROP TABLE Usuarios CASCADE CONSTRAINTS;


-- 2. CREACION DE TABLAS
create table Competiciones (
    id_competicion number generated always as identity,
    nombre_competicion varchar2(100),
    estado varchar2(20) default 'abierto',
    constraint comp_estado_ck check (estado in ('abierto', 'cerrado')),
    constraint comp_id_pk primary key(id_competicion)
);


create table Jornadas (
    id_jornada number generated always as identity,
    numero_jornada number,
    fecha_jornada date,
    id_competicion number,
    constraint comp_jornada_fk foreign key (id_competicion) 
                                    references competiciones(id_competicion),
    constraint jor_id_pk primary key(id_jornada)
);

create table Equipos (
    id_equipo number generated always as identity,
    nombre_equipo varchar2(100),
    fecha_fundacion date,
    constraint eq_id_pk primary key(id_equipo)
);

create table Jugadores (
    id_jugador number generated always as identity,
    nombre_jugador varchar2(100),
    apellido varchar2(100),
    nacionalidad varchar2(50),
    fecha_nacimiento date,
    nickname varchar2(50),
    rol varchar2(20),
    sueldo number(10, 2),
    id_equipo number,
    constraint jug_id_pk primary key(id_jugador),
    constraint jug_rol_ck check (rol in ('duelista', 'controlador', 'iniciador', 
                                        'centinela')),
    constraint jug_idEquipo_fk foreign key (id_equipo) references 
                                                            Equipos(id_equipo)
);

create table Enfrentamientos (
    id_partido number generated always as identity,
    fecha_enfrentamiento date,
    equipo1 varchar2(50),
    equipo2 varchar2(50),
    hora varchar2(10),
    id_jornada number,
    constraint enf_id_pk primary key(id_partido),
    constraint enf_idJornada_fk foreign key (id_jornada) 
                                        references Jornadas(id_jornada)
);

create table Equipos_Enfrentamientos (
    id_equipo number,
    id_partido number,
    resultado varchar2(50),
    constraint id_equipo_id_partido_pk primary key (id_equipo, id_partido),
    constraint inter_equipo_fk foreign key (id_equipo) 
                                            references Equipos(id_equipo),
    constraint inter_partido_fk foreign key (id_partido) 
                                        references Enfrentamientos(id_partido)
);

create table Usuarios (
    id_usuario number generated always as identity,
    nombre_usuario varchar2(50),
    contrasena varchar2(255),
    tipo varchar2(10),
    constraint tipo_usuario_ck check (tipo in ('user', 'admin')),
    constraint us_id_pk primary key(id_usuario)
);


-- 3. VISTAS
/*
COMENTARIO: esta vista la hemos creado con el fin de consolidar la informacion tecnica de los 
jugadores con los datos de sus equipos. El objetivo es simplificarnos las consultas 
para evitar el uso repetitivo de los join

COMO LO HEMOS USADO: Permite consultar el nombre del equipo y el nombre completo del jugador 
que esta ya concatenado. Proporcionamos la columna 'nombre_completo' estandarizando la presentación de la identidad del jugador en todas las aplicaciones
y tambien facilita el filtrado por 'id_equipo' o 'nombre_equipo' en procedimientos almacenados e informes.
*/
create or replace view vista_detalle_jugadores as
select  j.id_equipo, j.nickname, j.id_jugador,
        j.nombre_jugador || ' ' || j.apellido as nombre_completo,
        j.rol, e.nombre_equipo, 
        j.nacionalidad, j.sueldo, e.fecha_fundacion
from jugadores j join equipos e 
on j.id_equipo = e.id_equipo;





