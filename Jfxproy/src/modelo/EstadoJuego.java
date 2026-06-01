package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstadoJuego {

    public List<Jugador>    jugadores    = new ArrayList<>();
    public List<Territorio> territorios  = new ArrayList<>();
    public int              indiceTurno  = 0;
    public int              ultimoIngreso = 0;
    //npcs
    
    private static final String[] NPC_NOMBRES = {"El Mossad", "Los Masones", "BlackRock"};
    private static final javafx.scene.paint.Color[] NPC_COLORES = {
        javafx.scene.paint.Color.web("#170a0b"),   // rojo oscuro
        javafx.scene.paint.Color.web("#1c1626"),   // morado
        javafx.scene.paint.Color.web("#0a0b17")    // gris carbón
    };

    public EstadoJuego(List<ConfigJugador> configs) {
        inicializarJugadores(configs);
        inicializarMapa();
        aplicarCapitales(configs);
        distribuirTerritoriosRestantes();
        
    }
    
    public boolean esHumano() {
        return !jugadorActual().esNPC;
    }

    private void inicializarJugadores(List<ConfigJugador> configs) {
        for (ConfigJugador c : configs) {
            jugadores.add(new Jugador(c.titulo, c.color));
        }
    }

  
    
    private void inicializarMapa() {
        // --- LOS 4 ORIGINALES CONSERVADOS ---
        Territorio cloacan        = new Territorio("Cloacan");
        Territorio israel         = new Territorio("Israel");
        Territorio iranuke        = new Territorio("Iranuke");
        Territorio laBomba        = new Territorio("La Bomba");

        // --- LOS 39 NUEVOS ---
        Territorio tiltedTower    = new Territorio("Tilted Tower");
        Territorio newVegas       = new Territorio("New Vegas");
        Territorio elOso          = new Territorio("El Oso");
        Territorio elCrudo        = new Territorio("El Crudo");
        Territorio elMaple        = new Territorio("El Maple");
        Territorio agroenlandia   = new Territorio("Agroenlandia");
        Territorio elCapo         = new Territorio("El Capo");
        Territorio amazon         = new Territorio("Amazon°");
        Territorio terraseca      = new Territorio("Terraseca");
        Territorio laMarina       = new Territorio("La Marina");
        Territorio patagonia      = new Territorio("Patagonia");
        Territorio nuevoSol       = new Territorio("Nuevo Sol");
        Territorio reinoSeparado  = new Territorio("Reino Separado");
        Territorio ibailagos      = new Territorio("Ibailagos");
        Territorio hausefgaben    = new Territorio("Hausefgaben");
        Territorio nuevaRoma      = new Territorio("Nueva Roma");
        Territorio droenladia     = new Territorio("Droenladia");
        Territorio ivory          = new Territorio("Ivory");
        Territorio savahna        = new Territorio("Savahna");
        Territorio warlord        = new Territorio("Warlord");
        Territorio marSangre      = new Territorio("Mar Sangre");
        Territorio julien         = new Territorio("Julien");
        Territorio kampf          = new Territorio("Kampf");
        Territorio vodkistahn     = new Territorio("Vodkistahn");
        Territorio laPlaga        = new Territorio("La Plaga");
        Territorio leiribo        = new Territorio("Leiribo");
        Territorio nehongKong     = new Territorio("Nehong Kong");
        Territorio granAstro      = new Territorio("Gran Astro");
        Territorio xingXing       = new Territorio("XingXing");
        Territorio dosaka         = new Territorio("Dosaka");
        Territorio laOz           = new Territorio("La Oz");
        Territorio neolatam       = new Territorio("Neolatam");
        Territorio buenasia       = new Territorio("Buenasia");
        Territorio neoZelanda     = new Territorio("Neo Zelanda");
        Territorio losPolinesios  = new Territorio("Los Polinesios");
        Territorio marHostilsico  = new Territorio("Mar hostilsico");
        Territorio bigStJames     = new Territorio("Big St James");
        Territorio elOrigen       = new Territorio("El Origen");
        Territorio bajoTerra      = new Territorio("BajoTerra");

        // --- CONEXIONES RECONSTRUIDAS ---
        cloacan.adyacentes        = Arrays.asList(elCrudo, elCapo);
        israel.adyacentes         = Arrays.asList(nuevaRoma, julien, iranuke, laBomba);
        iranuke.adyacentes        = Arrays.asList(israel, laBomba, leiribo, vodkistahn);
        laBomba.adyacentes        = Arrays.asList(ivory, julien, israel, warlord, iranuke);
        
        tiltedTower.adyacentes    = Arrays.asList(elCrudo, newVegas, elMaple, reinoSeparado);
        newVegas.adyacentes       = Arrays.asList(elOso, elCrudo, tiltedTower, elMaple);
        elOso.adyacentes          = Arrays.asList(newVegas, bigStJames, laOz); // Cruce Pacífico
        elCrudo.adyacentes        = Arrays.asList(cloacan, newVegas, tiltedTower, elCapo);
        elMaple.adyacentes        = Arrays.asList(newVegas, tiltedTower, agroenlandia);
        agroenlandia.adyacentes   = Arrays.asList(elMaple, reinoSeparado);
        
        elCapo.adyacentes         = Arrays.asList(cloacan, elCrudo, amazon, terraseca);
        amazon.adyacentes         = Arrays.asList(elCapo, laMarina, ivory); // Cruce Atlántico Sur
        terraseca.adyacentes      = Arrays.asList(elCapo, laMarina, patagonia);
        laMarina.adyacentes       = Arrays.asList(amazon, terraseca, patagonia, nuevoSol);
        patagonia.adyacentes      = Arrays.asList(terraseca, laMarina, nuevoSol, elOrigen);
        nuevoSol.adyacentes       = Arrays.asList(laMarina, patagonia, kampf); // Cruce Sudamérica - África
        
        reinoSeparado.adyacentes  = Arrays.asList(tiltedTower, agroenlandia, hausefgaben, ibailagos);
        ibailagos.adyacentes      = Arrays.asList(reinoSeparado, hausefgaben, ivory, nuevaRoma);
        hausefgaben.adyacentes    = Arrays.asList(reinoSeparado, ibailagos, nuevaRoma, droenladia);
        nuevaRoma.adyacentes      = Arrays.asList(hausefgaben, droenladia, vodkistahn, julien, israel, ibailagos);
        droenladia.adyacentes     = Arrays.asList(hausefgaben, nuevaRoma, vodkistahn);
        
        ivory.adyacentes          = Arrays.asList(amazon, ibailagos, savahna, laBomba);
        savahna.adyacentes        = Arrays.asList(ivory, marSangre, warlord);
        warlord.adyacentes        = Arrays.asList(savahna, marSangre, julien, laBomba);
        marSangre.adyacentes      = Arrays.asList(savahna, warlord, kampf);
        julien.adyacentes         = Arrays.asList(nuevaRoma, israel, warlord, laBomba);
        kampf.adyacentes          = Arrays.asList(marSangre, nuevoSol, bajoTerra);
        
        vodkistahn.adyacentes     = Arrays.asList(nuevaRoma, droenladia, laPlaga, iranuke);
        laPlaga.adyacentes        = Arrays.asList(vodkistahn, leiribo, xingXing);
        leiribo.adyacentes        = Arrays.asList(iranuke, laPlaga, nehongKong);
        nehongKong.adyacentes     = Arrays.asList(leiribo, granAstro, buenasia);
        granAstro.adyacentes      = Arrays.asList(nehongKong, xingXing, dosaka);
        xingXing.adyacentes       = Arrays.asList(laPlaga, laOz, dosaka, granAstro);
        dosaka.adyacentes         = Arrays.asList(granAstro, xingXing, laOz, neolatam);
        laOz.adyacentes           = Arrays.asList(xingXing, dosaka, elOso); // Cierra Pacífico Norte
        neolatam.adyacentes       = Arrays.asList(dosaka, buenasia, marHostilsico);
        buenasia.adyacentes       = Arrays.asList(nehongKong, neolatam, neoZelanda);
        
        neoZelanda.adyacentes     = Arrays.asList(buenasia, losPolinesios, bajoTerra);
        losPolinesios.adyacentes  = Arrays.asList(neoZelanda, marHostilsico, bigStJames);
        marHostilsico.adyacentes  = Arrays.asList(neolatam, losPolinesios, bigStJames);
        bigStJames.adyacentes     = Arrays.asList(elOso, marHostilsico, losPolinesios); // Cierra Pacífico Sur
        elOrigen.adyacentes       = Arrays.asList(patagonia, bajoTerra);
        bajoTerra.adyacentes      = Arrays.asList(elOrigen, kampf, neoZelanda);

        territorios = Arrays.asList(
            cloacan, israel, iranuke, laBomba,
            tiltedTower, newVegas, elOso, elCrudo, elMaple, agroenlandia,
            elCapo, amazon, terraseca, laMarina, patagonia, nuevoSol,
            reinoSeparado, ibailagos, hausefgaben, nuevaRoma, droenladia,
            ivory, savahna, warlord, marSangre, julien, kampf,
            vodkistahn, laPlaga, leiribo, nehongKong, granAstro, xingXing, dosaka, laOz, neolatam, buenasia,
            neoZelanda, losPolinesios, marHostilsico, bigStJames, elOrigen, bajoTerra
        );
    }

    private void aplicarCapitales(List<ConfigJugador> configs) {
        for (int i = 0; i < configs.size(); i++) {
            ConfigJugador c = configs.get(i);
            Jugador       j = jugadores.get(i);
            for (Territorio t : territorios) {
                if (t.nombre.equals(c.nombreCapital)) {
                    t.dueno      = j;
                    t.tropas     = 4;
                    t.esCapital  = true;
                    t.bonusCapital = 200;
                    j.territorios.add(t);

                    // Bonus por agente
                    if (c.titulo.contains("Caudillo"))   t.tropas += 3;
                    if (c.titulo.contains("Fentnyahu"))  t.bonusCapital += 400;
                    if (c.titulo.contains("Агент")) {      t.edificios.add(new Edificio(TipoEdificio.AGENCIA)); t.edificios.add(new Edificio(TipoEdificio.CUARTEL));}
                    if (c.titulo.contains("Sierra7"))    j.liquidez += 1000;
                    break;
                }
            }
        }
    }
    
    public List<Jugador> npcs = new ArrayList<>();
    
    private void inicializarNPCs() {
        for (int i = 0; i < NPC_NOMBRES.length; i++) {
            Jugador npc    = new Jugador(NPC_NOMBRES[i], NPC_COLORES[i]);
            npc.esNPC      = true;
            npcs.add(npc);
        }
    }

    private void distribuirTerritoriosRestantes() {
        inicializarNPCs();
        List<Territorio> libres = new ArrayList<>();
        for (Territorio t : territorios) {
            if (t.dueno == null) libres.add(t);
        }
        java.util.Collections.shuffle(libres);
        for (int i = 0; i < libres.size(); i++) {
            Jugador npc    = npcs.get(i % npcs.size());
            Territorio t   = libres.get(i);
            t.dueno        = npc;
            t.tropas       = 2 + (int)(Math.random() * 3);
            npc.territorios.add(t);
        }
    }

    public Jugador jugadorActual() {
        return jugadores.get(indiceTurno);
    }

    public void siguienteTurno() {
        indiceTurno = (indiceTurno + 1) % jugadores.size();
    }
}