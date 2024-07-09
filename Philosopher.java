public class Philosopher extends BaseThread {
	private final Monitor monitor; // Monitor instance to control dining

	// Constructor
	public Philosopher(int id, Monitor monitor) {
		super(id);
		this.monitor = monitor;
	}

	// Helper method to simulate thinking
	private void think() {
		System.out.println("Philosopher " + this.getTID() + " is thinking.");
		try {
			// Maximum time a philosopher can think
			int MAX_THINKING_TIME = 1000;
			sleep((int) (Math.random() * MAX_THINKING_TIME));
		} catch (InterruptedException e) {
			System.err.println("Philosopher " + this.getTID() + " was interrupted while thinking.");
		}
	}

	// Helper method to simulate eating
	private void eat() {
		System.out.println("Philosopher " + this.getTID() + " is eating.");
		try {
			// Maximum time a philosopher can eat
			int MAX_EATING_TIME = 1000;
			sleep((int) (Math.random() * MAX_EATING_TIME));
		} catch (InterruptedException e) {
			System.err.println("Philosopher " + this.getTID() + " was interrupted while eating.");
		}
	}

	// Helper method to simulate talking
	private void talk() {
		System.out.println("Philosopher " + this.getTID() + " is talking.");
		saySomething();
		try {
			// Maximum time a philosopher can talk
			int MAX_TALKING_TIME = 1000;
			sleep((int) (Math.random() * MAX_TALKING_TIME));
		} catch (InterruptedException e) {
			System.err.println("Philosopher " + this.getTID() + " was interrupted while talking.");
		}
		System.out.println("Philosopher " + this.getTID() + " has finished talking.");
	}

	// The life cycle of the philosopher
	@Override
	public void run() {
		for (int i = 0; i < DiningPhilosophers.DINING_STEPS; i++) {
			monitor.pickUpChopsticks(this.getTID()); // Request to pick up forks
			eat(); // Philosopher is eating
			monitor.putDownChopsticks(this.getTID()); // Release the forks
			think(); // Philosopher is thinking
			// Optionally decide to talk
			if (Math.random() < 0.5) {
				monitor.requestTalk(this.getTID()); // Request permission to talk
				talk(); // Philosopher is talking
				monitor.endTalk(this.getTID()); // Indicate finished talking
			}
		}
	}

	public void saySomething()
	{
		String[] phrases =
				{
						"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
						"You know, true is false and false is true if you think of it",
						"2 + 2 = 5 for extremely large values of 2...",
						"If thee cannot speak, thee must be silent",
						"My number is " + getTID() + "."
				};

		System.out.println
				(
						"Philosopher " + getTID() + " says: " +
								phrases[(int)(Math.random() * phrases.length)]
				);
	}
}
