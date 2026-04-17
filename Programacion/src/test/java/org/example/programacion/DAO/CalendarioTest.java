package org.example.programacion.DAO;


import org.example.programacion.Modelo.Equipos;
import org.example.programacion.Vista.MenuAdmin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CalendarioTest {
    // Instanciamos MenuAdmin para acceder a sus métodos de validación
    MenuAdmin admin = new MenuAdmin();


    // --- REGLA 1: VALIDACIÓN DE EQUIPOS (PARIDAD) ---
    @Test
    @DisplayName("Regla 1.1: Lista de equipos vacía")
    void testListaVacía() {
        List<Equipos> listaVacia = new ArrayList<>();
        // Debería devolver false porque no puedes generar un calendario con 0 equipos
        assertFalse(admin.esNumeroDeEquiposValido(listaVacia), "Una lista vacía no debe ser válida");
    }

    @Test
    @DisplayName("Regla 1.2: Paridad de equipos (Impar)")
    void testParidad() {
        List<Equipos> lista = new ArrayList<>();
        lista.add(new Equipos(1, "A", LocalDate.now()));
        lista.add(new Equipos(2, "B", LocalDate.now()));
        lista.add(new Equipos(3, "C", LocalDate.now())); // 3 es impar

        assertFalse(admin.esNumeroDeEquiposValido(lista), "Debería fallar con 3 equipos (impar)");
    }

    @Test
    @DisplayName("Regla 1.3: Paridad de equipos (Par)")
    void testParidadPar() {
        List<Equipos> lista = new ArrayList<>();
        lista.add(new Equipos(1, "A", LocalDate.now()));
        lista.add(new Equipos(2, "B", LocalDate.now()));

        assertTrue(admin.esNumeroDeEquiposValido(lista), "Debería pasar con 2 equipos (par)");
    }


    // --- REGLA 2: VALIDACIÓN DE JUGADORES (MIN 2, MAX 6) ---
    @Test
    @DisplayName("Regla 2.1: Caso 0 jugadores")
    void testCeroJugadores() {
        Equipos e0 = new Equipos(0, "E0", LocalDate.now());
        // No añadimos a nadie, falla por no llegar al mínimo de 2
        assertFalse(admin.tieneJugadoresValidos(e0), "0 jugadores no debería ser válido");
    }

    @Test
    @DisplayName("Regla 2.2: Caso 1 jugador")
    void testUnJugador() {
        Equipos e1 = new Equipos(1, "E1", LocalDate.now());
        e1.anadirJugador("P1");
        assertFalse(admin.tieneJugadoresValidos(e1), "1 jugador no debería ser válido");
    }

    @Test
    @DisplayName("Regla 2.3: Caso 2 jugadores (Mínimo)")
    void testDosJugadores() {
        Equipos e2 = new Equipos(2, "E2", LocalDate.now());
        e2.anadirJugador("P1");
        e2.anadirJugador("P2");
        assertTrue(admin.tieneJugadoresValidos(e2), "2 jugadores debería ser válido");
    }

    @Test
    @DisplayName("Regla 2.4: Caso 6 jugadores(Máximo)")
    void testSeisJugadores() {
        Equipos e6 = new Equipos(6, "E6", LocalDate.now());
        for(int i=0; i<6; i++) e6.anadirJugador("P"+i);
        assertTrue(admin.tieneJugadoresValidos(e6), "6 jugadores debería ser válido");
    }

    @Test
    @DisplayName("Regla 2.5: Caso 7 jugadores (Exceso)")
    void testSieteJugadores() {
        Equipos e7 = new Equipos(7, "E7", LocalDate.now());
        for(int i=0; i<7; i++) e7.anadirJugador("P"+i);
        assertFalse(admin.tieneJugadoresValidos(e7), "7 jugadores no debería ser válido");
    }

    @Test
    @DisplayName("Regla 2.6: Caso equipo nulo (Seguridad)")
    void testEquipoNulo() {
        assertFalse(admin.tieneJugadoresValidos(null), "Un equipo nulo no debe ser válido y no debe romper el programa");
    }
}