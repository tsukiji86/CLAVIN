# Plugin Architecture 

Proposed architecture 

A plugin consists of a single jar that holds all the dependencies of the plugin (less 
dependencies included in CLAVIN itself)

Should be able to build against multiple version of CLAVIN (1.1, 1.0 etc)

Jar is cryptographically signed. 

Plugins reside in home directory under .clavin 

For an Apple, this would be ~/.clavin/plugins

Plugins will use semantic versioning. 




