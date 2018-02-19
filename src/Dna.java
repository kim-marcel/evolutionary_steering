
import java.util.Random;

public class Dna {
	
	Random r = new Random();
	
	private double attractionFood;
	private double attractionPoison;
	private int perceptionFood;
	private int perceptionPoison;
	
	public Dna(){
		attractionFood = (r.nextDouble() * 10) - 5;
		attractionPoison = (r.nextDouble() * 10) - 5;
		perceptionFood = r.nextInt(150) + 1;
		perceptionPoison = r.nextInt(150) + 1;
//		attractionFood = 5;
//		attractionPoison = -5;
//		perceptionFood = 150;
//		perceptionPoison = 150;
	}
	
	public Dna(Dna dna){
		if(r.nextDouble() <= 0.05){ //5 per cent chance of drastical mutation
			attractionFood = (r.nextDouble() * 10) - 5;
			attractionPoison = (r.nextDouble() * 10) - 5;
			perceptionFood = r.nextInt(150) + 1;
			perceptionPoison = r.nextInt(150) + 1;
		}else{
			//regular mutation
			this.attractionFood = dna.attractionFood + (r.nextDouble() * 4) - 2;
			this.attractionPoison = dna.attractionPoison + (r.nextDouble() * 4) - 2;
			this.perceptionFood = dna.perceptionFood + r.nextInt(60) - 30;
			this.perceptionPoison = dna.perceptionPoison + r.nextInt(60) - 30;
			
			if(perceptionFood <= 0){
				perceptionFood = 1;
			}
			
			if(perceptionPoison <= 0){
				perceptionPoison = 1;
			}
		}		
	}

	public double getAttractionFood() {
		return attractionFood;
	}

	public double getAttractionPoison() {
		return attractionPoison;
	}

	public int getPerceptionFood() {
		return perceptionFood;
	}

	public int getPerceptionPoison() {
		return perceptionPoison;
	}

}
