# Makefile.am for the SDL_bgrab library/DLL

lib_LTLIBRARIES = libSDL_bgrab.la

libSDL_bgrabincludedir = $(includedir)/SDL
libSDL_bgrabinclude_HEADERS =	\
	SDL_bgrab.h

libSDL_bgrab_la_SOURCES =		\
	SDL_bgrab.c

DEFS = @DEFS@ -DBUILD_DLL
libSDL_bgrab_la_LDFLAGS = 	\
	-version-info $(LT_CURRENT):$(LT_REVISION):$(LT_AGE)

DISTCLEANFILES = *~ *~c *~h *.cross.cache inc
