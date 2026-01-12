package game;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Frame extends JFrame implements RefreshHandler {

	public static final int FPS = 120;
	public static int WIDTH = 1600, HEIGHT = 1000;
	protected int x, y;
	Panel p;
	
	public Frame() {
		super("Game with AI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) screenSize.getWidth();
		int h = (int) screenSize.getHeight();
		x = (w-WIDTH) / 2;
		y = (h-HEIGHT) / 2;
		
		setBounds(x, y, WIDTH, HEIGHT);
		
		p = new Panel(this);
		add(p);
		
		setVisible(true);
	}

	@Override
	public void refresh() {
		repaint();
	}
}
