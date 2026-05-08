package logica;

import modelo.EstadoJuego;
import modelo.Jugador;
import java.util.Random;

public class SistemaFinanzas {

    private static final Random random = new Random();

    // ══════════════════════════════════
    //  PRÉSTAMOS
    // ══════════════════════════════════
    public static String tomarPrestamo(Jugador jugador, int monto) {
        if (jugador.prestamoPendiente > 0)
            return "YA TIENES PRÉSTAMO ACTIVO — PAGA PRIMERO";
        jugador.dinero            += monto;
        jugador.prestamoPendiente  = (int)(monto * 1.5);
        jugador.turnosPrestamo     = 3;
        return "PRÉSTAMO: +$" + monto + " | Deuda: $" + jugador.prestamoPendiente + " en 3 turnos";
    }

    public static String pagarPrestamo(Jugador jugador) {
        if (jugador.prestamoPendiente <= 0)
            return "NO TIENES PRÉSTAMOS ACTIVOS";
        if (jugador.dinero < jugador.prestamoPendiente)
            return "FONDOS INSUFICIENTES — DEUDA: $" + jugador.prestamoPendiente;
        jugador.dinero            -= jugador.prestamoPendiente;
        jugador.prestamoPendiente  = 0;
        jugador.turnosPrestamo     = 0;
        return "PRÉSTAMO LIQUIDADO";
    }

    public static void cobrarInteresesSiVence(Jugador jugador) {
        if (jugador.prestamoPendiente <= 0) return;
        jugador.turnosPrestamo--;
        if (jugador.turnosPrestamo <= 0) {
            // Penalización: se cobra del dinero disponible
            int penalizacion = (int)(jugador.prestamoPendiente * 0.5);
            jugador.dinero           -= jugador.prestamoPendiente + penalizacion;
            jugador.prestamoPendiente = 0;
        }
    }

    // ══════════════════════════════════
    //  CASINO
    // ══════════════════════════════════
    public static String apuestaSimple(Jugador jugador) {
        int costo = 500;
        if (!jugador.puedeGastar(costo))     return "FONDOS INSUFICIENTES";
        if (!jugador.tieneCasino())          return "REQUIERE CASINO DE GUERRA";
        if (jugador.apostaronEsteTurno)      return "YA APOSTASTE ESTE TURNO";
        jugador.dinero -= costo;
        jugador.apostaronEsteTurno = true;
        int dado = random.nextInt(6) + 1;
        if (dado % 2 == 0) {
            jugador.dinero += costo * 2;
            return "APUESTA — DADO: " + dado + " | ✓ GANASTE $" + (costo * 2);
        }
        return "APUESTA — DADO: " + dado + " | ✗ PERDISTE $" + costo;
    }

    public static String contratoRiesgo(Jugador jugador, boolean ganoAtaque) {
        int costo = 1000;
        if (!jugador.puedeGastar(costo))     return "FONDOS INSUFICIENTES";
        if (!jugador.tieneCasino())          return "REQUIERE CASINO DE GUERRA";
        if (jugador.apostaronEsteTurno)      return "YA APOSTASTE ESTE TURNO";
        jugador.dinero -= costo;
        jugador.apostaronEsteTurno = true;
        if (ganoAtaque) {
            jugador.dinero += costo * 3;
            return "CONTRATO RIESGO — ✓ VICTORIA | +$" + (costo * 3);
        }
        int penalizacion = Math.min(500, jugador.dinero);
        jugador.dinero -= penalizacion;
        return "CONTRATO RIESGO — ✗ DERROTA | -$" + (costo + penalizacion);
    }

    public static String mercadoNegro(Jugador jugador) {
        int costo = 800;
        if (!jugador.puedeGastar(costo))     return "FONDOS INSUFICIENTES";
        if (!jugador.tieneCasino())          return "REQUIERE CASINO DE GUERRA";
        if (jugador.apostaronEsteTurno)      return "YA APOSTASTE ESTE TURNO";
        jugador.dinero -= costo;
        jugador.apostaronEsteTurno = true;
        String[] resultados = {
            "ENVÍO EXITOSO | +$2000",
            "EMBOSCADA | -$500",
            "BONANZA NEGRA | +$3000",
            "MERCANCÍA CONFISCADA | $0",
            "DOBLE TRAICIÓN | +$1500",
            "OPERACIÓN FALLIDA | -$400"
        };
        int[] efectos = {2000, -500, 3000, 0, 1500, -400};
        int idx = random.nextInt(resultados.length);
        int efecto = efectos[idx];
        if (efecto < 0) efecto = Math.max(efecto, -jugador.dinero);
        jugador.dinero += efecto;
        return "MERCADO NEGRO — " + resultados[idx];
    }

    

    public static void resetTurno(Jugador jugador) {
        jugador.apostaronEsteTurno = false;
    }
}