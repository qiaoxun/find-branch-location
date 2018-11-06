@echo off

set Origin_JAVA_HOME=%JAVA_HOME%
set Origin_Path=%JAVA_HOME%/bin

set JAVA_HOME=C:\java\8\jdk
set Path=%JAVA_HOME%\bin

java -cp "C:\Users\test\project\findBranchLocation\find-branch-location-1.0.jar;C:\Users\test\project\findBranchLocation\hamcrest-core-1.3.jar;C:\Users\test\project\findBranchLocation\JavaEWAH-1.1.6.jar;C:\Users\test\project\findBranchLocation\jsch-0.1.54.jar;C:\Users\test\project\findBranchLocation\jzlib-1.1.1.jar;C:\Users\test\project\findBranchLocation\org.eclipse.jgit-5.1.3.201810200350-r.jar;C:\Users\test\project\findBranchLocation\slf4j-api-1.7.22.jar;C:\Users\test\project\findBranchLocation\slf4j-jdk14-1.7.2.jar" com.tools.solution2.App

set JAVA_HOME=%Origin_JAVA_HOME%
set Path=%Origin_Path%

REM pause