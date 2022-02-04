package mmj.SyntacticAnalyzer;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;
import mmj.Tools.KeyConstants;
import mmj.Tools.ThreadPool;
import mmj.Tools.Exceptions.SyntaxException;
import mmj.AbstractSyntaxTree.Package;
import mmj.AbstractSyntaxTree.*;
import mmj.AbstractSyntaxTree.Lists.*;
import mmj.AbstractSyntaxTree.References.*;
import mmj.AbstractSyntaxTree.Statements.*;
import mmj.AbstractSyntaxTree.Terminals.*;
import mmj.AbstractSyntaxTree.TypeDenoters.*;
import mmj.AbstractSyntaxTree.Declarations.*;
import mmj.AbstractSyntaxTree.Expressions.*;

//What are these ambiguous with?
import mmj.AbstractSyntaxTree.Statements.IxAssignStmt;
import mmj.AbstractSyntaxTree.TypeDenoters.MethodType;
import mmj.AbstractSyntaxTree.MethodComponents;

public class Parser implements KeyConstants{
	private final CompilerReporter reporter;
	private final ThreadPool pool;
	private final ScanningTask scan;
	private final String threadName;
	private Token token;
	private Queue<Token> queue;
	private boolean acceptQueue=true;
	private String currentClassName;
	
	private final String [][] precedent = { //Opposite lower to upper 
			{logicalDisjunction}, //Logical Disjunction
			{logicalConjunction}, //Logical Conjunction
			{bitwiseDisjunction}, //Bitwise Disjunction
			{bitwiseExclusiveDisjunction}, //Bitwise Exclusive Disjunction
			{bitwiseConjunction}, //Bitwise Conjunction
			{equalsOP,notEqualsOp}, //Equality
			{lessThen,lessThenEqual,greaterThen,greaterThenEqual,instanceOfOp}, //Relational
			{shiftLeft,shiftRightSigned,shiftRightUnsigned}, //shift
			{addition,subtraction}, //Additive
			{multiplication,division,modulo}, //Multiplicative 
			{exponent} //Exponential
	};
		
	public Parser(File f, ThreadPool p) throws Exception {
		pool=p;
		pool.setMinimumNumThreads(1);
		scan = new ScanningTask(new Scanner(f));
		pool.addTask(scan);
		queue=new LinkedList<Token>();
		threadName = Thread.currentThread().getName();
		reporter=Factory.getCompilerReporter();
		
	}
	
	public Package parse() {
		try {
			token = scan.getCurrentResult();
			pool.addTask(scan);
			reporter.addTrace("Scanner made token: " + token);
			return parseProgram();
		}catch(Exception e){}
		return null;
	}
	
	// (Class/Interface Declaration)* eot
	private Package parseProgram() throws Exception{
		ClassDeclList cdl = new ClassDeclList();
		InterfaceDeclList idl = new InterfaceDeclList();
		SourcePosition sp = token.posn;
		while(token.kind!=TokenKind.EOF)
			if(token.kind == TokenKind.Final || token.kind == TokenKind.Class)
				cdl.add(parseClass());
			else 
				idl.add(parseInterface());
		
		acceptIt(TokenKind.EOF);
		return new Package(cdl,idl,sp);
	}
	
	private InterfaceDecl parseInterface() throws Exception{
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Interface);
		Token id = token;
		acceptIt(TokenKind.ID);
		
		//Parent interfaces
		IdRefList implemented = new IdRefList();
		if(token.kind==TokenKind.Extends) {
			accept();
			Token intr = token;
			acceptIt(TokenKind.ID);
			implemented.add(new IdRef(new Identifier(intr),intr.posn));
			while(token.kind != TokenKind.OpenCurlyBracket) {
				acceptIt(TokenKind.Comma);
				intr = token;
				acceptIt(TokenKind.ID);
				implemented.add(new IdRef(new Identifier(intr),intr.posn));
			}
			accept();
		}else
			acceptIt(TokenKind.OpenCurlyBracket);
		
