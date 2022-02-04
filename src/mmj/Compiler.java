package mmj;

import java.io.File;

import mmj.SyntacticAnalyzer.Parser;
import mmj.Tools.CompilerReporter;
import mmj.Tools.Factory;
import mmj.Tools.ThreadPool;
import mmj.AbstractSyntaxTree.Package;

public class Compiler {

	
	
	public static void main(String [] args) {
		
		File f = new File(args[0]);
		CompilerReporter reporter = Factory.getCompilerReporter();
		ThreadPool pool = new ThreadPool();
		
		Package pack=null;
		try {
			Parser p = new Parser(f,pool);
			pack=p.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(reporter.assessErrors()) {
			System.exit(4);
		}
		System.exit(0);
		
	}

	
}
