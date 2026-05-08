package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstadoJuego {

    public List<Jugador>    jugadores    = new ArrayList<>();
    public List<Territorio> territorios  = new ArrayList<>();
    public int              indiceTurno  = 0;
    public int              ultimoIngreso = 0;

    public EstadoJuego(List<ConfigJugador> configs) {
        inicializarJugadores(configs);
        inicializarMapa();
        aplicarCapitales(configs);
        distribuirTerritoriosRestantes();
    }

    private void inicializarJugadores(List<ConfigJugador> configs) {
        for (ConfigJugador c : configs) {
            jugadores.add(new Jugador(c.titulo, c.color));
        }
    }

    private void inicializarMapa() {
        Territorio Terr_TiltedTowers          = new Territorio("Tilted Towers");
        Territorio Terr_Parangaricutirimicuaro = new Territorio("Parangaricutirimicuaro");
        Territorio Terr_Viltrum               = new Territorio("Viltrum");
        Territorio Terr_NTlaxcala             = new Territorio("Nuevo Tlaxcala");
        Territorio Terr_LosRumores             = new Territorio("Los Rumores");
        Territorio Terr_Neoaxaca                  = new Territorio("Neoaxaca");
        Territorio Terr_Moroleonidas            = new Territorio("Moroleónidas");
        Territorio frenteSur                  = new Territorio("Frente Sur");
        Territorio Terr_Israel                = new Territorio("Israel");
        Territorio Terr_Iranuke            = new Territorio("Iranuke");
        Territorio Terr_LaBomba               = new Territorio("La Bomba");
        Territorio Terr_Mextitlan              = new Territorio("Mextitlán");
        Territorio Terr_Torreon              = new Territorio("Torre del Eón");

        Terr_TiltedTowers.adyacentes          = Arrays.asList(Terr_Parangaricutirimicuaro, Terr_LaBomba, Terr_Viltrum);
        Terr_Parangaricutirimicuaro.adyacentes = Arrays.asList(Terr_TiltedTowers, frenteSur, Terr_Viltrum);
        Terr_Viltrum.adyacentes               = Arrays.asList(Terr_TiltedTowers, Terr_Parangaricutirimicuaro, Terr_NTlaxcala, Terr_Neoaxaca);
        Terr_NTlaxcala.adyacentes             = Arrays.asList(Terr_Viltrum, Terr_LosRumores, Terr_LaBomba);
        Terr_LosRumores.adyacentes             = Arrays.asList(Terr_NTlaxcala, Terr_Neoaxaca, Terr_Israel, Terr_Iranuke);
        Terr_Neoaxaca.adyacentes                  = Arrays.asList(Terr_Viltrum, Terr_LosRumores, Terr_Moroleonidas, Terr_Mextitlan);
        Terr_Moroleonidas.adyacentes            = Arrays.asList(Terr_Neoaxaca, frenteSur, Terr_Mextitlan);
        frenteSur.adyacentes                  = Arrays.asList(Terr_Parangaricutirimicuaro, Terr_Moroleonidas);
        Terr_Israel.adyacentes                = Arrays.asList(Terr_LosRumores, Terr_Iranuke);
        Terr_Iranuke.adyacentes            = Arrays.asList(Terr_LosRumores, Terr_Israel, Terr_Mextitlan);
        Terr_LaBomba.adyacentes               = Arrays.asList(Terr_TiltedTowers, Terr_NTlaxcala);
        Terr_Mextitlan.adyacentes              = Arrays.asList(Terr_Neoaxaca, Terr_Moroleonidas, Terr_Iranuke);
        Terr_Torreon.adyacentes              = Arrays.asList(Terr_Mextitlan, Terr_Moroleonidas, Terr_Iranuke);

        territorios = Arrays.asList(
            Terr_TiltedTowers, Terr_Parangaricutirimicuaro, Terr_Viltrum, Terr_NTlaxcala,
            Terr_LosRumores, Terr_Neoaxaca, Terr_Moroleonidas, frenteSur,
            Terr_Israel, Terr_Iranuke, Terr_LaBomba, Terr_Mextitlan, Terr_Torreon
        );
    }

    private void aplicarCapitales(List<ConfigJugador> configs) {
        for (int i = 0; i < configs.size(); i++) {
            ConfigJugador c = configs.get(i);
            Jugador       j = jugadores.get(i);
            for (Territorio t : territorios) {
                if (t.nombre.equals(c.nombreCapital)) {
                    t.dueno       = j;
                    t.tropas      = 5;
                    t.esCapital   = true;
                    t.bonusCapital = 200;
                    j.territorios.add(t);
                    break;
                }
            }
        }
    }

    private void distribuirTerritoriosRestantes() {
        int i = 0;
        for (Territorio t : territorios) {
            if (t.dueno != null) continue;
            Jugador j = jugadores.get(i % jugadores.size());
            t.dueno   = j;
            t.tropas  = 3;
            j.territorios.add(t);
            i++;
        }
    }

    public Jugador jugadorActual() {
        return jugadores.get(indiceTurno);
    }

    public void siguienteTurno() {
        indiceTurno = (indiceTurno + 1) % jugadores.size();
    }
}