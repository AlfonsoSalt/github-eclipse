package poo;

public class Naranja extends Fruta implements Comestible 
{

	@Override
	public String formaDeComer()
	{
		return "Jugo o gajos";
	}

	@Override
	public double caloriasAportadas() 
	{
		return 60;
	}

	@Override
	public String tipoSabor()
	{
		return "Citrico";
	}

}
