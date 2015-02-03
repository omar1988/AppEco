package simultation;

import java.io.IOException;

import ecosystem.MultiAgentSystem;

public class Launch {

	public static void main(String[] args) throws IOException {
		MultiAgentSystem sys = new MultiAgentSystem();
		
		sys.run();
	}

}
