package creator;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import game.BaseActor;
import game.Block;
import game.Enemy;
import game.Stage;
import game.StageOne;

public class Panel extends JPanel implements PanelEvent {

	private Stage stage;

	private BaseActor selected;
	private Frame f;

	public Panel(Frame f) {
		super();
		this.f = f;
		stage  = new StageOne(0);
		Timer t = new Timer(60/1000, e->{
			f.refresh();
		});
		t.start();

		setFocusable(true);
		requestFocus();

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(selected == null) return;

				int x = (int) e.getX();
				int y = (int) e.getY();

				int i = x / Block.BLOCK_SIZE;
				int j = y / Block.BLOCK_SIZE;

				stage.sostituisci(i, j, selected);
				f.repaint();
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(selected == null) return;

				int x = (int) e.getX();
				int y = (int) e.getY();

				int i = x / Block.BLOCK_SIZE;
				int j = y / Block.BLOCK_SIZE;

				stage.sostituisci(i, j, selected);
				f.repaint();

			}
		});
	}

	public void paint(Graphics g) {
		Point p = f.getMousePosition();
		int x = (int) p.getX();
		int y = (int) p.getY();

		stage.draw(g);

		if(selected!=null) {
			selected.setX(x-Block.BLOCK_SIZE/2);
			selected.setY(y-Block.BLOCK_SIZE/2);
			selected.setStage(stage);
			if(selected instanceof Block) {
				((Block)selected).draw(g);
			} else {
				((Enemy)selected).draw(g);
			}
		}
	}

	@Override
	public void selected(BaseActor e) {
		selected  = e;

	}

	@Override
	public void printStage() {
		stage.printStage();
	}

	@Override
	public void printEnemies() {
		stage.printEnemies();
	}

}
