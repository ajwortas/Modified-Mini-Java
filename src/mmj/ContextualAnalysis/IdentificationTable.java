package mmj.ContextualAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmj.AbstractSyntaxTree.Declarations.Declaration;
import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;

public class IdentificationTable {
	private HashMap<String, Declaration> classScope;
	private HashMap<String, List<Declaration>> memberScope;
	private List<HashMap<String, Declaration>> statementScope = new ArrayList<>();
	private final CompilerReporter reporter;
	private final InheritanceHelper inheritanceHelper;
	private int level=0;
	private static final int classLevel=0,memberLevel=1,statementlevel=2;
	
	public IdentificationTable(InheritanceHelper helper) {
		this.reporter=Factory.getCompilerReporter();
		this.inheritanceHelper=helper;
	}
	
	private IdentificationTable(InheritanceHelper helper, HashMap<String, Declaration> classLevel, HashMap<String, List<Declaration>> memberLevel, List<HashMap<String, Declaration>> statementLevel, int level) {
		this(helper);
		this.classScope=classLevel;
		this.memberScope=memberLevel;
		this.statementScope=statementLevel;
		this.level=level;
	}
	
	public IdentificationTable clone() {
		List<HashMap<String, Declaration>> clonedTable = new ArrayList<>();
		for(HashMap<String, Declaration> map:statementScope)
			clonedTable.add(map);
		return new IdentificationTable(inheritanceHelper,classScope,memberScope,clonedTable,level);
	}
	
	public void open() {
		if(level==classLevel)
			classScope = new HashMap<>();
		else if(level==memberLevel)
			memberScope = new HashMap<>();
		else
			statementScope.add(new HashMap<>());
		level++;
	}
	
	public void isolatedMethodScope() {
		classScope = new HashMap<>();
		memberScope = new HashMap<>();
		statementScope.clear();
		level=2;
	}
	
	public void close() {
		if(level==classLevel)
			classScope = null;
		else if(level==memberLevel)
			memberScope = new HashMap<>();
		else if(level>=statementlevel)
			statementScope.remove(level-statementlevel);
		else
			return;
		reporter.addThreadedTrace("Scope was removed at level "+level);
		level--;
	}
	
	public boolean enter(Declaration decl) {
		return enter(decl.name,decl);
	}
	
	public boolean enter(String name, Declaration decl) {
		if(level<0||level>statementScope.size()+statementlevel-1) {
			reporter.reportError("Trying to put to unknown scope level");
			return false;
		}
		if(level==classLevel) {
			if(classScope.containsKey(name)) {
				reporter.reportError(name+" already exists in the class scope");
				return false;
			}
			reporter.addThreadedTrace(name+" was added to the class level scope");
			classScope.put(name, decl);
			return true;
		}
		if(level==memberLevel) {
			if(memberScope.containsKey(name))
				if(inheritanceHelper.memberPutCheck(decl, memberScope.get(name))) {
					reporter.addThreadedTrace(name+" was added to the member level scope");
					memberScope.get(name).add(decl);
					return true;
				}else {
					reporter.reportError(name+" already exists in the class scope");
					return false;
				}
					
			List<Declaration> declList = new ArrayList<>();
			declList.add(decl);
			memberScope.put(name, declList);
			return true;
		}
		
		for(HashMap<String, Declaration> map:statementScope)
			if(map.containsKey(name)) {
				reporter.reportError(name+" already exists in the method scope");
				return false;
			}
		reporter.addThreadedTrace(name+" was added to the method level scope");
		statementScope.get(level-statementlevel).put(name, decl);
		return true;
	}
	
	public Declaration retrieve(String name) {
		for(int i=level-statementlevel;i>=0;i--) 
			if(statementScope.get(i).containsKey(name)) {
				reporter.addThreadedTrace(name+" was retrieved from the statement level scope");
				return statementScope.get(i).get(name);
			}	
		if(memberScope!=null && memberScope.containsKey(name)) {
			reporter.addThreadedTrace(name+" was retrieved from the member level scope");
			return memberScope.get(name).get(0);
		}
		if(classScope!=null && classScope.containsKey(name)) {
			reporter.addThreadedTrace(name+" was retrieved from the class level scope");
			return classScope.get(name);
		}
		reporter.reportError(name+" was not able to be found");
		return null;
	}
	
	public Declaration getClass(String name) {
		if(classScope.containsKey(name))
			return classScope.get(name);
		return null;
	}

	public List<Declaration> getMember(String name){
		if(memberScope.containsKey(name))
			return memberScope.get(name);
		return null;
	}
	
}
