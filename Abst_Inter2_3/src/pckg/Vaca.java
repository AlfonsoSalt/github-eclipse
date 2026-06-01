package pckg;

public class Vaca extends Animal implements Comestible {
	
    public Vaca(String nombre, int edad, double peso, Habitat habitat, Cuidador cuidador) {
        super(nombre, edad, peso, habitat, cuidador);
    }

    
    public String hacerSonido() {
        return "Muuu";
    }
    public String formaDeComer() {
        return "Asada";
    }
    public int caloriasAportadas() {
        return 500;
    }
}
