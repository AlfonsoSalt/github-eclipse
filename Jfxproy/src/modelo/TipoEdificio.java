package modelo;

public enum TipoEdificio {
    // INDUSTRIA
    PLANTA_ENERGETICA(600, "Planta Energética", "Habilita construcción militar"),
    FABRICA_SEMICONDUCTORES(2000, "Fábrica de Chips", "Habilita tecnología avanzada"),
    REACTOR_NUCLEAR(5000, "Reactor Nuclear", "Habilita armas nucleares"),

    // ECONOMÍA
    BANCO_CENTRAL(1000, "Banco Central", "+20% ingresos por turno"),
   

    // MILITAR
    CUARTEL(500, "Cuartel", "Produce soldados"),
    FABRICA_TANQUES(1500, "Fábrica de Tanques", "Produce tanques"),
    BASE_DRONES(1250, "Base de Drones", "Ataques a distancia"),
    SILO_MISILES(2000, "Silo de Misiles", "Lanza misiles"),

    // INTELIGENCIA
    AGENCIA(800, "Agencia de Inteligencia", "Habilita espionaje y sabotaje");

    public final int costo;
    public final String nombre;
    public final String descripcion;

    TipoEdificio(int costo, String nombre, String descripcion) {
        this.costo = costo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}