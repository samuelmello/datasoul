JDK_HOME=/home/ess/jdk1.5.0_06
CFLAGS=-g
DATASOUL_CLASSES=../../build/classes

libdatasoulsdl.so: datasoul_render_SDLDisplay.h datasoul_render_SDLDisplay.c
	gcc ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
	  -DUSE_PTHREAD\
  	  -shared datasoul_render_SDLDisplay.c -o libdatasoulsdl.so\
	  -lSDL -lSDL_bgrab -fPIC

datasoul_render_SDLDisplay.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLDisplay

clean:
	rm -f datasoul_render_SDLDisplay.h libdatasoulsdl.so
