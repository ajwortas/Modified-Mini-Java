package mmj.AbstractSyntaxTree.TypeDenoters;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.MethodComponents;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.AbstractSyntaxTree.Lists.TypeList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class MethodType extends TypeDenoter {

	public final TypeDenoter retType;
	public final TypeList typeList;
	
	public MethodType(TypeDenoter retType, TypeList typeL, SourcePosition sp) {
		super(TypeKind.Method, sp);
		this.retType=retType;
		typeList=typeL;
	}

	public MethodType(MethodComponents curriedMethod, int curriedArgs, SourcePosition sp) {
		super(TypeKind.Method, sp);
		typeList = new TypeList();
		retType=curriedMethod.retType;
		for(int i=curriedArgs-1;i<curriedMethod.paraList.size();i++) 
			typeList.add(curriedMethod.paraList.get(i).type);
	}
	
	
	@Override
	public void visit(ASTVisitor v) {
		v.visitMethodType(this);
	}

}
