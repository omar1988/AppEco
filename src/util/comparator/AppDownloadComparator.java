package util.comparator;

import java.util.Comparator;

import ecosystem.agent.App;

public class AppDownloadComparator implements Comparator<App> {//compares 2 apps by their download number

	@Override
	public int compare(App app1, App app2) {
		return app1.getTotalDownload() - app2.getTotalDownload();
	}

}
