package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public class SuperRef extends Reference{

	public SuperRef(SourcePosition sp) {
		super(sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitSuperReference(this);
	}

}
