package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.SyntacticAnalyzer.SourcePosition;

public class QualRef extends NestedReference {

	public final Identifier id;
	
	public QualRef(Identifier id, Reference ref, SourcePosition sp) {
		super(ref, sp);
		this.id=id;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitQualReference(this);
	}

}
