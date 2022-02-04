package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.AbstractSyntaxTree.Terminals.Assignment;
import mmj.SyntacticAnalyzer.SourcePosition;

public class IxAssignStmt extends AssignStmt {
	public IxAssignStmt(Reference reference, Assignment a, Expression assign, SourcePosition sp) {
		super(reference, a, assign, sp);
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitIxAssignStatement(this);
	}
}
