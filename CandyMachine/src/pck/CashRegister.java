package pck;

public class CashRegister {
	
	private int cashOnHand;
	
	public CashRegister() {
		cashOnHand = 500;
	}
	//Default constructor
		//To set the cash in the register 500 cents
		//Postcondition: 
	
	public CashRegister(int cashIn)
	{
		if (cashIn >= 0)
		cashOnHand = cashIn;
		else
		cashOnHand = 500;
	}
	//Constructor with parameters
	//Postcondition: cashOnHand = cashIn;
	
	public int currentBalance() {
		return cashOnHand;
	}
	//Method to show the current amount in the cash register
	//Postcondition: The value of the instance variable
	// cashOnHand is returned
	
	public void acceptAmount(int amountIn) {
		cashOnHand = cashOnHand + amountIn;
	}
	//Method to receive the amount deposited by
	//the customer and update the amount in the register
	//Postcondition: 
	
}
