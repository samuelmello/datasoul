/*

 SDL_bgrab - SDL based Threaded v4l Video Grabber

 LGPL (c) A. Schiffler

*/

#ifndef _SDL_vnc_h
#define _SDL_vnc_h

#include <math.h>

/* Set up for C function definitions, even when using C++ */
#ifdef __cplusplus
extern "C" {
#endif

#include <SDL/SDL.h>
#include <SDL/SDL_thread.h>

#include <sys/time.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <linux/types.h>

#include <linux/videodev.h>

/* ------ Defines used for framegrabber setup */

/* SETTING_ used for _get_setting() and _set_setting() */

#define SETTING_BRIGHTNESS	0
#define SETTING_HUE		1
#define SETTING_COLOUR		2
#define SETTING_CONTRAST	3

/* CHANNEL_ used for _set_channel() */

#define CHANNEL_TUNER		0
#define CHANNEL_COMPOSITE	1
#define CHANNEL_SVIDEO		2

/* VIDEOMODE_ used for _set_channel() */

#define VIDEOMODE_PAL		VIDEO_MODE_PAL			
#define VIDEOMODE_NTSC		VIDEO_MODE_NTSC
#define VIDEOMODE_SECAM		VIDEO_MODE_SECAM

/* FORMAT_ used by _start_grab_image() */

#define FORMAT_GREY		VIDEO_PALETTE_GREY
#define FORMAT_RGB565		VIDEO_PALETTE_RGB565
#define FORMAT_RGB24		VIDEO_PALETTE_RGB24
#define FORMAT_RGB32		VIDEO_PALETTE_RGB32
#define FORMAT_YUV422P		VIDEO_PALETTE_YUV422P
#define FORMAT_YUV420P		VIDEO_PALETTE_YUV420P

/* REGION_ used by _set_frequency() */

#define REGION_NTSC_BROADCAST		0
#define REGION_NTSC_CABLE		1
#define REGION_NTSC_CABLE_HRC		2
#define REGION_NTSC_BROADCAST_JAPAN	3
#define REGION_NTSC_CABLE_JAPAN		4
#define REGION_PAL_EUROPE		5
#define REGION_PAL_EUROPE_EAST		6
#define REGION_PAL_ITALY		7
#define REGION_PAL_NEWZEALAND		8
#define REGION_PAL_AUSTRALIA		9
#define REGION_PAL_IRELAND		10

/* ------- Defines for internal use  */

#define IMAGE_BUFFER_EMPTY	0
#define IMAGE_BUFFER_FULL	1
#define IMAGE_BUFFER_INUSE	2
    
/* ---- main SDL_bgrab structure ---- */
      
    typedef struct tSDL_bgrab {
	int bgrab_dev;

	int width;
	int height;
	int input;
	int format;

	struct video_mmap vid_mmap[2];
	int current_grab_number;
	struct video_mbuf vid_mbuf;
	char *bgrab_map;
	int grabbing_active;
	int have_new_frame;

        Uint32 rmask, gmask, bmask, amask;	// RGBA mask of grabbed image
	
	SDL_Surface *framebuffer;		// current framebuffer
	SDL_Surface *deframebuffer;		// deinterlaced framebuffer
	unsigned char *backbuffer;		// workbuffer
	
	SDL_mutex *buffer_mutex;
	SDL_Thread *grab_thread;
	SDL_cond *buffer_cond;
	
	int totalframecount;
	int image_size;
	int image_pixels;
	int framecount;

	int fps_update_interval;
	double fps;
	double lasttime;
    } tSDL_bgrab;


/* ---- Prototypes */

#ifdef WIN32
#ifdef BUILD_DLL
#define DLLINTERFACE __declspec(dllexport)
#else
#define DLLINTERFACE __declspec(dllimport)
#endif
#else
#define DLLINTERFACE
#endif


  
DLLINTERFACE int bgrabOpen(tSDL_bgrab *bgrab, char *device);

DLLINTERFACE void bgrabSetFpsInterval(tSDL_bgrab *bgrab, int interval);
DLLINTERFACE double bgrabGetFps(tSDL_bgrab *bgrab);

DLLINTERFACE int bgrabPrintInfo(tSDL_bgrab *bgrab);

DLLINTERFACE int bgrabGetSetting(tSDL_bgrab *bgrab, int which_setting);
DLLINTERFACE int bgrabSetSetting(tSDL_bgrab *bgrab, int which_setting, int value);

DLLINTERFACE int bgrabSetChannel(tSDL_bgrab *bgrab, int channel, int bgrabmode);
DLLINTERFACE int bgrabSetFrequency(tSDL_bgrab *bgrab, int region, int index);

DLLINTERFACE int bgrabStart (tSDL_bgrab *bgrab, int width, int height, int bgra);
DLLINTERFACE int bgrabStop(tSDL_bgrab *bgrab);

DLLINTERFACE int bgrabBlitFramebuffer(tSDL_bgrab *bgrab, SDL_Surface *screen, int deinterlace);

DLLINTERFACE int bgrabClose(tSDL_bgrab *bgrab);


/* Ends C function definitions when using C++ */
#ifdef __cplusplus
};
#endif

#endif				/* _SDL_bgrab_h */
