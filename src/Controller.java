
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import utilities.Vector;

public class Controller {

	private static final int FOOD_AMOUNT = 220;
	private static final int POISON_AMOUNT = 50;
	private static final int ANTS_AMOUNT = 30;
	private static final int PREDATOR_AMOUNT = 2;
	
	private boolean debug = false;

	Random r = new Random();

	ArrayList<Food> food;
	ArrayList<Poison> poison;
	ArrayList<Ant> ants;
	ArrayList<Predator> predator;

	Point panelSize;

	public Controller(Point size) {

		panelSize = new Point(size.x - 10, size.y - 30);

		food = new ArrayList<Food>();
		poison = new ArrayList<Poison>();
		ants = new ArrayList<Ant>();
		predator = new ArrayList<Predator>();

		for (int j = 0; j < FOOD_AMOUNT; j++) {
			food.add(new Food(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}

		for (int i = 0; i < POISON_AMOUNT; i++) {
			poison.add(new Poison(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}

		for (int i = 0; i < ANTS_AMOUNT; i++) {
			ants.add(new Ant(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}

		for (int i = 0; i < PREDATOR_AMOUNT; i++) {
			predator.add(new Predator(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}

	}

	public void drawAll(Graphics g) {
		for (int i = 0; i < food.size(); i++) {
			food.get(i).draw(g);
		}

		for (int i = 0; i < poison.size(); i++) {
			poison.get(i).draw(g);
		}

		for (int i = 0; i < ants.size(); i++) {
			ants.get(i).draw(g, debug);
		}

		for (int i = 0; i < predator.size(); i++) {
			predator.get(i).draw(g, debug);
		}
	}

	private void setTarget() {
		double distance = -1;
		Food foo = null;
		Ant ant = null;
		Poison pois = null;

		//Set Food as Target for Ants
		for (int i = 0; i < ants.size(); i++) {
			// Food
			for (int j = 0; j < food.size(); j++) {
				double d = getDistance(food.get(j), ants.get(i));
				if ((distance == -1 && d <= ants.get(i).dna.getPerceptionFood() / 2) || (d < distance && d <= ants.get(i).dna.getPerceptionFood() / 2)) {
					distance = d;
					foo = food.get(j);
				}
			}
			if (foo == null) {
				ants.get(i).setTarget(new Point(-1, -1));
			} else {
				ants.get(i).setTarget(new Point((int) Math.round(foo.getPosition().getX()),
						(int) Math.round(foo.getPosition().getY())));
			}
			foo = null;
			distance = -1;
		}
		
		// Set ants or poison as targets for predators
		boolean antFound = true;
		for (int i = 0; i < predator.size(); i++) {
			// Ants
			for (int j = 0; j < ants.size(); j++) {
				double d = getDistance(ants.get(j), predator.get(i));
				if ((distance == -1 && d <= predator.get(i).getPerception() / 2) || (d < distance && d <= predator.get(i).getPerception() / 2)) {
					distance = d;
					ant = ants.get(j);
				}
			}
			if (ant == null) {
				// If no ant found in perception look for poison --> ants have higher priority than poison
				distance = -1;
				for (int j = 0; j < poison.size(); j++) {
					double d = getDistance(poison.get(j), predator.get(i));
					if ((distance == -1 && d <= predator.get(i).getPerception() / 2) || (d < distance && d <= predator.get(i).getPerception() / 2)) {
						distance = d;
						pois = poison.get(j);
					}
				}
				if (pois == null) {
					predator.get(i).setTarget(new Point(-1, -1));
				} else {
					predator.get(i).setTarget(new Point((int) Math.round(pois.getPosition().getX()),
							(int) Math.round(pois.getPosition().getY())));
				}
				pois = null;
				distance = -1;
			} else {
				predator.get(i).setTarget(new Point((int) Math.round(ant.getPosition().getX()),
						(int) Math.round(ant.getPosition().getY())));
			}
			ant = null;
			distance = -1;
		}
	}

	private void setFleeingPoint() {
		double distance = -1;
		Poison pois = null;

		for (int i = 0; i < ants.size(); i++) {
			// Poison
			for (int j = 0; j < poison.size(); j++) {
				double d = getDistance(poison.get(j), ants.get(i));
				if ((distance == -1 && d <= ants.get(i).dna.getPerceptionPoison() / 2) || (d < distance && d <= ants.get(i).dna.getPerceptionPoison() / 2)) {
					distance = d;
					pois = poison.get(j);
				}
			}
			if (pois == null) {
				ants.get(i).setFleeingPoint(new Point(-1, -1));
			} else {
				ants.get(i).setFleeingPoint(new Point((int) Math.round(pois.getPosition().getX()),
						(int) Math.round(pois.getPosition().getY())));
			}
			pois = null;
			distance = -1;
		}
	}

	public void computeNextFrame(Point panelSize) {
		setTarget();
		setFleeingPoint();
		findHealthiest();
		moveAnimals();
		eat();
		addFood(panelSize);
		addPoison(panelSize);
	}
	
	private void moveAnimals(){
		//move ants
		for (int i = 0; i < ants.size(); i++) {
			reproduce(ants.get(i));
			ants.get(i).move(panelSize);
			ants.get(i).reduceHealth();
		}

		// move predators
		for (int i = 0; i < predator.size(); i++) {
			predator.get(i).move(panelSize);
		}
	}
	
	private void reproduce(Ant ant){
		if (r.nextDouble() <= ant.getReproductionProbability()) {
			ants.add(new Ant(new Vector(ant.getPosition().getX(), ant.getPosition().getY()), ant.dna));
		}
	}

	private void eat() {
		// Ants eating food
		for (int i = 0; i < ants.size(); i++) {
			for (int j = 0; j < food.size(); j++) {
				if (ants.get(i).getPosition().distance(food.get(j).getPosition()) < 7) {
					ants.get(i).eat(food.get(j));
					food.remove(j);
				}
			}
		}

		// Ants eating poison
		for (int i = 0; i < ants.size(); i++) {
			for (int j = 0; j < poison.size(); j++) {
				if (ants.get(i).getPosition().distance(poison.get(j).getPosition()) < 7) {
					ants.get(i).eat(poison.get(j));
					poison.remove(j);
				}
			}
		}
		
		// Predators eating ants
		for (int i = 0; i < predator.size(); i++) {
			for (int j = 0; j < ants.size(); j++) {
				if (predator.get(i).getPosition().distance(ants.get(j).getPosition()) < 7) {
					ants.remove(j);
				}
			}
		}
		
		// Predators eating poison
		for (int i = 0; i < predator.size(); i++) {
			for (int j = 0; j < poison.size(); j++) {
				if (predator.get(i).getPosition().distance(poison.get(j).getPosition()) < 7) {
					predator.get(i).eat(poison.get(j));
					poison.remove(j);
				}
			}
		}

		checkHealth();
	}

	private void checkHealth() {
		for (int i = 0; i < ants.size(); i++) {
			if (!ants.get(i).isAlive()) {
				// ant is dead
				Vector v = new Vector(ants.get(i).getPosition().getX(), ants.get(i).getPosition().getY());
				ants.remove(i);
				food.add(new Food(v));
			}
		}
	}

	private void findHealthiest() {
		if (ants.size() >= 1) {
			for (int i = 0; i < ants.size(); i++) {
				ants.get(i).setHealthiest(false);
			}
			ants.get(0).setHealthiest(true);
			double healthiestHealth = ants.get(0).getHealth();
			int healthiestAntNo = 0;
			for (int i = 0; i < ants.size(); i++) {
				if (ants.get(i).getHealth() > healthiestHealth) {
					ants.get(healthiestAntNo).setHealthiest(false);
					healthiestAntNo = i;
					ants.get(i).setHealthiest(true);
					healthiestHealth = ants.get(i).getHealth();
				}
			}
		}
	}

	private void addFood(Point size) {
		if (r.nextInt(100) <= 6) { // 6 per cent chance of adding new food in this frame
			food.add(new Food(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}
	}

	private void addPoison(Point size) {
		if (r.nextInt(100) <= 2) { // 2 per cent chance of adding new food in this frame
			poison.add(new Poison(new Vector(r.nextInt(panelSize.x), r.nextInt(panelSize.y))));
		}
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void addAnt(Point p){
		ants.add(new Ant(new Vector(r.nextInt(p.x), r.nextInt(p.y))));
	}
	
	public void addPoisonAtPoint(Point p){
		poison.add(new Poison(new Vector(p.x, p.y)));
	}
	
	public void addFoodAtPoint(Point p){
		food.add(new Food(new Vector(p.x, p.y)));
	}
	
	// returns distance between two Particles (animals or items)
	private double getDistance(Particle p1, Particle p2){
		Vector v = new Vector(p1.getPosition().getX() - p2.getPosition().getX(), p1.getPosition().getY() - p2.getPosition().getY());
		return v.getMag();
	}

}
