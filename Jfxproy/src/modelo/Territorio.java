package modelo;

import java.util.ArrayList;
import java.util.List;

public class Territorio {
    public String          nombre;
    public Jugador         dueno;
    public int             tropas;
    public int             ingresoBase   = 100;
    public boolean         esCapital     = false;
    public int             bonusCapital  = 0;
    public List<Territorio> adyacentes   = new ArrayList<>();
    public List<Edificio>   edificios    = new ArrayList<>();
    public List<Unidad>     unidades     = new ArrayList<>();

    public Territorio(String nombre) {
        this.nombre = nombre;
    }

    public boolean tieneEdificio(TipoEdificio tipo) {
        return edificios.stream().anyMatch(e -> e.tipo == tipo);
    }

    public int calcularIngreso() {
        int total = ingresoBase + bonusCapital;
        if (tieneEdificio(TipoEdificio.BANCO_CENTRAL)) total = (int)(total * 1.2);
        return total;
    }

    public int bonusAtaqueTotal() {
        return unidades.stream().mapToInt(u -> u.tipo.bonusAtaque).sum();
    }

    public int bonusDefensaTotal() {
        return unidades.stream().mapToInt(u -> u.tipo.bonusDefensa).sum();
    }
}//f