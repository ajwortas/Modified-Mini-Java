package mmj.SyntacticAnalyzer;

public class SourcePosition {

	private final int l,c;
	
	public SourcePosition(int line, int col) {
		l=line;
		c=col;
	}
	
	@Override
	public String toString() {
		return "Line: "+l+" Col: "+c+" "; 
	}
	
}
