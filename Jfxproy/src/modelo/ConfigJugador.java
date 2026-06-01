package modelo;
import javafx.scene.paint.Color;

public class ConfigJugador {
    public String titulo;
    public Color  color;
    public String nombreCapital;

    public ConfigJugador(String titulo, Color color, String nombreCapital) {
        this.titulo        = titulo;
        this.color         = color;
        this.nombreCapital = nombreCapital;
    }
}