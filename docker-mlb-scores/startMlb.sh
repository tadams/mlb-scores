#! /bin/sh

jarFile=$(ls /opt/*.jar)
echo "*** starting application: ${jarFile}"
java -jar $jarFile
