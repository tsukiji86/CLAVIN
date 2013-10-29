# Plugin Architecture 

Proposed architecture 

A plugin consists of a single jar that holds all the dependencies of the plugin (less 
dependencies included in CLAVIN itself)

Should be able to build against multiple version of CLAVIN (1.1, 1.0 etc)

Jar is cryptographically signed. 

Plugins reside in home directory under .clavin 

For an Apple, this would be ~/.clavin/plugins

Plugins will use semantic versioning. 


Example namespace: 

com.bericotech.clavin.plugin.ipaddress

and

com.bericotech.clavin.plugin.ipaddress.extractor

Example archetype parameters for clavin-plugin-hello

    Group Id: com.bericotech 
    Artifact Id: clavin-plugin-hello 
    Version: 0.1.0   # start with 0.1.0 starting out 
    Package: come.bericotech.clavin.plugin.hello