		//DefinedMethods
		InterfaceMethodDeclList imdl = new InterfaceMethodDeclList();
		while(token.kind != TokenKind.CloseCurlyBracket)
			imdl.add(parseInterfaceMethod());
		accept();
		return new InterfaceDecl(id.spelling,implemented, imdl, posn);
	}

	// Visibility access id id parameterList;
	public InterfaceMethodDecl parseInterfaceMethod() throws Exception{
		MemberDecl md = parseMemberDecl();
		ParameterList pl = parseParameterList();
		acceptIt(TokenKind.SemiColon);
		return new InterfaceMethodDecl(md,pl);
	}
	
	// final? class  id (extends id)? (implements id (, id)*)? { (FieldDec | MethodDec)* }
	private ClassDecl parseClass() throws Exception{
		SourcePosition posn = token.posn;
		boolean isFinal = parseFinal();
		acceptIt(TokenKind.Class);
		Token id = token;
		currentClassName=id.spelling;
		acceptIt(TokenKind.ID);
		
		//Parent Class(es)
		IdRefList parent = new IdRefList();
		if(token.kind==TokenKind.Extends) {
			accept();
			Token parentID = token;
			acceptIt(TokenKind.ID);
			parent.add(new IdRef(new Identifier(parentID),parentID.posn));
			while(token.kind == TokenKind.Comma) {
				accept();
				parentID = token;
				acceptIt(TokenKind.ID);
				parent.add(new IdRef(new Identifier(parentID),parentID.posn));
			}
		}
		
		//implemented Interfaces
		IdRefList implemented = new IdRefList();
		if(token.kind==TokenKind.Implements) {
			accept();
			Token intr = token;
			acceptIt(TokenKind.ID);
			implemented.add(new IdRef(new Identifier(intr),intr.posn));
			while(token.kind != TokenKind.OpenCurlyBracket) {
				acceptIt(TokenKind.Comma);
				intr = token;
				acceptIt(TokenKind.ID);
				implemented.add(new IdRef(new Identifier(intr),intr.posn));
			}
			accept();
		}else
			acceptIt(TokenKind.OpenCurlyBracket);
		
		//Member Declarations
		MethodDeclList mdl = new MethodDeclList();
		FieldDeclList fdl = new FieldDeclList();
		while(token.kind != TokenKind.OpenCurlyBracket) {
			MemberDecl member = parseClassMember();
			if(member instanceof MethodDecl) 
				mdl.add((MethodDecl)member);
			else 
				fdl.add((FieldDecl)member);
		}
		accept();
		return new ClassDecl(id.spelling, isFinal, fdl, mdl, parent, implemented, posn);
	}
	
	// Visibility access final? id
	private MemberDecl parseClassMember() throws Exception{
		MemberDecl md = parseMemberDecl();
		if(md.type.kind==TypeKind.Void || token.kind == TokenKind.OpenParentheses) {
			ParameterList pl = parseParameterList();
			StatementList sl = parseStatementList();
			return new MethodDecl(md,pl,sl,md.posn);
		}else{
			md = new FieldDecl(md,md.posn);
			if(token.spelling.equals("=")) {
				acceptItWithSpelling(TokenKind.Binop,"=");
				((FieldDecl)md).init = parseExpression();
			}
			acceptIt(TokenKind.SemiColon);
			return md;
		}
	}

	private MemberDecl parseMemberDecl() throws Exception {
		SourcePosition posn=token.posn;
		Visibility v = parseVisibility();

		//constructor
		if(token.kind == TokenKind.ID && token.spelling.equals(currentClassName)) {
			accept();
			return new MemberDecl(v, false, false, voidType, constructor, posn);
		}
		
		boolean isStatic = parseAccess();
		boolean isFinal = parseFinal();
		
		TypeDenoter type;
		if(token.kind == TokenKind.Void) {
			type = new BaseType(TypeKind.Void,token.posn);
			accept();
		}else
			type = parseType();
		
		Token id = token;
		acceptIt(TokenKind.ID);
		return new MemberDecl(v, isStatic, isFinal, type, id.spelling, posn);
	}
	
	//this|id|super|id(\(ArgList\)|[expr]) (.id([expr])?)*
	private Reference parseReference() throws Exception{
		SourcePosition posn = token.posn;
		Reference ref;
		switch(token.kind) {
		case This:
			accept();
			ref = new ThisRef(posn);
			break;
		case Super: 
			accept();
			ref = new SuperRef(posn);
			break;
		case ID:
			Identifier id=new Identifier(token);
			accept();
//			if(token.kind == TokenKind.OpenSquareBracket) {
//				accept();
//				Expression expr = parseExpression();
//				acceptIt(TokenKind.CloseSquareBracket);
//				ref = new IxRef(id,expr,posn);
//			}else 
				ref = new IdRef(id,posn);
			break;
		default:
			parseError("Unexpected reference type, expecting this, super, or id but got: "+token.kind);
			throw new SyntaxException();
		}
		
		// (.id([expr]|\(ArgList\))?)*
//		while(token.kind == TokenKind.Period || token.kind == TokenKind.OpenSquareBracket || token.kind == TokenKind.OpenParentheses) {
		nestedReferenceLoop:
		for(;;) {
			switch(token.kind) {
			case Period:
				accept();
				Identifier id=new Identifier(token);
				ref = new QualRef(id,ref,id.posn);
				continue nestedReferenceLoop;
			case OpenSquareBracket:
				accept();
				Expression ixExpr = parseExpression();
				acceptIt(TokenKind.CloseSquareBracket);
				ref = new IxRef(ref,ixExpr,ref.posn);
				continue nestedReferenceLoop;
			case OpenParentheses:
				accept();
				ExpressionList argL = parseArgumentList();
				acceptIt(TokenKind.CloseParentheses);
				ref = new CallRef(ref,argL,ref.posn);
				continue nestedReferenceLoop;
			default:
				break nestedReferenceLoop;
			}
		}
		return ref;
	}
	
	private Statement parseStatement() throws Exception {
		Statement retval = parseStatementNoSemiColon();
		if(!(retval instanceof ForStmt || retval instanceof ForEachStmt || retval instanceof WhileStmt || retval instanceof BlockStmt || retval instanceof IfStmt))
			acceptIt(TokenKind.SemiColon);
		return retval;
	}
	
	private Statement parseStatementNoSemiColon() throws Exception {
		switch(token.kind) {
		//For or for each loop
		case For: case Forall:
			return parseForLoop();
		//Do while loop
		case Do:
			return parseDoWhileLoop();
		//While loop
		case While:
			return parseWhileLoop();
		//If-else statement
		case If:
			return parseIfElse();
		//Free Stmt
		case Free:
			return parseFreeStmt();
		//block statement
		case OpenCurlyBracket:
			return parseBlockStmt();
		//return statement
		case Return:
			return parseReturnStmt();
		//assign statement
		case Int: case Char: case Lambda: case Boolean: case Final:
			return parseVarDeclStmt();
		case Unop:	
			return parseUnaryStmt();
		//Barrier stmt
		case Barrier:
			return parseBarrierStmt();
		//call, assign, VarDelc or ixAssign statement
		case ID:
			//VarDeclStmt in the case that it is a class array or class object, determines those then handles reference cases
			addToQueue();
			if(token.kind==TokenKind.OpenSquareBracket) {
				addToQueue();
				if(token.kind == TokenKind.CloseSquareBracket) {
					takeFromQueue();
					return parseVarDeclStmt();
				}
			}else if(token.kind == TokenKind.ID) {
				takeFromQueue();
				return parseVarDeclStmt();
			}
			takeFromQueue();
		case This: case Super:
			Reference ref = parseReference();
			if(ref instanceof CallRef)
				return new CallStmt(ref,ref.posn);
			if(ref instanceof IxRef)
				return parseIxAssignStmt(ref);
			return parseAssignStmt(ref);
		default:
			parseError("Unrecognized Statement token: " + token);
			return null;
		}
	}
	
	/* 
	 * Must Handle Precedent ordering first:
	 * Assignment (handled logically in code generation as part of an Assignment Statement)
	 * Ternary -> T ::= LD ? T : T
	 * Logical Disjunction -> LD ::= LC (op LC)*
	 * Logical Conjunction -> LC ::= BD (op BD)*  
	 * Bitwise Disjunction -> BD ::= Ed (op ED)*
	 * Exclusive Disjunction -> ED ::= BC (op BC)* 
	 * Bitwise Conjunction -> BC ::= E (op E)*
	 * Equality -> E ::= R (op R)*
	 * Relational -> R ::= S (op S)*
	 * Shift -> S ::= A (op A)*
	 * Additive -> A ::= M (op M)*
	 * Multiplicative -> M ::= E (op E)*
	 * Exponential -> E :: Expr (op Expr)*
	 */
	
	private Expression parseExpression() throws Exception {
		//Ternary
		Expression expr = parseBiop(0);
		if(token.kind == TokenKind.QuestionMark) {
			accept();
			Expression ifTrue = parseExpression();
			acceptIt(TokenKind.Colon);
			Expression ifFalse = parseExpression();
			expr = new TernaryExpr(expr,ifTrue,ifFalse,expr.posn);
		}
		return expr;
	}
	
	private Expression parseBiop(int level) throws Exception {
		Expression first = level == precedent.length ? parseNonBiop() : parseBiop(level+1);
		while(token.kind==TokenKind.Binop && containsSpelling(precedent[level])) {
			Operator op = new Operator(token);
			accept();
			Expression second = level == precedent.length ? parseNonBiop() : parseBiop(level+1);
			first = new BinaryExpr(first,op,second,first.posn);
		}
		return first;
	}
	
	private Expression parseNonBiop() throws Exception {
		Expression expr;
		Token t;
		SourcePosition posn = token.posn;
		ExpressionList argL;
		switch(token.kind) {
		case OpenParentheses: 
			accept();
			expr=parseExpression();
			acceptIt(TokenKind.CloseParentheses);
			return expr;
		case New:
			accept();
			if(token.kind == TokenKind.Lambda) {
				addToQueue();
				if(!token.spelling.equals("<")) {
					takeFromQueue();
					return parseNewLambdaExpr();
				}
				takeFromQueue();
			}
			TypeDenoter td = parseNonArrType();
			if(token.kind == TokenKind.OpenSquareBracket)
				return parseNewArrayExpr(td);
			if(td instanceof ClassType) {
				argL = parseArgumentList();
				return new NewObjectExpr((ClassType)td,argL,posn);
			}
			parseError("New objects must be an class type");
		case StringLiteral:
			t=token;
			accept();
			return new LiteralExpr(new StringLiteral(t),posn);
		case True: case False:
			t=token;
			accept();
			return new LiteralExpr(new BooleanLiteral(t),posn);	
		case Number:
			t=token;
			accept();
			return new LiteralExpr(new IntegerLiteral(t),posn);
		case Null:
			t=token;
			accept();
			return new LiteralExpr(new NullLiteral(t),posn);
		case Char:
			t=token;
			accept();
			return new LiteralExpr(new CharacterLiteral(t),posn);
		case Binop:
			if(!token.spelling.equals("-"))
				parseError("Binop "+token.spelling+" does not represent a unop");
		case Unop:
			t = token;
			accept();
			expr = parseNonBiop();
			return new UnaryExpr(expr,new Operator(t),posn);
		case This: case Super: case ID:
			Reference r = parseReference();
			return new RefExpr(r,posn);
		case Curry:
			accept();
			Reference ref = parseReference();
			argL = parseArgumentList();
			return new CurryExpr(ref,argL,posn);
		case OpenCurlyBracket: 
			accept();
			expr = parseExpression();
			if(token.kind==TokenKind.Colon) {
				expr = parseRangeExprTail(expr);
				acceptIt(TokenKind.CloseCurlyBracket);
				return expr;
			}
			argL = new ExpressionList();
			argL.add(expr);
			while(token.kind!=TokenKind.CloseCurlyBracket)
				argL.add(parseExpression());
			accept();
			return new ArrayLiteral(argL, posn);
		default:
			parseError("Token "+token.spelling+" does not appear to properly be part of a expression");
			return null;
		}
	}
	
	/*
	 * component parsing
	 */
	
	//[expr]([expr]* []*)*
 	private NewArrayExpr parseNewArrayExpr(TypeDenoter type) throws Exception {
		ExpressionList argL = new ExpressionList();		
		acceptIt(TokenKind.OpenSquareBracket);
		argL.add(parseExpression());
		acceptIt(TokenKind.CloseSquareBracket);
		boolean closedFound = false;
		while(token.kind == TokenKind.OpenSquareBracket) {
			accept();
			if(!closedFound && token.kind != TokenKind.CloseCurlyBracket) {
				argL.add(parseExpression());
				acceptIt(TokenKind.CloseSquareBracket);
			}else {
				closedFound=true;
				//Might replace later on
				argL.add(null);
				acceptIt(TokenKind.CloseSquareBracket);
			}
				
		}
		return new NewArrayExpr(type, argL, type.posn);
	}
	
	//lambda type \((id (,id)*)?)\) -> { StatementList }
	private NewMethodExpr parseNewLambdaExpr() throws Exception{
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Lambda);
		TypeDenoter retType = parseType();
		ParameterList paraL = parseParameterList();
		acceptIt(TokenKind.LambdaArrow);
		StatementList stmtL = parseStatementList();
		return new NewMethodExpr(new MethodComponents(retType, paraL, stmtL, posn),posn);
	}
	
	//Statement Components
	//for|forall \( type id:ref | stmt?;expr?;stmt? | type id: rangeLiteral\) stmt
	private Statement parseForLoop() throws Exception {
		SourcePosition posn = token.posn;
		boolean forall;
		if(token.kind==TokenKind.For) {
			accept();
			forall=false;
		}else {
			acceptIt(TokenKind.Forall);
			forall=true;
		}
		
		acceptIt(TokenKind.OpenParentheses);
		addToQueue(2);
		boolean isForEachOrRange = token.kind == TokenKind.Colon;
		addToQueue();
		//for final modifier
		isForEachOrRange |= token.kind == TokenKind.Colon;
		takeFromQueue();
		
		if(isForEachOrRange) {
			VarDecl vd = parseVarDecl();
			acceptIt(TokenKind.Colon);
			
			if(token.kind == TokenKind.Number) {
				RangeExpr range = parseRangeExpr();
				acceptIt(TokenKind.CloseParentheses);
				Statement stmt = parseStatement();
				return forall?new ForAllStmt(vd,range,stmt,posn):new ForAllStmt(vd,range,stmt,posn);
			}
			
			Reference array = parseReference();
			
			if(token.kind==TokenKind.Colon) {
				RangeExpr range = parseRangeExprTail(new RefExpr(array,array.posn));
				acceptIt(TokenKind.CloseParentheses);
				Statement stmt = parseStatement();
				return forall?new ForAllStmt(vd,range,stmt,posn):new ForAllStmt(vd,range,stmt,posn);
			}
			
			acceptIt(TokenKind.CloseParentheses);
			Statement stmt = parseStatement();
			return new ForEachStmt(vd,array,stmt,posn);
		}
		
		VarDeclStmt vds = null;
		if(token.kind!=TokenKind.SemiColon)
			vds = parseVarDeclStmt();
		acceptIt(TokenKind.SemiColon);
		
		Expression cond = null;
		if(token.kind!=TokenKind.SemiColon)
			cond = parseExpression();
		acceptIt(TokenKind.SemiColon);
		
		Statement stmt=null;
		switch(token.kind) {
		case Do: case While: case For: case OpenCurlyBracket:
			parseError("Non valid statement in for loop: "+token.kind);
		case CloseParentheses:
			break;
		default:
			stmt = parseStatementNoSemiColon();
		}
		acceptIt(TokenKind.CloseParentheses);
		Statement body = parseStatement();
		
		return forall?new ForAllStmt(vds, cond, stmt, body, posn):new ForStmt(vds, cond, stmt, body, posn);
	}
	
	//do stmt while \( expr \)
	private DoWhileStmt parseDoWhileLoop() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Do);
		Statement body = parseStatement();
		acceptIt(TokenKind.While);
		acceptIt(TokenKind.OpenParentheses);
		Expression cond = parseExpression();
		acceptIt(TokenKind.CloseParentheses);
		return new DoWhileStmt(cond, body, posn);
	}
	
	//free Reference
	private FreeStmt parseFreeStmt() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Free);
		Reference ref = parseReference();
		return new FreeStmt(ref,posn);
	}
	
	//while \(expr\) stmt
	private WhileStmt parseWhileLoop() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.While);
		acceptIt(TokenKind.OpenParentheses);
		Expression cond = parseExpression();
		acceptIt(TokenKind.CloseParentheses);
		Statement body = parseStatement();
		return new WhileStmt(cond, body, posn);
	}
	
	//if \( expr \) stmt (else stmt)? 
	private IfStmt parseIfElse() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.If);
		acceptIt(TokenKind.OpenParentheses);
		Expression cond = parseExpression();
		acceptIt(TokenKind.CloseParentheses);
		Statement ifBody = parseStatement();
		Statement elseBody = null;
		if(token.kind==TokenKind.Else) {
			accept();
			elseBody = parseStatement();
		}
		return new IfStmt(cond, ifBody, elseBody, posn);
	}
	
	// { stmtList }
	private BlockStmt parseBlockStmt() throws Exception {
		SourcePosition posn = token.posn;
		StatementList sl = new StatementList();
		acceptIt(TokenKind.OpenCurlyBracket);
		while(token.kind != TokenKind.CloseCurlyBracket)
			sl.add(parseStatement());
		acceptIt(TokenKind.CloseCurlyBracket);
		return new BlockStmt(sl,posn);
	}
	
	// return expr?
	private ReturnStmt parseReturnStmt() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Return);
		if(token.kind==TokenKind.SemiColon)
			return new ReturnStmt(null,posn);
		Expression expr = parseExpression();
		return new ReturnStmt(expr,posn);
	}
		
	// VarDecl = expr
	private VarDeclStmt parseVarDeclStmt () throws Exception {
		SourcePosition posn = token.posn;
		VarDecl var = parseVarDecl();
		acceptItWithSpelling(TokenKind.Assignment,"=");
		Expression expr = parseExpression();
		return new VarDeclStmt(var,expr,posn);
	}
	
	//unary reference
	private UnaryStmt parseUnaryStmt() throws Exception {
		SourcePosition posn = token.posn;
		Operator unary = new Operator(token);
		acceptIt(TokenKind.Unop);
		Reference ref = parseReference();
		return new UnaryStmt(unary,ref,posn);
	}
	
	// ref assign expr
	private AssignStmt parseAssignStmt(Reference ref) throws Exception {
		Assignment asg = new Assignment(token);
		acceptIt(TokenKind.Assignment);
		Expression expr = parseExpression();
		return new AssignStmt(ref,asg,expr,ref.posn);
	}
	
	// IxRef|QualRef assign expr
	private IxAssignStmt parseIxAssignStmt(Reference ref) throws Exception {
		Assignment asg = new Assignment(token);
		acceptIt(TokenKind.Assignment);
		Expression expr = parseExpression();
		return new IxAssignStmt(ref,asg,expr,ref.posn);
	}

	// barrier { {lambdaExpr paramList}* } || barrier { lambda[] paramList } 
	private BarrierStmt parseBarrierStmt() throws Exception {
		SourcePosition posn = token.posn;
		acceptIt(TokenKind.Barrier);
		acceptIt(TokenKind.OpenCurlyBracket);
		if(token.kind == TokenKind.OpenCurlyBracket) {
			ExpressionListList exprListList = new ExpressionListList();
			while(token.kind == TokenKind.OpenCurlyBracket) {
				ExpressionList exprList = new ExpressionList();
				while(token.kind != TokenKind.CloseCurlyBracket)
					exprList.add(parseExpression());
				accept();
				exprListList.add(exprList);
			}
			acceptIt(TokenKind.CloseCurlyBracket);
			return new BarrierStmt(exprListList,posn);
		}

		ExpressionList exprList = new ExpressionList();
		while(token.kind != TokenKind.CloseCurlyBracket)
			exprList.add(parseExpression());
		acceptIt(TokenKind.CloseCurlyBracket);
		return new BarrierStmt(exprList,posn);	
	}
	
	//Misc Components
	//type id
	private VarDecl parseVarDecl() throws Exception {
		boolean isFinal = parseFinal();
		TypeDenoter type = parseType();
		Token id = token;
		acceptIt(TokenKind.ID);
		return new VarDecl(id.spelling, type, isFinal, id.posn);
	}
	
	// boolean | int | char | ID | lambda<(type(,type)*)?:type> ([])*
	private TypeDenoter parseType() throws Exception {
		SourcePosition posn = token.posn;
		TypeDenoter type = parseNonArrType();
		while(token.kind == TokenKind.OpenSquareBracket) {
			accept();
			acceptIt(TokenKind.CloseSquareBracket);
			type = new ArrayType(type,posn);
		}
		return type;
	}
	
	// boolean | int | char | ID | lambda<(type(,type)*)?:type>
	private TypeDenoter parseNonArrType() throws Exception {
		SourcePosition posn = token.posn;
		TypeDenoter type=null;
		switch(token.kind) {
		case Boolean:
			accept();
			type = new BaseType(TypeKind.Boolean,posn);
			break;
		case Int:
			accept();
			type = new BaseType(TypeKind.Int,posn);
			break;
		case Char:
			accept();
			type = new BaseType(TypeKind.Char,posn);
			break;
		case ID:
			Token tk = token;
			accept();
			type = new ClassType(new Identifier(tk),posn);
			break;
		//lambda<(type(,type)*)?:type>
		case Lambda:
			accept();
			acceptItWithSpelling(TokenKind.Binop,"<");
			TypeList tl = new TypeList();
			while(token.kind!=TokenKind.Colon)
				tl.add(parseType());
			accept();
			TypeDenoter retType = parseType();
			acceptItWithSpelling(TokenKind.Binop,">");
			type = new MethodType(retType,tl,posn);
			break;
		default:
			parseError("Expecting boolean, int, lambda or ID but got: " + token);
			return null;
		}
		return type;
	}
	
	// public | private | protected | default
	private Visibility parseVisibility() throws Exception {
		switch(token.kind) {
		case Public:
			accept();
			return Visibility.Public;
		case Private:
			accept();
			return Visibility.Private;
		case Protected:
			accept();
			return Visibility.Protected;
		default:
			return Visibility.Default;
		}
	}
	
	// \((type id (,type id)*)?\)
	private ParameterList parseParameterList() throws Exception{
		acceptIt(TokenKind.OpenParentheses);
		ParameterList pdl = new ParameterList();
		if(token.kind != TokenKind.CloseParentheses) {
			SourcePosition posn = token.posn;
			boolean isFinal = parseFinal();
			TypeDenoter type = parseType();
			Token id = token;
			acceptIt(TokenKind.ID);
			pdl.add(new ParameterDecl(id.spelling,type,isFinal,posn));
			while(token.kind != TokenKind.CloseParentheses) {
				acceptIt(TokenKind.Comma);
				posn = token.posn;
				isFinal = parseFinal();
				type = parseType();
				id = token;
				acceptIt(TokenKind.ID);
				pdl.add(new ParameterDecl(id.spelling,type,isFinal,posn));
			}
		}
		acceptIt(TokenKind.CloseParentheses);
		return pdl;
	}
	
	// \((Expression (,Expression)*)?\)
	private ExpressionList parseArgumentList() throws Exception {
		acceptIt(TokenKind.OpenParentheses);
		ExpressionList argL = new ExpressionList();
		if(token.kind != TokenKind.CloseParentheses) {
			argL.add(parseExpression());
			while(token.kind != TokenKind.CloseParentheses) {
				acceptIt(TokenKind.Comma);
				argL.add(parseExpression());
			}
		}
		acceptIt(TokenKind.CloseParentheses);
		return argL;
	}
	
	// { Statement* }
	private StatementList parseStatementList() throws Exception{
		acceptIt(TokenKind.OpenCurlyBracket);
		StatementList stmtList = new StatementList();
		while(token.kind!=TokenKind.CloseCurlyBracket) 
			stmtList.add(parseStatement());
		accept();
		return stmtList;
	}
	
	// expr:(expr:)?:expr
	private RangeExpr parseRangeExpr() throws Exception{
		Expression base = parseExpression();
		return parseRangeExprTail(base);
	}
	
	//(expr:)?:expr
	private RangeExpr parseRangeExprTail(Expression base) throws Exception{
		acceptIt(TokenKind.Colon);
		Expression secondExpr = parseExpression();
		if(token.kind==TokenKind.Colon) {
			Expression thirdExpr = parseExpression();
			return new RangeExpr(base,secondExpr,thirdExpr,base.posn);
		}
		return new RangeExpr(base,secondExpr,base.posn);
	}
	
	// static?
	private boolean parseAccess() throws Exception{
		if(token.kind == TokenKind.Static) {
			accept();
			return true;
		}	
		return false;
	}
	
	// final?
	private boolean parseFinal() throws Exception{
		if(token.kind == TokenKind.Final) {
			accept();
			return true;
		}	
		return false;
	}
	
	// Helper Methods
	private void accept() throws Exception {
		acceptIt(token.kind);
	}
	
	private void takeFromQueue() throws Exception {
		queue.add(token);
		accept();
	}
	
	private void addToQueue() throws Exception {
		addToQueue(1);
	}
	
	private void addToQueue(int amount) throws Exception {
		acceptQueue=false;
		for(int i=0;i<amount;i++) {
			queue.add(token);
			accept();
		}
		acceptQueue=true;
	}
	
	private void acceptIt(TokenKind expected) throws Exception {
		if (token.kind == expected) {
			reporter.addTrace("Parser accepted: "+token.spelling);
			if(!acceptQueue || queue.size()==0) {
				token = scan.getCurrentResult();
				pool.addTask(scan);
				reporter.addTrace("Scanner made token: " + token);
			}else {
				token = queue.poll();
				reporter.addTrace("Queue retrieved token: " + token);
			}
		}else
			parseError("expected '" + expected +"' but found '" + token.kind + "'");
	}
	
	private void acceptItWithSpelling(TokenKind expectedKind, String spelling) throws Exception {
		if(spelling.equals(token.spelling))
			acceptIt(expectedKind);
		else
			parseError("expected spelling '" + spelling +"' but found '" + token.spelling + "'");
	}
	
	private void parseError(String e) throws SyntaxException {
		reporter.reportError("Parse error: " + e);
		reporter.addTrace(threadName+" has found an error");
		pTrace();
		throw new SyntaxException();
	}
	
	private void pTrace() {
		StackTraceElement [] stl = Thread.currentThread().getStackTrace();
		for (int i = stl.length - 1; i > 0 ; i--) {
			if(stl[i].toString().contains("parse"))
				reporter.addTrace(stl[i].toString());
		}
		reporter.addTrace("accepting: " + token.kind + " (\"" + token.spelling + "\")");	
	}
	
	private boolean containsSpelling(String [] arr) {
		return arrContains(token.spelling,arr);
	}
	
}
