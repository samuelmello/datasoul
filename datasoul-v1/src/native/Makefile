JDK_HOME=/usr/java/jdk1.6.0
CFLAGS=-g
DATASOUL_CLASSES=../../build/classes

libdatasoulsdl.so: datasoul_render_SDLDisplay.h datasoul_render_SDLDisplay.c
	gcc ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLDisplay.c -o libdatasoulsdl.so\
	  -lSDL

datasoul_render_SDLDisplay.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLDisplay

clean:
	rm -f datasoul_render_SDLDisplay.h
