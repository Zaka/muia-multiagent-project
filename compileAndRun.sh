#!/bin/sh
#
# use example:
# ./compileAndRun.sh GameOfLife.java
# you need to change these three variables
MASON_DIR=/home/zaka/ownCloud/muia/multiagentes/mason/lib/mason.19.jar
# JAVAC_CMD=/home/nswoboda/java/jdk1.8.0_60-sun/bin/javac
# JAVA_CMD=/home/nswoboda/java/jdk1.8.0_60-sun/bin/java
JAVAC_CMD=javac
JAVA_CMD=java

# you shouldn’t have to change the rest
CLASSPATH="$MASON_DIR:$CLASSPATH"
CLASSNAME=`basename $1 .java`
$JAVAC_CMD -classpath "$CLASSPATH" "$1" && \
    $JAVA_CMD -classpath "$CLASSPATH" "$CLASSNAME"
