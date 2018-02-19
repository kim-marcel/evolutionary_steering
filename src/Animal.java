
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import utilities.Vector;

public class Animal extends Particle {
	
	//Health will be reduced every frame by this amount
	protected static final double AGING_RATE = 0.25;
	protected static final int SIZE = 10;
	protected static final int MAX_HEALTH = 100;
	protected static final int MAX_SPEED = 2;
	protected static final double MAX_FORCE = 0.5;

	Random r = new Random();
	Dna dna;

	protected double health;
	protected Point target;
	protected Point fleeingPoint;
	protected int maxSpeed;
	protected double maxForce;
	protected Vector currentVelocity;
	
	protected Vector acceleration = new Vector(0, 0);

	public void setTarget(Point p) {
		target.setLocation(p.x, p.y);
	}

	public void setFleeingPoint(Point p) {
		fleeingPoint.setLocation(p.x, p.y);
	}
	
	public void draw(Graphics g){
		g.setColor(colour);
		g.fillOval((int) Math.round(pos.getX() - size / 2), (int) Math.round(pos.getY() - size / 2), size, size);
	}
	
	public void eat(Item i){
		health += i.getNutrition();
		health = health > MAX_HEALTH ? MAX_HEALTH : health;
	}

}
