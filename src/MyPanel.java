
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyPanel extends JPanel {

	private static final int UPDATE_FREQUENCY = 30;
	private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;

	Timer t;
	Controller c;
	Random r = new Random();

	/**
	 * Create the panel.
	 */
	public MyPanel(Point p) {
		c = new Controller(p);

		t = new Timer(UPDATE_FREQUENCY, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.computeNextFrame(p);
				repaint();
			}
		});

		t.start();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				switch (e.getButton()) {
				// left click
				case MouseEvent.BUTTON1:
					c.addFoodAtPoint(e.getPoint());
					break;
				// right click
				case MouseEvent.BUTTON3:
					c.addPoisonAtPoint(e.getPoint());
					break;
				default:
					break;
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
				switch (e.getKeyCode()) {
				// space bar pressed
				case KeyEvent.VK_SPACE:
					c.addAnt(p);
					break;
				// d-key pressed
				case KeyEvent.VK_D:
					c.setDebug(c.isDebug() ? false : true);
					break;
				default:
					// any other key pressses --> dont do anything
					break;
				}
			}
		});
	}

	public void paintComponent(Graphics g) {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		c.drawAll(g);
	}

}
