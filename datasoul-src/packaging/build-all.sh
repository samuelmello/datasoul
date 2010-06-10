#!/bin/bash -x

DSVERSION=$(awk -F= '/version/{ print $2 }' ../src/datasoul/version.properties)

##################################################

# Clean up
rm -Rf installers
ant clean 
mkdir installers

# Compile
ant jar

# Remove generated README.TXT file
rm -f dist/README.TXT

##################################################

# Build debian
mkdir -p debian/datasoul/usr/lib/datasoul
cd debian
sed -i "s/Version:.*/Version: ${DSVERSION}/" datasoul/DEBIAN/control
cp -r ../dist/* datasoul/usr/lib/datasoul
dpkg -b datasoul
mv datasoul.deb ../installers/datasoul_${DSVERSION}_all.deb
rm -Rf datasoul/usr/lib/datasoul/*
cd ..

# Build RPM
cd installers
alien -k --to-rpm datasoul_${DSVERSION}_all.deb
cd ..

# Build MacOSX
MACINSTDIR=installers/Datasoul-${DSVERSION}-MacOSX/Datasoul-${DSVERSION}.app
mkdir -p ${MACINSTDIR}
cp -r MacOSX/Contents ${MACINSTDIR}
cp -r dist/* ${MACINSTDIR}/Contents/Resources/Java
cd installers
zip -r Datasoul-${DSVERSION}-MacOSX.zip Datasoul-${DSVERSION}-MacOSX
cd ..
rm -Rf installers/Datasoul-${DSVERSION}-MacOSX

# Build Windows
makensis windows.nsi
mv Datasoul-${DSVERSION}.exe installers

# Create source tarball
mkdir datasoul-$DSVERSION
mkdir datasoul-$DSVERSION/src
cp  -r ../src/datasoul  datasoul-$DSVERSION/src
cp  -r ../lib           datasoul-$DSVERSION/lib
tar czvf installers/datasoul-$DSVERSION-source.tar.gz datasoul-$DSVERSION
rm -Rf datasoul-$DSVERSION

###################################################
# Clean up
ant clean

