package mmj.SyntacticAnalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;


public class Scanner {
	
	private final InputStream input;
	private final CompilerReporter reporter;
	private final KeywordMapping km;
	
	private char currentChar;
	private StringBuilder currentSpelling;

	private boolean eof = false;
	private int line,spellingCol,curCol;

	
	public Scanner(File f) throws FileNotFoundException {
		reporter=Factory.getCompilerReporter();
		input=new FileInputStream(f);
		line=1;
		spellingCol=1;
		curCol=1;
		km = new KeywordMapping();
		readChar();
	}
	
	public Token scan() {	
		//Skipping white space
		removeWhitespace();
		currentSpelling = new StringBuilder();
		
		TokenKind tk = scanToken();
		SourcePosition sp = new SourcePosition(line,spellingCol);
		String spelling = currentSpelling.toString();
		
		spellingCol=curCol;

		if(tk == TokenKind.Instanceof)
			return new Token(TokenKind.Binop,"instanceof",sp);
		return new Token(tk,spelling,sp);
	}
	
	
	
	private TokenKind scanToken() {
		if(eof)
			return TokenKind.EOF;
		
		//Handles Numbers
		if(isNumeric(currentChar)) {
			while(isNumeric(currentChar))
				takeChar();
			return TokenKind.Number;
		}
		
		//Handles Keywords/IDs
		if(isAlpha(currentChar)) {
			while(isAlpha(currentChar)||isNumeric(currentChar)|| currentChar == '_')
				takeChar();
			return determineAlphaNumCharSeq(currentSpelling.toString());
		}
		
		switch(currentChar) {
		case '(': 
			takeChar();
			return TokenKind.OpenParentheses;	
		case '{': 
			takeChar();
			return TokenKind.OpenCurlyBracket;
		case '[': 
			takeChar();
			return TokenKind.OpenSquareBracket;
		case ']': 
			takeChar();
			return TokenKind.CloseSquareBracket;
		case '}': 
			takeChar();
			return TokenKind.CloseCurlyBracket;
		case ')': 
			takeChar();
			return TokenKind.CloseParentheses;
		case '.': 
			takeChar();
			return TokenKind.Period;
		case ',': 
			takeChar();
			return TokenKind.Comma;
		case ';':
			takeChar();
			return TokenKind.SemiColon;
		case '?':
			takeChar();
			return TokenKind.QuestionMark;
		case ':':
			takeChar();
			return TokenKind.Colon;
		case '=':
			takeChar();
			if(currentChar=='=') {
				takeChar();
				return TokenKind.Binop;
			}
			return TokenKind.Assignment;
		case '!': case '~':
			takeChar();
			if(currentChar=='=') {
				takeChar();
				return TokenKind.Binop;
			}
			return TokenKind.Unop;
		case '<': 
			takeChar();
			//<< or <<=
			if(currentChar=='<') {
				takeChar();
				return checkAssignment();
			//< or <=
			}else 
				return checkAssignment();
		case '>': 
			takeChar();
			//>>, >>>, >>=, or >>>=
			if(currentChar=='>') {
				takeChar();
				//>>>, or >>>=
				if(currentChar=='>') 
					return checkAssignment();
				else 
					return checkAssignment();
			//> or >=
			}else 
				return checkAssignment();
		case '-': 
			takeChar();
			if(currentChar=='>') {
				takeChar();
				return TokenKind.LambdaArrow;
			}else if(currentChar=='-') {
				takeChar();
				return TokenKind.Unop;
			}else
				return checkAssignment();
		case '+':
			takeChar();
			if(currentChar=='+') {
				takeChar();
				return TokenKind.Unop;
			}else
				return checkAssignment();
		case '%': case '^':
			takeChar();
			return checkAssignment();
		case '&': case '|': case '*':
			char c=currentChar;
			takeChar();
			if(currentChar==c) {
				takeChar();
				return TokenKind.Binop;
			}else
				return checkAssignment();
		case '\'':
			nextChar();
			if(currentChar=='\\') {
				nextChar();
				switch(currentChar) {
				case 't': 	currentSpelling.append('\t'); break;
				case 'b': 	currentSpelling.append('\b'); break;
				case 'n': 	currentSpelling.append('\n'); break;
				case 'r': 	currentSpelling.append('\r'); break;
				case 'f': 	currentSpelling.append('\f'); break;
				case '\'': 	currentSpelling.append('\''); break;
				case '"': 	currentSpelling.append('\"'); break;
				case '\\': 	currentSpelling.append('\\'); break;
				default:
					reporter.reportError("Invalid escape character '"+currentChar+"'");
					eof=true;
					return TokenKind.ERROR;
				}
				nextChar();
			}else
				takeChar();
			if(currentChar!='\'') {
				reporter.reportError("Character missing closing '");
				eof=true;
				return TokenKind.ERROR;
			}
			nextChar();
			return TokenKind.Char;
		case '"':
			nextChar();
			while(!(currentChar=='"')) {
				if(eof) {
					reporter.reportError("String Literal does not end");
					break;
				}
				if(currentChar=='\\') {
					nextChar();
					switch(currentChar) {
					case 't': 	currentSpelling.append('\t'); break;
					case 'b': 	currentSpelling.append('\b'); break;
					case 'n': 	currentSpelling.append('\n'); break;
					case 'r': 	currentSpelling.append('\r'); break;
					case 'f': 	currentSpelling.append('\f'); break;
					case '\'': 	currentSpelling.append('\''); break;
					case '"': 	currentSpelling.append('\"'); break;
					case '\\': 	currentSpelling.append('\\'); break;
					default:
						reporter.reportError("Invalid escape character '"+currentChar+"'");
						eof=true;
						return TokenKind.ERROR;
					}
					nextChar();
				}else
					takeChar();
			}
			nextChar();
			return TokenKind.StringLiteral;
		case '/':
			takeChar();
			if(currentChar=='/') {
				currentSpelling = new StringBuilder();
				while(!(currentChar=='\n'||currentChar=='\r'||eof))
					nextChar();
				removeWhitespace();
				return scanToken();
			}else if(currentChar=='*') {
				nextChar();
				outer:
				for(;;) {
					while(currentChar!='*') {
						nextChar();
						if(eof) {
							reporter.reportError("Comment does not end");
							break outer;
						}
						if(currentChar == '\n')
							line++;
					}
					nextChar();
					if(currentChar=='/')
						break;
				}
				nextChar();
				removeWhitespace();
				currentSpelling = new StringBuilder();
				return scanToken();
			}else
				return checkAssignment();
		default:
			reporter.reportError("Unknown character");
			return TokenKind.ERROR;
		}
	}

