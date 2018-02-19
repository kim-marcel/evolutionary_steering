
import java.awt.Color;
import java.awt.Graphics;

import utilities.Vector;

public abstract class Particle {

	protected Vector pos;
	protected Color colour;
	protected int size;
	
	public void draw(Graphics g){
		g.setColor(colour);
		g.fillOval((int) Math.round(pos.getX() - size / 2), (int) Math.round(pos.getY() - size / 2), size, size);
	}
	
	public Vector getPosition(){
		return pos;
	}
	
}
