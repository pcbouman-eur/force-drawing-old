package nl.pcbouman.force_drawing_old;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Class for displaying the Graph
 * 
 * @author Paul Bouman
 * 
 */
public class GraphPainter extends Canvas implements MouseListener,
		MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2742110662653159178L;

	private Graph paintGraph;

	private boolean showForces;

	private int xScale = 10;

	private int yScale = 10;

	private int xShift = 20;

	private int yShift = 20;

	private int nodeD = 3;

	private double[] mins, maxs;

	private boolean mode3D = false;

	private boolean rescale = true;

	private int sourceX, sourceY;

	private Node currentNode;

	private Edge currentEdge;

	/**
	 * Creates a Painting Canvas for a Graph
	 * @param g The graph to be drawn
	 */
	public GraphPainter(Graph g) {
		super();
		paintGraph = g;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * @return the Graph Being Painted
	 */
	public Graph getPaintGraph() {
		return paintGraph;
	}

	/**
	 * @return if Forces are being drawn
	 */
	public boolean getShowForces() {
		return showForces;
	}

	/**
	 * When the mouse is clicked, the selected node is moved
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() == MouseEvent.BUTTON1_MASK && currentNode != null) {
			double x = e.getX();
			x -= xShift;
			x /= xScale;
			x += mins[0];
			double y = e.getY();
			y -= yShift;
			y /= yScale;
			y += mins[1];
			currentNode.coors[0] = x;
			currentNode.coors[1] = y;
			repaint();
		}

	}

	/**
	 * When the mouse is dragged, the graph should be rotated
	 */
	public void mouseDragged(MouseEvent me) {
		if (me.getModifiers() == MouseEvent.BUTTON3_MASK && mode3D) {
			double yRot = ((double) (sourceX - me.getX()) / 90);
			double xRot = ((double) (sourceY - me.getY()) / 90);

			sourceX = me.getX();
			sourceY = me.getY();

			// Calculate X-Axis Rotation Matrix
			Mat4D xMatrix = new Mat4D();
			xMatrix.mat[0][0] = 1;
			xMatrix.mat[3][3] = 1;
			xMatrix.mat[1][1] = Math.cos(xRot);
			xMatrix.mat[2][2] = Math.cos(xRot);
			xMatrix.mat[2][1] = Math.sin(xRot);
			xMatrix.mat[1][2] = -Math.sin(xRot);

			// Calculate Y-Axis Rotation Matrix
			Mat4D yMatrix = new Mat4D();
			yMatrix.mat[1][1] = 1;
			yMatrix.mat[3][3] = 1;
			yMatrix.mat[0][0] = Math.cos(yRot);
			yMatrix.mat[2][0] = -Math.sin(yRot);
			yMatrix.mat[0][2] = Math.sin(yRot);
			yMatrix.mat[2][2] = Math.cos(yRot);

			for (Node n : paintGraph.getNodes()) {
				xMatrix.transform(n);
				yMatrix.transform(n);
			}
			repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent arg0) {}

	// When the mouse is pressed, remember the starting position (for dragging)
	public void mousePressed(MouseEvent arg0) {
		sourceX = arg0.getX();
		sourceY = arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {}

	public void paint(Graphics g) {
		
		// Recalculate the scaling, if autoscaling is enabled
		if (rescale) {
			mins = paintGraph.getMins();
			maxs = paintGraph.getMaxs();
			double d0 = maxs[0] - mins[0];
			double d1 = maxs[1] - mins[1];
			xScale = (int) ((getWidth() - xShift) / d0);
			yScale = (int) ((getHeight() - yShift) / d1);
		}

		// Draw the edges
		g.setColor(Color.BLUE);
		Graphics2D g2d = (Graphics2D) g;
		Stroke oldStroke = g2d.getStroke();
		for (Edge e : paintGraph.getEdges()) {
			int x1 = xShift + (int) (xScale * (e.getFrom().coors[0] - mins[0]));
			int y1 = yShift + (int) (yScale * (e.getFrom().coors[1] - mins[1]));
			int x2 = xShift + (int) (xScale * (e.getTo().coors[0] - mins[0]));
			int y2 = yShift + (int) (yScale * (e.getTo().coors[1] - mins[1]));

			if (e == currentEdge) {
				g.setColor(Color.CYAN);
				g2d.setStroke(new BasicStroke(4));
			}
			g.drawLine(x1, y1, x2, y2);
			if (e == currentEdge) {
				g.setColor(Color.BLUE);
				g2d.setStroke(oldStroke);
			}
		}

		// Draw the nodes
		g.setColor(Color.RED);
		for (Node n : paintGraph.getNodes()) {
			int x = xShift + (int) (xScale * (n.coors[0] - mins[0]));
			int y = yShift + (int) (yScale * (n.coors[1] - mins[1]));

			if (!n.movable)
				g.setColor(Color.YELLOW);
			if (n == currentNode) {
				g.setColor(Color.CYAN);
				g.fillOval(x - (2 * nodeD), y - (2 * nodeD), 4 * nodeD,
						4 * nodeD);
			} else {
				g.fillOval(x - nodeD, y - nodeD, 2 * nodeD, 2 * nodeD);
			}
			if (n == currentNode || !n.movable)
				g.setColor(Color.RED);
		}

		// Draw the forces, if enabled
		if (showForces) {
			g.setColor(Color.GREEN);
			for (Node n : paintGraph.getNodes()) {
				int x = xShift + (int) (xScale * (n.coors[0] - mins[0]));
				int y = yShift + (int) (yScale * (n.coors[1] - mins[1]));
				int xf = x + (int) (xScale * n.forces[0]);
				int yf = y + (int) (yScale * n.forces[1]);
				g.drawLine(x, y, xf, yf);
			}
		}
	}

	/**
	 * Sets wheter or not 3D mode should be enabled
	 * @param b 3D mode
	 */
	public void set3D(boolean b) {
		mode3D = b;
	}

	/**
	 * Changes the current edge (for displaying)
	 * @param currentEdge
	 */
	public void setCurrentEdge(Edge currentEdge) {
		this.currentEdge = currentEdge;
	}

	/**
	 * Change the current node (for displaying & editing)
	 * @param currentNode
	 */
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	/**
	 * Changes the graph being displayed
	 * @param paintGraph the new graph to display
	 */
	public void setPaintGraph(Graph paintGraph) {
		this.paintGraph = paintGraph;
	}

	/**
	 * Change wheter or not the graph should be rescaled automatically
	 * @param b rescaling
	 */
	public void setRescale(boolean b) {
		rescale = b;
	}

	/**
	 * @param showForces
	 *            the showForces to set
	 */
	public void setShowForces(boolean showForces) {
		this.showForces = showForces;
	}

}
