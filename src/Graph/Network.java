package Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Network {
	private ArrayList<Node> graph = null;
	private String workloadPath = null;

	private int circuitRequests = 0;
	private int successfulRequests = 0;
	private int blockedRequests = 0;
	private int numberOfHops = 0;
	private int totalDelay = 0;

	public Network(ArrayList<Node> graph, String workloadPath) {
		this.graph = graph;
		this.workloadPath = workloadPath;
	}

	private ArrayList<Node> sdp(char node1, char node2) {
		ArrayList<Node> path = new ArrayList<Node>();
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
			for (int i = 0; i < graph.size(); i++) {
				queue.add(queue.poll());
			}

			// finding the node with smallest distance from u
			u = queue.poll();
			path.add(u);

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
		return path;

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

	public ArrayList<Path> createWorkload() throws IOException {
		// Creating the graph workload
		BufferedReader br = new BufferedReader(new FileReader(
				"simpleWorkload.txt"));

		String line = br.readLine();

		// Creating buffer variables
		ArrayList<Path> allPaths = new ArrayList<Path>();
		char node1, node2;
		float reqTime, reqDuration;
		String[] splitedSegments;

		while (line != null) {
			splitedSegments = line.split(" ");
			node1 = splitedSegments[1].charAt(0);
			node2 = splitedSegments[2].charAt(0);
			reqTime = Float.valueOf(splitedSegments[0]);
			reqDuration = Float.valueOf(splitedSegments[3]);

			// Determining routing path with SDP
			sdp(node1, node2);

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
			int i = 0;
			Path path = new Path();
			while (true) {
				// add one connection between nodeOne and nodeTwo
				if (!nodeOne.getLink(nodeTwo.getName()).isBusy()) {
					nodeOne.getLink(nodeTwo.getName()).addConnection();
					
					path.addLink(nodeOne.getLink(nodeTwo.getName()));
					System.out.println("added a connection between "
							+ nodeOne.getName() + " and " + nodeTwo.getName());

					// update nodeTwo and nodeOne along the routed path
					nodeTwo = nodeOne;
					nodeOne = graph.get(nodeTwo.getRoutedParent());

					// stop when reach first node of the connection
					if (nodeTwo.getName() == node1)
						break;
				} else {
					return null;
				}
			}
			//Will use this path to program the time stuff..
			allPaths.add(path);
			line = br.readLine();
			System.out.println();
		}

		br.close();
		return allPaths;
	}

}