package mmj.AbstractSyntaxTree.References;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.AbstractSyntaxTree.Declarations.Declaration;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class Reference extends ASTComponent{

	public TypeDenoter evalType;
	public Declaration decl;
	
	public Reference(SourcePosition sp) {
		super(sp);
	}

}
