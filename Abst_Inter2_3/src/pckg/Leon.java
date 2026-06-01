package pckg;

public class Leon extends Animal {
	
    public Leon(String nombre, int edad, double peso, Habitat habitat, Cuidador cuidador) {
        super(nombre, edad, peso, habitat, cuidador);
    }

    
    public String hacerSonido() {
        return "Rugido";
    }
}
