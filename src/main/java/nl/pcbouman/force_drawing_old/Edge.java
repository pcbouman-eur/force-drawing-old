package nl.pcbouman.force_drawing_old;

/**
 * Class that Represents an Edge in the Graph
 * @author Paul Bouman
 *
 */
public class Edge {
	private Node from, to;

	private double weight;

	/**
	 * Constructor for an Edge
	 * @param f The source node of this edge
	 * @param t The target node of this edge
	 * @param w The weight of this edge;
	 */
	
	public Edge(Node f, Node t, double w) {
		from = f;
		to = t;
		weight = w;
	}

	/**
	 * Returns the source node of this edge
	 * @return the source node
	 */
	public Node getFrom() {
		return from;
	}

	/**
	 * Return the target node of this edge
	 * @return the target node
	 */
	public Node getTo() {
		return to;
	}

	/**
	 * Returns the weight of this edge
	 * @return the weight of this edge
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Change the weight of this edge
	 * @param weight the new weight of this edge
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String toString() {
		return from.toString() + " -- " + to.toString();
	}

}
