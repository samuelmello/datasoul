#!/bin/bash -x

SOURCEDIR=../datasoul-src
DISTDIR=$SOURCEDIR/dist
DSVERSION=$(awk -F= '/version/{ print $2 }' $SOURCEDIR/src/datasoul/version.properties)

if [[ "$1" == "" ]]
then
	echo "Please use an action: build, macos, windows, debian, source"
fi


##################################################

# Clean up and build
if [[ "$1" == "build" ]]
then

rm -Rf installers
cd $SOURCEDIR
ant clean jar
if [[ ! -d dist/lib ]]
then
  cp -r lib dist
fi
rm -f dist/README.TXT
cd -
mkdir installers

fi

##################################################

if [[ "$1" == "debian" ]]
then

# Build debian
if [[ "$USER" != "root" ]]
then
	echo "Please run this script as root"
	exit 0
fi

mkdir -p debian/datasoul/usr/share/datasoul
cp -r $DISTDIR/* debian/datasoul/usr/share/datasoul
cd debian
sed -i "s/Version:.*/Version: ${DSVERSION}/" datasoul/DEBIAN/control
chown -R root.root datasoul
chmod g-w -R datasoul
dpkg -b datasoul
mv datasoul.deb ../installers/datasoul_${DSVERSION}_all.deb
rm -Rf datasoul/usr/share/datasoul/*
cd ..

# Build RPM
cd installers
alien -k --scripts --to-rpm datasoul_${DSVERSION}_all.deb
cd ..

fi

# Build MacOSX
if [[ "$1" == "macos" ]]
then

MACINSTDIR=installers/Datasoul-${DSVERSION}-MacOSX/Datasoul-${DSVERSION}.app
mkdir -p ${MACINSTDIR}
cp -r MacOSX/Contents ${MACINSTDIR}
mkdir -p ${MACINSTDIR}/Contents/Java
cp $DISTDIR/*jar ${MACINSTDIR}/Contents/Java
cp $DISTDIR/*jar ${MACINSTDIR}/Contents/Java
cp -r ../native/macos/* ${MACINSTDIR}/Contents
cd installers
zip -r Datasoul-${DSVERSION}-MacOSX.zip Datasoul-${DSVERSION}-MacOSX
cd ..
rm -Rf installers/Datasoul-${DSVERSION}-MacOSX

fi


if [[ "$1" == "windows" ]]
then

# Build Windows
makensis windows.nsi
mv Datasoul-${DSVERSION}.exe installers/Datasoul-${DSVERSION}-Windows.exe

fi

###################################################

if [[ "$1" == "source" ]]
then

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

fi
