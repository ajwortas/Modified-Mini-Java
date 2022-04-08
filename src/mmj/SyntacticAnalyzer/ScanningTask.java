package mmj.SyntacticAnalyzer;

import mmj.Tools.Task;

public class ScanningTask implements Task{

	private final Scanner scan;
	private Token current;
	
	public ScanningTask(Scanner s) {
		scan=s;
	}
	
	public synchronized Token getCurrentResult() throws InterruptedException {
		
		if(current==null) {
			this.wait();
		}
		Token retval = current;
		current=null;
		
		return retval;
	}
	
	@Override
	public synchronized void runTask() {
		current = scan.scan();
		this.notify();
	}

	@Override
	public String getTaskName() {
		return "Scanning Task";
	}

}
