package mmj.Tools;

public class Joiner {

	private final int expected;
	private int finished=0;
	
	public Joiner(int expected) {
		this.expected=expected;
	}
	
	public synchronized void finish() {
		finished++;
		if(expected==finished)
			notify();
	}
	
	public void join() throws InterruptedException {
		while(finished!=expected) 
			wait();
	}
}
