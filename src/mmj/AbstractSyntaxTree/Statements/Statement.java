package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class Statement extends ASTComponent{

	public Statement(SourcePosition sp) {
		super(sp);
	}

}
