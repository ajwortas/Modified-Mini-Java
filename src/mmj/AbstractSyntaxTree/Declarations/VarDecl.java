package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public class VarDecl extends LocalDecl{

	public VarDecl(String n, TypeDenoter t, boolean isFinal, SourcePosition sp) {
		super(n, t, isFinal, sp);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitVarDeclaration(this);
	}

}
