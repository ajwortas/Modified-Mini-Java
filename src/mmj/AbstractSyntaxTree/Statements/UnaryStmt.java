package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.AbstractSyntaxTree.Terminals.Operator;
import mmj.SyntacticAnalyzer.SourcePosition;

public class UnaryStmt extends Statement {

	public final Operator op;
	public final Reference ref;
	public UnaryStmt(Operator op, Reference ref, SourcePosition sp) {
		super(sp);
		this.op=op;
		this.ref=ref;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitUnaryStatement(this);
	}

}
