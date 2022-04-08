package mmj.SyntacticAnalyzer;

public enum TokenKind {
	//Keywords
		Class,
		Public,
		Protected,
		Private,
		Void,
		Static,
		Int,
		Boolean,
		This,
		Return,
		If,
		Else,
		While,
		New,
		True,
		False,
		Interface,
		Implements,
		Extends,
		Super,
		Instanceof,
		For,
		Final,
		Method,
		Lambda,
		Curry,
		Free,
		Do,
		Forall,
		Barrier,
		Break,
		Continue,
	//Operators
		Binop,
		Assignment,
		Unop,
	//Syntax
		OpenCurlyBracket,
		CloseCurlyBracket,
		OpenSquareBracket,
		CloseSquareBracket,
		Dash,
		OpenParentheses,
		CloseParentheses,
		SemiColon,
		Colon,
		QuestionMark,
		Comma,
		Period,
		LambdaArrow,
	//Variables	
		ID,
		StringLiteral,
		Char,
		Number,
		ArrayLiteral, 
		Null,
	//Other
		ERROR,
		EOF, 
}
