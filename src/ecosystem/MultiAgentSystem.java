package ecosystem;

import java.util.List;

import ecosystem.agent.Developer;
import ecosystem.agent.User;
import ecosystem.environment.Environment;

public class MultiAgentSystem {
	
	private int currentDay;//useless ?
	public static final int TOTAL_DURATION = 1080;//The ecosystem life span
	
	public MultiAgentSystem(){
		this.currentDay = 1;
	}
	
	private void runOnce(int day){
		Environment.getInstance().shuffleAgents();
		List<Developer> developers = Environment.getInstance().getDevelopers();
		List<User> users = Environment.getInstance().getUsers();
		for(Developer developer : developers) developer.buildingApp(day);
		for(User user : users) user.browse(day);
		Environment.getInstance().increaseAgentPopulation(day);
	}
	
	public void run(){
		//begins at i = 1, because at day 0, the 2 environments(i.e. store & environment) are initialized
		for(int i = 1 ; i <= MultiAgentSystem.TOTAL_DURATION ; i++){
			this.runOnce(i);
			this.currentDay++;
		}
	}

	/**
	 * @return the currentDay
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}

}
