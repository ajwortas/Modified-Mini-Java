package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.AbstractSyntaxTree.Terminals.Assignment;
import mmj.SyntacticAnalyzer.SourcePosition;

public class AssignStmt extends Statement {

	public final Reference ref;
	public final Expression expr;
	public final Assignment assignment;
	
	public AssignStmt(Reference reference, Assignment a, Expression expression, SourcePosition sp) {
		super(sp);
		ref=reference;
		expr=expression;
		assignment=a;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitAssignStatement(this);
	}

}
