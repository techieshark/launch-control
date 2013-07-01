06/08/2005

Things have changed quite a bit. Here's my (Andrew's) "build rocketview for
dummies" instructions 

Launchcontrol has been rolled into rocketview, so now all you neeed you to is
build and run rocketview:

$ cd java
$ ant jar

You should get a BUILD SUCCESSFUL message. Now copy over the configuration
files that rocketview needs when it's running the launchcontrol bits.

$ cd dist
$ cp ../src/launchcontrol/*.conf .

And now to run rocketview:

$ java -jar rocketview.jar

and to run launchcontrol:

$ java -jar rocketview.jar launchcontrol

9/13/2003 jamey: fixed up the ant build file; here's how to use it

To use ant, create a ~/.antrc file setting
 * JAVA_HOME to the directory containing bin/java and lib/tools.jar
 * ANT_OPTS and LD_LIBRARY_PATH to point at the java/ext directory
   from CVS

My .antrc looks like this:

EXT_DIR=~/cvs/psas/java/ext
export JAVA_HOME=/usr/lib/j2sdk-1.4.2/
ANT_OPTS=-Djava.ext.dirs=$EXT_DIR
export LD_LIBRARY_PATH=$EXT_DIR

The default build target is compile; `ant` and `ant compile` are
equivalent. Also available are 'launchcontrol' and 'rocketview'
targets which run their respective applications.


6/9/2003 dcassard: checked in big changes to cansocket, rocketview and
	stripchart subtrees

I couldn't get the ant build.xml to work on my system, so I provided
the brain-damaged build_script, a bash script that builds rocketview.
The manualuplink and launchcontrol directories are commented out,
but they could be added back in by someone who knows how those
subtrees should be built.

To get your very own copy of rocketview, pick a convenient directory
and do:
    % cvs checkout java
    % cd java
    % build_script

The complied and packaged code is all in the build/ directory, so
you can run rocketview with:
    % java -jar build/psas.jar

Also in the build/ subdirectory is rocketview.tar.gz, which
you can take to any java platform and unzip with some equivalent of:
    % gunzip <rocketview.tar.gz | tar xf -
This will populate your default direcdtory with all the jars
you need to do:
    % java -jar psas.jar

Rocketview is a listen-only program.  It listens for all UDP addresses
on port 4441 by default.  You can override the port on the command
line with:
    % java -jar psas.jar <port>

There is a src/rocketview/Read.me file that indexes
Can IDs, their Observer classes, and how messages bytes are
interpreted.

------------------------------------------------------------------
still to do:
	* many observers are only implemented as skeletons
	* the CanBusIDs file needs to be updated with the latest
		from the Flight Computer and hardware changes
	* Jamey's LogCanSocket funcionality is broken, i.e.
		rocket view doesn't log messages to a disk file.

