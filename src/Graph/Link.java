 package Graph;

public class Link {

	@Override
	public String toString() {
		return "Link [nodeOne=" + nodeOne.getName() + ", nodeTwo=" + nodeTwo.getName()
				+ ", propagation=" + propagation + ", capacity=" + capacity
				+ ", connections=" + currentConnections + "]";
	}
	private Node nodeOne,nodeTwo;
	private int propagation;
	private int capacity;
	private int currentConnections;
	
	public int getCurrentConnections() {
		return currentConnections;
	}

	public Node getNextNode(char currentNode){
		if(currentNode == nodeOne.getName())
			return nodeTwo;
		else
			return nodeOne;
	}

	public void setCurrentConnections(int currentConnections) {
		this.currentConnections = currentConnections;
	}


	public Link(int propagation, int capacity, Node nodeOne, Node nodeTwo){
		this.propagation = propagation;
		this.capacity = capacity;
		this.nodeOne = nodeOne;
		this.nodeTwo = nodeTwo;
	}
	
	
	public Node getNodeOne() {
		return nodeOne;
	}
	public void setNodeOne(Node nodeOne) {
		this.nodeOne = nodeOne;
	}
	public Node getNodeTwo() {
		return nodeTwo;
	}
	public void setNodeTwo(Node nodeTwo) {
		this.nodeTwo = nodeTwo;
	}
	public boolean isBusy() {
		return (capacity == currentConnections);}
	
	
	public int getPropagation() {
		return propagation;
	}
	public void setPropagation(int propagation) {
		this.propagation = propagation;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void addConnection(){
		this.currentConnections++;
	}
	
	public void removeConnection(){
		this.currentConnections--;
	}
	
}
