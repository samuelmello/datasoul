JDK_HOME=/usr/java/jdk1.6.0
CFLAGS=-O4
DATASOUL_CLASSES=../../build/classes

libdatasoulsdl.so: datasoul_render_SDLDisplay.h datasoul_render_SDLDisplay.c
	gcc ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
	  -DVIDEO4LINUX \
  	  -shared datasoul_render_SDLDisplay.c -o libdatasoulsdl.so\
	  -lSDL -lSDL_bgrab

datasoul_render_SDLDisplay.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLDisplay

clean:
	rm -f datasoul_render_SDLDisplay.h libdatasoulsdl.so
