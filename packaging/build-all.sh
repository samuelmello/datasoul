#!/bin/bash -x

SOURCEDIR=../datasoul-src
DISTDIR=$SOURCEDIR/dist
DSVERSION=$(awk -F= '/version/{ print $2 }' $SOURCEDIR/src/datasoul/version.properties)

##################################################

# Clean up and build
rm -Rf installers
cd $SOURCEDIR
ant clean jar
rm -f dist/README.TXT
cd -
mkdir installers

##################################################

# Build debian
mkdir -p debian/datasoul/usr/lib/datasoul
cp -r $DISTDIR/* debian/datasoul/usr/lib/datasoul
cd debian
sed -i "s/Version:.*/Version: ${DSVERSION}/" datasoul/DEBIAN/control
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
cp -r $DISTDIR/* ${MACINSTDIR}/Contents/Resources/Java
cd installers
zip -r Datasoul-${DSVERSION}-MacOSX.zip Datasoul-${DSVERSION}-MacOSX
cd ..
rm -Rf installers/Datasoul-${DSVERSION}-MacOSX

# Build Windows
makensis windows.nsi
mv Datasoul-${DSVERSION}.exe installers

###################################################
# Clean up
cd $SOURCEDIR
ant clean
cd -

# Create source tarball
mkdir datasoul-$DSVERSION
cp  -r $SOURCEDIR/*  datasoul-$DSVERSION
rm -rf datasoul-$DSVERSION/lib
rm -rf datasoul-$DSVERSION/nbproject/private
tar czvf installers/datasoul-$DSVERSION-source.tar.gz datasoul-$DSVERSION
rm -Rf datasoul-$DSVERSION


