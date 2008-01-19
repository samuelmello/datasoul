VERSION=1.0

all:
	rm -f datasoul*deb datasoul*rpm
	ant clean 
	ant jar
	cp dist/datasoul.jar debian/usr/lib/datasoul
	cp ../lib/xercesImpl.jar  debian/usr/lib/datasoul
	cp ../lib/xml-apis.jar debian/usr/lib/datasoul
	ant clean
	chmod a+x debian/usr/bin/datasoul
	dpkg --build debian datasoul-$(VERSION).deb
	alien --to-rpm -k datasoul-$(VERSION).deb

clean: 
	rm -f datasoul*deb datasoul*rpm
	ant clean
	rm debian/usr/lib/datasoul/datasoul.jar
	rm debian/usr/lib/datasoul/xercesImpl.jar
	rm debian/usr/lib/datasoul/xml-apis.jar
