@echo off
javac -Xlint:deprecation -cp ./class;./jars/craftbukkit.jar -d ./class ./src/com/hybris/bukkit/cmdLog/CmdLog.java
cd ./class
jar cvf "CmdLog.jar" ./plugin.yml ./com/
move /Y CmdLog.jar ../jars/
pause
