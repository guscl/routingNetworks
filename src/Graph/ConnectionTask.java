package Graph;

import java.util.TimerTask;

public class ConnectionTask extends TimerTask {
	
	private Link link= null;

	public ConnectionTask(Link link){
		this.link = link;
	}
	
	@Override
	public void run() {
		link.removeConnection();
	}

}
