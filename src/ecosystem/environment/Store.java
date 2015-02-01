package ecosystem.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import util.Strategy;
import util.comparator.AppDownloadComparator;
import ecosystem.agent.App;
import ecosystem.agent.Developer;

//The apps environment
public class Store {
	
	private static Store theStore;
	private List<App> apps;
	private List<App> NewAppsChart;
	
	//general values
	public static final int initApp = 500;
	
	public static final int N_MaxNewChart = 40;
	public static final int N_MaxTopChart = 50;
	
	private Store(){
		this.apps = new ArrayList<App>();
		this.NewAppsChart = new ArrayList<App>();
		this.initApps();
	}
	
	public static Store getInstance(){
		if(theStore == null) theStore = new Store();
		return theStore;
	}
	
	/**
	 * Inits the store by creating apps
	 */
	private void initApps(){
		Random r = new Random();
		for(int i = 0; i < Store.initApp ; i++){
			Developer dev;//selects a random dev from the environment
			Strategy strat;
			do{
				dev = Environment.getInstance().getDevelopers().get(r.nextInt(Environment.getInstance().getDevelopers().size()));
				strat = dev.getStrategy();
			}while(strat == Strategy.COPYCAT || strat == Strategy.FLEXIBLE);
			dev = new Developer(strat);
			App appToAdd = new App(dev, 0);
			this.apps.add(appToAdd);
		}
		
		for(int i = 0; i < Store.N_MaxNewChart ;i++){//inits the New Apps Chart
			//TODO check if an app doesn't exist more than once
			App newApp = this.apps.get(r.nextInt(this.apps.size()));
			this.NewAppsChart.add(newApp);
		}
	}
	
	public void addToNewAppsChart(App app){//every time an app is added, the oldest one is removed from the list
		this.NewAppsChart.add(app);
		this.NewAppsChart.remove(this.NewAppsChart.size() - 1);
	}
	
	/**
	 * Add an app to the store
	 * @param app the app to add
	 */
	public void uploadApp(App app){
		//TODO to check
		this.apps.add(app);
	}
	
	public void downloadApp(App app,int day){
		//TODO get the app and add download to it at given date
	}

	/**
	 * @return the apps
	 */
	public List<App> getApps() {
		return this.apps;
	}
	
	private void sortAppsByDownload(){
		Collections.sort(this.apps,new AppDownloadComparator());
	}
	
	private void shuffleApps(){
		Collections.shuffle(this.apps);
	}
	
	public App[] getTopAppsChart(){//TODO check list size
		this.sortAppsByDownload();
		App[] topApps = new App[Store.N_MaxTopChart];
		for(int i = 0; i < Store.N_MaxTopChart ; i++){
			topApps[i] = this.apps.get(this.apps.size() - i - 1);
		}
		return topApps;
	}
	
	public App[] getNewAppsChart(){
		//convert to array
		App[] newApps = new App[this.NewAppsChart.size()];
		newApps = this.NewAppsChart.toArray(newApps);
		return newApps;
	}
	
	/**
	 * Returns a random number of app
	 * @param search the number of app the user was looking for
	 * @return a random number of app
	 */
	public App[] getKeywordSearch(int search){
		App[] searchedApps = new App[search];
		Random r = new Random();
		for(int i = 0; i < search ; i++){//TODO make sure it doesn't return an array containing more than 2 times the same app
			searchedApps[i] = this.getApps().get(r.nextInt(this.getApps().size()));
		}
		return searchedApps;
	}

}
