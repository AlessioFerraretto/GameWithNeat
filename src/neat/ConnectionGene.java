package neat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public class ConnectionGene implements Serializable {
	
	private int inNode;
	private int outNode;
	private float weight;
	private boolean expressed;
	private int innovation;
	
	private Point inPoint, outPoint;
	
	public ConnectionGene(int inNode, int outNode, float weight, boolean expressed, int innovation) {
		this.inNode = inNode;
		this.outNode = outNode;
		this.weight = weight;
		this.expressed = expressed;
		this.innovation = innovation;
	}
	
	public ConnectionGene(ConnectionGene con) {
		this.inNode = con.inNode;
		this.outNode = con.outNode;
		this.weight = con.weight;
		this.expressed = con.expressed;
		this.innovation = con.innovation;
	}

	public int getInNode() {
		return inNode;
	}

	public int getOutNode() {
		return outNode;
	}

	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float newWeight) {
		this.weight = newWeight;
	}

	public boolean isExpressed() {
		return expressed;
	}
	
	public void disable() {
		expressed = false;
	}

	public int getInnovation() {
		return innovation;
	}
	
	public ConnectionGene copy() {
		return new ConnectionGene(inNode, outNode, weight, expressed, innovation);
	}

	

	public void setInPoint(Point p) {
		this.inPoint = p;
	}
	
	public Point getnPoint() {
		return inPoint;
	}

	public void setInPoint(int x, int y) {
		setInPoint(new Point(x,y));
	}

	public void setOutPoint(Point p) {
		this.outPoint = p;
	}
	
	public Point getOutPoint() {
		return outPoint;
	}

	public void setOutPoint(int x, int y) {
		setOutPoint(new Point(x,y));
	}
	
	public void draw(Graphics g) {
		if(weight>0) {
			g.setColor(Color.green);
		} else {
			g.setColor(Color.red);
		}
		g.drawLine((int) inPoint.getX() + NodeGene.RADIUS/2,(int) inPoint.getY() + NodeGene.RADIUS/2,
				(int) outPoint.getX() + NodeGene.RADIUS/2,(int)  outPoint.getY() + NodeGene.RADIUS/2);
	}
	
}
