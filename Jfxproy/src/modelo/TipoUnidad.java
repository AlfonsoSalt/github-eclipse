package modelo;

public enum TipoUnidad {
    SOLDADO(100,  "Soldado",  1, 1, "Unidad básica"),
    TANQUE(300,   "Tanque",   3, 1, "Alto ataque, baja defensa"),
    ELITЕ(500,    "Élite",    2, 3, "Alto ataque y defensa");

    public final int costo;
    public final String nombre;
    public final int bonusAtaque;
    public final int bonusDefensa;
    public final String descripcion;

    TipoUnidad(int costo, String nombre, int bonusAtaque, int bonusDefensa, String descripcion) {
        this.costo = costo;
        this.nombre = nombre;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefensa = bonusDefensa;
        this.descripcion = descripcion;
    }
}