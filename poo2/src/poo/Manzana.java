package poo;

public class Manzana extends Fruta implements Comestible
{

	@Override
	public String formaDeComer() 
	{
		return "Cruda o en Postre";
	}

	@Override
	public double caloriasAportadas() 
	{
		return 80;
	}

	@Override
	public String tipoSabor()
	{
		return "Dulce";
	}

}
