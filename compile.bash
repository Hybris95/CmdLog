#!/bin/bash
javac -cp "./class:./jars/craftbukkit.jar" -d "./class" "./src/com/hybris/bukkit/cmdLog/CmdLog.java"
cd ./class
jar cvf "CmdLog.jar" ./plugin.yml ./com/
mv CmdLog.jar ../jars/
