package Graph;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
	//This method returns zero if the objects are equal.
	//It returns a positive value if obj1 is greater than obj2. Otherwise, a negative value is returned.
    public int compare(Node node1, Node node2) {
        if (node1.getDist() == node2.getDist())
        	return 0;
        else if (node1.getDist() > node2.getDist())
        	return 1;
        else
        	return -1;
    }
}