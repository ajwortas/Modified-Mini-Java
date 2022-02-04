package mmj.SyntacticAnalyzer;

public class Token {

	public final TokenKind kind;
	public final String spelling;
	public final SourcePosition posn;
	
	public Token(TokenKind tt, String s, SourcePosition sp){
		kind=tt;
		spelling=s;
		posn=sp;
	}
	
	@Override
	public String toString() {
		return "'"+spelling+"' is of type: "+kind;
	}
	
}
