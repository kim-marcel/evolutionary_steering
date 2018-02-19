
import java.awt.Color;

import utilities.Vector;

public class Food extends Item{
	
	public Food(Vector pos){
		super();
		nutrition = 15;
		colour = Color.GREEN;
		this.pos = pos;
		size = 5;
		eaten = false;
	}
	
}