--AUTOR: EQUIPO 4
--FECHA: 20/04/2026


---  BLOQUES ANONIMOS  ---  
-- 1. BLOQUE ANONIMO: bloque anonimo para comprobar que el procedimiento informe_jugadores_equipo funciona

/*
COMENTARIO: INFORME DE JUGADORES

el objetivo es alidar el funcionamiento del procedimiento 'informe_jugadores_equipo' y tambien 
la integracion con la vista 'vista_detalle_jugadores'

ESCENARIOS DE PRUEBA:
1. CASO 'SI EXISTE': primero se pasa un equipo existente (por ejemplo 'KOI') el bloque debe recuperar 
   el cursor, extraer los datos (nombre, rol, sueldo) y mostrarlos por consola
2. CASO 'SI NO EXISTE': se pasa un nombre que no existe (por ejemplo 'Equipo1') el bloque debe 
   capturar la excepcion personalizada ORA-20005 lanzada por el procedimiento y 
   mostrar el mensaje de error 

DETALLES:
- el uso de %TYPE el cual garantiza la compatibilidad de las variables con la estructura de la vista
- la gestion del Cursor se demuestra el ciclo de vida completo del cursor (la apertura en procedimiento, 
fetch y close en el bloque).
*/

--1.1 CASO 'SI EXISTE'

declare
    v_cursor sys_refcursor;
    v_nombre_completo    vista_detalle_jugadores.nombre_completo%type; --VISTA
    v_rol     vista_detalle_jugadores.rol%type;  --VISTA
    v_sueldo     vista_detalle_jugadores.sueldo%type;  --VISTA
    
begin   
    dbms_output.put_line('INICIO DE INFORME');
    -- Llamada al procedimiento almacenado
    informe_jugadores_equipo('KOI', v_cursor);
    -- Recorrido del cursor devuelto
    loop
        fetch v_cursor into v_nombre_completo, v_rol, v_sueldo;
        exit when v_cursor%notfound;
        dbms_output.put_line('JUGADOR:'|| v_nombre_completo || '| Rol: ' 
                            || v_rol || '| Sueldo : ' || v_sueldo);
    end loop;
    -- Cierre del recurso
    close v_cursor;
    dbms_output.put_line('FIN INFORME');
    
    exception 
        when others then 
            dbms_output.put_line('Error de sistema '|| sqlerrm);
    
end;   

-- RESULTADO --    
/*
INICIO DE INFORME
JUGADOR:José Luis Aranguren| Rol: controlador| Sueldo : 50000
JUGADOR:Bogdan Naumov| Rol: iniciador| Sueldo : 55000
FIN INFORME

Procedimiento PL/SQL terminado correctamente.
*/

--1.2 CASO 'SI NO EXISTE'

declare
    v_cursor sys_refcursor;
    v_nombre_completo    vista_detalle_jugadores.nombre_completo%type; --VISTA
    v_rol     vista_detalle_jugadores.rol%type;  --VISTA
    v_sueldo     vista_detalle_jugadores.sueldo%type;  --VISTA
    
begin   
    dbms_output.put_line('INICIO DE INFORME');
    -- Llamada al procedimiento almacenado
    informe_jugadores_equipo('Equipo1', v_cursor);
    -- Recorrido del cursor devuelto
    loop
        fetch v_cursor into v_nombre_completo, v_rol, v_sueldo;
        exit when v_cursor%notfound;
        dbms_output.put_line('JUGADOR:'|| v_nombre_completo || '| Rol: ' 
                            || v_rol || '| Sueldo : ' || v_sueldo);
    end loop;
    -- Cierre del recurso
    close v_cursor;
    dbms_output.put_line('FIN INFORME');
    
    exception 
        when others then 
            dbms_output.put_line('Error de sistema '|| sqlerrm);
    
end;  

-- RESULTADO --    
/*
INICIO DE INFORME
Error de sistema ORA-20005: ERROR: el equipo "Equipo1" no existe

Procedimiento PL/SQL terminado correctamente.

*/




-- 2. BLOQUE ANONIMO: bloque anonimo para comprobar que el procedimiento informe_estadisticas_equipos funciona
/*
COMENTARIO: ESTADÍSTICAS GLOBALES

el objetivo es verificar la capacidad del sistema para realizar calculos agregados sobre el sueldo de todos los equipos.

FUNCIONALIDADES:
1. el bloque recibe y procesa 6 valores distintos por cada fila del cursor 
(nombre, fundacion, cantidad y estadisticas de sueldo)
2. tambien se comprueba que el formato de fecha y los redondeos de los 
promedios definidos en el procedimiento se visualizan correctamente
3. el resultado permite validar que el 'group by' del procedimiento funciona 
agrupando a los jugadores por sus respectivos equipos

CONTROL DE SALIDA:
- Se utiliza 'dbms_output.put_line' para generar un reporte legible por pantalla
*/
 
declare
    v_lista_equipos sys_refcursor;
    v_nombre_equipo    vista_detalle_jugadores.nombre_equipo%type;
    v_fecha_fundacion  vista_detalle_jugadores.fecha_fundacion%type;
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
        fetch v_lista_equipos into v_nombre_equipo, v_fecha_fundacion, v_cant, v_max, v_min, v_avg;
        exit when v_lista_equipos%NOTFOUND;
        dbms_output.put_line('Nombre de Equipo: ' || v_nombre_equipo || ' | Fundado en: ' 
                            || v_fecha_fundacion || ' | Jugadores: ' 
                            || v_cant || ' | Media: ' || v_avg);
    end loop;
    -- Cierre del recurso
    close v_lista_equipos;
    
    exception 
        when others then 
            dbms_output.put_line('Error de sistema '|| sqlerrm);
end;    

        


-- RESULTADO --
/*
ESTADÍSTICAS POR EQUIPO:
Nombre de Equipo: Giants | Fundado en: 01/07/08 | Jugadores: 2 | Media: 50500
Nombre de Equipo: Karmine Corp | Fundado en: 30/03/20 | Jugadores: 2 | Media: 46000
Nombre de Equipo: KOI | Fundado en: 15/12/21 | Jugadores: 2 | Media: 52500
Nombre de Equipo: Team Heretics | Fundado en: 24/08/16 | Jugadores: 2 | Media: 54000

Procedimiento PL/SQL terminado correctamente.

*/
    
    



