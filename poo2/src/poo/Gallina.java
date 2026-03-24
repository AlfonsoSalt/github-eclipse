package poo;

public class Gallina extends Animal implements Comestible
{
	@Override
	public String formaDeComer()
	{
		return "Cocida";
	}

	@Override
	public double caloriasAportadas()
	{
		return 200;
	}

	@Override
	public String hacerSonido() 
	{
		return "Cacareo";
	}

}
