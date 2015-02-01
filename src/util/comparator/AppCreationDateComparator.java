package util.comparator;

import java.util.Comparator;

import ecosystem.agent.App;

public class AppCreationDateComparator implements Comparator<App> {

	@Override
	public int compare(App app1, App app2) {
		return app1.getCreationDate() - app2.getCreationDate();
	}

}
