package ecosystem.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Position;
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
	
	public final static float Ppref = 0.45f;
	
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
		Random random = new Random();
		//2 separate 'for' loops blocks in order to let the top right 5*5 grid empty, as suggested by the article
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if (random.nextFloat() <= User.Ppref) this.preferences[i][j] = true;
				else this.preferences[i][j] = false;
			}
		}
		for(int i = 5 ; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				if (random.nextFloat() <= User.Ppref) this.preferences[i][j] = true;
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
		Random r = new Random();
		this.daysBtwBrowse = User.BROmin + r.nextInt(User.BROmax - User.BROmin + 1);
		this.daysElapsed = r.nextInt(this.daysBtwBrowse + 1);
	}
	
	/**
	 * The user choose (randomly) how many searches he wants to perform
	 * @return
	 */
	private int getKeywordSearchNumber(){
		Random r = new Random();
		return (User.KEYmin + r.nextInt(User.KEYmax - User.KEYmin + 1));
	}
	
	public void browse(int day){
		this.daysElapsed++;
		if(this.daysElapsed >= this.daysBtwBrowse){
			this.daysElapsed = 0;
			//select apps to download:
			App[] topApp = Store.getInstance().getTopAppsChart();
			App[] newApp = Store.getInstance().getNewAppsChart();
			App[] searchApp = Store.getInstance().getKeywordSearch(this.getKeywordSearchNumber());
			//browses the Top App Chart
			for(int i = 0; i < topApp.length; i++){
				App app = topApp[i];
				if(this.likesApp(app)){
					this.downloadApp(app, day);
				} 
			}
			//browses the New App Chart
			for(int i = 0; i < newApp.length; i++){
				App app = newApp[i];
				if(this.likesApp(app)){
					this.downloadApp(app, day);
				} 
			}
			//conducts a Keyword Search
			for(int i = 0; i < searchApp.length; i++){
				App app = searchApp[i];
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
