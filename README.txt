Name: Rushabh Bagadia, Subramaniam Seshadri, Paras Jagani 

Project Name: SCM

Team Initial: BJS

Contact info: rushabh2208@gmail.com, subramaniam.seshadri@outlook.com, parasjagani11@gmail.com


Class number: 6236


Section: CECS 543-02

Project part: Part 2

Intro:	This is the second part of our SCM (Source Code Management) project. In this project part, we add
	three new features: check-out, check-in (mostly already done), and labeling.
	The labeling feature allows the user to add a label (a text string) to a manifest file, in order to make
	it easier to remember. A manifest file must support up to 4 different labels at the same time. We can
	presume that the user is nice and always supplies a unique label – so we don't have to check for the
	label already existing in some other manifest file. A label is supposed to uniquely ID a manifest.
	The check-out ability lets a user recreate a specific version of the project tree. They do this by
	selecting a particular manifest file from the repo. The manifest specifies every version of every file
	required. The recreated project tree is installed in an empty directory, which the user also selects. The
	repo gets a new manifest file of the checked out version (for its records). The user should be able to
	specify the manifest file using a label.
	The check-in ability lets the user update the repository (repo) with changed files in the project tree.
	Each check-in is a (potentially) different "version" of the project tree, and gets an associated manifest
	file created for it. This allows the user to track modification history from a given project tree back,
	through various project versions, all the way to the repo's creation. Labels are forever, so we assume
	the user doesn't change his/her mind later.

External Requirements:	1. Java version 1.8 is required for the SCM project to run.
			
			2. Apache commons library (org.apache.commons.io.jar)

Build, Installation and Setup:	
			Refer the following to build and run the project:
	
			1. Change the directory to the 543-p2_BJS\src\ folder.
	
			2. Execute the following commands: (Commands used for compiling and adding jar files)	
				a. javac -cp ".;..\lib\org.apache.commons.io.jar" .\com\csulb\ase\model\*.java
				b. javac -cp ".;..\lib\org.apache.commons.io.jar" .\com\csulb\ase\service\*.java
		
				c. javac -cp ".;..\lib\org.apache.commons.io.jar" .\com\csulb\ase\scm\*.java
	
			3. To run the program, execute the following command:
		
				java -cp ".;..\lib\org.apache.commons.io.jar" com.csulb.ase.scm.Repository

Files List:
1. Repository.java
2. Helper.java
3. Manifest.java
4. FileProperties.java

Extra Features: Project stores the labels and corresponding manifest file names in an index.properties file.

Bugs: None detected