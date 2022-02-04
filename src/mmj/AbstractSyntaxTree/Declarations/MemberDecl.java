package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Visibility;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public class MemberDecl extends FinalableDeclaration {

    public final Visibility visibility;
    public final boolean isStatic;
    
    public MemberDecl(Visibility v, boolean isFinal, boolean isStatic, TypeDenoter mt, String name, SourcePosition posn) {
        super(name, mt, isFinal, posn);
        visibility = v;
        this.isStatic = isStatic;
       
    }
    
    public MemberDecl(MemberDecl md, SourcePosition posn){
    	super(md.name, md.type, md.isFinal, posn);
    	visibility = md.visibility;
    	this.isStatic = md.isStatic;
    }

	@Override
	public void visit(ASTVisitor v) {}

}
