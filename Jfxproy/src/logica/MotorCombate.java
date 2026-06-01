package logica;

import modelo.Territorio;
import java.util.Random;

public class MotorCombate {

    private Random random = new Random();
    private TablaEventos tablaEventos = new TablaEventos();

    public ResultadoCombate atacar(Territorio atacante, Territorio defensor) {
        int dadoAtacante = random.nextInt(6) + 1 + atacante.bonusAtaqueTotal();
        int dadoDefensor = random.nextInt(6) + 1 + defensor.bonusDefensaTotal();
        String evento = tablaEventos.eventoAleatorio();

        boolean atacanteGana = dadoAtacante > dadoDefensor;

        if (atacanteGana) {
            defensor.tropas--;
        } else {
            atacante.tropas--;
        }

        if (defensor.tropas <= 0) {
            defensor.dueno.territorios.remove(defensor);
            atacante.dueno.territorios.add(defensor);
            defensor.dueno = atacante.dueno;
            defensor.tropas = 1;
            atacante.tropas--;
        }

        return new ResultadoCombate(dadoAtacante, dadoDefensor, evento, atacanteGana);
    }
}