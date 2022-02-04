package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Lists.FieldDeclList;
import mmj.AbstractSyntaxTree.Lists.IdRefList;
import mmj.AbstractSyntaxTree.Lists.MethodDeclList;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.AbstractSyntaxTree.TypeDenoters.ClassType;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.SyntacticAnalyzer.Token;
import mmj.SyntacticAnalyzer.TokenKind;
import mmj.Tools.Factory;
import mmj.Tools.KeyConstants;
import mmj.Tools.Exceptions.CounterException;

public class ClassDecl extends FinalableDeclaration{

	public final FieldDeclList fieldList;
	public final MethodDeclList methodList,constructors;
	public final IdRefList parents,inherits;
	public final int classID;
	public boolean listsIncludeInheritance=false;
	
	public ClassDecl(String n, boolean isFinal, FieldDeclList fdl, MethodDeclList mdl, IdRefList extended, IdRefList inherits, SourcePosition sp) throws CounterException {
		super(n, new ClassType(new Identifier(new Token(TokenKind.Class,n,sp)),sp), isFinal, sp);
		fieldList=fdl;
		methodList = mdl;
		parents=extended;
		this.inherits=inherits;
		constructors=new MethodDeclList();
		for(MethodDecl md:methodList) 
			if(md.name.equals(KeyConstants.constructor))
				constructors.add(md);
		
		classID = Factory.getNextClassID();
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitClassDeclaration(this);
	}

}
