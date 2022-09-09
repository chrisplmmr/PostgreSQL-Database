import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		// Start the GUI:
		// GUI frame = new GUI();
		// frame.setVisible(true);

		String location1 = "Carl's Jr, Las Vegas NV";
		String location2 = "Driftwood Coffee, Peoria AZ";
		Question1 q = new Question1(location1, location2);
		ArrayList<String> f = q.solve();

		System.out.println("\nSOLVED!\n");
		for (String i : f) {
			System.out.println(i);
		}
	}
}
