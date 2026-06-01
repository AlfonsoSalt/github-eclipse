package modelo;

/**
  Representa capital bloqueado en una inversión activa.
 
  Cuando un jugador construye un edificio de alto rendimiento
  (Reactor Nuclear, Fábrica de Chips, etc.), parte del costo
  se convierte en una InversionReserva que madura en N turnos
  y regresa a liquidez con el retorno definido.
 
  Concepto financiero: activo ilíquido vs. efectivo disponible.
 */
public class InversionReserva {

    public enum TipoInversion {
        REACTOR_NUCLEAR   ("Reactor Nuclear",    5, 0.40),
        FABRICA_CHIPS     ("Fábrica de Chips",   3, 0.15),
        SILO_MISILES      ("Silo de Misiles",    4, 0.25),
        BASE_DRONES       ("Base de Drones",     2, 0.10);

        public final String nombre;
        public final int    turnosMadurez;   // turnos hasta que regresa a liquidez
        public final double retorno;         // fracción sobre el capital invertido

        TipoInversion(String nombre, int turnos, double retorno) {
            this.nombre        = nombre;
            this.turnosMadurez = turnos;
            this.retorno       = retorno;
        }
    }

  
    private final TipoInversion tipo;
    private final int capitalInvertido;   // dinero bloqueado al crear
    private int turnosRestantes;    // conteo


    public InversionReserva(TipoInversion tipo, int capitalInvertido) {
        this.tipo             = tipo;
        this.capitalInvertido = capitalInvertido;
        this.turnosRestantes  = tipo.turnosMadurez;
    }

 

    //calli se estaba rompiendo, esto es para sincronizar combate y finanzas
    public boolean avanzarTurno() {
        if (turnosRestantes > 0) turnosRestantes--;
        return turnosRestantes == 0;
    }

    //Monto que regresa a liquidez al madurar capital original + retorno porcentual.
    public int calcularRetorno() {
        return capitalInvertido + (int)(capitalInvertido * tipo.retorno);
    }

    // getters
    public TipoInversion getTipo(){ 
    	return tipo; }
    public int getCapital()         { 
    	return capitalInvertido; }
    public int getTurnosRestantes() { 
    	return turnosRestantes; }
    public boolean hasMadurado() { 
    	return turnosRestantes == 0; }

    @Override
    public String toString() {// se bugeaba e ignoraba
        return String.format("[%s | $%d bloqueado | %d turnos restantes]",
                tipo.nombre, capitalInvertido, turnosRestantes);
    }
}