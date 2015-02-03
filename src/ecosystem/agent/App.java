package ecosystem.agent;

import java.util.ArrayList;
import java.util.List;

import util.Position;
import ecosystem.MultiAgentSystem;
import ecosystem.environment.Environment;

public class App {//the app agent
	
	//private String name;
	private boolean[][] features;
	private int[] downloadNumber;//the downloads number per days
	private Developer createdBy;
	private int createdAt;
	private List<User> downloadedBy;
	List<Position> unwanted;
	List<Position> featurePositions;
	List<Position> emptyPositions;
	
	//general values
	public static final int Pfeat = 4;
	
	public App(Developer developer, int creationDay){
		this.features = new boolean[10][10];
		this.downloadNumber = new int[MultiAgentSystem.TOTAL_DURATION];
		this.createdBy = developer;
		this.createdAt = creationDay;
		this.downloadedBy = new ArrayList<User>();
		
		this.initFeatures();
	}
	
	/**
	 * Inits the app with random features.
	 * Note that aside from the strategy he chose,
	 * any developer's first app is created randomly
	 */
	private void initFeatures(){//TODO
		for(int i = 0 ; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if (Environment.r.nextInt(100) <= App.Pfeat) this.features[i][j] = true;
				else this.features[i][j] = false;
			}
		}
		unwanted = new ArrayList<Position>();
		for(int i = 0; i < 5 ; i++){
			for(int j = 5; j < 10 ; j++){
				if(this.features[i][j]) unwanted.add(new Position(i, j));
			}
		}
		featurePositions = new ArrayList<Position>();
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if(this.features[i][j]) featurePositions.add(new Position(i, j));
			}
		}
		emptyPositions = new ArrayList<Position>();
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if(!this.features[i][j]) emptyPositions.add(new Position(i, j));
			}
		}
	}
	
	public List<Position> getUnwantedFeaturePositions(){
		return unwanted;
	}
	
	public List<Position> getFeaturePositions(){
		return featurePositions;
	}
	
	public List<Position> getEmptyPositions(){
		return emptyPositions;
	}
	
	/**
	 * The number of features this app contains
	 * @return
	 */
	public int getFeaturesNumber(){
		return this.getFeaturePositions().size();
	}
	
	/**
	 * 
	 * @param user
	 */
	public void download(User user, int day){
		this.downloadedBy.add(user);
		this.downloadNumber[day]++;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTotalDownload(){
		int total = 0;
		for(int i = 0 ; i < this.downloadNumber.length ; i++){
			total = total + this.downloadNumber[i];
		}
		return total;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getAverageDownload(){
		return ((float) this.getTotalDownload()) / this.downloadNumber.length;
	}

	/**
	 * @return the features
	 */
	public boolean[][] getFeatures() {
		return this.features;
	}
	
	/**
	 * Returns the features of this app after a mutation.
	 * @return the features of this app after a mutation.
	 */
	public boolean[][] getFeaturesWithMutation(){
		boolean[][] featuresThroughMutation = new boolean[10][10];
		//copy the features array (a simple affectation would not suffice)
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				featuresThroughMutation[i][j] = this.features[i][j];
			}
		}
		int featuresNumber = this.getFeaturesNumber();
		//there is no move if the grid is either completely empty or full of features
		if(featuresNumber > 0 && featuresNumber < 100){
			//Select a random feature
			Position start = this.getFeaturePositions().get(Environment.r.nextInt(featuresNumber));
			//Select a random empty cell
			Position destination = this.getEmptyPositions().get(Environment.r.nextInt(this.getEmptyPositions().size()));
			
			featuresThroughMutation[start.getX()][start.getY()] = false;
			featuresThroughMutation[destination.getX()][destination.getY()] = true;
		}
		return featuresThroughMutation;
	}

	/**
	 * @param features the features to set
	 */
	public void setFeatures(boolean[][] features) {
		this.features = features;
		unwanted = new ArrayList<Position>();
		for(int i = 0; i < 5 ; i++){
			for(int j = 5; j < 10 ; j++){
				if(this.features[i][j]) unwanted.add(new Position(i, j));
			}
		}
		featurePositions = new ArrayList<Position>();
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if(this.features[i][j]) featurePositions.add(new Position(i, j));
			}
		}
		emptyPositions = new ArrayList<Position>();
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if(!this.features[i][j]) emptyPositions.add(new Position(i, j));
			}
		}
	}

	/**
	 * @return the downloadNumber
	 */
	public int[] getDownloadNumber() {
		return this.downloadNumber;
	}

	/**
	 * @return the developer that created this app
	 */
	public Developer getDeveloper() {
		return this.createdBy;
	}

	/**
	 * @return the users that downloaded this app
	 */
	public List<User> getUsers() {
		return this.downloadedBy;
	}

	/**
	 * @return the createdAt
	 */
	public int getCreationDate() {
		return this.createdAt;
	}
}
