package pckg;

public class Gallina extends Animal implements Comestible {
	
    public Gallina(String nombre, int edad, double peso, Habitat habitat, Cuidador cuidador) {
        super(nombre, edad, peso, habitat, cuidador);
    }

 
    public String hacerSonido() {
        return "Ccuuuucuruuuuuuucuruuuuuuu";
    }
    public String formaDeComer() {
        return "Caldo";
    }
    public int caloriasAportadas() {
        return 123;
    }
}
