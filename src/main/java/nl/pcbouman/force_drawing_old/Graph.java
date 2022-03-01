package nl.pcbouman.force_drawing_old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Data structure for a Graph
 * @author Paul Bouman
 *
 */
public class Graph
{
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	
	/**
	 * Add an edge to this graph
	 * @param edge the edge to be added
	 */
	public void addEdge(Edge edge) {
		edges.add(edge);
		
	}
	
	/**
	 * Add a node to this graph
	 * @param node the node to be added
	 */
	public void addNode(Node node) {
		nodes.add(node);
		
	}
	
	/**
	 * Returns the list with edges
	 * @return the edges
	 */
	public LinkedList<Edge> getEdges()
	{
		return edges;
	}
	
	/**
	 * Returns a vector with the maximum value for each coordinate in the graph
	 * @return the maximum coordinates
	 */
	public double[] getMaxs() {
		double [] maxs = new double[nodes.get(0).coors.length];
		for (int t=0; t < maxs.length; t++)
			maxs[t] = Double.MIN_VALUE;
		for (Node n : nodes)
		{
			for (int t=0; t < maxs.length; t++)
				maxs[t] = Math.max(maxs[t], n.coors[t]);
		}
	
		return maxs;
	}
	
	/**
	 * Returns a vector with the minimum value for each coordinate in the graph
	 * @return the minimum coordinates
	 */
	public double [] getMins()
	{
		double [] mins = new double[nodes.get(0).coors.length];
		for (int t=0; t < mins.length; t++)
			mins[t] = Double.MAX_VALUE;
		for (Node n : nodes)
		{
			for (int t=0; t < mins.length; t++)
				mins[t] = Math.min(mins[t], n.coors[t]);
		}
	
		return mins;
	}
	
	/**
	 * Returns the list of nodes
	 * @return the list of nodes
	 */
	public ArrayList<Node> getNodes()
	{
		return nodes;
	}

	/**
	 * Write the graph to Standard Output
	 */
	public void printGraph()
	{
		int t=0; 
		for (Node n : nodes)
			System.out.println("v_{"+(t++)+"} = ("+n.coors[0]+","+n.coors[1]+")");
	}
	
	public Graph clone()
	{
		Graph res = new Graph();
		
		HashMap<Node,Node> map = new HashMap<Node,Node>();
		for (Node n : nodes)
		{
			Node nn = new Node(n.coors.length);
			for (int t=0; t < n.coors.length; t++)
				nn.coors[t] = n.coors[t];
			res.addNode(nn);
			map.put(n,nn);
			nn.org = n;
		}
		for (Edge e : edges)
		{
			Edge en = new Edge(map.get(e.getFrom()),map.get(e.getTo()), e.getWeight());
			res.addEdge(en);
		}
		return res;
	}
}
