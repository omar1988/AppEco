package util.comparator;

import java.util.Comparator;

import ecosystem.agent.Developer;

public class DeveloperAverageDownloadComparator implements Comparator<Developer> {

	@Override
	public int compare(Developer dev1, Developer dev2) {
		if(dev1.getAverageDownload() <  dev2.getAverageDownload()) return -1;
		if(dev1.getAverageDownload() == dev2.getAverageDownload()) return 0;
		return 1;
	}

}
