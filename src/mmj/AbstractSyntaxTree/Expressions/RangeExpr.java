package mmj.AbstractSyntaxTree.Expressions;

import mmj.AbstractSyntaxTree.ASTVisitor;
import mmj.AbstractSyntaxTree.Terminals.IntegerLiteral;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.SyntacticAnalyzer.Token;
import mmj.SyntacticAnalyzer.TokenKind;
import mmj.Tools.KeyConstants;

public class RangeExpr extends Expression{
	public final Expression start,incr,end;
	
	private static final Expression DEFAULT_INCR = new LiteralExpr(new IntegerLiteral(
			new Token(TokenKind.Int,"1",KeyConstants.errPosn)),KeyConstants.errPosn);
	
	public RangeExpr(Expression start, Expression end, SourcePosition sp) {
		this(start,DEFAULT_INCR,end,sp);
	}
	
	public RangeExpr(Expression start, Expression incr, Expression end, SourcePosition sp) {
		super(sp);
		this.start=start;
		this.end=end;
		this.incr=incr;
	}

	@Override
	public void visit(ASTVisitor v) {
		v.visitRangeExpr(this);
	}
}
