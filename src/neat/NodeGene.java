package neat;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public class NodeGene implements Serializable {

	static int RADIUS = 30;
	
	public enum TYPE {
		INPUT,
		HIDDEN,
		OUTPUT,
		;
	}
	
	private TYPE type;
	private int id;
	private Point p;
	
	public NodeGene(TYPE type, int id) {
		this.type = type;
		this.id = id;
	}
	
	public NodeGene(NodeGene gene) {
		this.type = gene.type;
		this.id = gene.id;
	}

	public TYPE getType() {
		return type;
	}
	

	public int getId() {
		return id;
	}
	
	public void setPoint(Point p) {
		this.p = p;
	}
	
	public Point getPoint() {
		return p;
	}

	public void setPoint(int x, int y) {
		setPoint(new Point(x,y));
	}

	public void draw(Graphics g) {
		g.drawOval((int) p.getX(),(int) p.getY(), RADIUS, RADIUS);
	}
}
