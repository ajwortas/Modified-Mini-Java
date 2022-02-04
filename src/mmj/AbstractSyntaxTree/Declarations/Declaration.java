package mmj.AbstractSyntaxTree.Declarations;

import mmj.AbstractSyntaxTree.ASTComponent;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;

public abstract class Declaration extends ASTComponent {

	public final String name;
	public final TypeDenoter type;
	
	public Declaration(String n, TypeDenoter t, SourcePosition sp) {
		super(sp);		
		name=n;
		type=t;
	}


}
