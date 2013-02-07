package lab4_AirportSimulation;

public class Main {
	Airport airport;
	
	private Main() {
		airport = new Airport(0.35, 0.30, 2000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

}
