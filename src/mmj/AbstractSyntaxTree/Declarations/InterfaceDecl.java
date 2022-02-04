package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.IdRefList;
import mmj.AbstractSyntaxTree.Lists.InterfaceMethodDeclList;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.AbstractSyntaxTree.TypeDenoters.ClassType;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.SyntacticAnalyzer.Token;
import mmj.SyntacticAnalyzer.TokenKind;

public class InterfaceDecl extends Declaration{
	public final InterfaceMethodDeclList methodList;
	public final IdRefList extended;
	public int classID=-1;
	public InterfaceDecl(String n, IdRefList extended, InterfaceMethodDeclList imdl, SourcePosition sp) {
		super(n, new ClassType(new Identifier(new Token(TokenKind.Class,n,sp)),sp), sp);
		methodList=imdl;
		this.extended=extended;
	}
	@Override
	public void visit(ASTVisitor v) {
		v.visitInterfaceDeclaration(this);
	}
}
