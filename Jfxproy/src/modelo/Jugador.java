package modelo;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Jugador {
    public String titulo;
    public Color color;
    public List<Territorio> territorios = new ArrayList<>();
    public int tropasDisponibles = 0;
    public int dinero = 1000; // Capital inicial
    
    //finanzas
    public int     prestamoPendiente   = 0;
    public int     turnosPrestamo      = 0;
    public boolean apostaronEsteTurno  = false;
    public boolean contratoRiesgoActivo = false;
    
    public boolean puedeGastar(int cantidad) {
        return dinero >= cantidad;
    }

    // Desbloqueos
    public boolean tienePlanta() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.PLANTA_ENERGETICA));
    }
    public boolean tieneBanco() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.BANCO_CENTRAL));
    }
    public boolean tieneCasino() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.CASINO_GUERRA));
    }
    public boolean tieneAgencia() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.AGENCIA));
    }

    public Jugador(String titulo, Color color) {
        this.titulo = titulo;
        this.color = color;
    }
}