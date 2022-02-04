package mmj.ContextualAnalysis;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.MethodComponents;
import mmj.AbstractSyntaxTree.TypeKind;
import mmj.AbstractSyntaxTree.Declarations.ClassDecl;
import mmj.AbstractSyntaxTree.Declarations.FieldDecl;
import mmj.AbstractSyntaxTree.Declarations.FinalableDeclaration;
import mmj.AbstractSyntaxTree.Declarations.InterfaceDecl;
import mmj.AbstractSyntaxTree.Declarations.MemberDecl;
import mmj.AbstractSyntaxTree.Declarations.MethodDecl;
import mmj.AbstractSyntaxTree.Declarations.ParameterDecl;
import mmj.AbstractSyntaxTree.Declarations.VarDecl;
import mmj.AbstractSyntaxTree.Expressions.ArrayLiteral;
import mmj.AbstractSyntaxTree.Expressions.BinaryExpr;
import mmj.AbstractSyntaxTree.Expressions.CurryExpr;
import mmj.AbstractSyntaxTree.Expressions.Expression;
import mmj.AbstractSyntaxTree.Expressions.LiteralExpr;
import mmj.AbstractSyntaxTree.Expressions.NewArrayExpr;
import mmj.AbstractSyntaxTree.Expressions.NewMethodExpr;
import mmj.AbstractSyntaxTree.Expressions.NewObjectExpr;
import mmj.AbstractSyntaxTree.Expressions.RangeExpr;
import mmj.AbstractSyntaxTree.Expressions.RefExpr;
import mmj.AbstractSyntaxTree.Expressions.TernaryExpr;
import mmj.AbstractSyntaxTree.Expressions.UnaryExpr;
import mmj.AbstractSyntaxTree.Lists.ExpressionList;
import mmj.AbstractSyntaxTree.References.CallRef;
import mmj.AbstractSyntaxTree.References.IdRef;
import mmj.AbstractSyntaxTree.References.IxRef;
import mmj.AbstractSyntaxTree.References.NestedReference;
import mmj.AbstractSyntaxTree.References.QualRef;
import mmj.AbstractSyntaxTree.References.SuperRef;
import mmj.AbstractSyntaxTree.References.ThisRef;
import mmj.AbstractSyntaxTree.Statements.AssignStmt;
import mmj.AbstractSyntaxTree.Statements.BarrierStmt;
import mmj.AbstractSyntaxTree.Statements.BlockStmt;
import mmj.AbstractSyntaxTree.Statements.CallStmt;
import mmj.AbstractSyntaxTree.Statements.DoWhileStmt;
import mmj.AbstractSyntaxTree.Statements.ForAllStmt;
import mmj.AbstractSyntaxTree.Statements.ForEachStmt;
import mmj.AbstractSyntaxTree.Statements.ForStmt;
import mmj.AbstractSyntaxTree.Statements.FreeStmt;
import mmj.AbstractSyntaxTree.Statements.IfStmt;
import mmj.AbstractSyntaxTree.Statements.IxAssignStmt;
import mmj.AbstractSyntaxTree.Statements.ReturnStmt;
import mmj.AbstractSyntaxTree.Statements.Statement;
import mmj.AbstractSyntaxTree.Statements.UnaryStmt;
import mmj.AbstractSyntaxTree.Statements.VarDeclStmt;
import mmj.AbstractSyntaxTree.Statements.WhileStmt;
import mmj.AbstractSyntaxTree.Terminals.Assignment;
import mmj.AbstractSyntaxTree.Terminals.BooleanLiteral;
import mmj.AbstractSyntaxTree.Terminals.CharacterLiteral;
import mmj.AbstractSyntaxTree.Terminals.Identifier;
import mmj.AbstractSyntaxTree.Terminals.IntegerLiteral;
import mmj.AbstractSyntaxTree.Terminals.NullLiteral;
import mmj.AbstractSyntaxTree.Terminals.Operator;
import mmj.AbstractSyntaxTree.Terminals.StringLiteral;
import mmj.AbstractSyntaxTree.TypeDenoters.ArrayType;
import mmj.AbstractSyntaxTree.TypeDenoters.BaseType;
import mmj.AbstractSyntaxTree.TypeDenoters.ClassType;
import mmj.AbstractSyntaxTree.TypeDenoters.MethodType;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.SyntacticAnalyzer.Token;
import mmj.SyntacticAnalyzer.TokenKind;
import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;
import mmj.Tools.KeyConstants;

