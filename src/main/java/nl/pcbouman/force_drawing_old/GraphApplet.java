package nl.pcbouman.force_drawing_old;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Applet for Force Based Drawing of 2D or 3D Graphs
 * @author Paul Bouman
 *
 */
public class GraphApplet extends JPanel implements ActionListener, Runnable,
		ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3051227619459179075L;

	private Graph g;

	private Forces f;

	private GraphPainter gp;

	private JTabbedPane panes;

	private JPanel draw;

	private JPanel graph;

	private GraphEditPane edit;

	//private JPanel file;

	private long sleepTime = 0;

	private JCheckBox rescale;

	private JCheckBox mode3D;

	private JTextField defaultWeight;

	private JButton step;

	private JButton stepn;

	private JButton improve;

	private JButton animate;

	private JButton forces;

	private JButton binTree;

	private JButton gridGraph;

	private JButton randomGraph;
	
	private JButton readFile;

	//private Node currentNode;

	public GraphApplet() {
		super();
		init();
	}

	// Process Input
	public void actionPerformed(ActionEvent e) {

		// Do a step
		if (e.getSource() == step) {
			animate.setText("Animate");
			f.calculateForces();
			f.moveVertices();
			g.printGraph();
			gp.repaint();
		}

		// Ask the user for an amount of steps
		if (e.getSource() == stepn) {
			try {
				String a = JOptionPane.showInputDialog("Amount of steps?");
				int i = Integer.parseInt(a);
				animate.setText("Animate");
				for (int t = 0; t < i; t++) {
					f.calculateForces();
					f.moveVertices();
				}
				gp.repaint();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid Number");
			}
		}

		// Change wether forces should be displayed or not
		if (e.getSource() == forces) {
			if (forces.getText().equals("Show Forces")) {
				gp.setShowForces(true);
				forces.setText("Hide Forces");
			} else {
				gp.setShowForces(false);
				forces.setText("Show Forces");

			}
			gp.repaint();
		}

		// Do an improvement run
		if (e.getSource() == improve) {
			animate.setText("Animate");
			f.improve(0.01, 10000);
			gp.repaint();
		}

		// Create a new binary tree
		if (e.getSource() == binTree) {
			changeRandomProperties();
			String s = JOptionPane
					.showInputDialog("Levels of Complete Binary Tree?");
			try {
				int i = Integer.parseInt(s);
				animate.setText("Animate");
				changeGraph(RandomGraph.binTree(i));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid number");
			}
		}

		// Create a new Grid Graph
		if (e.getSource() == gridGraph) {
			changeRandomProperties();
			String w = JOptionPane.showInputDialog("Width of Grid Graph?");
			String h = JOptionPane.showInputDialog("Height of Grid Graph?");
			try {
				int width = Integer.parseInt(w);
				int height = Integer.parseInt(h);
				animate.setText("Animate");
				changeGraph(RandomGraph.gridGraph(width, height));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid Number");
			}
		}

		// Create a new Connected Random Graph
		if (e.getSource() == randomGraph) {
			changeRandomProperties();
			String n = JOptionPane.showInputDialog("Amount of nodes?");
			String p = JOptionPane
					.showInputDialog("Chance that there is an edge between two non consecutive nodes?");
			try {
				int size = Integer.parseInt(n);
				double chance = Double.parseDouble(p);
				animate.setText("Animate");
				changeGraph(RandomGraph.randomConnected(size, chance));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid Number");
			}
		}
		
		// Read a Graph File
		if (e.getSource() == readFile)
		{
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(readFile);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				int d = 2;
				if (mode3D.isSelected())
					d = 3;
				Graph g = StringGraphReader.readGraph(file,d);
				changeGraph(g);
			}
		}

		// Animate the Graph
		if (e.getSource() == animate) {
			if (animate.getText().equals("Stop Animation")) {
				animate.setText("Animate");
			} else {
				try {
					String st = JOptionPane
							.showInputDialog("Sleeptime after each iteration in ms?");
					sleepTime = Long.parseLong(st);
					f.calculateForces();
					animate.setText("Stop Animation");
					Thread t = new Thread(this);
					t.start();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Invalid Number");
				}
			}
		}
	}

	/**
	 * Change the graph currently on Display
	 * @param gr the new graph to display
	 */
	private void changeGraph(Graph gr) {
		g = gr;
		if (g.getNodes().get(0).coors.length > 2) {
			gp.set3D(true);
		} else {
			gp.set3D(false);
		}
		gp.setPaintGraph(g);
		f = new Forces(g);
		f.calculateForces();
		edit.setEditGraph(gr);
		gp.repaint();
	}

	/**
	 * Updates the random graph properties in the static class
	 */
	private void changeRandomProperties() {
		if (mode3D.isSelected()) {
			RandomGraph.defaultDimensions = 3;
		} else {
			RandomGraph.defaultDimensions = 2;
		}
		try {
			int i = Integer.parseInt(defaultWeight.getText());
			RandomGraph.defaultWeight = i;
		} catch (Exception e) {
			defaultWeight.setText("" + RandomGraph.defaultWeight);
		}
	}

	/**
	 * Repaints the graph and updates the current nodes
	 *
	 */
	public void doRepaint() {
		gp.setCurrentNode(edit.getCurrentNode());
		gp.setCurrentEdge(edit.getCurrentEdge());
		gp.repaint();
	}

	/**
	 * Initiliase GUI
	 */
	public void init() {
		// Create initial graph
		g = RandomGraph.binTree(4);
		f = new Forces(g);
		f.calculateForces();
		gp = new GraphPainter(g);
		gp.set3D(true);

		// Create the Panel with Drawing Controls
		draw = new JPanel(new GridLayout(6, 1));

		step = new JButton("Step");
		step.addActionListener(this);
		draw.add(step);

		stepn = new JButton("Step n");
		stepn.addActionListener(this);
		draw.add(stepn);

		improve = new JButton("Improve");
		improve.addActionListener(this);
		draw.add(improve);

		animate = new JButton("Animate");
		animate.addActionListener(this);
		draw.add(animate);

		forces = new JButton("Show Forces");
		forces.addActionListener(this);
		draw.add(forces);

		rescale = new JCheckBox("Auto Rescale", true);
		rescale.addChangeListener(this);
		draw.add(rescale);

		// Create the Panel with Graph Creation controls
		graph = new JPanel(new GridLayout(6, 1));

		binTree = new JButton("Binary Tree");
		binTree.addActionListener(this);
		graph.add(binTree);

		gridGraph = new JButton("Grid Graph");
		gridGraph.addActionListener(this);
		graph.add(gridGraph);

		randomGraph = new JButton("Random Graph");
		randomGraph.addActionListener(this);
		graph.add(randomGraph);
		
		readFile = new JButton("Read File");
		readFile.addActionListener(this);
		graph.add(readFile);

		mode3D = new JCheckBox("3D Mode", true);
		graph.add(mode3D);

		JPanel weightpanel = new JPanel();
		weightpanel.add(new JLabel("Default weight: "));
		defaultWeight = new JTextField("1", 5);
		weightpanel.add(defaultWeight);
		graph.add(weightpanel);

		// Create the Panel with Graph Edit Controls
		edit = new GraphEditPane(g, this);

		// file = new JPanel();

		// Add the control panels to a tabbed pane
		panes = new JTabbedPane(JTabbedPane.BOTTOM);
		panes.add(draw, "Control");
		panes.add(graph, "Graph");
		panes.add(edit, "Edit");
		//panes.add(file,"File");

		// Create the Main GUI Layout
		this.setLayout(new BorderLayout());
		this.add(gp, BorderLayout.CENTER);
		this.add(panes, BorderLayout.EAST);

	}

	// Animation Thread
	public void run() {
		while (animate.getText().equals("Stop Animation")) {
			f.moveVertices();
			f.calculateForces();
			gp.repaint();
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Changes the Current Edge of the Graph
	 * @param e the new current edge
	 */
	public void setCurrentEdge(Edge e) {
		gp.setCurrentEdge(e);
	}

	/**
	 * Changes the Current Node of the Graph
	 * @param n the new current node
	 */
	public void setCurrentNode(Node n) {
		gp.setCurrentNode(n);
	}

	// Listener method for rescaling
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == rescale) {
			if (rescale.isSelected()) {
				gp.setRescale(true);
			} else {
				gp.setRescale(false);
			}
		}

	}

}
