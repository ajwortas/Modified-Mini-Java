package mmj.ContextualAnalysis;

import java.util.List;

import mmj.AbstractSyntaxTree.Declarations.ClassDecl;
import mmj.AbstractSyntaxTree.Declarations.Declaration;
import mmj.AbstractSyntaxTree.Declarations.InterfaceDecl;
import mmj.AbstractSyntaxTree.Declarations.MemberDecl;
import mmj.AbstractSyntaxTree.Declarations.MethodDecl;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.References.CallRef;
import mmj.AbstractSyntaxTree.TypeDenoters.ClassType;
import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;

public class InheritanceHelper {

	private final CompilerReporter reporter;
	
	public InheritanceHelper() {
		this.reporter=Factory.getCompilerReporter();
	}
	
	
	public boolean memberPutCheck(Declaration put, List<Declaration> methods) {
		//TODO
		return false;
	}
	
	public List<MemberDecl> allClassMembers(ClassDecl clazz){
		return null;
	}
	
	public boolean verifyClass(ClassDecl clazz) {
		return false;
	}
	
	public boolean verifyInterface(InterfaceDecl inter) {
		return false;
	}
	
	public boolean classContainsMethod(ClassDecl clazz, CallRef ref) {
		return false;
	}
	
	public Compare relationship(ClassType class1, ClassType class2) {
		return null;
	}

	public MethodDecl findConstructor(ClassDecl child, ExpressionList args) {
		return null;
	}
	
}