public class ContextualAnalysis implements ASTVisitor, KeyConstants {
	private final static String typeMismatch="Type mismatch found",
								finalViolation="Attempted writing to final variable",
								loneDeclError="A variable declaration is legal here",
								argNumError="Invalid number of arguments provided",
								illegalConstructorCall="This/Super can only be called in the constructor",
								ambigiousConstructorCall="This/Super call neither doesn't match the given parameters or is ambigious",
								invalidSuperReference="The current class does not have a parent",
								lookupError="Identifier not found in scope";

	private final CompilerReporter reporter;
	private IdentificationTable scope;
	private final InheritanceHelper helper;
	protected ClassDecl currentClass;
	protected MethodDecl currentMethod;
	private static final ReturnStmt defaultNull = new ReturnStmt(new LiteralExpr(new NullLiteral(new Token(TokenKind.Null,"null",errPosn)),errPosn),errPosn),
						 defaultVoid = new ReturnStmt(null,errPosn),
						 defaultInt = new ReturnStmt(new LiteralExpr(new IntegerLiteral(new Token(TokenKind.Int,"0",errPosn)),errPosn),errPosn), 
						 defaultChar = new ReturnStmt(new LiteralExpr(new CharacterLiteral(new Token(TokenKind.Char," ",errPosn)),errPosn),errPosn), 
						 defaultBoolean = new ReturnStmt(new LiteralExpr(new BooleanLiteral(new Token(TokenKind.Boolean,"false",errPosn)),errPosn),errPosn);
				 
	public ContextualAnalysis(IdentificationTable baseScope) {
		reporter = Factory.getCompilerReporter();
		helper = Factory.getInheritanceHelper();
		scope=baseScope;
	}
	
	//DECLARATIONS
	
	@Override
	public void visitClassDeclaration(ClassDecl decl) {
		helper.verifyClass(decl);
		currentClass=decl;
		
		scope.open();
		for(MemberDecl md:helper.allClassMembers(decl)) 
			scope.enter(md);
		for(FieldDecl fd:decl.fieldList)
			fd.visit(this);
		for(MethodDecl md:decl.methodList)
			md.visit(this);
		scope.close();
		
		decl.fieldList.sort();
		decl.methodList.sort();
	}

	@Override
	public void visitInterfaceDeclaration(InterfaceDecl decl) {
		helper.verifyInterface(decl);
	}

	@Override
	public void visitFieldDeclaration(FieldDecl decl) {
		decl.type.visit(this);
		
		if(decl.init != null) {
			decl.init.visit(this);
			if(!checkTypingLeftLowerOrEqual(decl.type, decl.init.evaluatedType))
				reportError(typeMismatch,decl.posn);
		}
		
	}
	
	@Override
	public void visitMethodDeclaration(MethodDecl decl) {
		currentMethod=decl;
		scope.open();
		
		decl.type.visit(this);
		
		for(ParameterDecl pd:decl.paraList)
			pd.visit(this);
		for(Statement stmt:decl.stmtList)
			stmt.visit(this);
		
		if(!(decl.stmtList.get(decl.stmtList.size()-1) instanceof ReturnStmt)) {
			if(decl.type.kind == TypeKind.Void)
				decl.stmtList.add(defaultVoid);
			if(decl.type.kind == TypeKind.Array || decl.type.kind == TypeKind.Method || decl.type.kind == TypeKind.Class)
				decl.stmtList.add(defaultNull);
			if(decl.type.kind == TypeKind.Boolean)
				decl.stmtList.add(defaultBoolean);
			if(decl.type.kind == TypeKind.Char)
				decl.stmtList.add(defaultChar);
			if(decl.type.kind == TypeKind.Int)
				decl.stmtList.add(defaultInt);
		}
		
		scope.close();
	}
	
