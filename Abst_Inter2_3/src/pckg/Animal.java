package pckg;

public abstract class Animal {
	
	protected String nombre;
    protected int edad;
    protected double peso;
    protected Habitat habitat;
    protected Cuidador cuidador;

    public Animal(String nom, int ed, double p, Habitat hab, Cuidador cuid) {
        this.nombre = nom;
        this.edad = ed;
        this.peso = p;
        this.habitat = hab;
        this.cuidador = cuid;
    }

    public abstract String hacerSonido();

    public void mostrarInfo() {
        System.out.println("Animal: " + nombre);
        System.out.println("Edad: " + edad + " años");
        System.out.println("Peso: " + peso + " kg");
        System.out.println("Hábitat: " + habitat.gettipo() + " (" + habitat.getregion() + ")");
        System.out.println("Cuidador: " + cuidador.getnombre() + ", " + cuidador.getespecialidad());
        System.out.println("Sonido: " + hacerSonido());
    }

}
