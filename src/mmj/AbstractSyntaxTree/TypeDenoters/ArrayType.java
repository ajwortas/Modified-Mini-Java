package mmj.AbstractSyntaxTree.TypeDenoters;


import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.AbstractSyntaxTree.Declarations.FieldDecl;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.Tools.KeyConstants;

public class ArrayType extends TypeDenoter{

	public final TypeDenoter elt;
	public int dimensions;
	public final FieldDecl length = KeyConstants.arrLen;
	
	public ArrayType(TypeDenoter elementType, SourcePosition sp) {
		super(TypeKind.Array, sp);
		elt=elementType;
	}
	
	public ArrayType(ArrayType innerArray, SourcePosition sp) {
		super(TypeKind.Array, sp);
		elt=innerArray;
		dimensions=innerArray.dimensions+1;
	}
	
	@Override
	public void visit(ASTVisitor v) {
		v.visitArrayType(this);
	}

}
