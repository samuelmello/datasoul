#!/bin/bash -x

DSVERSION=$(cat datasoul.xml | awk -F\< "/<version>/{ print \$2 }" | awk -F\> "{ print \$2}")
rm -Rf datasoul-$DSVERSION

##################################################

# Clean up
rm -Rf installers
ant clean 
find ../src -name "*#*" | xargs rm -f

# Setup vesion
echo "version=$DSVERSION" > ../src/datasoul/version.properties

# Compile
ant jar

##################################################

# Build windows 
#BUILDER_CMD=/opt/installbuilder-5.4.10/bin/builder
#${BUILDER_CMD} build datasoul.xml windows

# Build MacOSX
MACINSTDIR=installers/Datasoul-${DSVERSION}-MacOSX/Datasoul-${DSVERSION}.app
mkdir -p ${MACINSTDIR}
cp -r MacOSX/Contents ${MACINSTDIR}
cp -r dist/* ${MACINSTDIR}/Contents/Resources/Java
cd installers
zip -r Datasoul-${DSVERSION}-MacOSX.zip Datasoul-${DSVERSION}-MacOSX
cd ..
rm -Rf installers/Datasoul-${DSVERSION}-MacOSX

# Create source tarball
mkdir datasoul-$DSVERSION
mkdir datasoul-$DSVERSION/src
cp  -r ../src/datasoul  datasoul-$DSVERSION/src
find datasoul-$DSVERSION/src -name "CVS" | xargs rm -Rf
tar czvf installers/datasoul-$DSVERSION-source.tar.gz datasoul-$DSVERSION
rm -Rf datasoul-$DSVERSION

###################################################
# Clean up
ant clean


