package Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.Timer;

public class Network {
	public static final int SHP = 1;
	public static final int SDP = 2;
	public static final int LLP = 3;
	
	private ArrayList<Node> graph = null;
	private String workloadPath = null;
	
	private Timer scheduler = new Timer();

	private int circuitRequests = 0;
	private int successfulRequests = 0;
	private int blockedRequests = 0;
	private int numberOfHops = 0;
	private int totalDelay = 0;

	public Network(ArrayList<Node> graph, String workloadPath) {
		this.graph = graph;
		this.workloadPath = workloadPath;
	}

	private void shp(char node1) {
		Node node = null;

		// Finding the starting node inside the array
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).getName() == node1)
				node = graph.get(i);
		}

		// initialize queues
		PriorityQueue<Node> queue = new PriorityQueue<Node>(graph.size(),
				new NodeComparator());
		for (int i = 0; i < graph.size(); i++) {
			queue.add(graph.get(i));
		}

		// initializing all distances with infinity
		for (int i = 0; i < graph.size(); i++) {
			graph.get(i).setDist(9999999);
			graph.get(i).setVisited(false);
		}
		// node one has distance 0
		node.setDist(0);

		// start with node one
		Node u = node;
		Node v;

		while (!queue.isEmpty()) {
			// update queue ordering
			for (int i = 0; i < queue.size(); i++) {
				queue.add(queue.poll());
			}

			// finding the node with smallest distance from u
			u = queue.poll();

			// update distance value of all adjacent nodes
			for (int i = 0; i < u.getLinks().size(); i++) {
				// get adjacent node
				v = u.getLinks().get(i).getNextNode(u.getName());

				// relaxation of the distances
				if (v.getDist() > u.getDist() + 1) {
					v.setDist(u.getDist() + 1);
					v.setRoutedParent(u.getIndex());
					if (!queue.contains(v)) {
						queue.add(v);
					}
				}
			}
		}
	}

	private void sdp(char node1) {
		Node node = null;

		// Finding the starting node inside the array
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).getName() == node1)
				node = graph.get(i);
		}

		// initialize queues
		PriorityQueue<Node> queue = new PriorityQueue<Node>(graph.size(),
				new NodeComparator());
		for (int i = 0; i < graph.size(); i++) {
			queue.add(graph.get(i));
		}

		// initializing all distances with infinity
		for (int i = 0; i < graph.size(); i++) {
			graph.get(i).setDist(9999999);
			graph.get(i).setVisited(false);
		}
		// node one has distance 0
		node.setDist(0);

		// start with node one
		Node u = node;
		Node v;

		while (!queue.isEmpty()) {
			// update queue ordering
			for (int i = 0; i < queue.size(); i++) {
				queue.add(queue.poll());
			}

			// finding the node with smallest distance from u
			u = queue.poll();

			// update distance value of all adjacent nodes
			for (int i = 0; i < u.getLinks().size(); i++) {
				// get adjacent node
				v = u.getLinks().get(i).getNextNode(u.getName());

				// relaxation of the distances
				if (v.getDist() > u.getDist()
						+ u.getLinks().get(i).getPropagation()) {
					v.setDist(u.getDist()
							+ u.getLinks().get(i).getPropagation());
					v.setRoutedParent(u.getIndex());
					if (!queue.contains(v)) {
						queue.add(v);
					}
				}
			}
		}
	}
	
	private void llp(char node1) {
		Node node = null;

		// Finding the starting node inside the array
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).getName() == node1)
				node = graph.get(i);
		}

		// initialize queues
		PriorityQueue<Node> queue = new PriorityQueue<Node>(graph.size(),
				new NodeComparator());
		for (int i = 0; i < graph.size(); i++) {
			queue.add(graph.get(i));
		}

		// initializing all distances with infinity
		for (int i = 0; i < graph.size(); i++) {
			graph.get(i).setDist(9999999);
			graph.get(i).setVisited(false);
		}
		// node one has distance 0
		node.setDist(0);

		// start with node one
		Node u = node;
		Node lastU = null;
		int aux = 0;
		Node v;
		double conn, cap, dist;

		while (!queue.isEmpty()) {
			//avoiding infinite loops
			if (u.equals(lastU)){
				aux++;
				if (aux > 1)
					break;
			}
			
			// update queue ordering
			for (int i = 0; i < queue.size(); i++) {
				queue.add(queue.poll());
			}	
			
			// finding the node with smallest distance from u
			lastU = u;
			u = queue.poll();

			// update distance value of all adjacent nodes
			for (int i = 0; i < u.getLinks().size(); i++) {
				// get adjacent node
				v = u.getLinks().get(i).getNextNode(u.getName());
				
				//relaxation of the distances
				conn = u.getLinks().get(i).getCurrentConnections();
				cap = u.getLinks().get(i).getCapacity();
				dist = conn/cap;
				if (v.getDist() > dist) {					
					v.setRoutedParent(u.getIndex());
					if (u.getDist() > dist)
						v.setDist(u.getDist());
					else
						v.setDist(dist);
					
					if (!queue.contains(v))
						queue.add(v);
					
				}				
			}				
		}
	}

	public int getCircuitRequests() {
		return circuitRequests;
	}

	public int getSuccessfulRequests() {
		return successfulRequests;
	}

	public int getBlockedRequests() {
		return blockedRequests;
	}

	public int getNumberOfHops() {
		return numberOfHops;
	}

	public int getTotalDelay() {
		return totalDelay;
	}

	public void createWorkload(int routingForm)
			throws IOException {
		// Creating the graph workload
		BufferedReader br = new BufferedReader(new FileReader(
				"simpleWorkload.txt"));

		String line = br.readLine();

		// Creating buffer variables
		char node1, node2;
		float reqTime, reqDuration;
		String[] splitedSegments;

		boolean aux = true;
		while (line != null) {
			splitedSegments = line.split(" ");
			node1 = splitedSegments[1].charAt(0);
			node2 = splitedSegments[2].charAt(0);
			reqTime = Float.valueOf(splitedSegments[0]);
			reqDuration = Float.valueOf(splitedSegments[3]);

			if (routingForm == Network.SHP)
				shp(node1);
			else if (routingForm == Network.SDP)
				sdp(node1);
			else if (routingForm == Network.LLP)
				llp(node1);
			
			Node nodeTwo = null;
			Node nodeOne = null;

			// initializing nodeTwo as last node of connection
			for (int i = 0; i < graph.size(); i++) {
				if (graph.get(i).getName() == node2)
					nodeTwo = graph.get(i);
			}
			// initializing nodeOne as nodeTwo`s parent
			nodeOne = graph.get(nodeTwo.getRoutedParent());

			System.out.println("new path");
			// Creating the connections from the path
			ArrayList<Link> path = new ArrayList<Link>();
			while (true) {
				// add one connection between nodeOne and nodeTwo
				if (!nodeOne.getLink(nodeTwo.getName()).isBusy()) {
					nodeOne.getLink(nodeTwo.getName()).addConnection();
					path.add(nodeOne.getLink(nodeTwo.getName()));
					System.out.println("\n\radded a connection between "
							+ nodeOne.getName() + " and " + nodeTwo.getName());
					System.out.println("number of connections in link "+nodeOne.getName()+"-"+nodeTwo.getName()+": "+nodeOne.getLink(nodeTwo.getName()).getCurrentConnections()+"/"+nodeOne.getLink(nodeTwo.getName()).getCapacity());

					// update nodeTwo and nodeOne along the routed path
					nodeTwo = nodeOne;
					nodeOne = graph.get(nodeTwo.getRoutedParent());

					// stop when reach first node of the connection
					if (nodeTwo.getName() == node1)
						successfulRequests++;
						break;
				} else {
					// Set this path to null so it doesn't count as success
					path = new ArrayList<Link>();
					break;
				}
			}
			// Will use this path to program the time stuff..			
			for(int i = 0; i< path.size();i++){
				Calendar date = Calendar.getInstance();
				//date.se
				//scheduler.schedule(new ConnectionTask(path.get(i)), reqDuration);
			}
			
			
			line = br.readLine();
		}

		br.close();
		
	}
}