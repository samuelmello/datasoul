#include <stdio.h>
#include <string.h>
#include <jni.h> 
#include <SDL/SDL.h>
#include <SDL/SDL_thread.h>
#include "datasoul_render_SDLDisplay.h"

#ifdef VIDEO4LINUX
#define USE_PTHREAD
#include "linux/SDL_bgrab-1.0.0/SDL_bgrab.h"
#endif

#ifdef USE_PTHREAD
#include <pthread.h>
#include <sched.h>
#endif

#define FRAMETIME_MS 30

#define BACKGROUND_MODE_STATIC 0
#define BACKGROUND_MODE_LIVE   1

typedef struct {
	SDL_Surface *screen;
	SDL_Surface *overlay[2];
	int overlayActive;
	SDL_Surface *background;
	int black;
	int clear;
	int needRefresh;
	int stopDisplay;
	int bgMode;
	int debugMode;

#ifdef USE_PTHREAD
	pthread_t displayThread;
#else
	SDL_Thread *displayThread;
#endif
	
#ifdef VIDEO4LINUX
	tSDL_bgrab bgrab;
	int deintrelaceMode;
	int inputSrc;
	int inputMode;
#endif
} globals_t;

static globals_t globals;

#ifdef USE_PTHREAD
void* displayThread (void *arg){
#else
int displayThread (void *arg){
#endif
	
	Uint32 time1, time2, time3;
	
	while (globals.stopDisplay == 0){
		time1 = SDL_GetTicks();
		
		if (globals.needRefresh || globals.bgMode == BACKGROUND_MODE_LIVE){

			// Update the screen

			// we are in black?
			if (globals.black && globals.needRefresh){
				SDL_FillRect (globals.screen, NULL, 0);	
			}else{
				// ok, "normal" painting"
				
				
				if (globals.bgMode == BACKGROUND_MODE_LIVE){
#ifdef VIDEO4LINUX
					bgrabBlitFramebuffer(&globals.bgrab, globals.screen, globals.deintrelaceMode /* deintrelace */);
#endif
				}else{
					SDL_BlitSurface(globals.background, NULL, globals.screen, NULL);
				}

				if (! globals.clear){
					SDL_BlitSurface(globals.overlay[globals.overlayActive], NULL, globals.screen, NULL);
				}
			}
			time3 = SDL_GetTicks();
			SDL_Flip(globals.screen);
			//globals.needRefresh = 0;

		}
		time2 = SDL_GetTicks();
		
		// Sleep until the next screen refresh
		if ( (time2 - time1) < FRAMETIME_MS ){
			SDL_Delay ( FRAMETIME_MS - (time2 - time1) );
		}

		if (globals.debugMode) {
			fprintf(stderr, "Processing: %d ms, Sleeping: %d ms\n", 
					time1, time2, time3, (time2 - time1), 
					FRAMETIME_MS - (time2 - time1));
		}
	}

}


/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_init
(JNIEnv *env, jobject obj, jint width, jint height, jint top, jint left){


        SDL_Surface *surface;

        Uint32 rmask, gmask, bmask, amask;
        SDL_Rect rect;

	char envopt[256];


	if (width <= 0){
		width = 640;
	}
	if (height <= 0){
		height = 480;
	}
		
	sprintf(envopt, "SDL_VIDEO_WINDOW_POS=%d,%d", left, top);
	putenv(envopt);
	
        if( SDL_Init( SDL_INIT_EVERYTHING ) == -1 )
        {
                return ;
        }


#ifdef VIDEO4LINUX
	bgrabOpen(&globals.bgrab,"/dev/video0");

	/* Print some device info */
	bgrabPrintInfo(&globals.bgrab);

	/* Configure card */
	bgrabSetChannel(&globals.bgrab, 2, 0);
	
	/* Start! */
        bgrabStart(&globals.bgrab, width, height, 1);
	
#endif
	

	globals.screen = SDL_SetVideoMode( width, height, 0, SDL_SWSURFACE | SDL_DOUBLEBUF | SDL_NOFRAME);
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

        surface = SDL_CreateRGBSurface(SDL_SWSURFACE, width, height, 32,
                        rmask, gmask, bmask, amask);

        globals.overlay[0] = SDL_DisplayFormatAlpha(surface);
        globals.overlay[1] = SDL_DisplayFormatAlpha(surface);
        globals.background = SDL_DisplayFormatAlpha(surface);
	
	SDL_FreeSurface(surface);

	globals.needRefresh = 1;
	globals.stopDisplay = 0;
	
#ifdef USE_PTHREAD
	
	// Init the painting thread using the RoundRobin Scheduler
	// and the highest priority
	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setschedpolicy(&attr, SCHED_RR);
        struct sched_param param;
	param.__sched_priority = sched_get_priority_max(SCHED_RR);
	pthread_attr_setschedparam(&attr, &param);
	pthread_create(&globals.displayThread, &attr, &displayThread, NULL);

	// also setup the process scheduler to RoundRobin
	/*
	int x = sched_setscheduler(0, SCHED_RR, &param);
	if (x != 0){
		perror("setsched");
	}
	*/
#else
	globals.displayThread = SDL_CreateThread( &displayThread, NULL);
#endif
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

#ifdef VIDEO4LINUX
	  bgrabStop(&globals.bgrab);
	  bgrabClose(&globals.bgrab);
#endif

	  
#ifdef USE_PTHREAD
	  pthread_join(globals.displayThread, (void*) &x);
#else
	  SDL_WaitThread(globals.displayThread, &x);
#endif
	  
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
                memcpy ((Uint8*)surface->pixels+i, &tmp, 4);
        }

	
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    displayOverlay
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_displayOverlay
  (JNIEnv *env, jobject obj, jobject bytebuf){

	  int x;
	  if ( globals.overlayActive == 0){
		  x = 1;
	  }else{
		  x = 0;
	  }
	setImageOnSurface(env, globals.overlay[x], bytebuf);
	globals.overlayActive = x;
	globals.needRefresh = 1;

	if (globals.debugMode > 0){
		fprintf(stdout, "Received overlay image!\n");
	}
	
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

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    setBackgroundMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_setBackgroundMode
  (JNIEnv *env, jobject obj, jint mode){
	  globals.bgMode = mode;
	  globals.needRefresh = 1;
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    setInputSrc
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_setInputSrc
  (JNIEnv *env, jobject obj, jint src){

#ifdef VIDEO4LINUX
	globals.inputSrc = src;
	bgrabSetChannel(&globals.bgrab, globals.inputSrc, globals.inputMode);
#endif
	  
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    setInputMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_setInputMode
  (JNIEnv *env, jobject obj, jint mode){

#ifdef VIDEO4LINUX
	globals.inputMode = mode;
	bgrabSetChannel(&globals.bgrab, globals.inputSrc, globals.inputMode);
#endif

}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    setDeintrelaceMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_setDeintrelaceMode
  (JNIEnv *env, jobject obj, jint mode){

#ifdef VIDEO4LINUX
	  globals.deintrelaceMode = mode;
#endif
	  
}

/*
 * Class:     datasoul_render_SDLDisplay
 * Method:    setDebugMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLDisplay_setDebugMode
  (JNIEnv *env, jobject obj, jint mode){

	globals.debugMode = mode;

}

