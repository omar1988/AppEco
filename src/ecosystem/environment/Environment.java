package ecosystem.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.RandomGenerator;
import util.Strategy;
import util.comparator.DeveloperAverageDownloadComparator;
import util.comparator.DeveloperDownloadComparator;
import ecosystem.agent.App;
import ecosystem.agent.Developer;
import ecosystem.agent.User;

//The global environment (for users and developers)
public class Environment {
	
	private static Environment globalEnvironment;
	
	private static boolean init=false;
	
	public static final RandomGenerator r = new RandomGenerator();
	
	private List<User> users;
	private List<Developer> developers;
	
	//users
	public static final int POPminUser = 150;
	public static final int POPmaxUser = 4000;
	public static final double D_User = -4f;
	public static final double S_User = -0.0038f;
	//developers
	public static final int POPminDev = 100;
	public static final int POPmaxDev = 1200;
	public static final double D_Dev = -4f;
	public static final double S_Dev = -0.005f;
	
	private Environment(){
		this.users = new ArrayList<User>();
		this.developers = new ArrayList<Developer>();
		this.increaseAgentPopulation(0);//TODO make sure this step isn't made twice
	}
	
	public static Environment getInstance(){
		if(globalEnvironment == null) globalEnvironment = new Environment();
		init=true;
		return globalEnvironment;
	}
	
	/**
	 * Tells by how many users the population would increase at a given day
	 * @param day the day
	 * @return the number of users to add
	 */
	public static int evaluateNewUsersPopulationSize(int day){
		if(!init || getInstance().users.size()<POPmaxUser){
			double numerator = Environment.POPmaxUser - Environment.POPminUser;
			double exp = Environment.S_User*day - Environment.D_User;
			double denominator = 1 + (Math.exp(exp));
			double result = Environment.POPminUser + (numerator/denominator);
			return (int) result;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * Tells by how many developers the population would increase at a given day
	 * @param day the day
	 * @return the number of developers to add
	 */
	public static int evaluateNewDevelopersPopulationSize(int day){
		if(!init || getInstance().developers.size()<POPmaxDev){
			double numerator = Environment.POPmaxDev - Environment.POPminDev;
			double exp = Environment.S_Dev*day - Environment.D_Dev;
			double denominator = 1 + (Math.exp(exp));
			double result = Environment.POPminDev + (numerator/denominator);
			return (int) result;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * Increase the agent (users and developers) population at a given day
	 * @param day the day they are created in
	 */
	public void increaseAgentPopulation(int day){
		int usersToAdd = Environment.evaluateNewUsersPopulationSize(day);
		int developersToAdd = Environment.evaluateNewDevelopersPopulationSize(day);
		
		for(int i = 0; i < usersToAdd; i++){
			this.users.add(new User());
		}
		for(int i = 0; i < developersToAdd; i++){
			this.developers.add(new Developer(Strategy.getRandom()));
		}
	}

	/**
	 * Shuffle the agents lists (i.e. users & developers)
	 */
	public void shuffleAgents(){
		Collections.shuffle(this.users);
		Collections.shuffle(this.developers);
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * @return the developers
	 */
	public List<Developer> getDevelopers() {
		return this.developers;
	}
	
	private void sortDevelopersByTotalDownloads(){
		Collections.sort(this.developers,new DeveloperDownloadComparator());
	}
	
	private void sortDevelopersByAverageDownloads(){
		Collections.sort(this.developers, new DeveloperAverageDownloadComparator());
	}
	
	public Developer[] get20BestDevelopers(){
		this.sortDevelopersByTotalDownloads();
		Developer[] bestDev = new Developer[20];
		for(int i = 0 ; i < 20 ; i++){
			bestDev[i] = this.developers.get(this.developers.size() - i - 1);
		}
		return bestDev;
	}
	
	public Developer[] get20BestAverageDevelopers(){
		this.sortDevelopersByAverageDownloads();
		Developer[] bestAvgDev = new Developer[20];
		for(int i = 0 ; i < 20 ; i++){
			bestAvgDev[i] = this.developers.get(this.developers.size() - i - 1);
		}
		return bestAvgDev;
	}
	
	//look at part 4-2
	public float getAverageDownloadsPerApp(Strategy strategy){//TODO make sure it isn't the FLEXIBLE strategy
		int totalDownload = 0;
		int totalApps = 0;
		for(Developer dev : this.developers){
			if(dev.getStrategy() == strategy){
				totalDownload = totalDownload + dev.getTotalDownloads();
				totalApps = totalApps + dev.getAppsNumber();
			}
		}
		return ((float) totalDownload) / totalApps;
	}
	//look at part 4-2
	public float getTop20TotalDownloads(Strategy strategy){
		Developer[] best = this.get20BestDevelopers();
		int devNumber = 0;
		for(int i = 0; i <  best.length; i++){
			if(best[i].getStrategy() == strategy) devNumber++;
		}
		return ((float) devNumber)/ 20;
	}
	//look at part 4-2
	public float getTop20AverageDownloads(Strategy strategy){
		Developer[] bestAvg = this.get20BestAverageDevelopers();
		int devNumber = 0;
		for(int i = 0; i <  bestAvg.length; i++){
			if(bestAvg[i].getStrategy() == strategy) devNumber++;
		}
		return ((float) devNumber)/ 20;
	}
	//look at part 4-2
	public float getZeroDownloads(Strategy strategy){
		int zero = 0;
		for(Developer dev : this.developers){
			if(dev.getTotalDownloads() == 0 && dev.getStrategy() == strategy) zero++;
		}
		return ((float) zero) / this.developers.size();
	}
	//look at part 4-2
	public float getFitness(Strategy strategy, int level){
		int potentialDownloads = 0;
		for(Developer dev : this.developers){
			if(dev.getStrategy() == strategy){
				dev.sortUploadedAppsByCreationDate();
				App app = dev.getUploadedApp().get(level);
				for(User user : this.users){
					if(user.likesApp(app)) potentialDownloads++;
				}
			}
		}
		return (float) potentialDownloads / this.users.size();
	}
}
