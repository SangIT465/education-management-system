@echo off
@REM Maven Wrapper for Windows

setlocal
set DIR=%~dp0
set WRAPPER_JAR=%DIR%.mvn\wrapper\maven-wrapper.jar

if not exist "%WRAPPER_JAR%" (
    echo Maven Wrapper jar not found.
    echo Please download from: https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    echo And place in: %DIR%.mvn\wrapper\
    exit /b 1
)

if not defined JAVA_HOME (
    set JAVACMD=java
) else (
    set JAVACMD=%JAVA_HOME%\bin\java
)

"%JAVACMD%" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%DIR%" org.apache.maven.wrapper.MavenWrapperMain %*
