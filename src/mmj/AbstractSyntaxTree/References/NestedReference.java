package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class NestedReference extends Reference {

	public final Reference ref;
	
	public NestedReference(Reference nested, SourcePosition sp) {
		super(sp);
		ref=nested;
	}

	@Override
	public abstract void visit(ASTVisitor v);
}
