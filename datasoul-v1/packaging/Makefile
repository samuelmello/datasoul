VERSION=1.0

all:
	rm -f datasoul*deb datasoul*rpm
	rm -rf debian.tmp
	ant clean 
	ant jar
	cp -r debian debian.tmp
	cp dist/datasoul.jar debian.tmp/usr/lib/datasoul
	cp ../lib/xercesImpl.jar  debian.tmp/usr/lib/datasoul
	cp ../lib/xml-apis.jar debian.tmp/usr/lib/datasoul
	ant clean
	chmod a+x debian.tmp/usr/bin/datasoul
	find debian.tmp -name "CVS" | xargs rm -rf
	dpkg --build debian.tmp datasoul-$(VERSION).deb
	alien --to-rpm -k datasoul-$(VERSION).deb
	rm -rf debian.tmp

clean: 
	rm -f datasoul*deb datasoul*rpm
	ant clean
	rm -rf debian.tmp
