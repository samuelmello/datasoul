JDK_HOME=/usr/lib/j2sdk1.5-sun
QT_INCLUDE=/usr/include/qt4
CFLAGS=-O4 -Wall -g
DATASOUL_CLASSES=../../build/classes

LIBS=libsdlrender.so libsdlliverender.so 

all: $(LIBS) 

clean:
	rm -f datasoul_render_SDLLiveContentRender.h datasoul_render_SDLContentRender.h $(LIBS)

# SDL Render
libsdlrender.so: datasoul_render_SDLContentRender.h datasoul_render_SDLContentRender.c
	$(CC) ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLContentRender.c -o libsdlrender.so\
	  -lSDL 

datasoul_render_SDLContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLContentRender


# SDL Live Render
libsdlliverender.so: datasoul_render_SDLLiveContentRender.h libSDL_bgrab.a datasoul_render_SDLLiveContentRender.c
	$(CC) ${CFLAGS} -I${JDK_HOME}/include -I${JDK_HOME}/include/linux\
  	  -shared datasoul_render_SDLLiveContentRender.c libSDL_bgrab.a -o libsdlliverender.so\
	  -lSDL 

datasoul_render_SDLLiveContentRender.h:
	${JDK_HOME}/bin/javah -classpath ${DATASOUL_CLASSES} datasoul.render.SDLLiveContentRender

