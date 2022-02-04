package mmj.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadPool {

	public static final String threadNaming = "Helper Thread ";
	private final List<Thread> threads;
	private final ArrayBlockingQueue<Task> queue;
	private final CompilerReporter reporter;
	
	public ThreadPool() {
		reporter = Factory.getCompilerReporter();
		threads = new ArrayList<Thread>();
		queue = new ArrayBlockingQueue<Task>(10);
	}
	
	public void setMinimumNumThreads(int num) {
		while(threads.size()<num) {
			String threadName = threadNaming+threads.size();
			Thread t = new Thread(new TaskRunnable(this,threadName));
			t.setName(threadName);
			t.run();
			threads.add(t);
		}
	}
	
	protected synchronized Task getNextTask() throws InterruptedException {
		return queue.take();
	}
	
	public void addTask(Task task) {
		queue.add(task);
	}
	
	
	public void terminateThreads() {
		for(Thread t:threads) 
			t.interrupt();
	}
	
}