	//Handles traversing
	private void takeChar() {
		curCol++;
		currentSpelling.append(currentChar);
		nextChar();
	}
	
	private void removeWhitespace() {
		while(!eof && isWhiteSpace(currentChar))
			nextChar();
	}
	
	private void nextChar() {
		if(!eof)
			readChar();
	}
	
	private void readChar() {
		try {
			int nextChar=input.read();
			if(nextChar==-1) {
				reporter.addTrace("Scanner has reached the end of the file");
				eof=true;
				currentChar=0;
			}else
				currentChar=(char)nextChar;
		} catch (IOException e) {
			reporter.reportError("I/O Exception when Reading char");
			eof=true;
			currentChar=0;
		}
	}
	
	
	//character checks
	private boolean isNumeric(char c) {
		return (c >= '0') && (c <= '9');
	}
	
	private boolean isAlpha(char c) {
		return ((c >='a')&&(c<='z')) || ((c >='A')&&(c<='Z'));
	}

	private TokenKind determineAlphaNumCharSeq(String s) {
		TokenKind tk = km.getTokenKind(s);
		if(tk==null)
			return TokenKind.ID;
		return tk;
	}
	
	private boolean isWhiteSpace(char c) {
		switch(c) {
		case ' ':
			curCol++;
			spellingCol = curCol;
			return true;
		case '\n':
			curCol=1;
			spellingCol=curCol;
			line++;
		case '\r':
			return true;
		case '\t':
			curCol+=4;
			spellingCol=curCol;
			return true;
		}
		return false;
	}
	
	private TokenKind checkAssignment() {
		if(currentChar=='=') {
			takeChar();
			return TokenKind.Assignment;
		}	
		return TokenKind.Binop;
	}
	
}
