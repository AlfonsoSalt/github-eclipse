package pck;

public class CashRegister {
	
	private int dinero;
	
	public CashRegister() {
		dinero = 5;
	}
	//Default constructor
		//To set the cash in the register 500 cents
		//Postcondition: 
	
	public CashRegister(int dinRecibido)
	{
		if (dinRecibido >= 0)
		dinero = dinRecibido;
		else
		dinero = 500;
	}
	//Constructor with parameters
	//Postcondition: dinero = dinRecibido;
	
	public int dinActual() {
		return dinero;
	}
	//Method to show the current amount in the cash register
	//Postcondition: The value of the instance variable
	// dinero is returned
	
	public void recibirDinero(int amountIn) {
		dinero = dinero + amountIn;
	}
	//Method to receive the amount deposited by
	//the customer and update the amount in the register
	//Postcondition: 
	
}
