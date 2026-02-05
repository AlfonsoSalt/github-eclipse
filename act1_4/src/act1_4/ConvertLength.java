package act1_4;


import java.util.Scanner;

public class ConvertLength {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int feet=0;
		int inches=0;
		int totalinches=0;
		
		double centimeters=0;
		
		final double CENTIMETERS_PER_INCH = 2.54;
		final int INCHES_PER_FOOT =12;
		
		System.out.println("Calculadora de pies a centimetros, ingrese pies y pulgadas ");
		
		System.out.println("Ingrese Pies: ");
		Scanner input= new Scanner(System.in);
		feet=input.nextInt();
		
		System.out.println("Ingrese Pulgadas: ");
		input= new Scanner(System.in);
		inches=input.nextInt();
		
		System.out.println("Datos recibidos: "+ feet +" pies y "+inches+" pulgadas ");
		
		totalinches= inches+ (feet*INCHES_PER_FOOT);
		
		centimeters= totalinches*CENTIMETERS_PER_INCH;
		
		System.out.println("Resultado: "+centimeters);
	}

}
