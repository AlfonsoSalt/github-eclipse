package logica;

import java.util.Random;

public class TablaEventos {
    
    private static final String[] EVENTOS = {
        "Fallo de comunicaciones. El atacante pierde 1 tropa extra.",
        "Intercepción nuclear. Ambos pierden 2 tropas.",
        "Deserción masiva. El defensor pierde 1 tropa adicional.",
        "Terreno minado. El atacante pierde 1 tropa extra.",
        "Refuerzo de emergencia. El defensor gana 1 tropa.",
        "Traición interna. El atacante gana ventaja: relanza su dado más bajo.",
        "Tormenta electromagnética. Ambos ignoran su dado más alto.",
        "Campo de batalla tóxico. El ganador pierde 1 tropa.",
        "Sin evento. Combate limpio.",
    };

    private Random random = new Random();

    public String eventoAleatorio() {
        return EVENTOS[random.nextInt(EVENTOS.length)];
    }
}