package com.bericotech.clavin.cli;

import java.util.Arrays;
import java.util.List;

import com.bericotech.clavin.WorkflowDemo;
import com.bericotech.clavin.index.IndexDirectoryBuilder;
import com.bericotech.clavin.resolver.ResolvedLocation;

public class Main {

	
	// TODO: pull version out of build
	private static void version() {
		System.out.println("CLAVIN 1.1-SNAPSHOT" );
	}
	
	// TODO: subcommand output should check for available plugins
	private static void usage() { 

		String s = "";
		s += "\tUsage: clavin [-v] [-h] command [<args>]\n";
		s += "\t-v, --version                    Print the version and exit.\n";
		s += "\t-h, --help                       Print this help.\n";
		s += "\n";
		s += "Available subcommands:\n";
		s += "\tindex\n";
		s += "\tdemo\n";
		//s += "\tplugin\n";
    
		System.out.print(s);
		System.exit(0);
}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		// Using a very simplistic argument parser for now 
		// TODO: replace with a real CLI parsing library
						
		if (args.length == 0)
			usage();
		
		if (args[0].equals("-v") || args[0].equals("--version"))
			version();
		
		if (args[0].equals("-h") || args[0].equals("--help")) 
			usage();
			
		if (args[0].equals("index")) {
			// index the data
			// this should take more options such as a path to the gazetteer location
			// as well as the path to the index directory
			IndexDirectoryBuilder.main(Arrays.copyOfRange(args, 1, args.length));
			
		} else if (args[0].equals("demo")) {
			// run the workflow demo
			WorkflowDemo.main(Arrays.copyOfRange(args, 1, args.length));
			
		} else {
			System.out.println("Unknown command");
			usage();
		}
		
		        
	}

}
