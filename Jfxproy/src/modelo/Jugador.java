package modelo;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**puedeGastar solo consulta liquidez.
 * La riqueza total = liquidez + reservas .*/
public class Jugador {

    // agente
    public String titulo;
    public Color  color;

    // terrenos
    public List<Territorio> territorios      = new ArrayList<>();
    public int              tropasDisponibles = 0;

  
    public boolean apostaronEsteTurno    = false;
    public boolean contratoRiesgoActivo  = false;
    public boolean esNPC                 = false;

    // DEUDA
    public int prestamoPendiente = 0;
    public int turnosPrestamo    = 0;

    // SISTEMA DE FINANZAS: liquidez y reservas etcc... Calli aqui ponemos el sistema no basico
    public int liquidez = 0;
    //bienes para venta
    public int[] Bienes = new int[3];

    /**Capital total bloqueado en inversiones.
      NO se usa directamente para gastar; es solo informativo.
      Si la maestra de finanzas pregunta aqui es lo educativo
     */
    public int reservas = 0;

    //para el hud
    public int ultimoIngreso = 0;

    //Inversiones activas, calli con list se hace más facil el manejo
    public List<InversionReserva> inversionesActivas = new ArrayList<>();

    
    public Jugador(String titulo, Color color) {
        this.titulo = titulo;
        this.color  = color;
    }

   
    public boolean puedeGastar(int cantidad) {
        if (cantidad <= 0)  return true;
        if (liquidez < 0)   return false;
        // Mantener reserva mínima operativa: no quedar en $0 de liquidez
        // si hay préstamo pendiente (para cubrir interés del próximo turno).
        if (prestamoPendiente > 0) {
            int interesSiguiente = (int)(prestamoPendiente * 0.15);
            return (liquidez - cantidad) >= interesSiguiente;
        }
        return liquidez >= cantidad;
    }

    // Gasta dinero de la liquidez. Poncho con este ilegal es para que no lo vuelvan a romper
    public void gastar(int cantidad) {
        if (!puedeGastar(cantidad))
            throw new IllegalStateException("Fondos insuficientes en liquidez.");
        liquidez -= cantidad;
    }

  
    public void recibirLiquidez(int cantidad) {
        liquidez += cantidad;
    }

    //INVERSIONES ocupa balancear

    //Crea una inversión bloqueando capital de la liquidez.
     
    public boolean invertirEnReserva(InversionReserva.TipoInversion tipo, int capital) {
        if (liquidez < capital) return false;
        liquidez  -= capital;
        reservas  += capital;
        inversionesActivas.add(new InversionReserva(tipo, capital));
        return true;
    }

    /**
     * Debe llamarse cada turno desde SistemaEconomia.
     * Madura las inversiones listas y mueve su retorno a liquidez.
     * Retorna lista de textos de log para el HUD.
     */
    public List<String> procesarInversiones() {
        List<String> logs = new ArrayList<>();
        Iterator<InversionReserva> it = inversionesActivas.iterator();
        while (it.hasNext()) {
            InversionReserva inv = it.next();
            if (inv.avanzarTurno()) {
                int retorno = inv.calcularRetorno();
                reservas -= inv.getCapital();       // libera reserva
                liquidez += retorno;                // retorno a liquidez
                logs.add(String.format("✦ %s maduró → +$%d a liquidez",
                        inv.getTipo().nombre, retorno));
                it.remove();
            }
        }
        return logs;
    }

    //edificios 
    
    public boolean tieneCuartel() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.CUARTEL));
    }
    public boolean tieneChips() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.FABRICA_SEMICONDUCTORES));
    }
    public boolean tieneBaseDrones() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.BASE_DRONES));
    }
    public boolean tieneSilo() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.SILO_MISILES));
    }
    public boolean tieneReactor() {
        return territorios.stream().anyMatch(t -> t.tieneEdificio(TipoEdificio.REACTOR_NUCLEAR));
    }

    public boolean tienePlanta() {
        return territorios.stream()
                .anyMatch(t -> t.tieneEdificio(TipoEdificio.PLANTA_ENERGETICA));
    }
    
    public boolean tieneTanque() {
        return territorios.stream()
                .anyMatch(t -> t.tieneEdificio(TipoEdificio.FABRICA_TANQUES));
    }

    public boolean tieneBanco() {
        return territorios.stream()
                .anyMatch(t -> t.tieneEdificio(TipoEdificio.BANCO_CENTRAL));
    }

    

    public boolean tieneAgencia() {
        return territorios.stream()
                .anyMatch(t -> t.tieneEdificio(TipoEdificio.AGENCIA));
    }

    public int riquezaTotal() {
        return liquidez + reservas;
    }

}