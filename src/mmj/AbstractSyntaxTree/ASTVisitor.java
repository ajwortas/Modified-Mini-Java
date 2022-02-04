package mmj.AbstractSyntaxTree;

import mmj.AbstractSyntaxTree.Declarations.*;
import mmj.AbstractSyntaxTree.Expressions.*;
import mmj.AbstractSyntaxTree.References.*;
import mmj.AbstractSyntaxTree.Statements.*;
import mmj.AbstractSyntaxTree.Terminals.*;
import mmj.AbstractSyntaxTree.TypeDenoters.*;
import mmj.AbstractSyntaxTree.TypeDenoters.MethodType;

public interface ASTVisitor {

	//Declarations 
	void visitClassDeclaration(ClassDecl decl);
	void visitInterfaceDeclaration(InterfaceDecl decl);
	void visitMethodDeclaration(MethodDecl decl);
	void visitFieldDeclaration(FieldDecl decl);
	void visitVarDeclaration(VarDecl decl);
	void visitParameterDeclaration(ParameterDecl decl);
	
	//References
	void visitIDReference(IdRef ref);
	void visitThisReference(ThisRef ref);
	void visitSuperReference(SuperRef ref);
	void visitQualReference(QualRef ref);
	void visitIxReference(IxRef ref);
	void visitCallReference(CallRef ref);
	
	//Statements
	void visitAssignStatement(AssignStmt stmt);
	void visitCallStatement(CallStmt stmt);
	void visitBlockStatement(BlockStmt stmt);
	void visitIfStatement(IfStmt stmt);
	void visitIxAssignStatement(IxAssignStmt stmt);
	void visitReturnStatement(ReturnStmt stmt);
	void visitVarDeclStatement(VarDeclStmt stmt);
	void visitWhileStatement(WhileStmt stmt);
	void visitDoWhileStatement(DoWhileStmt stmt);
	void visitForStatement(ForStmt stmt);
	void visitForEachStatement(ForEachStmt stmt);
	void visitUnaryStatement(UnaryStmt stmt);
	void visitFreeStatement(FreeStmt stmt);
	void visitBarrierStmt(BarrierStmt barrierStmt);
	void visitForAllStmt(ForAllStmt forAllStmt);
	
	//Expressions
	void visitBinaryExpression(BinaryExpr expr);
	void visitLiteralExpression(LiteralExpr expr);
	void visitNewArrayExpression(NewArrayExpr expr);
	void visitNewObjectExpression(NewObjectExpr expr);
	void visitNewMethodExpression(NewMethodExpr expr);
	void visitReferenceExpression(RefExpr expr);
	void visitUnaryExpression(UnaryExpr expr);
	void visitTurnaryExpression(TernaryExpr expr);
	void visitCurryExpression(CurryExpr expr);
	
	//TypeDenoters
	void visitArrayType(ArrayType arr);
	void visitBaseType(BaseType base);
	void visitClassType(ClassType clazz);
	void visitMethodType(MethodType method);

	//Terminals
	void visitBooleanLiteral(BooleanLiteral bool);
	void visitIntegerLiteral(IntegerLiteral integer);
	void visitCharacterLiteral(CharacterLiteral integer);
	void visitNullLiteral(NullLiteral nullLit);
	void visitStringLiteral(StringLiteral string);
	void visitIdentifier(Identifier identifier);
	void visitOperator(Operator operator);
	void visitAssignment(Assignment assignment);
	void visitArrayLiteral(ArrayLiteral arrayLiteral);
	void visitRangeExpr(RangeExpr rangeLiteral);
	
	
}
