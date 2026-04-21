--AUTOR: EQUIPO 4
--FECHA: 20/04/2026


---  PROCEDIMIENTOS  ---
-- 1. PROCEDIMIENTO informe_jugadores_equipo
/*
COMENTARIO: nuestro proposito es generar un listado detallado de los jugadores que pertenezca a un equipo en especifico

¿COMO FUNCIONA?
1. primero se comprueba si el nombre del equipo ya existe en la tabla 'equipos'
2. despues se verifica si el equipo tiene jugadores asociados antes de abrir el cursor
3. despues se utiliza la vista 'vista_detalle_jugadores' para obtener los datos los cuales ya estan formateados anteriormente
4. por ultimo se implementan excepciones personalizadas para diferenciar si el equipo 
no existe (-20005) si el equipo esta vacio (-20007) o ha ocurrido un error del sistema (-20006)


EN JAVA
Este procedimiento se usa en CRUDEquipos.java
*/


create or replace procedure informe_jugadores_equipo
(       p_nombre_equipo in varchar2,
        p_cursor out sys_refcursor
)
as
        v_id_equipo number;
        v_existe_jugadores number;
        e_no_encontrado exception; 
        e_no_jugadores exception;
        
begin 

    begin 
        select id_equipo into v_id_equipo
        from equipos
        where lower(nombre_equipo)=lower(p_nombre_equipo);
        
        exception
            when no_data_found then 
                raise e_no_encontrado;      
    end;            
        
        select count(*) into v_existe_jugadores
        from vista_detalle_jugadores ---VISTA
        where id_equipo = v_id_equipo;
    
        if v_existe_jugadores = 0 then 
            raise e_no_jugadores;
             
        else
            open p_cursor for
                select nombre_completo, rol, sueldo
                from vista_detalle_jugadores ---VISTA
                where id_equipo = v_id_equipo;
        end if;
        
exception
            
    when e_no_encontrado then
        raise_application_error(-20005, 'ERROR: el equipo "'
                                    || p_nombre_equipo ||'" no existe');
    when e_no_jugadores then
        raise_application_error(-20007, 'ERROR: el equipo "' 
                    || p_nombre_equipo || '" existe pero no tiene jugadores');
    when others then
        raise_application_error(-20006, 'Error inesperado del sistema al
                            generar el informe' ||sqlerrm);


end informe_jugadores_equipo;
    




-- 2. PROCEDIMIENTO informe_estadisticas_equipos
/*
COMENTARIO: el objetivo es proporcionar una vision general y tambien estadistica del sueldo 
y tambien de la composición de todos los equipos 

¿COMO FUNCIONA?
1. primero se utiliza funciones de grupo (count, max...) sobre la vista 'vista_detalle_jugadores' 
para calcular las metricas por equipo
2. despues se aplica la función nvl() para asegurar que si un equipo no tiene sueldos registrados, 
se devuelva 0 en lugar de un valor nulo
3. despues se redondea el sueldo medio a 2 decimales para que la salida este mas limpia
4. por ultimo se devuelve un sys_refcursor que esta ordenado alfabeticamente por el nombre del equipo
lo que hace mas facil su lectura por ejemplo en bloques anonimos



EN JAVA
Este procedimiento se usa en EstadisticasEquipos.java
*/

create or replace procedure informe_estadisticas_equipos
(       p_cursor out sys_refcursor
)
as

begin 
    open p_cursor for 
        select nombre_equipo, fecha_fundacion, 
                count(id_jugador) as num_jugadores, 
                nvl(max(sueldo),0) as sueldo_max,
                nvl(min(sueldo),0) as sueldo_min,
                nvl(round(avg(sueldo),2),0) as sueldo_medio
        from vista_detalle_jugadores --VISTA 
        group by id_equipo, nombre_equipo, fecha_fundacion
        order by nombre_equipo;
        
        exception
            when others then
                raise_application_error(-20006, 'Error inesperado del sistema al
                                generar el informe' ||sqlerrm);

end;              
        
