package Graph;

import java.util.ArrayList;

public class Path {
	private ArrayList<Link> path = new ArrayList<Link>();
	
	public void  addLink(Link link){
		path.add(link);
	}
	
	public ArrayList<Link> getPath(){
		return path;
	}

	@Override
	public String toString() {
		return "Path [path=" + path + "]" + "\n";
	}
	
	
}