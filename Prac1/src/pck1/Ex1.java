package pck1;
import java.util.Scanner;
import java.lang.Math;


public class Ex1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
float a,b,c=0;

System.out.println("Para un polinomio de la forma ax^2 + bx + c = 0 : ");

System.out.println("Ingrese a: ");
Scanner input= new Scanner(System.in);//el new Scanner() es un contructor()
a=input.nextInt();

System.out.println("Ingrese b: ");
b=input.nextInt();

System.out.println("Ingrese c: ");
c=input.nextInt();

//primera raíz:
double r1,r2;

if( ((b*b)-(4*a*c))>0) {
	System.out.println("dos raíces encontradas");
}
else if( ((b*b)-(4*a*c))==0) {
	System.out.println("una raíz encontradas");
}
else if( ((b*b)-(4*a*c))<=0) {
	System.out.println("0 encontradas");
}

r1=(((-1*b)	+	Math.sqrt((b*b)-(4*a*c))	)/2*a);
r2=(((-1*b)	-	Math.sqrt((b*b)-(4*a*c))	)/2*a);

System.out.println("Raíz: "+r1);
System.out.println("Raíz: "+r2);

	}

}
