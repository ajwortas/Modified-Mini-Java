package mmj.Tools;

import java.util.ArrayList;
import java.util.List;

public class CompilerReporter {

	
	private List<String> tracing;
	private List<String> errors;
	private final boolean enableTracing;
	
	public CompilerReporter() {
		this(true);
	}
	
	public CompilerReporter(boolean enableTracing) {
		tracing = new ArrayList<String>();
		errors = new ArrayList<String>();
		this.enableTracing=enableTracing;
	}
	
	//arraylist is not thread safe
	private synchronized void trace(String trace) {
		tracing.add(trace);
	}
	
	public void addTrace(String trace) {
		if(!enableTracing) return;
		trace(trace);
	}
	
	public void addThreadedTrace(String trace) {
		if(!enableTracing) return;
		String thread = Thread.currentThread().getName();
		trace(thread+" "+trace);
	}
	
	public void printTraces() {
		for(String trace:tracing)
			if(trace.contains(ThreadPool.threadNaming))
				System.out.println("### "+trace);
			else
				System.out.println("*** "+trace);
		tracing = new ArrayList<String>();
	}
	
	public void clearCurrentTraces() {
		tracing = new ArrayList<String>();
	}
	
	public synchronized void reportError(String err) {
		errors.add(err);
	}
	
	public boolean assessErrors() {
		for(String str:errors)
			System.err.println(">>> "+str);
		return errors.size()!=0;
	}
	
	public boolean hasErrors() {
		return errors.size()>0;
	}
	
}
