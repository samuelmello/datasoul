#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <jni.h> 
#include <SDL/SDL.h>
#include <SDL/SDL_thread.h>
#include "datasoul_render_SDLLiveContentRender.h"
#include "linux/SDL_bgrab-1.0.0/SDL_bgrab.h"
#include <pthread.h>
#include <sched.h>

#define FRAMETIME_MS 30

#define BACKGROUND_MODE_STATIC 0
#define BACKGROUND_MODE_LIVE   1

typedef struct {
	SDL_Surface *screen;
	SDL_Surface *overlay[2];
	Uint8 *blitaux;
	int blitauxSize;
	int overlayActive;
	int black;
	int clear;
	int stopDisplay;
	int debugMode;

	pthread_t displayThread;
	
	tSDL_bgrab bgrab;
	char device[512];
	int deintrelaceMode;
	int inputSrc;
	int inputMode;

} globals_t;

static globals_t globals;

void* displayThread (void *arg){
	
	Uint32 time1, time2, time3 = 0;
	
	globals.debugMode = 1;

	while (globals.stopDisplay == 0){
		time1 = SDL_GetTicks();
		
		// Update the screen

		// we are in black?
		if (globals.black){
			SDL_FillRect (globals.screen, NULL, 0);	
		}else{
			// ok, "normal" painting"
			bgrabBlitFramebuffer(&globals.bgrab, globals.screen, globals.deintrelaceMode /* deintrelace */);

			if (! globals.clear){
				SDL_BlitSurface(globals.overlay[globals.overlayActive], NULL, globals.screen, NULL);

			}
		}
		time3 = SDL_GetTicks();
		SDL_Flip(globals.screen);

		time2 = SDL_GetTicks();
		// Sleep until the next screen refresh
		if ( (time2 - time1) < FRAMETIME_MS ){
			SDL_Delay ( FRAMETIME_MS - (time2 - time1) );
		}


//		if (globals.debugMode) {
			fprintf(stderr, "Processing: %d ms, Sleeping: %d ms (%d, %d, %d)\n", 
					(time2 - time1), 
					FRAMETIME_MS - (time2 - time1),
					time1, time2, time3);
//		}

	}

	return NULL;

}



/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    init
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_init
(JNIEnv *env, jobject obj, jint width, jint height, jint top, jint left){


        SDL_Surface *surface;

        Uint32 rmask, gmask, bmask, amask;

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


	bgrabOpen(&globals.bgrab, globals.device);

	/* Print some device info */
	bgrabPrintInfo(&globals.bgrab);

	/* Configure card */
	bgrabSetChannel(&globals.bgrab, 2, 0);
	
	/* Start! */
        bgrabStart(&globals.bgrab, width, height, 1);
	

	globals.screen = SDL_SetVideoMode( width, height, 0, SDL_HWSURFACE | SDL_DOUBLEBUF | SDL_NOFRAME );
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

        surface = SDL_CreateRGBSurface(SDL_HWSURFACE, width, height, 32,
                        rmask, gmask, bmask, amask);

        globals.overlay[0] = SDL_DisplayFormatAlpha(surface);
        globals.overlay[1] = SDL_DisplayFormatAlpha(surface);
	
	SDL_FreeSurface(surface);

	globals.blitauxSize = width * height * 4;
	globals.blitaux = (Uint8*) malloc( globals.blitauxSize );

	globals.stopDisplay = 0;
	
	
	// Init the painting thread using the FIFO RealTime Scheduler
	// and the highest priority
	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setschedpolicy(&attr, SCHED_FIFO);
        struct sched_param param;
	param.__sched_priority = sched_get_priority_max(SCHED_FIFO);
	pthread_attr_setschedparam(&attr, &param);
	pthread_create(&globals.displayThread, &attr, &displayThread, NULL);

	// also setup the process scheduler to FIFO RealTime
	/*
	int x = sched_setscheduler(0, SCHED_FIFO, &param);
	if (x != 0){
		perror("setsched");
	}
	*/
	

}



