
/*
Procedimiento almacenado en la base de datos, que permita después en Java, ver el
informe con la relación de los jugadores de un equipo concreto. De cada jugador se
verá el nombre, apellido, rol y salario. El nombre del equipo le llegará como
parámetro. Las excepciones serán visualizadas en el programa Java.
*/

/*
Procedimiento informe	-20005	El equipo no existe.
Procedimiento informe -20006 Error inesperado del sistema
Procedimiento informe   -20007 El equipo existe pero no tiene jugadores.
*/


create or replace procedure informe_jugadores_equipo
(       p_nombre_equipo in varchar2,
        p_cursor out sys_refcursor
)
as
        v_id_equipo number;
        v_existe_jugadores number;
        
begin 

    begin 
        select id_equipo into v_id_equipo
        from equipo
        where lower(nombre_equipo)=lower(p_nombre_equipo);
        
        exception
            when no_data_found then 
                raise_application_error(-20005, 'ERROR: el equipo "'
                                            || p_nombre_equipo ||'" no existe');
    end; 
    
    select count(*) into v_existe_jugadores
    from jugador 
    where id_equipo = v_id_equipo;
    
    if v_existe_jugadores = 0 then 
        raise_application_error(-20007, 'ERROR: el equipo "' 
                    || p_nombre_equipo || '" existe pero no tiene jugadores');
        
    end if;
    
    open p_cursor for
        select nombre_jugador, apellido, rol, sueldo
        from jugador
        where id_equipo = v_id_equipo;
        
        
        exception
            when others then
                raise_application_error(-20006, 'Error inesperado del sistema al generar el informe' ||sqlerrm);


end;      


    
    
    
    
    
    
/*
Procedimiento almacenado en la base de datos, que permita después en Java, ver el
informe de la relación de los equipos que conforman la competición incluyendo para
cada equipo el nombre del mismo, la fecha de creación, la cantidad de jugadores que
hay en ese equipo, el salario máximo, el salario mínimo y la media de los salarios de
los jugadores de ese equipo. Las excepciones serán visualizadas en el programa
Java.
*/

create or replace procedure informe_estadisticas_equipos
(       p_cursor out sys_refcursor
)
as

begin 
    open p_cursor for 
        select e.nombre_equipo, e.fecha_fundacion, 
                count(j.id_jugador) as num_jugadores, 
                nvl(max(j.sueldo),0) as sueldo_max,
                nvl(min(j.sueldo),0) as sueldo_min,
                nvl(round(avg(j.sueldo),2),0) as sueldo_medio
        from equipo e left join jugador j
        on e.id_equipo=j.id_equipo
        group by e.id_equipo, e.nombre_equipo, e.fecha_fundacion
        order by e.nombre_equipo;
        
        exception
            when others then
                raise_application_error(-20008, 'Error al generar informe ' || sqlerrm);

end;              
        
        
