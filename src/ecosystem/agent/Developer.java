package ecosystem.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import util.Strategy;
import util.comparator.AppCreationDateComparator;
import util.comparator.AppDownloadComparator;
import ecosystem.environment.Store;

public class Developer{//the developer agent

	//private String name;
	private List<App> uploadedApp;
	private boolean active;
	private Strategy strategy;
	private int devDuration;
	private int daysTaken;
	
	public final static int DEVmin = 1;//time min a developer can build an app
	public final static int DEVmax = 180;//time max a developer can build an app
	
	public final static float Pinactive = 0.0027f;
	public final static float POnNewChart = 0.001f;
	
	public Developer(Strategy strategy) {
		this.uploadedApp = new ArrayList<App>();
		this.active = true;
		this.strategy = strategy;
		this.initDevDuration();
		this.daysTaken = 0;
	}
	
	private void initDevDuration(){
		Random r = new Random();
		this.devDuration = Developer.DEVmin + r.nextInt(Developer.DEVmax - Developer.DEVmin + 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public App getLatestApp(){
		App latest = this.uploadedApp.get(0);
		for(App app : this.uploadedApp){
			if(app.getCreationDate() > latest.getCreationDate()) latest = app;
		}
		return latest;
	}
	
	/**
	 * 
	 * @return
	 */
	public App getBestApp(){
		App best = this.uploadedApp.get(0);
		for(App app : this.uploadedApp){
			if(app.getTotalDownload() > best.getTotalDownload()) best = app;
		}
		return best;
	}
	
	public int getTotalDownloads(){//TODO check if apps are updated after any download
		int totalDl = 0;
		for(App app : this.uploadedApp) totalDl = totalDl + app.getTotalDownload();
		return totalDl;
	}
	
	public float getAverageDownload(){
		return ((float) this.getTotalDownloads()) / this.getAppsNumber();
	}
	
	public int getAppsNumber(){
		return this.uploadedApp.size();
	}
	
	/**
	 * The developer has completed the app he was working on.
	 * He can now release it on the store.
	 */
	private void releaseApp(int day){
		Strategy strategy = this.strategy;
		Random r = new Random();
		while(strategy == Strategy.FLEXIBLE){//selects a new strategy
			if(r.nextFloat() <= 0.99f){
				App[] top = Store.getInstance().getTopAppsChart();
				strategy = top[r.nextInt(top.length)].getDeveloper().getStrategy();
			}
			else{
				strategy = Strategy.getRandom();
			}
		}
		
		App appToRelease;
		if(this.uploadedApp.isEmpty() || strategy == Strategy.INNOVATOR){
			appToRelease = this.createNewApp(day);
		}
		else{

			switch (strategy){
				case MILKER:
					appToRelease = this.copy(this.getLatestApp(), day);
					break;
				case OPTIMISER:
					appToRelease = this.copy(this.getBestApp(), day);
					break;
				case COPYCAT:
					appToRelease = this.copy(Store.getInstance().getTopAppsChart()[r.nextInt(Store.getInstance().getTopAppsChart().length)], day);
					break;
				default://Normally never reached...//TODO to check
					appToRelease = new App(this, day);
			}
		}
		this.uploadedApp.add(appToRelease);
		//updates the store too
		Store.getInstance().uploadApp(appToRelease);
		//if he is lucky enough, his app appears on the New Apps Chart
		if(r.nextFloat() <= Developer.POnNewChart) Store.getInstance().addToNewAppsChart(appToRelease);
	}
	
	/**
	 * Every time this method is called, the developer spends a day building its app.
	 * When the apps is built, he can create a new one.
	 */
	public void buildingApp(int day){
		if(this.active){
			this.daysTaken++;
			if(this.daysTaken >= this.devDuration){
				this.releaseApp(day);
				this.daysTaken = 0;
			}
			Random r = new Random();
			//becomes inactive
			if(r.nextFloat() <= Developer.Pinactive) this.stopDevelopment();
		}
	}
	
	/**
	 * Copy an app with or without going through a mutation
	 * @param app the app to copy from
	 * @param creationDay the day this app was copied
	 * @return a copied app
	 */
	private App copy(App app, int creationDay){
		Random r = new Random();
		App copy = new App(this, creationDay);
		boolean[][] features;
		if(r.nextFloat() <= 0.5f){//goes through a mutation
			features = app.getFeaturesWithMutation();
		}
		else{//simply copies its features
			features = app.getFeatures();
		}
		copy.setFeatures(features);
		return copy;
	}
	
	/**
	 * Create an app with random features
	 * @param creationDay the day this app was created
	 * @return a new app with random features
	 */
	private App createNewApp(int creationDay){
		return new App(this, creationDay);
	}
	
	/**
	 * Sorts apps by download number: the app that has the least downloads to the app that has the most
	 */
	public void sortUploadedAppsByDownload(){
		Collections.sort(this.uploadedApp, new AppDownloadComparator());
	}
	
	/**
	 * Sorts apps by creation date: the oldest to the newest app
	 */
	public void sortUploadedAppsByCreationDate(){
		Collections.sort(this.uploadedApp, new AppCreationDateComparator());
	}

	/**
	 * @return true if and only if the developer is active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * Doesn't develop apps anymore
	 */
	public void stopDevelopment() {
		this.active = false;
	}

	/**
	 * @return the strategy
	 */
	public Strategy getStrategy() {
		return this.strategy;
	}

	/**
	 * @return the uploadedApp
	 */
	public List<App> getUploadedApp() {
		return this.uploadedApp;
	}

}
