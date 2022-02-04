package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Declarations.VarDecl;
import mmj.AbstractSyntaxTree.References.Reference;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ForEachStmt extends Statement{
	public final VarDecl var;
	public final Reference ref;
	public final Statement body;
	public ForEachStmt(VarDecl var, Reference array, Statement stmt, SourcePosition sp) {
		super(sp);
		this.var=var;
		this.ref=array;
		this.body=stmt;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitForEachStatement(this);
	}
}
