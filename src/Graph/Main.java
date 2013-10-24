package Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		try {
			// Creation of the topology
			createTopology(nodes);
			// System.out.println(nodes.toString());

			// Set the network
			Network network = new Network(nodes, "workload.txt");
			network.createWorkload(Network.LLP);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void createTopology(ArrayList<Node> nodes) throws IOException {
		// Creating the graph topology
		BufferedReader br = new BufferedReader(new FileReader(
				"simpleTopology.txt"));

		String line = br.readLine();
		// Creating buffer variables
		char node1, node2;
		int propagation, capacity;
		Link link = null;
		Node nodeOne = null, nodeTwo = null;
		String[] splitedSegments;

		// go through every line of the input file
		while (line != null) {
			splitedSegments = line.split(" ");
			node1 = splitedSegments[0].charAt(0);
			node2 = splitedSegments[1].charAt(0);
			propagation = Integer.valueOf(splitedSegments[2]);
			capacity = Integer.valueOf(splitedSegments[3]);

			nodeOne = new Node(node1);
			nodeTwo = new Node(node2);

			// verifying if nodes have already been stored
			boolean one = false;
			boolean two = false;
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getName() == node1) {
					nodeOne = nodes.get(i);
					one = true;
				}
				if (nodes.get(i).getName() == node2) {
					nodeTwo = nodes.get(i);
					two = true;
				}
			}

			// store nodes if they have not already been stored
			if (!one) {
				nodeOne.setIndex(nodes.size());
				nodes.add(nodeOne);
			}
			if (!two) {
				nodeTwo.setIndex(nodes.size());
				nodes.add(nodeTwo);
			}

			// Creating the link between the nodes
			link = new Link(propagation, capacity, nodeOne, nodeTwo);

			// Adding the link to the nodes
			nodeOne.addLink(link);
			nodeTwo.addLink(link);

			line = br.readLine();
		}

		br.close();

	}

}