	@Override
	public void visitParameterDeclaration(ParameterDecl decl) {
		scope.enter(decl);
		decl.type.visit(this);
	}

	@Override
	public void visitVarDeclaration(VarDecl decl) {
		scope.enter(decl);
		decl.type.visit(this);
	}

	//REFERENCES
	
	@Override
	public void visitIDReference(IdRef ref) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitThisReference(ThisRef ref) {
		ref.decl=currentClass;
		ref.evalType=currentClass.type;
	}

	@Override
	public void visitSuperReference(SuperRef ref) {
		if(currentClass.parents.size()==0) {
			reportError(invalidSuperReference,ref.posn);
			ref.evalType=unsupType;
		}else if(currentClass.parents.size()==1) {
			ref.decl=scope.getClass(currentClass.parents.get(0).id.spelling);
			ref.evalType=ref.decl.type;
		}
		//TODO If there is more than one parent class then decl and evalType cannot be determined yet
	}

	@Override
	public void visitQualReference(QualRef ref) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitIxReference(IxRef ref) {
		ref.ref.visit(this);
		ref.ixExpr.visit(this);
		
		if(checkType(ref.ixExpr.evaluatedType,intType)!=Compare.equal)
			reportError(typeMismatch,ref.ixExpr.posn);
		if(!(ref.ref.evalType instanceof ArrayType)) {
			reportError(typeMismatch,ref.ref.posn);
			ref.evalType=errType;
		}else 
			ref.evalType=((ArrayType)ref.ref.evalType).elt;
		
		ref.decl = ref.ref.decl;
	}

	@Override
	public void visitCallReference(CallRef ref) {
		ref.ref.visit(this);
		for(Expression expr:ref.argList)
			expr.visit(this);
		
		MethodComponents method;
		if(ref.ref instanceof ThisRef) {
			if(!currentMethod.name.equals(constructor)) {
				reportError(illegalConstructorCall,ref.posn);
				ref.evalType=errType;
				return;
			}
			MethodDecl constructor=null;
			for(MethodDecl md:currentClass.constructors)
				if(parametersMatch(md.mt,ref.argList,null,false)) {
					constructor=md;
					break;
				}
			if(constructor==null) {
				reportError(ambigiousConstructorCall,ref.posn);
				ref.evalType=errType;
				return;
			}
			ref.decl=constructor;
			method=constructor.mt;
		}else if(ref.ref instanceof SuperRef) {
			
		}else if(ref.ref instanceof NestedReference && ((NestedReference)ref.ref).ref instanceof SuperRef) {
			
		}else {
//			method=ref.ref.decl.;
//			ref.decl=ref.ref.decl;
		}

		
		
		
		if(!(ref.ref.evalType instanceof MethodType)) {
			reportError(typeMismatch,ref.ref.posn);
			ref.evalType=errType;
			return;
		}
		
//		ref.evalType=method.retType;
//		parametersMatch(method, ref.argList, ref.ref.posn, true);
	}

	//STATEMENTS
	@Override
	public void visitAssignStatement(AssignStmt stmt) {
		stmt.expr.visit(this);
		stmt.ref.visit(this);
		stmt.assignment.visit(this);
		
		if(!(checkTypingLeftLowerOrEqual(stmt.ref.evalType,stmt.expr.evaluatedType) 
		 && checkType(stmt.ref.evalType,stmt.assignment.evalType) == Compare.equal))
			reportError(typeMismatch,stmt.posn);
		
		if(stmt.ref.decl instanceof FinalableDeclaration 
				&& ((FinalableDeclaration)stmt.ref.decl).isFinal 
				&& !(stmt.ref.decl instanceof FieldDecl && currentMethod.name.equals(constructor)))
			reportError(finalViolation,stmt.posn);
	}

	@Override
	public void visitCallStatement(CallStmt stmt) {
		stmt.ref.visit(this);
	}

	@Override
	public void visitBlockStatement(BlockStmt blockStmt) {
		scope.open();
		for(Statement stmt:blockStmt.stmtList)
			stmt.visit(this);
		scope.close();
	}

