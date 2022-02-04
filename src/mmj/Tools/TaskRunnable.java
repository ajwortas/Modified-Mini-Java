package mmj.Tools;

public class TaskRunnable implements Runnable{

	private final ThreadPool pool;
	private final CompilerReporter reporter;
	private final String threadName;
	
	public TaskRunnable(ThreadPool p, String tn) {
		pool=p;
		reporter=Factory.getCompilerReporter();
		threadName=tn;
	}
	
	
	@Override
	public void run() {
		for(;;) 
			try {
				Task t = pool.getNextTask();
				reporter.addTrace(threadName+" has aquired task " + t.getTaskName());
				t.runTask();
				reporter.addTrace(threadName+" has finished task "+t.getTaskName());
			} catch (InterruptedException e) {
				reporter.addTrace("Thread "+threadName+" interruted while working on task. Terminating thread.");
				break;
			}
	}
}
