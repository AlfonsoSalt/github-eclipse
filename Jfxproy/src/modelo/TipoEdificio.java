package modelo;

public enum TipoEdificio {
    // INDUSTRIA
    PLANTA_ENERGETICA(500, "Planta Energética", "Habilita construcción militar"),
    FABRICA_SEMICONDUCTORES(1000, "Fábrica de Chips", "Habilita tecnología avanzada"),
    REACTOR_NUCLEAR(2000, "Reactor Nuclear", "Habilita armas nucleares"),

    // ECONOMÍA
    BANCO_CENTRAL(800, "Banco Central", "+20% ingresos por turno"),
    CASINO_GUERRA(1200, "Casino de Guerra", "Habilita apuestas"),
    MERCADO_ARMAS(1500, "Mercado de Armas", "Vende tropas a otros jugadores"),

    // MILITAR
    CUARTEL(300, "Cuartel", "Produce soldados"),
    FABRICA_TANQUES(700, "Fábrica de Tanques", "Produce tanques"),
    BASE_DRONES(900, "Base de Drones", "Ataques a distancia"),
    SILO_MISILES(1500, "Silo de Misiles", "Lanza misiles"),

    // INTELIGENCIA
    AGENCIA(600, "Agencia de Inteligencia", "Habilita espionaje y sabotaje");

    public final int costo;
    public final String nombre;
    public final String descripcion;

    TipoEdificio(int costo, String nombre, String descripcion) {
        this.costo = costo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}