package nl.pcbouman.force_drawing_old;

/**
 * Class that does the calculations of the forces in the graph
 * @author Paul Bouman
 *
 */
public class Forces {
	private Graph g;

	private double repelFactor = 1;

	/**
	 * Constructor for a forces object.
	 * @param graph The graph the forces should be calculated on
	 */
	public Forces(Graph graph) {
		g = graph;
	}

	/**
	 * Recalculates the forces in the graph.
	 *
	 */
	public void calculateForces() {
		// First reset all force vectors
		for (Node n : g.getNodes()) {
			n.forces = new double[n.coors.length];
			n.forceCount = 0;
		}

		// Calculate the attraction for all edges
		for (Edge e : g.getEdges()) {
			edgeAttract(e);
			e.getFrom().forceCount++;
			e.getTo().forceCount++;
		}

		// Calculate the repelling for each pair of nodes
		for (int x = 0; x < g.getNodes().size(); x++)
			for (int y = x + 1; y < g.getNodes().size(); y++) {
				Node v = g.getNodes().get(x);
				Node u = g.getNodes().get(y);
				nodeRepel(v, u);
				v.forceCount++;
				u.forceCount++;
			}

		// Normalise the forcevectors
		for (Node n : g.getNodes()) {
			for (int t = 0; t < n.forces.length; t++)
				if (n.forceCount != 0)
					n.forces[t] /= n.forceCount;
		}

	}

	/**
	 * Calculates the euclidian distance between two nodes
	 * @param v One node
	 * @param u The other node
	 * @return The euclidian distance
	 */
	public double dist(Node v, Node u) {
		double sum = 0;
		for (int t = 0; t < v.coors.length; t++) {
			sum += Math.pow(v.coors[t] - u.coors[t], 2);
		}

		double dist = Math.sqrt(sum);
		return dist;
	}

	/**
	 * Calculates the attraction values on both nodes of an edge and
	 * add the result to the forces vectors of these nodes.
	 * @param e The edge that should be attracted
	 */
	private void edgeAttract(Edge e) {
		Node u = e.getFrom();
		Node v = e.getTo();
		double dist = dist(u, v);

		double factor = Math.log(dist / e.getWeight());
		for (int t = 0; t < v.forces.length; t++) {
			if (dist == 0) {
				u.forces[t] += factor * 1;
				v.forces[t] += factor * -1;
			} else {
				u.forces[t] += factor * ((v.coors[t] - u.coors[t]) / dist);
				v.forces[t] += factor * ((u.coors[t] - v.coors[t]) / dist);
			}
		}
	}

	/**
	 * Returns the sum of the forces
	 * @return the sum of all forces in the graph
	 */
	public double getForceSum() {
		double result = 0;

		for (Node n : g.getNodes()) {
			result += n.getForce();
		}

		return result;
	}

	/**
	 * Improves the coordinates of the nodes, until either the number
	 * of steps is reached, or the improvements fall below the threshold.
	 * @param threshold The threshold of the minimum improvement amount
	 * @param step The maximum amount of steps
	 */
	public void improve(double threshold, int step) {
		calculateForces();
		double currentSum = getForceSum();
		int t = 0;
		while (currentSum > threshold && t < step) {
			moveVertices();
			calculateForces();
			currentSum = getForceSum();
			t++;
		}
		System.out.println("Current Sum: " + currentSum);
	}

	/**
	 * Moves all nodes according to the forces working on them
	 *
	 */
	public void moveVertices() {
		for (Node n : g.getNodes()) {
			if (n.movable) {
				for (int t = 0; t < n.coors.length; t++)
					n.coors[t] += n.forces[t];
			}
		}
	}

	/**
	 * Calculates the repel forces between two nodes
	 * @param v One node
	 * @param u The other node
	 */
	private void nodeRepel(Node v, Node u) {
		double dist = dist(v, u);
		double dist2 = dist * dist;

		double factor = repelFactor / dist2;
		for (int t = 0; t < v.forces.length; t++) {
			if (dist == 0) {
				u.forces[t] += factor * 1;
				v.forces[t] += factor * -1;
			} else {
				v.forces[t] += factor * ((v.coors[t] - u.coors[t]) / dist);
				u.forces[t] += factor * ((u.coors[t] - v.coors[t]) / dist);
			}
		}
	}
}
