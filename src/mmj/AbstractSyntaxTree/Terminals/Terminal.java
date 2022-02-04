package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.Token;
import mmj.SyntacticAnalyzer.TokenKind;

public abstract class Terminal extends ASTComponent{

	public TypeDenoter evalType;
	public final TokenKind kind;
	public final String spelling;
	
	public Terminal(Token t) {
		super(t.posn);
		kind = t.kind;
		spelling = t.spelling;
	}

}
