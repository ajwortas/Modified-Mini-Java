package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Visibility;
import mmj.AbstractSyntaxTree.Lists.ParameterList;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public class InterfaceMethodDecl extends Declaration {

	public final ParameterList paraList;
    public final Visibility visibility;
    public final boolean isStatic;
	
	public InterfaceMethodDecl(Visibility v, boolean isStatic, TypeDenoter mt, String name, ParameterList paraL, SourcePosition posn) {
        super(name, mt, posn);
        visibility = v;
        this.isStatic = isStatic;
		paraList=paraL;
	}

	public InterfaceMethodDecl(MemberDecl md, ParameterList paraL) {
        super(md.name, md.type, md.posn);
        visibility = md.visibility;
        this.isStatic = md.isStatic;
		paraList=paraL;
	}
	
	@Override
	public void visit(ASTVisitor v) {}

}
