package logica;

public class ResultadoCombate {
    public int dadoAtacante;
    public int dadoDefensor;
    public String evento;
    public boolean atacanteGano;

    public ResultadoCombate(int dadoAtacante, int dadoDefensor, String evento, boolean atacanteGano) {
        this.dadoAtacante = dadoAtacante;
        this.dadoDefensor = dadoDefensor;
        this.evento = evento;
        this.atacanteGano = atacanteGano;
    }
}