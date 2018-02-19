
import java.awt.Color;

import utilities.Vector;

public class Poison extends Item{

	public Poison(Vector pos){
		super();
		nutrition = -30;
		colour = Color.RED;
		this.pos = pos;
		size = 5;
		eaten = false;
	}
	
}
