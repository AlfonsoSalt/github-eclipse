package poo;

public class Main
{

	public static void main(String[] args) 
	{
			Gallina g = new Gallina();
			g.nombre = "turuleca";
			g.edad = 6;
			g.peso = 25;
			System.out.println("El sonido que hace es " + g.hacerSonido());
			System.out.println("La forma de comer es " + g.formaDeComer());
			System.out.println("Nombre: " + g.nombre);
			System.out.println("Edad: " + g.edad + " años");
			System.out.println("Peso: " + g.peso + " kg");
			
			System.out.println("--------------------");
			
			Leon l = new Leon();
			l.nombre = "Nala";
			l.edad = 11;
			l.peso = 200;
			System.out.println("El sonido que hace es " + l.hacerSonido());
			System.out.println("Nombre: " + l.nombre);
			System.out.println("Edad: " + l.edad + " años");
			System.out.println("Peso: " + l.peso + " kg");
			
			System.out.println("--------------------");
			
			Manzana m = new Manzana();
			InformacionNutricional infom = new InformacionNutricional();
			infom.calorias = 52;
			infom.azucar = 10;
			infom.fibra = 2.5;
			infom.proteinas = 14;
			m.infoNutricional = infom;
			
			System.out.println("El tipo de sabor es: " + m.tipoSabor());
			System.out.println("La forma de comer es: " + m.formaDeComer());
			System.out.println("Calorias: " + m.infoNutricional.calorias);
			System.out.println("Azúcares: " + m.infoNutricional.azucar);
			System.out.println("Fibras: " + m.infoNutricional.fibra);
			System.out.println("Proteinas: " + m.infoNutricional.proteinas + " gr");
	}

}
