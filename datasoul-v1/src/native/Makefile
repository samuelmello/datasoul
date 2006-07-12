JDK_HOME=/usr/java/jdk1.6.0
CFLAGS=-O4 -Wall
DATASOUL_CLASSES=../../build/classes

all: libdatasoulsdl.so 

libdatasoulsdl.so: datasoul_render_SDLContentRender.h datasoul_render_SDLContentRender.c
	gcc ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
	  -DVIDEO4LINUX \
  	  -shared datasoul_render_SDLContentRender.c -o libdatasoulsdl.so\
	  -lSDL -lSDL_bgrab -fPIC

datasoul_render_SDLContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLContentRender


clean:
	rm -f datasoul_render_SDLContentRender.h libdatasoulsdl.so 
