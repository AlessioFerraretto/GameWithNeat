package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class Panel extends JPanel implements RefreshHandler {

	private Set<Integer> inputs;
	private RefreshHandler rh;
	private GameThread t;

	public Panel(RefreshHandler rh) {
		super();
		this.rh = rh;
		inputs = new HashSet<Integer>();

		t = new GameThread(this);

		setFocusable(true);
		requestFocus();		

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				synchronized (inputs) {
					inputs.remove(e.getKeyCode());
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				synchronized (inputs) {
					inputs.add(e.getKeyCode());
				}

			}
		});

		t.start();
	}

	public void paint(Graphics g) {
		if(t.getGameState().equals(GameState.LOSE)) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(Color.white);
			g.drawString("GAME OVER", 100, 100);
		} else if(t.getGameState().equals(GameState.WIN)) {
			g.setColor(Color.green);
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(Color.white);
			g.drawString("YOU WON", 100, 100);
		} else {
			t.getStage().draw(g);
		}
	}

	public void refresh() {
		rh.refresh();
	}

	public synchronized Set<Integer> getInputs() {
		return inputs;
	}

}
