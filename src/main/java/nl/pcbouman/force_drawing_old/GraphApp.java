package nl.pcbouman.force_drawing_old;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Windowed version of the Graph Applet
 * 
 * @author Paul Bouman
 */
public class GraphApp extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2772510951682215232L;

	/**
	 * Create new Graph Application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GraphApp app = new GraphApp();
		app.setVisible(true);
	}

	/**
	 * Creates the Window for the Graph Application
	 */
	public GraphApp() {
		super();
		setSize(600, 600);
		setTitle("Seminar Graph Drawing - Force Based Drawing");
		getContentPane().add(new GraphApplet(), BorderLayout.CENTER);
		center();
		validate();
		addWindowListener(this);
	}

	/**
	 * Centers the window
	 * 
	 */
	public void center() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle frame = getBounds();
		setLocation((screen.width - frame.width) / 2,
				(screen.height - frame.height) / 2);
	}

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
}
