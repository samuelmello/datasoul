JDK_HOME=/usr/java/jdk1.6.0
CFLAGS=-O4 -Wall -g
DATASOUL_CLASSES=../../build/classes

LIBS=libsdlrender.so libsdlliverender.so 

all: $(LIBS) 

libsdlrender.so: datasoul_render_SDLContentRender.h datasoul_render_SDLContentRender.c
	$(CC) ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLContentRender.c -o libsdlrender.so\
	  -lSDL 

datasoul_render_SDLContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLContentRender

libsdlliverender.so: datasoul_render_SDLLiveContentRender.h libSDL_bgrab.a datasoul_render_SDLLiveContentRender.c
	$(CC) ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLLiveContentRender.c libSDL_bgrab.a -o libsdlliverender.so\
	  -lSDL 

datasoul_render_SDLLiveContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLLiveContentRender



clean:
	rm -f datasoul_render_SDLLiveContentRender.h datasoul_render_SDLContentRender.h $(LIBS)
