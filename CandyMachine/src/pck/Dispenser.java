package pck;

public class Dispenser {
	
	public int inventarioDisp; //variable to store the number of

	//items in the dispenser
	private int precio; //variable to store the precio of an item
	
	public Dispenser() {
		inventarioDisp = 50; precio = 50;
	}
	//Default constructor to set the precio and number of
	//items to the default values
	//Postcondition: 
	public Dispenser(int setNoOfItems, int setprecio) {
		inventarioDisp = setNoOfItems;
		precio = setprecio;
	}
	//Constructor with parameters to set the precio and number
	//of items in the dispenser specified by the user
	//Postcondition: 

	public int contarInv() {
		return inventarioDisp ;
	}
	//Method to show the number of items in the dispenser
	//Postcondition: The value of the instance variable
	//
	public int getProductprecio() {
		return precio;
	}
	//Method to show the precio of the item
	//Postcondition: The value of the instance
	// variable precio is returned
	public void makeSale() {
		inventarioDisp = inventarioDisp - 1;
	}
	//Method to reduce the number of items by 1
	//Postcondition: 
	
}
