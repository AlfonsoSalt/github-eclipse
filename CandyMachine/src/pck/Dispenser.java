package pck;

public class Dispenser {
	
	private int numberOfItems; //variable to store the number of

	//items in the dispenser
	private int cost; //variable to store the cost of an item
	
	public Dispenser() {
		numberOfItems = 50; cost = 50;
	}
	//Default constructor to set the cost and number of
	//items to the default values
	//Postcondition: 
	public Dispenser(int setNoOfItems, int setCost) {
		numberOfItems = setNoOfItems;
		cost = setCost;
	}
	//Constructor with parameters to set the cost and number
	//of items in the dispenser specified by the user
	//Postcondition: 

	public int getCount() {
		return numberOfItems ;
	}
	//Method to show the number of items in the dispenser
	//Postcondition: The value of the instance variable
	//
	public int getProductCost() {
		return cost;
	}
	//Method to show the cost of the item
	//Postcondition: The value of the instance
	// variable cost is returned
	public void makeSale() {
		numberOfItems = numberOfItems - 1;
	}
	//Method to reduce the number of items by 1
	//Postcondition: 
	
}
