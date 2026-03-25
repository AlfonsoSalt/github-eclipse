package Figuras;
import java.util.Date;

public class GeometryObject 
{
	private String color = "Red";
    private boolean filled;				//true or false
    private Date dateCreated;

    public GeometryObject() 			// Empty constructor 
    {dateCreated = new Date();}

    public GeometryObject(String color, boolean filled)   // Constructor with parameters
    {
        this.color = color;
        this.filled = filled;
        this.dateCreated = new Date();
    }

    public String getColor() 			// get: Access its value
    {return color;}

    public void setColor(String color) // set: Modify its value
    {this.color = color;}

    public boolean isFilled() 
    {return filled;}

    public void setFilled(boolean filled) 
    {this.filled = filled;}

    public Date getDateCreated() 
    {return dateCreated;}
    
    @Override
    public String toString() 
    {return "Fecha de creación: " + dateCreated + "\ncolor: " + color + "\nRelleno: " + filled;}
}