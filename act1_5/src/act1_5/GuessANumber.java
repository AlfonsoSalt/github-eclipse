package act1_5;
import java.util.Scanner;
//testest
public class GuessANumber {

	public static void main(String[] args) {
		
		int num1=0;
		int res=0;
		int i=0;

		
		System.out.println("Number guesser game ");
		System.out.println("Try to guess a number and find the correct number with my hints ");
		num1= (int) ((Math.random()*1000)%100);	
		System.out.println("Enter your number an press enter: ");
		
		
		while(num1!=res) {

				Scanner input= new Scanner(System.in);
				res=input.nextInt();
			
			if(res==num1) {
				System.out.println("Congratulations! the number was "+num1);
				System.out.println("You tried "+i +" times.");
			}
			else if(res<num1) {
				System.out.println("Your number is  than the answer ");
				i++;
			}
			else if(res>num1) {
				System.out.println("Your number is higher than the answer ");
				i++;
			}
		
		}
}
	
}