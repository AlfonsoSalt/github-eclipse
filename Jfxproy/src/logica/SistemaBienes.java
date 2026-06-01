package logica;

import modelo.Jugador;
import java.util.Random;

/**
 * Mercado de Bienes: PETRÓLEO, CHIPS, URANIO.
 * Precios fluctúan cada turno. Jugadores compran/venden especulativamente.
 * Inventario en Jugador.Bienes[].
 *
 * Concepto enseñado: oferta-demanda, timing de mercado, comprar bajo / vender alto.
 */
public class SistemaBienes {

    public enum Bien {
        PETROLEO("Petróleo", 500),
        CHIPS   ("Chips",    800),
        URANIO  ("Uranio",  1500);

        public final String nombre;
        public final int    precioBase;
        Bien(String n, int p) { nombre = n; precioBase = p; }
    }

    private static final Random rng    = new Random();
    private static final int[]  precios = {500, 800, 1500};
    private static final int[]  deltas  = {0, 0, 0};   // cambio del último turno (para indicador ▲▼)

    // Llamar una vez por turno (desde VistaMapa.aplicarRefuerzo)
    public static void fluctuar() {
        for (int i = 0; i < 3; i++) {
            Bien c = Bien.values()[i];
            int d = (rng.nextInt(7) - 3) * 50;          // -150 a +150
            precios[i] = Math.max(c.precioBase / 3,
                         Math.min(c.precioBase * 4, precios[i] + d));
            deltas[i]  = d;
        }
    }

    public static int precio(Bien c) { return precios[c.ordinal()]; }
    public static int delta (Bien c) { return deltas [c.ordinal()]; }
    public static int stock (Jugador j, Bien c) {
        if (j.Bienes == null) j.Bienes = new int[3];
        return j.Bienes[c.ordinal()];
    }

    public static String comprar(Jugador j, Bien c) {
        if (j.Bienes == null) j.Bienes = new int[3];
        int p = precios[c.ordinal()];
        if (!j.puedeGastar(p)) return "LIQUIDEZ INSUFICIENTE — NECESITAS $" + p;
        j.gastar(p);
        j.Bienes[c.ordinal()]++;
        return String.format("COMPRADO %s @ $%d | STOCK %d", c.nombre, p, j.Bienes[c.ordinal()]);
    }

    public static String vender(Jugador j, Bien c) {
        if (j.Bienes == null || j.Bienes[c.ordinal()] <= 0)
            return "NO TIENES " + c.nombre.toUpperCase();
        int p = precios[c.ordinal()];
        j.Bienes[c.ordinal()]--;
        j.recibirLiquidez(p);
        return String.format("VENDIDO %s @ $%d | +$%d A LIQUIDEZ", c.nombre, p, p);
    }
}