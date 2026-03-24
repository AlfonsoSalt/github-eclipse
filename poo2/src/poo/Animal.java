package poo;

public abstract class Animal 
{
	String nombre;
	int edad;
	double peso;
	
	public abstract String hacerSonido();
	//Method without body
	
	public void mostratInfo()
	{
		System.out.println(nombre + " - " + edad + " años");
	}
}
