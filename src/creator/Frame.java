package creator;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import game.RefreshHandler;

public class Frame extends JFrame implements RefreshHandler, MousePositionEvent {

	public static final int FPS = 60;
	public static int WIDTH = 1600, HEIGHT = 630;
	protected int x, y;
	Panel p;
	ButtonPanel p2;
	
	public Frame() {
		super("Level creator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) screenSize.getWidth();
		int h = (int) screenSize.getHeight();
		x = (w-WIDTH) / 2;
		y = (h-HEIGHT) / 2;
		
		setBounds(x, y, WIDTH, HEIGHT);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));		
		
		p = new Panel(this);
		add(p);
		
		p2 = new ButtonPanel(p);
		add(p2);
		
		setVisible(true);
	}

	@Override
	public void refresh() {
		repaint();
	}
	
	public Point getMousePosition() {
		Point reference = getContentPane().getLocationOnScreen();
        int x = MouseInfo.getPointerInfo().getLocation().x-reference.x;
        int y = MouseInfo.getPointerInfo().getLocation().y-reference.y; 
        return new Point(x,y);
	}
}
