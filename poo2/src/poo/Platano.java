package poo;

public class Platano extends Fruta implements Comestible 
{

	@Override
	public String formaDeComer() 
	{
		return "Directo";
	}

	@Override
	public double caloriasAportadas() 
	{
		return 100;
	}

	@Override
	public String tipoSabor()
	{
		return "Dulce";
	}

}
