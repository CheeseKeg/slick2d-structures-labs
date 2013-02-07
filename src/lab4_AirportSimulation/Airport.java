package lab4_AirportSimulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Airport extends BasicGame {
	
	private static AppGameContainer app;
	
	private int /*duration,*/ currentUpdate, simulationDelay;
	private double newLandingProbability, newTakeoffProbability;
	private Random rand;
	private Queue<Airplane> airQueue, groundQueue;
	private Runway runway;
	private long lastUpdate;
	
	private Image airplaneImage, backgroundImage;
	
	private double runwayX, runwayY, takeoffX, takeoffY, landingX, landingY;
	
	public Airport(/*int duration,*/ double newLandingProbability, double newTakeoffProbability, int simulationDelay) {
		super("Airport Simulator");
		
		// -- AIRPORT SIMULATOR STUFF --
		
		//this.duration = duration;
		this.currentUpdate = 0;
		this.newLandingProbability = newLandingProbability;
		this.newTakeoffProbability = newTakeoffProbability;
		this.simulationDelay = simulationDelay;
		this.lastUpdate = System.currentTimeMillis();

		rand = new Random();
		
		airQueue = new LinkedList<Airplane>();
		groundQueue = new LinkedList<Airplane>();
		
		runway = new Runway(2);
		
		// --SLICK STUFF--
		
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/native/" + LWJGLUtil.getPlatformName());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode(750, 370, false);
			app.setVSync(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	// -- SLICK GAME METHOD OVERRIDES --
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setShowFPS(true);
		
		this.runwayX = -70;
		this.runwayY = 298;
		
		this.landingX = app.getWidth() - 400;
		this.landingY = 80;
		
		this.takeoffX = app.getWidth() - 400;
		this.takeoffY = 298;
		
		this.airplaneImage = new Image("lab4_AirportSimulation/images/plane.png");
		this.backgroundImage = new Image("lab4_AirportSimulation/images/background.png");
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		backgroundImage.draw();
		
		Airplane airplaneHandle;
		Iterator<Airplane> it;
		it = airQueue.iterator();
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.render(g, airplaneImage);
		}
		it = groundQueue.iterator();
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.render(g, airplaneImage);
		}
		airplaneHandle = runway.getCurrentAirplane();
		if (airplaneHandle != null)
			airplaneHandle.render(g, airplaneImage);
		
		g.setColor(Color.white);
		g.drawString("Airport Simulator", gc.getWidth() - 170, 10);
		g.drawString("Update: " + currentUpdate, gc.getWidth() - 130, gc.getHeight() - 30);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		rand.nextDouble();
		if (System.currentTimeMillis() - lastUpdate > simulationDelay) {
			lastUpdate = System.currentTimeMillis();
			runSimulation();
		}
		
		Airplane airplaneHandle;
		Iterator<Airplane> it;
		it = airQueue.iterator();
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.update(delta);
		}
		it = groundQueue.iterator();
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.update(delta);
		}
		airplaneHandle = runway.getCurrentAirplane();
		if (airplaneHandle != null)
			airplaneHandle.update(delta);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE)
			app.exit();
	}
	
	// -- ORIGINAL AIRPORT SIMULATOR METHODS --
	
	private void runSimulation() {
		System.out.println("Update: " + currentUpdate);
		currentUpdate++;
		
		Airplane airplaneHandle;
		
		// Randomly add planes
		for (int i = 0; i < 2; i++) {
			if (newLandingProbability >= Math.pow(rand.nextDouble(), i)) {
				airplaneHandle = new Airplane(generateAirplaneNumber(), app.getWidth(), landingY, landingX, landingY, 30*(simulationDelay/1000.0));
				airQueue.add(airplaneHandle);
				
				System.out.println("Now queued for landing: " + airplaneHandle);
			}
		}
		if (newTakeoffProbability >= rand.nextDouble()) {
			airplaneHandle = new Airplane(generateAirplaneNumber(), app.getWidth(), takeoffY, takeoffX, takeoffY, 30*(simulationDelay/1000.0));
			groundQueue.add(airplaneHandle);

			System.out.println("Now queued for takeoff: " + airplaneHandle);
		}
		
		// Load the runway
		if (runway.isVacant())
			if (!airQueue.isEmpty()) {
				runway.setOccupied(airQueue.poll(), true);
				runway.getCurrentAirplane().setDestPos(runwayX, runwayY);
				
				System.out.println("Now landing: " + runway.getCurrentAirplane());
			} else if (!groundQueue.isEmpty()) {
				runway.setOccupied(groundQueue.poll(), false);
				runway.getCurrentAirplane().setDestPos(runwayX, runwayY);
				
				System.out.println("Now taking off: " + runway.getCurrentAirplane());
			}
		
		// Set queued airplane destinations
		Iterator<Airplane> it;
		
		it = airQueue.iterator();
		int i = 0;
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.setDestPos(landingX + (airplaneImage.getWidth() + 10)*i, landingY);
			i++;
		}
		
		it = groundQueue.iterator();
		i = 0;
		while(it.hasNext()) {
			airplaneHandle = it.next();
			
			airplaneHandle.setDestPos(takeoffX + (airplaneImage.getWidth() + 10)*i, takeoffY);
			i++;
		}
		
		// Output information about the runway
		System.out.println(runway
				+ "\nNumber of planes waiting to land: " + airQueue.size()
				+ "\nNumber of planes waiting to take off: " + groundQueue.size());
		
		// Tick runway
		runway.tick();
		
		System.out.println();
	}
	
	private int generateAirplaneNumber() {
		return (int)Math.round(rand.nextDouble()*1000);
	}
	
}
