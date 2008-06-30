#!/bin/bash -x

DSVERSION=$(cat datasoul.xml | awk -F\< "/<version>/{ print \$2 }" | awk -F\> "{ print \$2}")
rm -Rf datasoul-$DSVERSION

# Clean up
rm -Rf installers
ant clean 
find ../src -name "*#*" | xargs rm -f

# Setup vesion
echo "version=$DSVERSION" > ../src/datasoul/version.properties

# Compile
ant jar

# Build installers
BUILDER_CMD=/opt/installbuilder-5.4.10/bin/builder
${BUILDER_CMD} build datasoul.xml linux 
${BUILDER_CMD} build datasoul.xml windows
${BUILDER_CMD} build datasoul.xml osx 
${BUILDER_CMD} build datasoul.xml rpm
${BUILDER_CMD} build datasoul.xml deb

# Clean up
ant clean

# Create source tarball
mkdir datasoul-$DSVERSION
mkdir datasoul-$DSVERSION/src
cp  -r ../src/datasoul  datasoul-$DSVERSION/src
find datasoul-$DSVERSION/src -name "CVS" | xargs rm -Rf
tar czvf installers/datasoul-$DSVERSION-source.tar.gz datasoul-$DSVERSION
rm -Rf datasoul-$DSVERSION

# Compress MacOS X installer
cd installers
OSX=$(ls -1 | grep osx)
zip -r $OSX.zip $OSX
rm -Rf $OSX
cd ..
