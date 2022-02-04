package mmj.AbstractSyntaxTree.TypeDenoters;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.SyntacticAnalyzer.SourcePosition;

public class ClassType extends TypeDenoter{

	public final Identifier className;
	
	public ClassType(Identifier name, SourcePosition sp) {
		super(TypeKind.Class, sp);
		className=name;
	}
	
	@Override
	public void visit(ASTVisitor v) {
		v.visitClassType(this);
	}

}
