package mmj.Tools;

import mmj.Tools.Exceptions.CounterException;

public class Counter {
	private final int cap,start;
	private int count;
	public Counter(int start, int cap) {
		count=start;
		this.start=start;
		this.cap=cap;
	}
	public Counter(int start) {
		this(start,1<<16);
	}
	public Counter() {
		this(0);
	}
	
	private synchronized void modify(int num) {
		count+=num;
	}
	
	public int next() throws CounterException {
		if(count>=cap)
			throw new CounterException("Count: "+count+" is at or above the cap: "+cap);
		modify(1);
		return count-1;
	}
	public void undo() throws CounterException {
		if(count<start)
			throw new CounterException("Count: "+count+" is below the start: "+start);
		modify(-1);
	}
}
