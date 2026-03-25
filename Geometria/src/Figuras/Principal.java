package Figuras;

public class Principal 
{
	public static void main(String[] args) 
	{
        Circulo miCirculo = new Circulo(8.8, "Naranja", true);
             
        Rectangle miRectangulo = new Rectangle();
        miRectangulo.setWidth(5);
        miRectangulo.setHeight(3);
        miRectangulo.setColor("Azul");
        miRectangulo.setFilled(false);
  
        System.out.println("CÍRCULO");
        miCirculo.printCircle(); 
        System.out.println("Área: " + miCirculo.getArea());
        System.out.println("Diámetro: " + miCirculo.getDiameter());
     
        System.out.println("¿Está lleno?: " + miCirculo.isFilled());         
        System.out.println("\n");
  
        System.out.println("RECTÁNGULO");
        System.out.println("Color: " + miRectangulo.getColor());
        System.out.println("Área: " + miRectangulo.getArea());
        System.out.println("Perímetro: " + miRectangulo.getPerimeter());
       
        System.out.println("Info General: " + miRectangulo.toString());
        System.out.println("\n");

        GeometryObject figuraGenérica = new Circulo(4.0, "Rojo", false);
        System.out.println("Círculo polimórfico: " + figuraGenérica.getColor());
	}
}
