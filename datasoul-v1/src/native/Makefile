JDK_HOME=/usr/java/jdk1.6.0
CFLAGS=-O4 -Wall
DATASOUL_CLASSES=../../build/classes

all: libsdlrender.so 

libsdlrender.so: datasoul_render_SDLContentRender.h datasoul_render_SDLContentRender.c
	$(CC) ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLContentRender.c -o libsdlrender.so\
	  -lSDL 

datasoul_render_SDLContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLContentRender


clean:
	rm -f datasoul_render_SDLContentRender.h libsdlrender.so 
