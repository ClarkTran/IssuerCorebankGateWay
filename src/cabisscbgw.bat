@echo off

SET TICIS_HOME=D:\\TransInfotech\\CAB_workspace\\IssuerCorebankGateWay\bin
SET LOYALTY_HOME=D:\\TransInfotech\\CAB_workspace\\IssuerCorebankGateWay

REM SET THE JAVA_HOME
SET JAVA_HOME=C:\Program Files\Java\jdk1.7.0_71

REM CHECK IF LOYALTY IS INSTALLED IN THE HOME DIRECTORY
REM if not exist %LOYALTY_HOME%\LOYALTY.jar goto NO_LOYALTY

REM CHANGE THE CURRENT WORKING DIRECTORY TO THE LOYALTY HOME
cd %TICIS_HOME%

REM SET THE PARAMETER ENTERED VALUE TO A VARIABLE ARG0

SET ARG0=NONE

if not "%1" == "" SET ARG0=%1

REM CHECK FOR THE JAVA HOME
if not exist "%JAVA_HOME%\bin\java.exe" goto NO_JAVA

REM ALL THE PARAMETERS ARE OK.

REM SET THE CLASSPATH

SET CLASSPATH=

echo %classpath%

echo AFTER CLASSPATH

SET LOYALTYPATH=%TICIS_HOME%;%LOYALTY_HOME%\lib\cryptix\cryptix32.jar;%LOYALTY_HOME%\lib\mail\activation.jar;%LOYALTY_HOME%\lib\mail\mail.jar;%LOYALTY_HOME%\lib\mail\mailapi.jar;%LOYALTY_HOME%\lib\mail\smtp.jar;
SET LOYALTYPATH=%LOYALTYPATH%;C:\NEWCACIS_MONITORING\OpenJMS\lib\jms-1.1.jar;C:\NEWCACIS_MONITORING\OpenJMS\lib\derby-10.1.1.0.jar;C:\NEWCACIS_MONITORING\OpenJMS\lib\openjms-0.7.7-alpha-3.JAR;C:\NEWCACIS_MONITORING;
SET LOYALTYPATH=%LOYALTYPATH%;%LOYALTY_HOME%\lib\jpos\commons-collections-2.1.1.jar;%LOYALTY_HOME%\lib\jpos\commons-beanutils.jar;%LOYALTY_HOME%\lib\jpos\commons-digester.jar;
SET LOYALTYPATH=%LOYALTYPATH%;%LOYALTY_HOME%\lib\jpos\commons-collections-2.0.jar;%LOYALTY_HOME%\lib\jpos\jdbm-0.20.jar;%LOYALTY_HOME%\lib\jpos\mx4j-impl.jar;%LOYALTY_HOME%\lib\jpos\mx4j-tools.jar;%LOYALTY_HOME%\lib\jpos\xalan_2_0_0.jar;%LOYALTY_HOME%\lib\jpos\xdoclet-jmx-module-1.2.jar;%LOYALTY_HOME%\lib\jpos\xerces_1_2_3.jar;%LOYALTY_HOME%\lib\jpos\ant.jar;%LOYALTY_HOME%\lib\jpos\commons-logging.jar;%LOYALTY_HOME%\lib\jpos\jdom.jar;%LOYALTY_HOME%\lib\jpos\log4j.jar;%LOYALTY_HOME%\lib\jpos\mx4j-jmx.jar;%LOYALTY_HOME%\lib\jpos\optional.jar;%LOYALTY_HOME%\lib\jpos\xdoclet-1.2.jar;%LOYALTY_HOME%\lib\jpos\xjavadoc-1.0.2.jar;%LOYALTY_HOME%\lib\jpos\xdoclet-mx4j-module-1.2.jar;%LOYALTY_HOME%\lib\jpos\jpos.jar;
SET LOYALTYPATH=%LOYALTYPATH%;%LOYALTY_HOME%\lib\db\commons-dbcp-1.2.1.jar;%LOYALTY_HOME%\lib\db\commons-pool.jar;%LOYALTY_HOME%\lib\db\commons-collections.jar;
SET LOYALTYPATH=%LOYALTYPATH%;%LOYALTY_HOME%\lib\db\ojdbc14.jar;
SET CLASSPATH=%LOYALTYPATH%

REM SET THE CLASS NAME TO BE EXECUTED
SET CLASSNAME = com.transinfo.tplus.TPlusAdminDaemon

echo %CLASSPATH%

if %ARG0% == -start ( 
	SET CLASSNAME=com.transinfo.tplus.TPlusAdminDaemon
	goto CONTINUE
)

if %ARG0% == -stop (
	SET CLASSNAME=com.transinfo.tplus.TPlusStopRefresh -stop
	goto CONTINUE
)

if %ARG0% == -port (

	SET CLASSNAME=com.transinfo.tplus.TPlusStopRefresh -port %2
	goto CONTINUE
)

if %ARG0% == -voice (

	SET CLASSNAME=com.transinfo.tplus.TPlusStopRefresh -voice
	goto CONTINUE
)

if %ARG0% == -echo (

	SET CLASSNAME=com.transinfo.tplus.TPlusStopRefresh -echo
	goto CONTINUE
)

REM INVALID REQUEST IDENTIFIED
echo Invalid command.
echo Usage: 
echo loyaltyexec -start to start the Acquirer server.
echo loyaltyexec -stop to stop the Acquirer server.
goto END

:CONTINUE

REM LOAD THE CLASS
::"%JAVA_HOME%\bin\java" -classpath %CLASSPATH% %CLASSNAME%

::below line will be used to debug the program on development server
"%JAVA_HOME%\bin\java" -Xdebug -classpath %CLASSPATH% -Xrunjdwp:transport=dt_socket,address=6666,server=y,suspend=y %CLASSNAME%

if errorlevel 67 goto END
goto END


:NO_TPLUS
echo Error: LOYALTY_HOME variable does not point to your Acquirer DIRECTORY.Please edit your startup script. 
pause
goto END

:NO_JAVA
echo Error: JAVA_HOME variable does not point to a Java 2
echo installation. Please edit your startup script.  
pause
goto END

:END
