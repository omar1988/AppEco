package ecosystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import util.Strategy;
import ecosystem.agent.App;
import ecosystem.agent.Developer;
import ecosystem.agent.User;
import ecosystem.environment.Environment;

public class MultiAgentSystem {
	
	int i=0;
	

	File userFile;
	FileWriter userFileWriter;
	
	File priceFile;
	FileWriter priceFileWriter;

	
	private int currentDay;//useless ?
	public static final int TOTAL_DURATION = 1080;//The ecosystem life span
	
	public MultiAgentSystem() throws IOException{
		this.currentDay = 1;

		userFile = new File("./user.csv");
		userFileWriter = new FileWriter(userFile);
		
		priceFile = new File("./price.csv");
		priceFileWriter = new FileWriter(priceFile);

	}
	
	private void runOnce(int day) throws IOException{
		i++;
		long debut=System.currentTimeMillis();
		
		
		List<Developer> developers = Environment.getInstance().getDevelopers();
		List<User> users = Environment.getInstance().getUsers();
		

		for(Developer developer : developers) developer.buildingApp(day);
		for(User user : users) user.browse(day);
		
		Environment.getInstance().increaseAgentPopulation(day);
		
		System.out.println("day "+i+" / "+(System.currentTimeMillis()-debut)+" ms");
		
		double prixTotal = 0;
		double nbApp = 0;
		
		for(Developer dev : Environment.getInstance().getDevelopers()){
			nbApp+=dev.getAppsNumber();
			for(App app : dev.getUploadedApp()){
				prixTotal+=app.getPrice();
			}
		}
		
		priceFileWriter.write(i+","+prixTotal/nbApp+"\n");
		
		userFileWriter.write(i+","+developers.size()+","+users.size()+"\n");
		
		
		if(i%25==0){
			priceFileWriter.flush();
			userFileWriter.flush();
		}
		

	}
	
	public void run() throws IOException{
		//begins at i = 1, because at day 0, the 2 environments(i.e. store & environment) are initialized
		for(int i = 1 ; i < MultiAgentSystem.TOTAL_DURATION ; i++){
			this.runOnce(i);
			this.currentDay++;
		}
		userFileWriter.close();
		priceFileWriter.close();
	}

	/**
	 * @return the currentDay
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}

}
