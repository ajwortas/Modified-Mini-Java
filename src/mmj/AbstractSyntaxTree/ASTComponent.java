package mmj.AbstractSyntaxTree;

import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class ASTComponent {

	public SourcePosition posn;
	public ASTComponent(SourcePosition sp) {
		posn=sp;
	}
	
	public abstract void visit(ASTVisitor v);
	
}
