package lab4_AirportSimulation;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Airplane {
	
	private int flightNumber;
	private double x, y, destX, destY, divisor;
	
	public Airplane(int flightNumber, double x, double y, double destX, double destY, double divisor) {
		this.flightNumber = flightNumber;
		setPos(x, y);
		setDestPos(destX, destY);
		this.divisor = divisor;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDestPos(double x, double y) {
		destX = x;
		destY = y;
	}
	
	public void update(int delta) {
		x += (destX - x)/divisor;
		y += (destY - y)/(divisor/2);
	}
	
	public void render(Graphics g, Image img) {
		//g.setColor(Color.yellow);
		//g.drawRect((float)x, (float)y, 40, 40);
		
		img.draw((float)x, (float)y, img.getWidth(), img.getHeight());
		
		g.setColor(Color.white);
		g.drawString("" + flightNumber, (float)x + 5, (float)y - 20);
	}
	
	public String toString() {
		return "Flight " + flightNumber;
	}
	
}
