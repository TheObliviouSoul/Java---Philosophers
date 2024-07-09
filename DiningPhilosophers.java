import java.util.Scanner;

public class DiningPhilosophers {
	public static final int DINING_STEPS = 1000;
	public static Monitor soMonitor = null;

	public static void main(String[] argv) {
		System.out.println("Welcome to the Dining Philosophers simulation.");

		Scanner scanner = new Scanner(System.in);
		int numPhilosophers = 0;

		// Prompt for a valid number of philosophers
		while (numPhilosophers < 3) {
			System.out.println("Please enter the number of philosophers (at least 3): ");
			while (!scanner.hasNextInt()) {
				System.out.println("\"" + scanner.next() + "\" is not an acceptable number. Please enter a positive number (bigger than or equal to 3): ");
			}
			numPhilosophers = scanner.nextInt();
			if (numPhilosophers < 3) {
				System.out.println("\"" + numPhilosophers + "\" is not an acceptable number. Please enter a positive number (bigger than or equal to 3): ");
			}
		}

		// Initialize monitor and philosophers
		soMonitor = new Monitor(numPhilosophers);
		Philosopher[] philosophers = new Philosopher[numPhilosophers];
		System.out.println(numPhilosophers + " philosophers came in for a dinner.");

		for (int i = 0; i < numPhilosophers; i++) {
			philosophers[i] = new Philosopher(i + 1, soMonitor);
			philosophers[i].start();
		}

		// Wait for all philosophers to finish
		for (Philosopher philosopher : philosophers) {
			try {
				philosopher.join();
			} catch (InterruptedException e) {
				System.err.println("The main thread was interrupted.");
			}
		}

		System.out.println("All philosophers have left. System terminates normally.");
		scanner.close();
	}
}
