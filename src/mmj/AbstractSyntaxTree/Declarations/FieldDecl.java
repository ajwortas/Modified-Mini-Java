package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.Tools.Factory;
import mmj.Tools.Exceptions.CounterException;

public class FieldDecl extends MemberDecl {

	public Expression init;
	public final int fieldID;
	
	public FieldDecl(MemberDecl md, SourcePosition posn) {
		super(md, posn);
		int id;
		try {
			id = Factory.getNextFieldID();
		} catch (CounterException e) {
			id=0;
			e.printStackTrace();
		}
		fieldID=id;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitFieldDeclaration(this);
	}

}
