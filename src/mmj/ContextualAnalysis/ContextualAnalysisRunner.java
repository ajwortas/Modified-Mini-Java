package mmj.ContextualAnalysis;

import java.util.ArrayList;
import java.util.List;

import mmj.AbstractSyntaxTree.Package;
import mmj.AbstractSyntaxTree.Declarations.ClassDecl;
import mmj.AbstractSyntaxTree.Declarations.Declaration;
import mmj.AbstractSyntaxTree.Declarations.InterfaceDecl;
import mmj.Tools.Joiner;
import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;
import mmj.Tools.InternalClasses;
import mmj.Tools.ThreadPool;

public class ContextualAnalysisRunner {
	
	private final ThreadPool pool;
	private final CompilerReporter reporter;
	private final IdentificationTable unboundScope;
	private final InheritanceHelper helper;
	
	public ContextualAnalysisRunner(ThreadPool tp) {
		pool=tp;
		reporter=Factory.getCompilerReporter();
		helper = new InheritanceHelper();
		unboundScope = new IdentificationTable(helper);
		unboundScope.isolatedMethodScope();
	}
	public ContextualAnalysisRunner(CompilerReporter cr, IdentificationTable unboundScope, InheritanceHelper h) {
		pool=null;
		reporter=cr;
		helper = h;
		this.unboundScope = unboundScope;
	}
	
	//Only the main thread should run this method. Helper threads will be tasked with evaluating each interface and breaking each
	//class down into method tasks to be completed
	public void analyze(Package p) throws InterruptedException {
		IdentificationTable table = new IdentificationTable(helper);
		List<Declaration> totalDecls = resolveClassScope(p,table);
		Joiner joiner = new Joiner(totalDecls.size());
		
//		for(Declaration decl:totalDecls) 
//			pool.addTask(new ContextualAnalysisTask(decl, reporter, joiner, table.clone(), helper));
		
		joiner.join();
	}

	private List<Declaration> resolveClassScope(Package p, IdentificationTable table) {
		List<Declaration> retval = new ArrayList<Declaration>();
		table.open();
		for(ClassDecl cd:p.classList) {
			table.enter(cd.name, cd);
			retval.add(cd);
		}
		for(InterfaceDecl id:p.interfaceList) {
			table.enter(id.name, id);
			retval.add(id);
		}
		for(ClassDecl internal:InternalClasses.getInternalClasses())
			table.enter(internal.name, internal);
		return retval;	
	}
	
}
