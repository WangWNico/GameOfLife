@REM Helper script to run tasks on this program

@REM sets variables
@ECHO OFF
@FOR %%A IN ("%~dp0.") DO SET base=%%~dpA
@SET "src=%base%\src\main\java"
@SET "script=%0"
@SET "arg1=%1"
@ECHO ON

@REM If/Else chain for the task to execute
@IF "%arg1%"=="build" (
	@ECHO Compiling code...
	@javac -d "%src%" -sourcepath "%src%" "%src%\*.java"
	@ECHO Compiled into "%src%"
) ELSE IF "%arg1%"=="run" (
	@ECHO Running program...
	@Echo %src%
	@java -cp "%src%;." GameOfLife %2 %3 %4
) ELSE IF "%arg1%"=="javadoc" (
	@ECHO Compiling Javadocs...
	@javadoc -d docs -version -sourcepath "%src%" "%src%\GameOfLife.java"
	@ECHO Javadocs are compiled and located at "%base%\docs\index.html"
) ELSE IF "%arg1%"=="all" (
	@ECHO Compiling Javadocs...
	@javadoc -d docs -version -sourcepath "%src%" "%src%\GameOfLife.java"
	@ECHO.
	@ECHO Javadocs are compiled and located at "%base%\docs\index.html"
	@ECHO.

	@ECHO Compiling code...
	@javac -d "%src%"" -sourcepath "%src%" "%src%\*.java"
	@ECHO Compiled into "%src%"
	@ECHO.
	
	@ECHO Running program...
	@java -cp "%src%;." GameOfLife %*
) ELSE IF "%arg1%"=="help" (
	@goto HELP
) ELSE (
	@REM default or unknown task is called
	@goto HELP
)

@exit /b


@REM help message to print
:HELP
	@SET "TAB=	"
	@ECHO How to use %0:
	@ECHO %0 javadoc
	@ECHO %TAB%- this compiles all of the Javadocs in the program into ./docs/
	@ECHO %0 build
	@ECHO %TAB%- this compiles the java code into bytecode into "%src%"
	@ECHO %0 run ...
	@ECHO %TAB%- this runs the compiled butecode
	@ECHO %0 all
	@ECHO %TAB%- runs all of the previous options in order
	@ECHO %0 help
	@ECHO %TAB%- displays this help message
	@exit /b
