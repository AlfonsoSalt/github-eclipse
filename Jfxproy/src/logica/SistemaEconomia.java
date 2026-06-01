package logica;

import modelo.*;
import java.util.List;

public class SistemaEconomia {

    // Tasa de inflación global — leída por VistaMapa para display
    public static double tasaInflacion = 0.0;

    // ── Inversiones ───────────────────────────────────────────────────────
    public List<String> procesarInversionesMaduras(EstadoJuego estado) {
        return estado.jugadorActual().procesarInversiones();
    }

    // ── Ingresos ──────────────────────────────────────────────────────────
//  Ingresos      
   public String cobrarIngresos(EstadoJuego estado) {
       Jugador actual = estado.jugadorActual();
       
       // Guardamos el mensaje de posible embargo
       String mensajeEmbargo = SistemaFinanzas.cobrarInteresesSiVence(actual);
       
       int total = 0;
       for (Territorio t : actual.territorios) total += t.calcularIngreso();
       if (actual.tieneBanco()) total = (int)(total * 1.20);

       // Inflación: reduce ingresos si hay exceso de dinero en circulación
       actualizarInflacion(estado.jugadores);
       if (tasaInflacion > 0) total = (int)(total * (1.0 - tasaInflacion));
       
       actual.ultimoIngreso = total;
       actual.recibirLiquidez(total);
       
       return mensajeEmbargo; // Retorna nulo si no hubo cobranza forzada
   }

    private void actualizarInflacion(List<Jugador> jugadores) {
        int total = jugadores.stream().mapToInt(Jugador::riquezaTotal).sum();
        int base  = jugadores.size() * 5000;
        double ratio = (double) total / base;
        // Inflación inicia cuando dinero total > 1.5× base, hasta 30% máximo
        tasaInflacion = ratio <= 1.5 ? 0.0 : Math.min(0.30, (ratio - 1.5) * 0.12);
    }

    // ── Construcción ──────────────────────────────────────────────────────
    public boolean construir(Jugador jugador, Territorio territorio, TipoEdificio tipo) {
        if (!jugador.puedeGastar(tipo.costo)) return false;
        if (territorio.tieneEdificio(tipo))   return false;

        InversionReserva.TipoInversion tipoInv = mapearInversion(tipo);
        if (tipoInv != null) {
            if (!jugador.invertirEnReserva(tipoInv, tipo.costo)) return false;
        } else {
            jugador.gastar(tipo.costo);
        }
        territorio.edificios.add(new Edificio(tipo));
        return true;
    }

    private InversionReserva.TipoInversion mapearInversion(TipoEdificio tipo) {
        return switch (tipo) {
            case REACTOR_NUCLEAR          -> InversionReserva.TipoInversion.REACTOR_NUCLEAR;
            case FABRICA_SEMICONDUCTORES  -> InversionReserva.TipoInversion.FABRICA_CHIPS;
            case SILO_MISILES             -> InversionReserva.TipoInversion.SILO_MISILES;
            case BASE_DRONES              -> InversionReserva.TipoInversion.BASE_DRONES;
            default                       -> null;
        };
    }

    // TROPAAS
    public boolean comprarUnidad(Jugador jugador, Territorio territorio, TipoUnidad tipo) {
        if (!jugador.puedeGastar(tipo.costo)) return false;

        // Nuevos requisitos jerárquicos (usando || para exigir ambos)
        if (tipo == TipoUnidad.SOLDADO && !jugador.tienePlanta()) return false;
        if (tipo == TipoUnidad.TANQUE && (!jugador.tieneTanque() || !jugador.tienePlanta())) return false;
        if (tipo == TipoUnidad.ELITЕ && (!jugador.tieneChips() || !jugador.tienePlanta())) return false;

        jugador.gastar(tipo.costo);
        territorio.unidades.add(new Unidad(tipo));
        territorio.tropas++;
        return true;
    }

    // ── Reset ─────────────────────────────────────────────────────────────
    public void resetTurno(EstadoJuego estado) {
        Jugador actual = estado.jugadorActual();
        actual.apostaronEsteTurno   = false;
        actual.contratoRiesgoActivo = false;
    }
}