What is this?
This Maven module is aimed at generating a Zip file containing everything needed for immediate use: binaries, sources, javadoc, readme, license, etc.

How to run it?
From parent project (root of JavaSimon source tree, not from dist project)
  mvn -Pdist clean install

How doest it work?
1) Enables the dist Maven profile:
2) In parent project, generates a Jar file containing aggregated sources from all modules
3) In parent project, generates javadoc for all modules and put it a Jar file
4) In modules, everything is compiled, tested and packaged as usual
5) In dist module (which is included in the build only when dist profile is activated), runs a Maven Assembly to build a Zip file containing everything
