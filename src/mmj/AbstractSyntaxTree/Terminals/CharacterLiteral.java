package mmj.AbstractSyntaxTree.Terminals;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.SyntacticAnalyzer.Token;

public class CharacterLiteral extends Terminal{
	public CharacterLiteral(Token t) {
		super(t);
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitCharacterLiteral(this);
	}
}
