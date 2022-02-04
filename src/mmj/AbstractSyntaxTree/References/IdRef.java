package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.SyntacticAnalyzer.SourcePosition;

public class IdRef extends Reference {

	public final Identifier id;
	
	public IdRef(Identifier id, SourcePosition sp) {
		super(sp);
		this.id=id;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitIDReference(this);
	}

}
