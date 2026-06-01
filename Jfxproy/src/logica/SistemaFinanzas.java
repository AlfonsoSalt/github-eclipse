package logica;

import modelo.Jugador;
import java.util.Random;

public class SistemaFinanzas {

    private static final Random random = new Random();

    // ── Préstamos ─────────────────────────────────────────────────────────

    // FIX: prestamoPendiente almacena el TOTAL A DEVOLVER (capital + interés)
    // Sincronizado con UI: $500 → debes $750 en 3 turnos / $2000 → debes $3000 en 3 turnos
    public static String tomarPrestamo(Jugador jugador, int monto) {
        if (jugador.prestamoPendiente > 0) return "YA TIENES PRÉSTAMO ACTIVO — PAGA PRIMERO";
        int totalDevolver = (monto == 500) ? 750 : 3000;
        jugador.prestamoPendiente = totalDevolver;
        jugador.turnosPrestamo    = 3;
        jugador.recibirLiquidez(monto);
        return String.format("PRÉSTAMO +$%d | DEBES $%d EN 3 TURNOS", monto, totalDevolver);
    }

    public static String pagarPrestamo(Jugador jugador) {
        if (jugador.prestamoPendiente == 0) return "NO TIENES PRÉSTAMOS ACTIVOS";
        if (!jugador.puedeGastar(jugador.prestamoPendiente))
            return "LIQUIDEZ INSUFICIENTE — NECESITAS $" + jugador.prestamoPendiente;
        jugador.gastar(jugador.prestamoPendiente);
        jugador.prestamoPendiente = 0;
        jugador.turnosPrestamo    = 0;
        return "PRÉSTAMO LIQUIDADO";
    }

    //El banco ahora aplica EMBARGO si no pagas a tiempo
    public static String cobrarInteresesSiVence(Jugador jugador) {
        if (jugador.prestamoPendiente == 0) return null;

        jugador.turnosPrestamo--;
        if (jugador.turnosPrestamo <= 0) {
            int deuda = jugador.prestamoPendiente;
            
            // Si el jugador tiene el dinero, el banco se lo cobra automáticamente
            if (jugador.liquidez >= deuda) {
                jugador.liquidez -= deuda;
                jugador.prestamoPendiente = 0;
                jugador.turnosPrestamo = 0;
                return "EL BANCO HA COBRADO AUTOMÁTICAMENTE TU DEUDA DE $" + deuda;
            } else {
                // EMBARGO: No le alcanza. El banco toma todo y destruye un edificio.
                int liquidezEmbargada = jugador.liquidez;
                jugador.liquidez = 0;
                
                int mora = (int)(deuda * 0.25);
                jugador.prestamoPendiente = (deuda - liquidezEmbargada) + mora;
                jugador.turnosPrestamo = 3; // Reinicia el martirio
                
                boolean edificioDestruido = false;
                for (modelo.Territorio t : jugador.territorios) {
                    if (!t.edificios.isEmpty()) {
                        t.edificios.remove(0);
                        edificioDestruido = true;
                        break; // Solo destruye 1 como advertencia
                    }
                }
                
                String msg = "¡EMBARGO! EL BANCO TOMÓ TU LIQUIDEZ Y APLICÓ MORA.";
                if (edificioDestruido) msg += " EDIFICIO DESTRUIDO.";
                return msg;
            }
        }
        return null;
    }

    // ── Casino ────────────────────────────────────────────────────────────

    public static String apuestaSimple(Jugador jugador, int costo) {
        if (!jugador.puedeGastar(costo))    return "FONDOS INSUFICIENTES EN LIQUIDEZ";
        if (jugador.apostaronEsteTurno)      return "YA APOSTASTE ESTE TURNO";
        jugador.gastar(costo);
        jugador.apostaronEsteTurno = true;
        int dado = random.nextInt(6) + 1;
        if (dado >= 4) { jugador.recibirLiquidez(costo * 2); return "DADO " + dado + " | GANASTE $" + costo * 2; }
        else           { return "DADO " + dado + " | PERDISTE $" + costo; }
    }

    public static String contratoRiesgo(Jugador jugador, boolean ganoAtaque) {
        int resultado = Math.max(500, 1000 + random.nextInt(1000));
        if (ganoAtaque) {
            jugador.recibirLiquidez(resultado);
            return "CONTRATO EJECUTADO | +$" + resultado;
        } else {
            int pen = resultado / 2;
            jugador.liquidez = Math.max(0, jugador.liquidez - pen);
            return "CONTRATO PERDIDO | -$" + pen;
        }
    }

    public static String mercadoNegro(Jugador jugador) {
        int dado = random.nextInt(5);
        return switch (dado) {
            case 0 -> { jugador.recibirLiquidez(2000); yield "OP. EXITOSA | +$2000"; }
            case 1 -> { jugador.liquidez = Math.max(0, jugador.liquidez - 1000); yield "EMBOSCADA | -$1000"; }
            case 2 -> { jugador.recibirLiquidez(3000); yield "BONANZA NEGRA | +$3000"; }
            case 3 -> { jugador.liquidez = Math.max(0, jugador.liquidez - 5000); yield "AÊea¿üaq23e | -5000"; }
            default -> "MERCANCÍA CONFISCADA | $0";
        };
    }
}