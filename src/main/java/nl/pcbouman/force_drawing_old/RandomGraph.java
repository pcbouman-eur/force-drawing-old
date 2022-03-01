package nl.pcbouman.force_drawing_old;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Utility Class for generation of Graphs
 * @author Paul Bouman
 *
 */
public abstract class RandomGraph {
	
	/**
	 * The default weight an edge should get
	 */
	public static int defaultWeight = 1;
	
	/**
	 * The number of dimensions the graphs should get
	 */
	public static int defaultDimensions = 3;
	
	/**
	 * Creates a random Graph, that is connected
	 * (There is an undirected path from every node, to every other node)
	 * @param nodes The number of nodes
	 * @param edgeChance The chance that an edge is added to the graph
	 * @return The random graph
	 */
	public static Graph randomConnected(int nodes,double edgeChance)
	{
		Graph g = new Graph();
		if (nodes < 1) return g;
		ArrayList<Node> ns = new ArrayList<Node>(nodes);
		ns.add(new Node(defaultDimensions));
		g.addNode(ns.get(0));
		
		for (int t=1; t < nodes; t++)
		{
			ns.add(new Node(defaultDimensions));
			g.addNode(ns.get(t));
			Edge e = new Edge(ns.get(t),ns.get(t-1),defaultWeight);
			g.addEdge(e);
		}
		
		for (int x=0; x < nodes; x++)
			for (int y=0; y < nodes; y++)
			{
				if (x!=y && x-1 != y && y-1 != x)
				{
					if (Math.random() < edgeChance)
					{
						Edge e = new Edge(ns.get(x),ns.get(y),defaultWeight);
						g.addEdge(e);
					}
				}
			}
		
		return g;
	}
	
	/**
	 * Creates a complete binary tree with a certain depth.
	 * The number of nodes is 2^depth
	 * @param depth The depth of the tree
	 * @return the generated tree
	 */
	public static Graph binTree(int depth)
	{
		Graph g = new Graph();
		Node n = new Node(defaultDimensions);
		LinkedList<Node> front = new LinkedList<Node>();
		front.add(n);
		g.addNode(n);
		for (int t=0; t < depth; t++)
		{
			LinkedList<Node> newFront = new LinkedList<Node>();
			for (Node v : front)
			{
				Node u1 = new Node(defaultDimensions);
				Node u2 = new Node(defaultDimensions);
				Edge e1 = new Edge(v,u1,defaultWeight);
				Edge e2 = new Edge(v,u2,defaultWeight);
				g.addNode(u1);
				g.addNode(u2);
				g.addEdge(e1);
				g.addEdge(e2);
				newFront.add(u1);
				newFront.add(u2);
			}
			front = newFront;
		}
		return g;
	}
	
	/**
	 * Creates a grid graph.
	 * The number of nodes is xdim * ydim
	 * @param xdim The amount of nodes in the x-axis
	 * @param ydim The amount of nodes in the y-axis
	 * @return The resulting graph
	 */
	public static Graph gridGraph(int xdim, int ydim)
	{
		Graph g = new Graph();
		Node [][] nodes = new Node[xdim][ydim];
		for (int x = 0; x < xdim; x++)
			for (int y=0; y < ydim; y++)
			{
				nodes[x][y] = new Node(defaultDimensions);
				nodes[x][y].coors[0] = Math.random()*xdim; 
				nodes[x][y].coors[1] = Math.random()*ydim;
				g.addNode(nodes[x][y]);
			}
		
		for (int x = 0; x < xdim - 1; x++)
			for (int y =0; y < ydim - 1; y++)
			{
				Edge e = new Edge(nodes[x][y],nodes[x+1][y],defaultWeight);
				g.addEdge(e);
				e = new Edge(nodes[x][y],nodes[x][y+1],1);
				g.addEdge(e);
			}
		
		for (int x = 0; x < xdim - 1; x++)
			g.addEdge(new Edge(nodes[x][ydim-1],nodes[x+1][ydim-1],defaultWeight));
		for (int y = 0; y < ydim - 1; y++)
			g.addEdge(new Edge(nodes[xdim-1][y],nodes[xdim-1][y+1],defaultWeight));
		
		return g;
	}
}
