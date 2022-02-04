package mmj.AbstractSyntaxTree;

import mmj.AbstractSyntaxTree.Lists.ParameterList;
import mmj.AbstractSyntaxTree.Lists.StatementList;
import mmj.AbstractSyntaxTree.TypeDenoters.TypeDenoter;
import mmj.SyntacticAnalyzer.SourcePosition;
import mmj.Tools.Factory;
import mmj.Tools.Exceptions.CounterException;

public class MethodComponents {

	public final TypeDenoter retType;
	public final ParameterList paraList;
	public final StatementList stmtList;
	public final int methodID;
	
	public MethodComponents(TypeDenoter returnType, ParameterList paraL, StatementList stmtL, SourcePosition sp) throws CounterException {
		retType=returnType;
		paraList = paraL;
		stmtList=stmtL;
		methodID = Factory.getNextMethodID();
	}
	
	public MethodComponents(MethodComponents mc, int numCurried){
		retType=mc.retType;
		stmtList=mc.stmtList;
		methodID=mc.methodID;
		paraList=new ParameterList();
		for(int i=numCurried;i<mc.paraList.size();i++)
			paraList.add(mc.paraList.get(i));
	}
	
}
