
/* 
bloque anonimo para comprobar que el procedimiento informe_estadisticas_equipos funciona
*/


/*
COMENTARIO: Este bloque prueba el procedimiento 'informe_estadisticas_equipos'.
            Muestra por consola la relación de todos los equipos con sus estadísticas salariales.
*/
    
declare
    v_lista_equipos sys_refcursor;
    v_nom equipo.nombre_equipo%type;
    v_fecha equipo.fecha_fundacion%type;
    v_cant number;
    v_max number;
    v_min number;
    v_avg number;
    
begin
    dbms_output.put_line('ESTADÍSTICAS POR EQUIPO:');
    -- Llamada al procedimiento almacenado
    informe_estadisticas_equipos(v_lista_equipos);
    -- Recorrido del cursor devuelto
    loop
        fetch v_lista_equipos into v_nom, v_fecha, v_cant, v_max, v_min, v_avg;
        exit when v_lista_equipos%NOTFOUND;
        dbms_output.put_line('Nombre: ' || v_nom || ' | Fundado: ' || v_fecha || 
                             ' | Jugadores: ' || v_cant || ' | Media: ' || v_avg);
    end loop;
    -- Cierre del recurso
    close v_lista_equipos;
    
    exception 
        when others then 
            dbms_output.put_line('Error de sistema '|| sqlerrm);
end;    

        
        
        
        
    //bloque anonimo para comprobar que el procedimiento informe_jugadores_equipo funciona


/*
COMENTARIO: Este bloque demuestra la funcionalidad del procedimiento 'informe_jugadores_equipo'.
            Simula la llamada que se realizaría desde Java, pasando como parámetro el nombre de un equipo 
            existente ('Fnatic') y recorriendo el cursor de salida para mostrar los datos de los jugadores.
*/
declare
    v_cursor sys_refcursor;
    v_nombre varchar2(50);
    v_apellido varchar2(50);
    v_rol varchar2(50);
    v_salario number;
    
begin   
    dbms_output.put_line('INICIO DE INFORME');
    -- Llamada al procedimiento almacenado
    informe_jugadores_equipo('Fnatic', v_cursor);
    -- Recorrido del cursor devuelto
    loop
        fetch v_cursor into v_nombre, v_apellido, v_rol, v_salario;
        exit when v_cursor%notfound;
        dbms_output.put_line('JUGADOR:'|| v_nombre ||' ' || v_apellido || '| Rol: ' || v_rol || 
                                    '| Salario : ' || v_salario);
    end loop;
    -- Cierre del recurso
    close v_cursor;
    dbms_output.put_line('FIN INFORME');
    
    exception 
        when others then 
            dbms_output.put_line('Error de sistema '|| sqlerrm);
    
end;    

    
    



