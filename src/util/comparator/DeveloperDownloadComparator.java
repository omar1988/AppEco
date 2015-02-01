package util.comparator;

import java.util.Comparator;

import ecosystem.agent.Developer;

public class DeveloperDownloadComparator implements Comparator<Developer> {

	@Override
	public int compare(Developer dev1, Developer dev2) {
		return dev1.getTotalDownloads() - dev2.getTotalDownloads();
	}

}
