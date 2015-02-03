package ecosystem.agent;

import java.util.ArrayList;
import java.util.List;

import util.Position;
import ecosystem.environment.Environment;
import ecosystem.environment.Store;

public class User{//the user agent
	
	//private String name;
	private boolean[][] preferences;
	private List<App> downloadedApps;
	private int daysBtwBrowse;
	private int daysElapsed;
	
	//general values
	public final static int BROmin = 1;//time min a user can pass without browsing apps
	public final static int BROmax = 360;//time max a user can pass without browsing app
	
	public final static int KEYmin = 0;
	public final static int KEYmax = 50;
	
	public final static int Ppref = 45; // sur 100
	
	public User() {
		this.preferences = new boolean[10][10];
		this.downloadedApps = new ArrayList<App>();
		
		this.initBrowseValues();
		this.initPreferences();
	}
	
	/**
	 * Inits the user preferences
	 */
	private void initPreferences(){
		//TODO to check
		//2 separate 'for' loops blocks in order to let the top right 5*5 grid empty, as suggested by the article
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if (Environment.r.nextInt(100) <= User.Ppref) this.preferences[i][j] = true;
				else this.preferences[i][j] = false;
			}
		}
		for(int i = 5 ; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if (Environment.r.nextInt(100) <= User.Ppref) this.preferences[i][j] = true;
				else this.preferences[i][j] = false;
			}
		}
	}
	
	/**
	 * For a given app, tells if its features fit with the user preferences
	 * @param app the app to test
	 * @return true if and only if all the features an app contains fits with the user preferences
	 */
	public boolean likesApp(App app){
		//this app contains unwanted features, so no user will like it
		if(app.getUnwantedFeaturePositions().size() > 0) return false;
		for(Position p : app.getFeaturePositions()){
			int x = p.getX();
			int y = p.getY();
			//this features doesn't match with the user expectations
			if(!this.preferences[x][y]) return false;
		}
		return true;
	}
	
	public List<Position> getPreferencePositions(){
		//TODO to improve
		List<Position> preferencePositions = new ArrayList<Position>();
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if(this.preferences[i][j]) preferencePositions.add(new Position(i, j));
			}
		}
		return preferencePositions;
	}
	
	/**
	 * Inits daysBtwBrowse and daysElapsed
	 */
	private void initBrowseValues(){
		this.daysBtwBrowse = User.BROmin + Environment.r.nextInt(User.BROmax - User.BROmin + 1);
		this.daysElapsed = Environment.r.nextInt(this.daysBtwBrowse + 1);
	}
	
	/**
	 * The user choose (randomly) how many searches he wants to perform
	 * @return
	 */
	private int getKeywordSearchNumber(){
		return (User.KEYmin + Environment.r.nextInt(User.KEYmax - User.KEYmin + 1));
	}
	
	public void browse(int day){
		this.daysElapsed++;
		if(this.daysElapsed >= this.daysBtwBrowse){
			this.daysElapsed = 0;
			//select apps to download:
			List<App> topApp = Store.getInstance().getTopAppsChart();
			List<App> newApp = Store.getInstance().getNewAppsChart();
			List<App> searchApp = Store.getInstance().getKeywordSearch(this.getKeywordSearchNumber());
			//browses the Top App Chart
			for(int i = 0; i < topApp.size(); i++){
				App app = topApp.get(i);
				if(this.likesApp(app)){
					this.downloadApp(app, day);
				} 
			}
			//browses the New App Chart
			for(int i = 0; i < newApp.size(); i++){
				App app = newApp.get(i);
				if(this.likesApp(app)){
					this.downloadApp(app, day);
				} 
			}
			//conducts a Keyword Search
			for(int i = 0; i < searchApp.size(); i++){
				App app = searchApp.get(i);
				if(this.likesApp(app)){
					this.downloadApp(app, day);
				} 
			}
		}
	}
	
	/**
	 * Download an app
	 * @param app the app the user downloads
	 * @param day when he downloads it
	 */
	private void downloadApp(App app, int day){
		//TODO make sure an app can't be downloaded more than once !!!!
		app.download(this, day);
		this.downloadedApps.add(app);
	}

}
