#include <stdio.h>
#include <string.h>
#include <jni.h> 
#include "SDL/SDL.h"
#include "SDL/SDL_thread.h"
#include "datasoul_render_SDLDisplay.h"

#define FRAMETIME_MS 30

typedef struct {
	SDL_Surface *screen;
	SDL_Surface *overlay;
	SDL_Surface *background;
	int black;
	int clear;
	int needRefresh;
	int stopDisplay;
	SDL_Thread *displayThread;
} globals_t;

static globals_t globals;

int displayThread (void *arg){

	Uint32 time1, time2;
	
	while (globals.stopDisplay == 0){
	
		time1 = SDL_GetTicks();
		
		if (globals.needRefresh){
			// Update the screen

			// we are in black?
			if (globals.black){
				SDL_FillRect (globals.screen, NULL, 0);	
			}else{
				// ok, "normal" painting"
				SDL_BlitSurface(globals.background, NULL, globals.screen, NULL);
				if (! globals.clear){
					SDL_BlitSurface(globals.overlay, NULL, globals.screen, NULL);
				}
			}
			SDL_Flip(globals.screen);
			globals.needRefresh = 0;
		}
		
		// Sleep until the next screen refresh
		time2 = SDL_GetTicks();
		if ( (time2 - time1) < FRAMETIME_MS ){
			SDL_Delay ( FRAMETIME_MS - (time2 - time1) );
		}
		
	}

}


/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_init
(JNIEnv *env, jobject obj){


        SDL_Surface *surface;

        Uint32 rmask, gmask, bmask, amask;
        SDL_Rect rect;


        if( SDL_Init( SDL_INIT_EVERYTHING ) == -1 )
        {
                return ;
        }

        globals.screen = SDL_SetVideoMode( 640, 480, 32, SDL_SWSURFACE | SDL_DOUBLEBUF );
#if SDL_BYTEORDER == SDL_BIG_ENDIAN
        rmask = 0xff000000;
        gmask = 0x00ff0000;
        bmask = 0x0000ff00;
        amask = 0x000000ff;
#else
        rmask = 0x000000ff;
        gmask = 0x0000ff00;
        bmask = 0x00ff0000;
        amask = 0xff000000;
#endif

        surface = SDL_CreateRGBSurface(SDL_SWSURFACE, 640, 480, 32,
                        rmask, gmask, bmask, amask);

        globals.overlay = SDL_DisplayFormatAlpha(surface);
        globals.background = SDL_DisplayFormatAlpha(surface);
	
	SDL_FreeSurface(surface);

	globals.needRefresh = 1;
	globals.stopDisplay = 0;
	globals.displayThread = SDL_CreateThread( &displayThread, NULL);

}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    cleanup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_cleanup
  (JNIEnv *env, jobject obj){

	  int x;
	  globals.stopDisplay = 1;
	  SDL_WaitThread(globals.displayThread, &x);
	  
}


void setImageOnSurface(JNIEnv *env, SDL_Surface *surface, jobject bytebuf){

	Uint8 Rshift, Gshift, Bshift, Ashift;
        Uint32 tmp;
	Uint8 *buf;
	int i;
	long size;

        buf = (Uint8*) (*env)->GetDirectBufferAddress(env, bytebuf);
        size = (*env)->GetDirectBufferCapacity(env, bytebuf);

        Rshift =  surface->format->Rshift;
        Gshift =  surface->format->Gshift;
        Bshift =  surface->format->Bshift;
        Ashift =  surface->format->Ashift;

        for (i=0; i<size; i=i+4){
                tmp = 0;
                tmp |= buf[i]   << Ashift;
                tmp |= buf[i+1] << Bshift;
                tmp |= buf[i+2] << Gshift;
                tmp |= buf[i+3] << Rshift;
                //if (i<20){
                //      fprintf(stderr, "%08x \n", tmp);
                //}
                memcpy (surface->pixels+i, &tmp, 4);
        }

	
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    displayOverlay
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_displayOverlay
  (JNIEnv *env, jobject obj, jobject bytebuf){

	setImageOnSurface(env, globals.overlay, bytebuf);
	globals.needRefresh = 1;
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    nativeSetBackground
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_nativeSetBackground
  (JNIEnv *env, jobject obj, jobject bytebuf){

	setImageOnSurface(env, globals.background, bytebuf);
	globals.needRefresh = 1;

}
 

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    black
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_black
  (JNIEnv *env, jobject obj, jint active){
	  globals.black = active;
	  globals.needRefresh = 1;
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    clear
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_clear
  (JNIEnv *env, jobject obj, jint active){
	  globals.clear = active;
	  globals.needRefresh = 1;
}



