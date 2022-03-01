package nl.pcbouman.force_drawing_old;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel for editing a Graph
 * @author Paul Bouman
 *
 */
public class GraphEditPane extends JPanel implements ListSelectionListener,
		ChangeListener, KeyListener {
	/**
	 * Inner class for the Edge MVC-Model
	 * @author Paul Bouman
	 *
	 */
	private class EdgeListModel implements ListModel {

		public void addListDataListener(ListDataListener arg0) {
		}

		public Object getElementAt(int arg0) {
			return editGraph.getEdges().get(arg0);
		}

		public int getSize() {
			return editGraph.getEdges().size();
		}

		public void removeListDataListener(ListDataListener arg0) {
		}

	}

	/**
	 * Inner class for the Node MVC-model
	 * @author Paul Bouman
	 *
	 */
	private class NodeListModel implements ListModel {

		public void addListDataListener(ListDataListener l) {
		}

		public Object getElementAt(int index) {
			return editGraph.getNodes().get(index);
		}

		public int getSize() {
			return editGraph.getNodes().size();
		}

		public void removeListDataListener(ListDataListener l) {
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Graph editGraph;

	private GraphApplet parent;

	private JList nodeList;

	private JList edgeList;

	private JCheckBox nodeMovable;

	private JTextField nodeLabel, edgeWidth;

	private JLabel xCoor, yCoor, xForce, yForce;

	private JScrollPane show1, show2;

	/**
	 * Constructor for editing a Graph
	 * @param g The graph to be edited
	 * @param ga The parent Graph Display Module
	 */
	public GraphEditPane(Graph g, GraphApplet ga) {
		super();
		editGraph = g;
		init();
		parent = ga;
	}

	/**
	 * Returns the current edge
	 * @return the current edge
	 */
	public Edge getCurrentEdge() {
		return (Edge) edgeList.getSelectedValue();
	}

	/**
	 * Returns the current node
	 * @return the current node
	 */
	public Node getCurrentNode() {
		return (Node) nodeList.getSelectedValue();
	}

	/**
	 * Returns the graph the is currently being edites
	 * @return the graph being edited
	 */
	public Graph getEditGraph() {
		return editGraph;
	}

	private void init() {
		setLayout(new GridLayout(2, 1));

		// Create the panel for node editing
		JPanel nodePanel = new JPanel(new BorderLayout());
		nodeList = new JList(new NodeListModel());
		nodeList.addListSelectionListener(this);

		nodeMovable = new JCheckBox("Movable");
		nodeMovable.addChangeListener(this);

		show1 = new JScrollPane(nodeList);
		nodePanel.add(show1, BorderLayout.CENTER);
		JPanel nodeProps = new JPanel(new GridLayout(3, 2));
		nodeProps.add(nodeMovable);
		nodeLabel = new JTextField(3);
		nodeLabel.addKeyListener(this);
		nodeProps.add(nodeLabel);
		xCoor = new JLabel("x:");
		yCoor = new JLabel("y:");
		xForce = new JLabel("xF:");
		yForce = new JLabel("yF:");
		nodeProps.add(xCoor);
		nodeProps.add(yCoor);
		nodeProps.add(xForce);
		nodeProps.add(yForce);

		nodePanel.add(nodeProps, BorderLayout.SOUTH);
		add(nodePanel);

		// Create the panel for edge editing
		JPanel edgePanel = new JPanel(new BorderLayout());

		edgeList = new JList(new EdgeListModel());
		edgeList.addListSelectionListener(this);

		show2 = new JScrollPane(edgeList);
		edgePanel.add(show2, BorderLayout.CENTER);

		JPanel edgeProps = new JPanel(new GridLayout(1, 2));
		edgeProps.add(new JLabel("Length:"));
		edgeWidth = new JTextField(4);
		edgeWidth.addKeyListener(this);
		edgeProps.add(edgeWidth);
		edgePanel.add(edgeProps, BorderLayout.SOUTH);

		add(edgePanel);
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == nodeLabel) {
			if (nodeList.getSelectedValue() != null) {
				Node n = (Node) nodeList.getSelectedValue();
				n.id = nodeLabel.getText();
			}
		}
		if (e.getSource() == edgeWidth) {
			if (edgeList.getSelectedValue() != null) {
				Edge edge = (Edge) edgeList.getSelectedValue();
				try {
					edge.setWeight(Double.parseDouble(edgeWidth.getText()));
				} catch (Exception ex) {
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Change the graph that should be edited
	 * @param editGraph the new graph to edit
	 */
	public void setEditGraph(Graph editGraph) {
		this.editGraph = editGraph;
		nodeList.validate();
		edgeList.validate();
		show1.validate();
		show2.validate();
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == nodeMovable) {
			if (nodeList.getSelectedValue() != null) {
				Node n = (Node) nodeList.getSelectedValue();
				n.movable = nodeMovable.isSelected();
			}
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == nodeList) {
			parent.doRepaint();
			if (nodeList.getSelectedValue() != null) {
				Node n = (Node) nodeList.getSelectedValue();
				nodeMovable.setSelected(n.movable);
				nodeLabel.setText("" + n.id);

				NumberFormat f = new DecimalFormat("##.#####");
				xCoor.setText("x:" + f.format(n.coors[0]));
				yCoor.setText("y:" + f.format(n.coors[1]));
				xForce.setText("xF:" + f.format(n.forces[0]));
				yForce.setText("yF:" + f.format(n.forces[1]));
			}
		}
		if (e.getSource() == edgeList) {
			parent.doRepaint();
			if (edgeList.getSelectedValue() != null) {
				Edge edge = (Edge) edgeList.getSelectedValue();
				edgeWidth.setText("" + edge.getWeight());
			}
		}
	}
}
