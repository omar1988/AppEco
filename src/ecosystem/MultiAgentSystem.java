package ecosystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import util.Strategy;
import ecosystem.agent.Developer;
import ecosystem.agent.User;
import ecosystem.environment.Environment;

public class MultiAgentSystem {
	
	int i=0;
	

	File userFile;
	FileWriter userFileWriter;

	
	private int currentDay;//useless ?
	public static final int TOTAL_DURATION = 1080;//The ecosystem life span
	
	public MultiAgentSystem() throws IOException{
		this.currentDay = 1;

		userFile = new File("./user.csv");
		userFileWriter = new FileWriter(userFile);
	}
	
	private void runOnce(int day) throws IOException{
		i++;

		List<Developer> developers = Environment.getInstance().getDevelopers();
		List<User> users = Environment.getInstance().getUsers();
		
		long debut=System.currentTimeMillis();
		for(Developer developer : developers) developer.buildingApp(day);
		System.out.println("day "+i+" / 1 : "+(System.currentTimeMillis()-debut));
		debut=System.currentTimeMillis();
		
		for(User user : users) user.browse(day);
		System.out.println("day "+i+" / 2 : "+(System.currentTimeMillis()-debut));
		Environment.getInstance().increaseAgentPopulation(day);
		
		System.out.println("devs : "+developers.size());
		System.out.println("utilisateurs : "+users.size());
		
		userFileWriter.write(i+","+developers.size()+","+users.size()+"\n");
		
		for(Strategy s : Strategy.values()){
			System.out.println(s+" download /apps : "+Environment.getInstance().getAverageDownloadsPerApp(s));
		}
	}
	
	public void run() throws IOException{
		//begins at i = 1, because at day 0, the 2 environments(i.e. store & environment) are initialized
		for(int i = 1 ; i < MultiAgentSystem.TOTAL_DURATION ; i++){
			this.runOnce(i);
			this.currentDay++;
		}
		userFileWriter.close();
	}

	/**
	 * @return the currentDay
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}

}
