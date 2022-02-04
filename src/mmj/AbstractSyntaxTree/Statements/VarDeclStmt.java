package mmj.AbstractSyntaxTree.Statements;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Declarations.VarDecl;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.SyntacticAnalyzer.SourcePosition;

public class VarDeclStmt extends Statement{
	public final VarDecl decl;
	public final Expression expr;
	public VarDeclStmt(VarDecl decl, Expression expression, SourcePosition sp) {
		super(sp);
		this.decl=decl;
		expr=expression;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitVarDeclStatement(this);
	}

}
