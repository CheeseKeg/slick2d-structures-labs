package lab4_AirportSimulation;

public class Runway {

	private boolean vacant, landing;
	private int timeUntilVacant, occupationTime;
	private Airplane currentAirplane;
	
	
	public Runway(int occupationTime) {
		this.occupationTime = occupationTime;
		setVacant();
	}
	
	public void setVacant() {
		if (vacant)
			currentAirplane = null;
		else
			vacant = true;
		
		timeUntilVacant = 0;
	}
	
	public Airplane getCurrentAirplane() {
		return currentAirplane;
	}
	
	public void setOccupied(Airplane currentAirplane, boolean landing) {
		this.landing = landing;
		this.currentAirplane = currentAirplane;
		vacant = false;
		timeUntilVacant = occupationTime;
	}
	
	public int tick() {
		if (timeUntilVacant > 0) {
			--timeUntilVacant;
			
		}
		
		if (timeUntilVacant == 0) {
			setVacant();
		}
		
		return timeUntilVacant;
	}
	
	public boolean isVacant() {
		return vacant;
	}
	
	public String toString() {
		return vacant ? "The runway is vacant" : "The runway is occupied; " + currentAirplane + " is " + (landing ? "landing" : "taking off");
	}
}