	@Override
	public void visitIfStatement(IfStmt stmt) {
		stmt.cond.visit(this);
		stmt.ifTrue.visit(this);
		
		boolean falseIsNull = stmt.ifFalse!=null;
		
		if(falseIsNull)
			stmt.ifFalse.visit(this);
		
		if(checkType(stmt.cond.evaluatedType,boolType)!=Compare.equal)
			reportError(typeMismatch,stmt.posn);
		
		if(stmt.ifTrue instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
		
		if(!falseIsNull && stmt.ifFalse instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
		
	}

	@Override
	public void visitIxAssignStatement(IxAssignStmt stmt) {
		stmt.ref.visit(this);
		stmt.expr.visit(this);
		stmt.assignment.visit(this);
		
		if(!(checkTypingLeftLowerOrEqual(stmt.ref.evalType,stmt.expr.evaluatedType) 
				 && checkType(stmt.ref.evalType,stmt.assignment.evalType) == Compare.equal))
					reportError(typeMismatch,stmt.posn);
				
		if(stmt.ref.decl instanceof FinalableDeclaration 
				&& ((FinalableDeclaration)stmt.ref.decl).isFinal 
				&& !(stmt.ref.decl instanceof FieldDecl && currentMethod.name.equals(constructor)))
			reportError(finalViolation,stmt.posn);
	}

	@Override
	public void visitReturnStatement(ReturnStmt stmt) {
		if(stmt.expr!=null) {
			stmt.expr.visit(this);
			if(!checkTypingLeftLowerOrEqual(currentMethod.type, stmt.expr.evaluatedType)) 
				reportError(typeMismatch,stmt.posn);
		}else
			if(checkType(currentMethod.type,voidType)!=Compare.equal)
				reportError(typeMismatch,stmt.posn);
	}

	@Override
	public void visitVarDeclStatement(VarDeclStmt stmt) {
		stmt.expr.visit(this);
		stmt.decl.visit(this);	
		
		if(!checkTypingLeftLowerOrEqual(stmt.decl.type, stmt.expr.evaluatedType)) 
			reportError(typeMismatch,stmt.posn);
	}

	@Override
	public void visitWhileStatement(WhileStmt stmt) {
		stmt.cond.visit(this);
		stmt.body.visit(this);
		
		if(checkType(stmt.cond.evaluatedType,boolType)!=Compare.equal)
			reportError(typeMismatch,stmt.posn);
		
		if(stmt.body instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
	}

	@Override
	public void visitDoWhileStatement(DoWhileStmt stmt) {
		stmt.cond.visit(this);
		stmt.body.visit(this);
		
		if(checkType(stmt.cond.evaluatedType,boolType)!=Compare.equal)
			reportError(typeMismatch,stmt.posn);
		
		if(stmt.body instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
	}

	@Override
	public void visitForStatement(ForStmt stmt) {
		scope.open();
		if(stmt.range==null) {
			if(stmt.init!=null) stmt.init.visit(this);
			if(stmt.cond!=null) stmt.cond.visit(this);
			if(stmt.incr!=null) stmt.incr.visit(this);
			if(checkType(stmt.cond.evaluatedType,boolType)!=Compare.equal)
				reportError(typeMismatch,stmt.posn);
		}else {
			stmt.var.visit(this);
			stmt.range.visit(this);
			if(checkType(stmt.var.type,intType)!=Compare.equal)
				reportError(typeMismatch,stmt.posn);
		}
		
		stmt.body.visit(this);	
		if(stmt.body instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
		
		scope.close();
	}

	@Override
	public void visitForEachStatement(ForEachStmt stmt) {
		scope.open();
		
		stmt.ref.visit(this);
		stmt.var.visit(this);
		stmt.body.visit(this);
		
		if(!(stmt.ref.evalType instanceof ArrayType))
			reportError(typeMismatch,stmt.ref.posn);
		else 
			if(!checkTypingLeftLowerOrEqual(stmt.var.type, ((ArrayType)stmt.ref.evalType).elt))
				reportError(typeMismatch,stmt.var.posn);
		
		if(stmt.body instanceof VarDeclStmt)
			reportError(loneDeclError,stmt.posn);
		
		scope.clone();
	}

	@Override
	public void visitUnaryStatement(UnaryStmt stmt) {
		stmt.op.visit(this);
		stmt.ref.visit(this);
		
		if(checkType(stmt.op.evalType,stmt.ref.evalType)!=Compare.equal)
			reportError(typeMismatch,stmt.posn);
	}

	@Override
	public void visitFreeStatement(FreeStmt stmt) {
		stmt.ref.visit(this);
		
		if(stmt.ref.evalType instanceof BaseType && stmt.ref.evalType.kind != TypeKind.Error)
			reportError(typeMismatch,stmt.ref.posn);
	}
	
	@Override
	public void visitBarrierStmt(BarrierStmt stmt) {
		if(stmt.arrAndParams==null) {
			for(ExpressionList exprL:stmt.lambdas) {
				for(Expression expr:exprL)
					expr.visit(this);
				if(!(exprL.get(0) instanceof RefExpr) || exprL.get(0).evaluatedType.kind == TypeKind.Method) 
					reportError(typeMismatch,stmt.posn);
				
				RefExpr lambda = (RefExpr)exprL.get(0);
				//TODO
				
			}
				
		}else {
			
		}
	}

	@Override
	public void visitForAllStmt(ForAllStmt stmt) {
		visitForStatement(stmt);		
	}
	
	//EXPRESSIONS

	private final String [] opRequiresNumericReturnsNumeric = {exponent,modulo,division,shiftLeft,shiftRightSigned,shiftRightUnsigned,addition,subtraction,multiplication},
							opRequiresNumericReturnsBoolean={lessThen,lessThenEqual,greaterThen,greaterThenEqual},
							opRequiresBoolean= {logicalDisjunction,logicalConjunction},
							opRequiresBooleanOrNumeric= {bitwiseDisjunction,bitwiseExclusiveDisjunction,bitwiseConjunction},
							opRequresAny= {equalsOP, notEqualsOp};
	
	@Override
	public void visitBinaryExpression(BinaryExpr expr) {
		expr.left.visit(this);
		expr.right.visit(this);
		expr.op.visit(this);
		
		if(arrContains(expr.op.spelling,opRequiresNumericReturnsNumeric,opRequiresNumericReturnsBoolean)) 
			binaryExpressionHelper(expr,TypeKind.Int);
		else if(arrContains(expr.op.spelling,opRequiresBoolean)) 
			binaryExpressionHelper(expr,TypeKind.Boolean);
		else if(arrContains(expr.op.spelling,opRequiresBooleanOrNumeric)) 
			binaryExpressionHelper(expr,TypeKind.Int,TypeKind.Boolean);
		else if(arrContains(expr.op.spelling,opRequresAny)) 
			binaryExpressionHelper(expr,TypeKind.Int,TypeKind.Boolean,TypeKind.Char,TypeKind.Class,TypeKind.Method,TypeKind.Null,TypeKind.String,TypeKind.Array);
		else if(expr.op.spelling.equals(instanceOfOp)) {
			binaryExpressionHelper(expr,TypeKind.Class);
		}else {
			expr.evaluatedType=errType;
		}
	}

	private void binaryExpressionHelper(BinaryExpr expr, TypeKind ... required) {
		if(expr.left.evaluatedType ==  errType || expr.right.evaluatedType==errType || expr.op.evalType==errType) {
			expr.evaluatedType=errType;
			return;
		}
		
		boolean leftContains=arrContains(expr.left.evaluatedType.kind,required);
		if(leftContains&&arrContains(expr.right.evaluatedType.kind,required)) {
			expr.evaluatedType=expr.op.evalType;
		}else {
			expr.evaluatedType=errType;
			reportError(typeMismatch, leftContains ? expr.right.posn : expr.left.posn);
		}
	}
	
	@Override
	public void visitLiteralExpression(LiteralExpr expr) {
		expr.lit.visit(this);
		expr.evaluatedType=expr.lit.evalType;
	}

	@Override
	public void visitNewArrayExpression(NewArrayExpr expr) {
		expr.elType.visit(this);
		
		boolean errorFound=false;
		for(Expression exprL:expr.size) {
			if(exprL==null) break;
			exprL.visit(this);
			if(!checkTypingLeftLowerOrEqual(exprL.evaluatedType, intType)) {
				errorFound=true;
				reportError(typeMismatch, exprL.posn);
			}
		}
		if(errorFound) {
			expr.evaluatedType=errType;
		}else {
			ArrayType type=new ArrayType(expr.elType,errPosn);
			type.dimensions=expr.size.size();
			expr.evaluatedType=type;
		}
	}

	@Override
	public void visitNewObjectExpression(NewObjectExpr expr) {
		expr.ct.visit(this);
		for(Expression arg:expr.argList)
			arg.visit(this);
	
		//Type checking
		
		expr.evaluatedType=expr.ct;
		
		ClassDecl cd = (ClassDecl)expr.ct.className.decl;
		if(cd.constructors.size()==0&&expr.argList.size()==0) return;
		
		boolean foundMatch=false;
		for(MethodDecl md:cd.constructors) {
			if(parametersMatch(md.mt, expr.argList, expr.posn, false)) {
				foundMatch=true;
				break;
			}
		}
		
		if(!foundMatch) {
			expr.evaluatedType=errType;
			reportError("No Constructor Matches Given Args", expr.posn);
		}
		
	}

	@Override
	public void visitNewMethodExpression(NewMethodExpr expr) {
		IdentificationTable currentScope = scope.clone();
		scope.isolatedMethodScope();
		
		expr.newMethod.retType.visit(this);
		for(ParameterDecl pd:expr.newMethod.paraList)
			pd.visit(this);
		for(Statement stmt:expr.newMethod.stmtList)
			stmt.visit(this);
		
		scope=currentScope;
		
		expr.evaluatedType=expr.newMethod.retType;
	}

	@Override
	public void visitReferenceExpression(RefExpr expr) {
		expr.ref.visit(this);
		
		if(expr.ref.decl instanceof MethodDecl && !((MethodDecl)expr.ref.decl).isStatic) {
			expr.evaluatedType=errType;
			this.reportError("Non-static methods cannot be passed as a reference", expr.posn);
		}else {
			expr.evaluatedType=expr.ref.evalType;
		}	
	}

	@Override
	public void visitUnaryExpression(UnaryExpr expr) {
		expr.op.visit(this);
		expr.expr.visit(this);
		
		if(expr.op.spelling.equals(notUnop)) {
			expr.evaluatedType=checkType(expr.expr.evaluatedType, boolType)==Compare.equal?boolType:errType;
		}else if(expr.op.spelling.equals(subtraction)) {
			expr.evaluatedType=checkType(expr.expr.evaluatedType, intType)==Compare.equal?intType:errType;
		}else {
			expr.evaluatedType=errType;
			reportError("UnknownError",expr.posn);
		}
	}

	@Override
	public void visitTurnaryExpression(TernaryExpr expr) {
		expr.cond.visit(this);
		expr.ifFalse.visit(this);
		expr.ifTrue.visit(this);
		
		if(!checkTypingLeftLowerOrEqual(expr.cond.evaluatedType, boolType)) {
			expr.evaluatedType=errType;
			reportError(typeMismatch,expr.cond.posn);
		}
		
		switch(checkType(expr.ifFalse.evaluatedType,expr.ifTrue.evaluatedType)) {
		case class1subclassOf2:
			expr.evaluatedType=expr.ifFalse.evaluatedType;
			break;
		case class2subclassOf1:
			expr.evaluatedType=expr.ifTrue.evaluatedType;
			break;
		case equal:
			expr.evaluatedType=expr.ifTrue.evaluatedType;
			break;
		case notEqual:
			expr.evaluatedType=errType;
			reportError(typeMismatch,expr.ifTrue.posn);
			break;		
		}
		
	}

	@Override
	public void visitCurryExpression(CurryExpr expr) {
		expr.method.visit(this);
		for(Expression arg:expr.argList) 
			arg.visit(this);
		
		if(!(expr.method.decl instanceof MethodDecl)) {
			expr.evaluatedType=errType;
			reportError(typeMismatch,expr.method.posn);
			return;
		}
		
		MethodDecl md = (MethodDecl)expr.method.decl;
		
		if(expr.argList.size()>md.mt.paraList.size()) {
			expr.evaluatedType=errType;
			reportError(argNumError, expr.posn);
			return;
		}
	
		boolean hasError=false;
		for(int i=0;i<expr.argList.size();i++) {
			if(!checkTypingLeftLowerOrEqual(expr.argList.get(i).evaluatedType,md.mt.paraList.get(i).type)) {
				hasError=true;
				reportError(typeMismatch,expr.argList.get(i).posn);
			}
		}
		
		if(hasError) {
			expr.evaluatedType=errType;
		}else {
			expr.evaluatedType=new MethodType(md.mt,expr.argList.size(),errPosn);
		}
		
	}

	@Override
	public void visitArrayLiteral(ArrayLiteral expr) {
		for(Expression e:expr.exprList) 
			e.visit(this);
		
		//Type checking
		
		if(expr.exprList.size()==0) {
			//not an error but should allow the empty array to be
			//used for any type
			ArrayType type = new ArrayType(errType,errPosn);
			type.dimensions=1;
			expr.evaluatedType=type;
			return;
		}
		
		Expression lowest = expr.exprList.get(0);
		
		for(Expression e:expr.exprList) {
			switch(checkType(lowest.evaluatedType, e.evaluatedType)) {
			case class1subclassOf2: case equal:
				break;
			case class2subclassOf1:
				lowest=e;
				break;
			case notEqual:
				expr.evaluatedType=errType;
				return;
			}
		}
		
		ArrayType type;
		if(lowest.evaluatedType instanceof ArrayType) {
			type=new ArrayType((ArrayType)lowest.evaluatedType,errPosn);
		}else {
			type = new ArrayType(lowest.evaluatedType,errPosn);
			type.dimensions=1;
		}
		expr.evaluatedType=type;	
	}
	
	@Override
	public void visitRangeExpr(RangeExpr expr) {
		expr.start.visit(this);
		expr.incr.visit(this);
		expr.end.visit(this);
		
		boolean hasError=false;
		if(!checkTypeIsEqual(expr.start.evaluatedType,intType)) {
			hasError=true;
			reportError(typeMismatch,expr.start.posn);
		}
		if(!checkTypeIsEqual(expr.incr.evaluatedType,intType)) {
			hasError=true;
			reportError(typeMismatch,expr.incr.posn);
		}
		if(!checkTypeIsEqual(expr.end.evaluatedType,intType)) {
			hasError=true;
			reportError(typeMismatch,expr.end.posn);
		}
		
		if(hasError) {
			expr.evaluatedType=errType;
		}else {
			ArrayType type=new ArrayType(intType,errPosn);
			type.dimensions=1;
			expr.evaluatedType=type;
		}
	}	
	
	// TYPES	
	
	
	@Override
	public void visitArrayType(ArrayType arr) {
		arr.elt.visit(this);
	}

	
	@Override
	public void visitBaseType(BaseType base) {}

	
	@Override
	public void visitClassType(ClassType clazz) {
		clazz.className.visit(this);
		if(scope.getClass(clazz.className.spelling)==null) {
			reportError(lookupError,clazz.posn);
		}
	}
	

	@Override
	public void visitMethodType(MethodType method) {
		
		
	}


	//TERMINALS	
	
	
	@Override
	public void visitBooleanLiteral(BooleanLiteral bool) {
		bool.evalType=boolType;
	}

	@Override
	public void visitIntegerLiteral(IntegerLiteral integer) {
		integer.evalType=intType;
	}

	@Override
	public void visitCharacterLiteral(CharacterLiteral character) {
		character.evalType=charType;
	}

	@Override
	public void visitNullLiteral(NullLiteral nullLit) {
		nullLit.evalType=nullType;
	}

	@Override
	public void visitStringLiteral(StringLiteral string) {
		string.evalType=stringType;
	}

	@Override
	public void visitIdentifier(Identifier identifier) {
		identifier.decl = scope.retrieve(identifier.spelling);
		
		if(identifier.decl==null) {
			identifier.evalType=errType;
			reportError(lookupError,identifier.posn);
		}else {
			identifier.evalType=identifier.decl.type;
		}
	}

	@Override
	public void visitOperator(Operator op) {
		if(arrContains(op.spelling,opRequiresNumericReturnsNumeric,opRequiresBooleanOrNumeric)) {
			op.evalType=intType;
		}else if(arrContains(op.spelling,opRequresAny,opRequiresBoolean,opRequiresNumericReturnsBoolean)||op.spelling.equals(instanceOfOp)||op.spelling.equals(notUnop)) {
			op.evalType=boolType;
		}else {
			op.evalType=errType;
		}
	}

	
	@Override
	public void visitAssignment(Assignment assignment) {}

	//HELPERS
	
	
	//General Helper Methods
	
	private void reportError(String msg, SourcePosition posn) {
		reporter.reportError("### "+posn.toString()+msg);
	}
	
	
	private boolean checkTypingLeftLowerOrEqual(TypeDenoter td1, TypeDenoter td2) {
		Compare c = checkType(td1,td2);
		if(c==Compare.equal||c==Compare.class1subclassOf2)
			return true;
		return false;
	}

	private boolean checkTypeIsEqual(TypeDenoter td1,TypeDenoter td2) {
		return checkType(td1,td2)==Compare.equal;
	}
	
	
	private Compare checkType(TypeDenoter td1, TypeDenoter td2) {
		if(td1.kind == TypeKind.Error || td1.kind == TypeKind.Error)
			return Compare.equal;
		if(td1.kind == TypeKind.Unsupported || td1.kind == TypeKind.Unsupported)
			return Compare.notEqual;
		if((td1.kind == TypeKind.Null && td2.kind==TypeKind.Class) || (td1.kind==TypeKind.Class && td2.kind == TypeKind.Null))
			return Compare.equal;
		
		if(td1.kind==TypeKind.Array) {
			if(td2.kind!=TypeKind.Array)
				return Compare.notEqual;
			ArrayType ar1=(ArrayType)td1,ar2=(ArrayType)td2;
			if(ar1.dimensions!=ar2.dimensions) 
				return Compare.notEqual;
			return checkType(ar1.elt,ar2.elt);
		}
		
		if(td1.kind==TypeKind.Method) {
			if(td2.kind!=TypeKind.Method)
				return Compare.notEqual;
			MethodType mt1=(MethodType)td1,mt2=(MethodType)td2;
			if(checkType(mt1.retType,mt2.retType)!=Compare.equal || mt1.typeList.size()!=mt2.typeList.size())
				return Compare.notEqual;
			for(int i=0;i<mt1.typeList.size();i++)
				if(checkType(mt1.typeList.get(i),mt2.typeList.get(i))==Compare.equal)
					return Compare.notEqual;
			return Compare.equal;
		}
		
		if(td1.kind==TypeKind.Class) {
			if(td2.kind!=TypeKind.Class)
				return Compare.notEqual;
			return helper.relationship((ClassType) td1, (ClassType) td2);
		}
	
		if(td1.kind==td2.kind)
			return Compare.equal;
		return Compare.notEqual;
	}

	private boolean parametersMatch(MethodComponents method, ExpressionList argL, SourcePosition posn, boolean reportErrors){
		if(method.paraList.size()!=argL.size()) {
			if(reportErrors)
				reportError(argNumError,posn);
			return false;
		}
		for(int i=0;i<method.paraList.size();i++)
			if(!checkTypingLeftLowerOrEqual(method.paraList.get(i).type, argL.get(i).evaluatedType))
				if(reportErrors) {
					reportError(typeMismatch,argL.get(i).posn);
					return false;
				}else
					return false;
		return true;
	}

}
