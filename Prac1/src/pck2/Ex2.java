package pck2;
import java.util.Scanner;
import java.lang.Math;


public class Ex2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
float a,b,c,d,e,f=0;

System.out.println("Para un sistema de ecuaciones del tipo : ");
System.out.println("ax + by = e");
System.out.println("cx + dy = f");


System.out.println("Ingrese a: ");
Scanner input= new Scanner(System.in);
a=input.nextInt();

System.out.println("Ingrese b: ");
b=input.nextInt();

System.out.println("Ingrese e: ");
e=input.nextInt();

System.out.println("Ingrese c: ");
c=input.nextInt();

System.out.println("Ingrese d: ");
d=input.nextInt();

System.out.println("Ingrese f: ");
f=input.nextInt();


double r1,r2;

if( ((a*d)-(b*c))==0) {
	System.out.println("sin solución");
}

else {

r1=((e*d)-(b*f)/((a*d)-(b*c)));
r2=((a*f)-(e*c)/((a*d)-(b*c)));

System.out.println("X: "+r1);
System.out.println("Y: "+r2);}
	}

}
