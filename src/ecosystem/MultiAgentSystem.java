package ecosystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import util.Strategy;
import ecosystem.agent.App;
import ecosystem.agent.Developer;
import ecosystem.agent.User;
import ecosystem.environment.Environment;
import ecosystem.environment.Store;

public class MultiAgentSystem {
	
	int i=0;
	

	File file;
	FileWriter fileWriter;
	
	private int currentDay;
	public static final int TOTAL_DURATION = 1080;//The ecosystem life span
	
	public MultiAgentSystem() throws IOException{
		this.currentDay = 1;

		file = new File("./plot.csv");
		fileWriter = new FileWriter(file);
		
		fileWriter.write("jour,"
				+ "prix moyen,"
				+ "prix moyen top app,"
				+ "nombre d'app,"
				+ "nombre de devs,"
				+ "nombre d'util,"
				+ "INNOVATOR,"
				+ "MILKER,"
				+ "OPTIMISER,"
				+ "COPYCAT,"
				+ "dl moy INNOVATOR,"
				+ "dl moy MILKER,"
				+ "dl moy OPTIMISER,"
				+ "dl moy COPYCAT\n");

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
		double prixTotalTop = 0;
		
		int innov = 0;
		int milker = 0;
		int optimiser = 0;
		int copycat = 0;
		
		for(Developer dev : Environment.getInstance().getDevelopers()){
			nbApp+=dev.getAppsNumber();
			switch(dev.getStrategy()){
				case INNOVATOR : innov++;break;
				case MILKER : milker++;break;
				case OPTIMISER : optimiser++;break;
				case COPYCAT : copycat++;break;
				default:;
			}
			for(App app : dev.getUploadedApp()){
				prixTotal+=app.getPrice();
			}
		}
		
		for(App app : Store.getInstance().getTopAppsChart()){
				prixTotalTop+=app.getPrice();
		}
		

		fileWriter.write(i+"");
		
		fileWriter.write(","+String.format(Locale.ENGLISH,"%.3f",prixTotal/nbApp));		
		
		fileWriter.write(","+String.format(Locale.ENGLISH,"%.3f",prixTotalTop/Store.getInstance().getTopAppsChart().size()));
		
		fileWriter.write(","+nbApp);
		fileWriter.write(","+developers.size()+","+users.size());
		

		fileWriter.write(","+innov+","+milker+","+optimiser+","+copycat);

		for(Strategy s : Strategy.values()){
			if(s!=Strategy.FLEXIBLE){
				fileWriter.write(","+String.format(Locale.ENGLISH,"%.5f",Environment.getInstance().getAverageDownloadsPerApp(s)));
			}
		}
		
		fileWriter.write("\n");
		
		if(i%25==0){
			fileWriter.flush();
		}
		

	}
	
	public void run() throws IOException{
		//begins at i = 1, because at day 0, the 2 environments(i.e. store & environment) are initialized
		for(int i = 1 ; i < MultiAgentSystem.TOTAL_DURATION ; i++){
			this.runOnce(i);
			this.currentDay++;
		}
		fileWriter.close();

	}

	/**
	 * @return the currentDay
	 */
	public int getCurrentDay() {
		return this.currentDay;
	}

}
