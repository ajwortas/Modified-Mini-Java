package mmj.AbstractSyntaxTree;

import mmj.AbstractSyntaxTree.Lists.ClassDeclList;
import mmj.AbstractSyntaxTree.Lists.InterfaceDeclList;
import mmj.SyntacticAnalyzer.SourcePosition;

public class Package extends ASTComponent{

	public Package(ClassDeclList cdl, InterfaceDeclList idl, SourcePosition sp) {
		super(sp);
		classList = cdl;
		interfaceList=idl;
	}

	@Override
	public void visit(ASTVisitor v) {
		
	}

	public final ClassDeclList classList;
	public final InterfaceDeclList interfaceList;
	
}
