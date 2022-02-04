package mmj.Tools;


import mmj.AbstractSyntaxTree.TypeKind;
import mmj.AbstractSyntaxTree.Visibility;
import mmj.AbstractSyntaxTree.Declarations.FieldDecl;
import mmj.AbstractSyntaxTree.Declarations.MemberDecl;
import mmj.AbstractSyntaxTree.TypeDenoters.*;
import mmj.SyntacticAnalyzer.SourcePosition;

public interface KeyConstants {

	public static final String constructor = "_constructor";
	public static final SourcePosition errPosn = new SourcePosition(-1,-1);
	public static final TypeDenoter intType = new BaseType(TypeKind.Int,errPosn),
									boolType = new BaseType(TypeKind.Boolean,errPosn),
									charType = new BaseType(TypeKind.Char,errPosn),
									voidType = new BaseType(TypeKind.Void,errPosn),
									nullType = new BaseType(TypeKind.Null,errPosn),
									stringType = new BaseType(TypeKind.String,errPosn),
									errType = new BaseType(TypeKind.Error,errPosn),
									unsupType = new BaseType(TypeKind.Unsupported,errPosn);
	
	public static final FieldDecl arrLen = new FieldDecl(new MemberDecl(Visibility.Public,true,false,intType,"length",errPosn),errPosn);
	
	public static final String logicalDisjunction="||",
							   logicalConjunction="&&",
							   bitwiseDisjunction="|",
							   bitwiseExclusiveDisjunction="^",
							   bitwiseConjunction="&",
							   equalsOP="==",
							   notEqualsOp="!=",
							   lessThen="<",
							   lessThenEqual="<=",
							   greaterThen=">",
							   greaterThenEqual=">=",
							   instanceOfOp="instanceof",
							   shiftLeft="<<",
							   shiftRightSigned=">>",
							   shiftRightUnsigned=">>>",
							   addition="+",
							   subtraction="-",
							   multiplication="*",
							   division="/",
							   modulo="%",
							   exponent="**",
							   notUnop="!";
	
	public default boolean arrContains(Object str, Object []... arrs) {
		for(Object [] arr:arrs)
			for(Object element:arr)
				if(element.equals(str))
					return true;
		return false;
	}
	
}
