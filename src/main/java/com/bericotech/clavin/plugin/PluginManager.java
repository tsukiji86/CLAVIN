package com.bericotech.clavin.plugin;

public class PluginManager {

	public static void main(String[] args) {
		
		// call be using vagrant plugin 
		
		if (args.length > 1) {
			if (args[0] == "install") {
				install_plugin(args[1]);
			}
		}
		
	}
	
	
	private static void install_plugin(String plugin) {
		// check repository for plugin 
	    // this will be stored on github under releases 
		
		// github 
		// clavin-plugin-demo 
		
		// download with http client 
		
		
		
	}
	
    private static void usage_plugin(String message) {
        System.out.println("Usage: clavin plugin <command> [<args>]");
        System.out.println("");
 
        System.out.println("Available subcommands:");
        System.out.println("	install)");
        //System.out.println("    license");
        System.out.println("    list");
        //System.out.println("    uninstall");
        //System.out.println("    update");
        

        if (message != null) {
            System.out.println();
            System.out.println("Message:");
            System.out.println("   " + message);
        }
    }
	
	
}
