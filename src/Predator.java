
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import utilities.Vector;

public class Predator extends Animal{
	
	private static final double ATTRACTION_ANT = 3.0;
	
	// Override MAX_SPEED and MAX_FORCE values so that ants have a chance to run away and survive
	private static final int MAX_SPEED = 1;
	private static final double MAX_FORCE = 0.2;
	private static final int PERCEPTION = 200;
	
	private int perception;
	
	public Predator(Vector v){
		super();
		this.pos = v;
		colour = Color.BLACK;
		health = MAX_HEALTH;
		size = SIZE;
		target = new Point(0, 0);
		
		maxSpeed = MAX_SPEED;
		maxForce = MAX_FORCE;
		perception = PERCEPTION;
		currentVelocity = new Vector((r.nextDouble() * 10) - 5, (r.nextDouble() * 10) - 5);
	}
	
	public void draw(Graphics g, boolean debug){
		g.setColor(colour);
		
		Vector v1 = new Vector(currentVelocity.getX(), currentVelocity.getY());
		v1.normalize();
		v1.mult(size);

		Point p1 = new Point((int) Math.round(v1.getX() + pos.getX()), (int) Math.round(v1.getY() + pos.getY()));

		v1.mult(-1);

		Vector v2 = new Vector(v1.getY() * -1, v1.getX());
		Vector v3 = new Vector(v1.getY(), v1.getX() * -1);

		v2.normalize();
		v3.normalize();

		v2.mult(size / 2);
		v3.mult(size / 2);

		Point p2 = new Point((int) Math.round(v2.getX() + pos.getX() + v1.getX()),
				(int) Math.round(v2.getY() + pos.getY() + v1.getY()));
		Point p3 = new Point((int) Math.round(v3.getX() + pos.getX() + v1.getX()),
				(int) Math.round(v3.getY() + pos.getY() + v1.getY()));
		
		g.fillPolygon(new int[] { p2.x, p1.x, p3.x }, new int[] { p2.y, p1.y, p3.y }, 3);
		g.drawPolygon(new int[] { p2.x, p1.x, p3.x }, new int[] { p2.y, p1.y, p3.y }, 3);
		
		if(debug){
			g.drawOval((int) Math.round(pos.getX() - perception / 2), (int) Math.round(pos.getY() - perception / 2), perception, perception);
		}
	}
	
	public void move(Point panelSize) {
		behavior();
		boundaries(panelSize);
		
		pos.set(pos.getX() + currentVelocity.getX(), pos.getY() + currentVelocity.getY());
		currentVelocity.setVector(currentVelocity.add(acceleration));

		acceleration.mult(0);
	}

	private void behavior() {
		Vector desiredVelocity = new Vector(0, 0);

		// seek
		if (!(target.x == -1 && target.y == -1)) {
			desiredVelocity.setVector(new Vector(target.getX() - pos.getX(), target.getY() - pos.getY()));
			desiredVelocity.setMag(maxSpeed);
			desiredVelocity.mult(ATTRACTION_ANT);

			Vector steer = desiredVelocity.sub(currentVelocity);
			steer.limit(maxForce);
			applyForce(steer);
		}
	}
	
	private void boundaries(Point panelSize) {
		Vector desiredVelocity = new Vector(0, 0);

		// stay in boundaries
		if (pos.getX() < 0) {
			desiredVelocity.setVector(new Vector(maxSpeed, this.currentVelocity.getY()));

			desiredVelocity.normalize();
			desiredVelocity.mult(maxSpeed);
			Vector steer = desiredVelocity.sub(currentVelocity);
			steer.limit(maxForce);
			applyForce(steer);
		} else if (pos.getX() > panelSize.x) {
			desiredVelocity.setVector(new Vector(-maxSpeed, this.currentVelocity.getY()));

			desiredVelocity.normalize();
			desiredVelocity.mult(maxSpeed);
			Vector steer = desiredVelocity.sub(currentVelocity);
			steer.limit(maxForce);
			applyForce(steer);
		}

		if (pos.getY() < 0) {
			desiredVelocity.setVector(new Vector(currentVelocity.getX(), maxSpeed));
			desiredVelocity.normalize();
			desiredVelocity.mult(maxSpeed);
			Vector steer = desiredVelocity.sub(currentVelocity);
			steer.limit(maxForce);
			applyForce(steer);
		} else if (pos.getY() > panelSize.y) {
			desiredVelocity.setVector(new Vector(currentVelocity.getX(), -maxSpeed));
			desiredVelocity.normalize();
			desiredVelocity.mult(maxSpeed);
			Vector steer = desiredVelocity.sub(currentVelocity);
			steer.limit(maxForce);
			applyForce(steer);
		}
	}

	private void applyForce(Vector v) {
		acceleration.add(v);
	}
	
	public int getPerception(){
		return perception;
	}
	
}
