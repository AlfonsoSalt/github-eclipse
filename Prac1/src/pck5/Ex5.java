package pck5;
import java.util.Scanner;
public class Ex5 {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		
		double r1x,r1y,r1w,r1h;
		double r2x,r2y,r2w,r2h;

        
        System.out.print("Introduzca  para el primer rectángulo centro (x) : ");
       r1x = input.nextDouble();	//1
       System.out.print("centro (y) : ");
        r1y = input.nextDouble();
        System.out.print("Ancho  : ");
        r1w = input.nextDouble();
        System.out.print("Altura : ");
        r1h = input.nextDouble();

        
        System.out.print("Introduzca  para el segundo rectángulo centro (x) : "); 
        r2x = input.nextDouble();	//2
        System.out.print("centro (y) : ");
        r2y = input.nextDouble();
        System.out.print("Ancho  : ");
        r2w = input.nextDouble();
        System.out.print("Altura : ");
        r2h = input.nextDouble();

        // Ccentros
        double xcent = Math.abs(r1x - r2x);
        double ycent = Math.abs(r1y - r2y);

        
        if (((xcent+ r2w) / 2) <= (r1w / 2) && ((ycent + r2h) / 2) <= (r1h / 2)) {
            System.out.println("r2 está dentrode r1");
        } 
        
        else if (xcent <=( (r1w + r2w) / 2) && ycent <= ((r1h + r2h) / 2)) {
            System.out.println("r2 cruza con r1");
        } 
        else {
            System.out.println("r2 y r1 no chocan");
        }
        

	}

}
