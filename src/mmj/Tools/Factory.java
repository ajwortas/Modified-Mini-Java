package mmj.Tools;

import mmj.ContextualAnalysis.InheritanceHelper;
import mmj.Tools.Exceptions.CounterException;

public class Factory {

	private static final Counter classCounter, methodCounter, fieldCounter;
	private final static CompilerReporter reporter;
	private final static InheritanceHelper helper;
	//Creates singletons
	static {
		reporter=new CompilerReporter();
		classCounter=new Counter();
		methodCounter=new Counter();
		fieldCounter=new Counter();
		helper = new InheritanceHelper();
	}
	
	public static int getNextClassID() throws CounterException {
		return classCounter.next();
	}
	public static int getNextMethodID() throws CounterException {
		return methodCounter.next();
	}
	public static int getNextFieldID() throws CounterException {
		return fieldCounter.next();
	}
	public static CompilerReporter getCompilerReporter() {
		return reporter;
	}
	public static InheritanceHelper getInheritanceHelper() {
		return helper;
	}
}
