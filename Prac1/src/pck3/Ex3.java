package pck3;

import java.util.Scanner;

public class Ex3 {

	public static void main(String[] args) {
		
		int num1=0;
		int num2=0;
		int res=0;

		

		num1= (int) ((Math.random()*1000)%100);	
		num2= (int) ((Math.random()*1000)%100);

		
		System.out.println("El número generado fué: "+num1);
		System.out.println("El número generado fué: "+num2);
		System.out.println("Ingrese la suma de "+num1+" y "+num2);
		
		Scanner input= new Scanner(System.in);
		res=input.nextInt();
		
		if(res==(num1+num2)) {
			System.out.println("Si le sabes ");
		}
		else {
			System.out.println("No le sobas ");
		}
}
	
}
