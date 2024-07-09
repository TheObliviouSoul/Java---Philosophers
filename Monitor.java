public class Monitor {
	private enum State {THINKING, EATING, TALKING}
	private State[] state;
	private boolean[] shakers = new boolean[2]; // Represents the availability of two pepper shakers
	private int numPhilosophers;
	private int[] philosopherPriority; // Priority for eating
	private int talkingPriority = 0; // Priority for talking to prevent starvation

	public Monitor(int numPhilosophers) {
		this.numPhilosophers = numPhilosophers;
		state = new State[numPhilosophers];
		philosopherPriority = new int[numPhilosophers];
		for (int i = 0; i < numPhilosophers; i++) {
			state[i] = State.THINKING;
			philosopherPriority[i] = 0; // Initial priority for eating
		}
		shakers[0] = false;
		shakers[1] = false;
	}

	public synchronized void pickUpChopsticks(int piTID) {
		int index = piTID - 1; // Adjust for 0-based indexing
		philosopherPriority[index] = getMaxPriority() + 1; // Increase priority to prevent starvation
		while (!canPickUpChopsticks(index)) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
		state[index] = State.EATING;
		requestShakers();
	}
	private int getMaxPriority() {
		int max = philosopherPriority[0];
		for (int i = 1; i < philosopherPriority.length; i++) {
			if (philosopherPriority[i] > max) {
				max = philosopherPriority[i];
			}
		}
		return max;
	}
	private boolean canPickUpChopsticks(int index) {
		int left = (index + numPhilosophers - 1) % numPhilosophers;
		int right = (index + 1) % numPhilosophers;
		// Check for deadlock avoidance condition for the last philosopher
		if (index == numPhilosophers - 1) {
			return state[right] != State.EATING && state[left] != State.EATING && state[index] != State.EATING && !isAnyoneTalking();
		} else {
			return state[left] != State.EATING && state[right] != State.EATING && state[index] != State.EATING && !isAnyoneTalking();
		}
	}

	public synchronized void putDownChopsticks(int piTID) {
		int index = piTID - 1;
		state[index] = State.THINKING;
		releaseShakers();
		notifyAll();
	}

	public synchronized void requestShakers() {
		// Wait until there is at least one available shaker
		while (!(!shakers[0] || !shakers[1])) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		// Assign an available shaker to the philosopher
		if (!shakers[0]) {
			shakers[0] = true; // Mark as in use
		} else if (!shakers[1]) {
			shakers[1] = true; // Mark as in use
		}
	}

	// Method for a philosopher to release pepper shakers
	public synchronized void releaseShakers() {
		// Release the shakers
		shakers[0] = false;
		shakers[1] = false;
		notifyAll(); // Notify other philosophers that shakers are now available
	}

	public synchronized void requestTalk(int piTID) {
		int index = piTID - 1;
		// Update talking priority to current maximum + 1
		int currentTalkingPriority = ++talkingPriority;
		while (state[index] == State.TALKING || isAnyoneTalking() || currentTalkingPriority != talkingPriority) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
		state[index] = State.TALKING;
	}

	public synchronized void endTalk(int piTID) {
		int index = piTID - 1;
		if (state[index] == State.TALKING) {
			state[index] = State.THINKING;
			notifyAll(); // Notify others to check if they can talk or eat
		}
	}

	private int getMinEatingPriority() {
		int min = philosopherPriority[0];
		for (int i = 1; i < philosopherPriority.length; i++) {
			if (philosopherPriority[i] < min) {
				min = philosopherPriority[i];
			}
		}
		return min;
	}

	private boolean isAnyoneTalking() {
		for (State st : state) {
			if (st == State.TALKING) {
				return true;
			}
		}
		return false;
	}
}
