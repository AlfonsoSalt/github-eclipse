package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un territorio del mapa.
 *
 * calcularIngreso() devuelve solo el ingreso BASE del territorio.
 * El bonus de Banco Central (+20%) se aplica a NIVEL DE JUGADOR
 * en SistemaEconomia.cobrarIngresos(), no aquí.
 * Eso evita doble-conteo y hace explícito que el banco
 * es un activo del jugador, no del territorio.
 */
public class Territorio {

    // ── campos ────────────────────────────────────────────────────────────
    public String          nombre;
    public Jugador         dueno        = null;
    public int             tropas       = 0;
    public int             ingresoBase  = 100;
    public boolean         esCapital    = false;
    public int             bonusCapital = 0;
    public String          nombreCapital = "";

    public List<Territorio> adyacentes = new ArrayList<>();
    public List<Edificio>   edificios  = new ArrayList<>();
    public List<Unidad>     unidades   = new ArrayList<>();

    
    public Territorio(String nombre) {
        this.nombre = nombre;
    }


    public boolean tieneEdificio(TipoEdificio tipo) {
        return edificios.stream().anyMatch(e -> e.tipo == tipo);
    }

    // ── economía del territorio ───────────────────────────────────────────

    /**
     * Ingreso BASE del territorio por turno.
     *
     * Fuentes:
     *   - ingresoBase fijo del territorio
     *   - bonusCapital si es capital
     *   - edificios productivos presentes (Planta, Fábrica, etc.)
     *
     * NO incluye el bonus de Banco Central — eso lo aplica
     * SistemaEconomia sobre la suma total del jugador.
     */
    public int calcularIngreso() {
        if (dueno == null) return 0;

        int total = ingresoBase;

        if (esCapital) {
            total += bonusCapital;
        }

        // Bonus de edificios productivos en este territorio
        for (Edificio e : edificios) {
            switch (e.tipo) {
                case PLANTA_ENERGETICA:      total += 150; break;
                case FABRICA_SEMICONDUCTORES: total += 200; break;
                case CUARTEL:                total += 50;  break;
                case FABRICA_TANQUES:        total += 100; break;
                // Edificios de retorno diferido (Reactor, Silo, Drones, Chips)
                // NO generan ingreso directo — su retorno va via InversionReserva
                default: break;
            }
        }

        return total;
    }

    // ── combate ───────────────────────────────────────────────────────────

    /**
     * Bonus de ataque total de las unidades presentes en este territorio.
     */
    public int bonusAtaqueTotal() {
        return unidades.stream()
                .mapToInt(u -> u.tipo.bonusAtaque)
                .sum();
    }

    /**
     * Bonus de defensa total de las unidades presentes en este territorio.
     */
    public int bonusDefensaTotal() {
        return unidades.stream()
                .mapToInt(u -> u.tipo.bonusDefensa)
                .sum();
    }
}