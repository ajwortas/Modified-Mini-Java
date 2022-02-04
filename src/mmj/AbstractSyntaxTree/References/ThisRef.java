package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ThisRef extends Reference{

	public ThisRef(SourcePosition sp) {
		super(sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitThisReference(this);
	}

}
