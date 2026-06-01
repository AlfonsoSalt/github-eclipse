package modelo;

public enum TipoUnidad {
    SOLDADO(200,  "Soldado",  0, 0, "Unidad básica"),
    TANQUE(800,   "Tanque",   2, 1, "Alto ataque, baja defensa"),
    ELITЕ(1500,    "ELITE",    3, 3, "Alto ataque y defensa");

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