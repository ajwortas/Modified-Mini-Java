package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Declarations.Declaration;
import mmj.SyntacticAnalyzer.Token;

public class Identifier extends Terminal{

	public Declaration decl;
	
	public Identifier(Token t) {
		super(t);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitIdentifier(this);
	}

}
