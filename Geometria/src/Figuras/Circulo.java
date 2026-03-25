package Figuras;

public class Circulo extends GeometryObject 	// Circle inherits everything from GeometryObject
{
    private double radius;	// Private: access just with set and get

    public Circulo() {}		// Empty constructor, but calls the empty constructor from GeometryObject

    public Circulo(double radius) // Circle assigns radius
    {this.radius = radius;}

    public Circulo(double radius, String color, boolean filled) 
    {
        super(color, filled);
        this.radius = radius;	 // Save radius
    }

    public double getRadius() 	// Access
    {return radius;}

    public void setRadius(double radius)  // Modify
    {this.radius = radius;}

    public double getArea() 
    {return Math.PI * radius * radius;}	  // Finally, mathematical calculations

    public double getPerimeter() 
    {return 2 * Math.PI * radius;}

    public double getDiameter() 
    {return 2 * radius;}

    public void printCircle() 
    {System.out.println("Fecha de creación: " + getDateCreated()+ " y su radio es " + radius);}
}
