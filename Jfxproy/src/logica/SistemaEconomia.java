package logica;

import modelo.EstadoJuego;
import modelo.Jugador;
import modelo.Territorio;
import modelo.TipoUnidad;

public class SistemaEconomia {

	public static void cobrarIngresos(EstadoJuego estado) {
	    Jugador actual = estado.jugadorActual();
	    SistemaFinanzas.cobrarInteresesSiVence(actual);
	    SistemaFinanzas.resetTurno(actual);
	    int total = 0;
	    for (Territorio t : actual.territorios) {
	        total += t.calcularIngreso();
	    }
	    actual.dinero      += total;
	    estado.ultimoIngreso = total;
	}

	public static boolean construir(Jugador jugador, Territorio territorio, modelo.TipoEdificio tipo) {
	    if (!jugador.puedeGastar(tipo.costo)) return false;
	    if (territorio.tieneEdificio(tipo))   return false;
	    jugador.dinero -= tipo.costo;
	    territorio.edificios.add(new modelo.Edificio(tipo));
	    return true;
	}

	public static boolean comprarUnidad(Jugador jugador, Territorio territorio, TipoUnidad tipo) {
	    if (!jugador.puedeGastar(tipo.costo)) return false;
	    if (!jugador.tienePlanta())           return false;
	    jugador.dinero -= tipo.costo;
	    territorio.unidades.add(new modelo.Unidad(tipo));
	    territorio.tropas++;
	    return true;
	}
}