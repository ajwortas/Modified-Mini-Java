package mmj.SyntacticAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class KeywordMapping {

	private final Map<String, TokenKind> map;
	
	public KeywordMapping() {
		map = new HashMap<String, TokenKind>();
		map.put("class", TokenKind.Class);
		map.put("public", TokenKind.Public);
		map.put("protected", TokenKind.Protected);
		map.put("private", TokenKind.Private);
		map.put("void", TokenKind.Void);
		map.put("static", TokenKind.Static);
		map.put("int", TokenKind.Int);
		map.put("boolean", TokenKind.Boolean);
		map.put("this", TokenKind.This);
		map.put("return", TokenKind.Return);
		map.put("if",TokenKind.If);
		map.put("else", TokenKind.Else);
		map.put("while", TokenKind.While);
		map.put("new", TokenKind.New);
		map.put("true", TokenKind.True);
		map.put("false", TokenKind.False);
		map.put("interface", TokenKind.Interface);
		map.put("implements", TokenKind.Implements);
		map.put("extends", TokenKind.Extends);
		map.put("super", TokenKind.Super);
		map.put("instanceof", TokenKind.Instanceof);
		map.put("for", TokenKind.For);
		map.put("final", TokenKind.Final);
		map.put("method", TokenKind.Method);
		map.put("do", TokenKind.Do);
		map.put("curry", TokenKind.Curry);
		map.put("free", TokenKind.Free);
		map.put("lambda", TokenKind.Lambda);
		map.put("barrier", TokenKind.Barrier);
		map.put("forall", TokenKind.Forall);
	}
	
	public TokenKind getTokenKind(String key) {
		return map.get(key);
	}
	
}