/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    cleanup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_cleanup
  (JNIEnv *env, jobject obj){

	  int x;
	  globals.stopDisplay = 1;

	  bgrabStop(&globals.bgrab);
	  bgrabClose(&globals.bgrab);
	  pthread_join(globals.displayThread, (void*) &x);
	  
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
		memcpy(globals.blitaux+i, &tmp, 4);
        }
	// now send it to the hardware surface
	memcpy ((Uint8*)surface->pixels, globals.blitaux, globals.blitauxSize);

	
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    displayOverlay
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_displayOverlay
  (JNIEnv *env, jobject obj, jobject bytebuf){

	  int x;
	  if ( globals.overlayActive == 0){
		  x = 1;
	  }else{
		  x = 0;
	  }
	setImageOnSurface(env, globals.overlay[x], bytebuf);
	globals.overlayActive = x;

	//if (globals.debugMode > 0){
		fprintf(stderr, "Received overlay image! newactive=%d\n", x);
	//}
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setBlack
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setBlack
  (JNIEnv *env, jobject obj, jint active){
	  globals.black = active;
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setClear
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setClear
  (JNIEnv *env, jobject obj, jint active){
	  globals.clear = active;
}


/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setInputSrc
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setInputSrc
  (JNIEnv *env, jobject obj, jint src){

	globals.inputSrc = src;
	bgrabSetChannel(&globals.bgrab, globals.inputSrc, globals.inputMode);
	if (globals.debugMode){
		fprintf(stderr, "Setting Source (src=%d, mode=%d)\n", 
			globals.inputSrc, globals.inputMode);
	}
	  
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setInputMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setInputMode
  (JNIEnv *env, jobject obj, jint mode){

	globals.inputMode = mode;
	bgrabSetChannel(&globals.bgrab, globals.inputSrc, globals.inputMode);
	if (globals.debugMode){
		fprintf(stderr, "Setting Input (src=%d, mode=%d)\n", 
			globals.inputSrc, globals.inputMode);
	}


}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setDeintrelaceMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setDeintrelaceMode
  (JNIEnv *env, jobject obj, jint mode){

	  globals.deintrelaceMode = mode;
	  
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setDebugMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setDebugMode
  (JNIEnv *env, jobject obj, jint mode){

	globals.debugMode = mode;

}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setWindowTitle
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setWindowTitle
	(JNIEnv *env, jobject obj, jstring title){

     const char *str;
     str = (*env)->GetStringUTFChars(env, title, NULL);
     if (str == NULL) {
         return; /* OutOfMemoryError already thrown */
     }

     SDL_WM_SetCaption(str, NULL);	

     (*env)->ReleaseStringUTFChars(env, title, str);

}


/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    shutdown
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_shutdown
  (JNIEnv *env, jobject obj){
	SDL_Quit();
}

/*
 * Class:     datasoul_render_SDLContentRender
 * Method:    setX11Display
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setX11Display
  (JNIEnv *env, jobject obj, jstring x11displayStr){

	char *envoptX11 = (char*)malloc(512);
        const char *str;
        str = (*env)->GetStringUTFChars(env, x11displayStr, NULL);
        snprintf(envoptX11, 511, "DISPLAY=%s", str);
        (*env)->ReleaseStringUTFChars(env, x11displayStr, str);
	putenv(envoptX11);
	// do NOT free envoptX11. putenv stores only a pointer to it.
	// freeing envoptX11 will lose the value
}


/*
 * Class:     datasoul_render_SDLLiveContentRender
 * Method:    setDeviceName
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_datasoul_render_SDLLiveContentRender_setDeviceName
  (JNIEnv *env, jobject obj, jstring devName){

        const char *str = (*env)->GetStringUTFChars(env, devName, NULL);
        snprintf(globals.device, 511, "%s", str);
        (*env)->ReleaseStringUTFChars(env, devName, str);

}

