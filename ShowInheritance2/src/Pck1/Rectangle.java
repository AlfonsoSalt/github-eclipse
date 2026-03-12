package Pck1;
import java.lang.String;

public class Rectangle {
	
	double width=0.0;
	double height=0.0;
	boolean fill = false;
	String color="White";
	
	Rectangle(){
		width=10;
		height=20;
	}
	
	Rectangle(double w, double h){
		width=w;
		height=h;
	}
	
	Rectangle(double w, double h, String c){
		width=w;
		height=h;
		color= c;
	}
	
	void setColor(String c) {
		color=c;
	}
	
	void setFill(boolean f) {
		fill=f;
	}
	
	double getWidth() {
		return width;
	}
	
	void setWidth(double w) {
		width=w;
	}
	
	double getHeight() {
		return width;
	}
	
	void setHeight(double h) {
		height=h;	
	}
	
	double getArea() {
		return width*height;
	}
	
	double getPerimeter() {
		return (width*2)+(height*2);
	}

}
