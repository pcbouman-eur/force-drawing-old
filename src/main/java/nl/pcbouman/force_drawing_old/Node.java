package nl.pcbouman.force_drawing_old;

/**
 * Class that represents a node in the Graph
 * @author Paul Bouman
 *
 */
public class Node {
	
	// For unique ID generation
	private static int num = 1;

	/**
	 * The coordinate vector of this node
	 */
	public double[] coors;

	/**
	 * The force vector of this node
	 */
	public double[] forces;

	/**
	 * A normalisation counter
	 */
	public int forceCount;

	/**
	 * Back pointer if this node represents another node in another graph
	 */
	public Node org;

	/**
	 * Identification Object for this node
	 */
	public Object id;

	/**
	 * Wheter or not this node can be moved
	 */
	public boolean movable = true;

	/**
	 * Create a new node with vector dimenions dim
	 * @param dim the dimensions of the position and force vectors
	 */
	public Node(int dim) {
		coors = new double[dim];
		for (int t = 0; t < dim; t++)
			coors[t] = Math.random();
		forces = new double[dim];
		id = num++;
	}
	
	/**
	 * Create a new node with 
	 * @param ido
	 * @param dim
	 */
	
	public Node(Object ido, int dim)
	{
		this(dim);
		id = ido;
	}

	/**
	 * Calculates the force on this node
	 * @return the force on this node
	 */
	public double getForce() {
		double sum = 0;
		for (double d : forces)
			sum += d * d;
		return Math.sqrt(sum);
	}

	public String toString() {
		return id.toString();
	}

}
