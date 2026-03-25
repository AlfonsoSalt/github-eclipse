package Figuras;

public class Rectangle extends GeometryObject 
{
	private double width;
    private double height;

    public Rectangle() {}		// Empty constructor, but calls the empty constructor from GeometryObject

    public Rectangle(double width, double height)   // Rectangle properties
    {
        this.width = width;
        this.height = height;
    }

    public Rectangle(double width, double height, String color, boolean filled) 	// Full constructor
    {
        super(color, filled);
        this.width = width;
        this.height = height;
    }

    public double getWidth() 
    {return width;}

    public double getHeight() 
    {return height;}

    public void setWidth(double width) 
    {this.width = width;}

    public void setHeight(double height) 
    {this.height = height;}

    public double getArea() 
    {return width * height;}

    public double getPerimeter() 
    {return 2 * (width + height);}
}
