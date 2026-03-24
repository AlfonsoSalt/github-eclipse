package poo;

public class Vaca extends Animal implements Comestible 
{

	@Override
	public String formaDeComer() 
	{
		return "Carne";
	}

	@Override
	public double caloriasAportadas()
	{
		return 300;
	}

	@Override
	public String hacerSonido()
	{
		return "Mugido";
	}

}
