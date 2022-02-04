package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.MethodComponents;
import mmj.AbstractSyntaxTree.Lists.ParameterList;
import mmj.AbstractSyntaxTree.Lists.StatementList;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.Tools.Exceptions.CounterException;

public class MethodDecl extends MemberDecl {
	
	public final StatementList stmtList;
	public final MethodComponents mt;
	public final ParameterList paraList;
	
	public MethodDecl(MemberDecl md, ParameterList paraL, StatementList stmtL, SourcePosition posn) throws CounterException {
		super(md,posn);
		stmtList=stmtL;
		paraList=paraL;
		mt = new MethodComponents(md.type,paraL,stmtL,posn);
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitMethodDeclaration(this);
	}

}
