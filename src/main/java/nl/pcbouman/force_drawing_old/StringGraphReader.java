package nl.pcbouman.force_drawing_old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;


public class StringGraphReader
{
	
	public static void main(String [] args)
	{
		//transformGraph(new File(args[0]),new File(args[1]));
		transformGraph(new File("amicitia0703.dot"),new File("amicitia0703.xml"));
	}
	
	public static void transformGraph(File in, File out)
	{
		try
		{
			HashMap<String,Integer> indexMap = new HashMap<String,Integer>();
			HashMap<Integer,String> stringMap = new HashMap<Integer,String>();
			
			LinkedList<int[]> edgeList = new LinkedList<int []>();
			
			int id = 0;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in)));

			
			while (br.ready())
			{
				String line = br.readLine();
				String tokens[] = line.split(" -- ");
				String from = tokens[0].substring(1, tokens[0].length()-1);
				String to = tokens[1].substring(1, tokens[1].length()-1);
				
				Integer fnode = indexMap.get(from);
				Integer tnode = indexMap.get(to);
				
				if (fnode==null)
				{
					fnode = new Integer(id++);
					indexMap.put(from, fnode);
					stringMap.put(fnode,from);
				}
				if (tnode==null)
				{
					tnode = new Integer(id++);
					indexMap.put(to, tnode);
					stringMap.put(tnode,to);
				}
				
				int [] edge = new int[2];
				edge[0] = fnode;
				edge[1] = tnode;
				edgeList.add(edge);
			}
			
			PrintWriter pw = new PrintWriter(new FileOutputStream(out));
			/*pw.println("PIG:0");
			pw.println("Graph_A");
			for (int [] edge : edgeList)
			{
				int from = edge[0];
				int to = edge[1];
				pw.println(""+from+" "+to);
			}
			*/
			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			pw.println("<!--");
			
			for (Integer i : stringMap.keySet())
			{
				pw.println(""+i+" is "+stringMap.get(i));
			}
			
			pw.println("-->");
			
			pw.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
			pw.println("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			pw.println("  xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
			pw.println("    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
			pw.println("<key id=\"Pigale/version\" for=\"graph\" attr.name=\"Pigale version\" attr.type=\"string\">");
			pw.println("<default>1.2.12</default>");
			pw.println("</key>");
			pw.println("<key id=\"Pigale/V/16\" for=\"node\" attr.name=\"Coordinates\" attr.type=\"string\"/>");
			pw.println("<graph id=\"graph\" edgedefault=\"undirected\">");
			for (Integer i : stringMap.keySet())
			{
				pw.println("<node id=\"n"+i+"\">");
				pw.println("<data key=\"Pigale/V/16\">"+(int)(Math.random()*600)+","+(int)(Math.random()*600)+"</data>");
				pw.println("</node>");
			}
			for (int [] edge : edgeList)
			{
				pw.println("<edge source=\"n"+edge[0]+"\" target=\"n"+edge[1]+"\"/>");
			}
			pw.println("</graph>");
			pw.println("</graphml>");
			
			pw.flush();
			pw.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
	}
	
	public static Graph readGraph(File f, int dim)
	{
		Graph resGraph = new Graph();
		try
		{
			HashMap<String,Node> indexMap = new HashMap<String,Node>();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

			
			while (br.ready())
			{
				String line = br.readLine();
				String tokens[] = line.split(" -- ");
				String from = tokens[0].substring(1, tokens[0].length()-1);
				String to = tokens[1].substring(1, tokens[1].length()-1);
				
				Node fnode = indexMap.get(from);
				Node tnode = indexMap.get(to);
				
				if (fnode==null)
				{
					fnode = new Node(from,dim);
					indexMap.put(from, fnode);
					resGraph.addNode(fnode);
				}
				if (tnode==null)
				{
					tnode = new Node(to,dim);
					indexMap.put(to, tnode);
					resGraph.addNode(tnode);
				}
				
				resGraph.addEdge(new Edge(fnode,tnode,1.0));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resGraph;
	}

}
