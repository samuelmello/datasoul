/*  

 SDL_bgrab - SDL based Threaded v4l Video Grabber

 LGPL (c) A. Schiffler, aschiffler@appwares.com

*/

#ifdef WIN32
#include <windows.h>
#endif

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

#include <string.h>

#include "SDL_bgrab.h"

/* Endian data routines */

#if SDL_BYTEORDER == SDL_BIG_ENDIAN
 #define swap_16(x) (x)
 #define swap_32(x) (x)
 unsigned char bitfield[8]={1,2,4,8,16,32,64,128};
#else
 #define swap_16(x) ((((x) & 0xff) << 8) | (((x) >> 8) & 0xff))
 #define swap_32(x) (((x) >> 24) | (((x) & 0x00ff0000) >> 8)  | (((x) & 0x0000ff00) << 8)  | ((x) << 24))
 unsigned char bitfield[8]={128,64,32,16,8,4,2,1};
#endif

/* Define this to generate lots of info while the library is running. */
/* #define DEBUG */
                                                         
#ifdef DEBUG
 #define DBMESSAGE 	printf
 #define DBERROR 	printf("Error: "); printf
#else
 #define DBMESSAGE 	//
 #define DBERROR 	printf("Error: "); printf
#endif


/* NOTE: Requires BTTV compatible framegrabber card for full functionality. */
/*       Might work with other cards/input devices.                         */


/* Channel Frequencies */

#include "frequencies.c"


/* Deinterlace routine */

#include "deinterlace.c"


/* Routine to calculate the frequency for a channel */

int bgrab_get_frequency(int region, int index)
{
 if ((region>=0) && (region<NUM_CHANNEL_LISTS)) {
  if ((index>=0) && (index<chanlists[region].count)) {  
   DBMESSAGE("Region %s : Channel %s\n",chanlists[region].name,chanlists[region].list[index].name);
   return(chanlists[region].list[index].freq);
  }
 }  
 return(0);
}


/* Routine to get the current time as float */
double bgrab_get_time(void)
{
  struct timeval tv;
  double curtime;
  
  /* get wallclock time */
  gettimeofday(&tv, NULL);
  curtime=(double)tv.tv_sec+1.0e-6*(double)tv.tv_usec;
  return (curtime); 
}

/* Call for each frame to get FPS calculation */
void bgrab_calc_fps(tSDL_bgrab *bgrab)
{
 double curtime;
 
 /* Check interval */
 if (bgrab->fps_update_interval<1) {
  bgrab->fps=0.0;
 } else {
  if ( (bgrab->framecount<0) || (bgrab->framecount>=bgrab->fps_update_interval)) {
   /* Initialize counter */
   bgrab->framecount=0;
   bgrab->lasttime=bgrab_get_time();
   bgrab->fps=0.0;
  } else {
   bgrab->framecount++;
   if (bgrab->framecount==bgrab->fps_update_interval) {
    curtime=bgrab_get_time();
    bgrab->fps=(double)bgrab->framecount/(curtime-bgrab->lasttime);
    bgrab->lasttime=curtime;
    bgrab->framecount=0;
   }
  }
 }  
}

/* Set the interval (in frames) after which the fps counter is updated */
void bgrabSetFpsInterval(tSDL_bgrab *bgrab, int interval)
{
 bgrab->fps_update_interval=interval;
 bgrab->framecount=-1;
}

/* Return fps */
double bgrabGetFps(tSDL_bgrab *bgrab)
{
 return(bgrab->fps);
}
 
/* Prints device specific information to stderr */
int bgrabPrintInfo(tSDL_bgrab *bgrab)
{
 struct video_capability bgrab_caps;
 struct video_channel bgrab_chnl;
 struct video_audio bgrab_aud;
 char BooleanText[2][4]={"YES\0","NO \0"};
 int max_tuner;
 int i;
 
 if (ioctl (bgrab->bgrab_dev, VIDIOCGCAP, &bgrab_caps) == -1) {
  DBERROR("ioctl (VIDIOCGCAP)");
  return 0;
 } else {
  
  /* List capabilities */
  
  fprintf (stderr,"Device Info: ");
  fprintf (stderr,"%s\n",bgrab_caps.name);

  fprintf (stderr," Can capture ... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_CAPTURE) == 0)]);
  fprintf (stderr," Can clip ...... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_CLIPPING) == 0)]);
  fprintf (stderr," Channels ...... : %i\n",bgrab_caps.channels);

  fprintf (stderr," Has tuner ..... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_TUNER) == 0)]);
  fprintf (stderr," Ovl overwrites  : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_FRAMERAM) == 0)]);
  fprintf (stderr," Audio devices . : %i\n",bgrab_caps.audios);

  fprintf (stderr," Has teletext .. : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_TELETEXT) == 0)]);
  fprintf (stderr," Can scale ..... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_SCALES) == 0)]);
  fprintf (stderr," Width min-max . : %i-%i\n",bgrab_caps.minwidth,bgrab_caps.maxwidth);

  fprintf (stderr," Can overlay ... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_OVERLAY) == 0)]);
  fprintf (stderr," Monochrome .... : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_MONOCHROME) == 0)]);
  fprintf (stderr," Height min-max  : %i-%i\n",bgrab_caps.minheight,bgrab_caps.maxheight);
  
  fprintf (stderr," Can chromakey . : %s  ",BooleanText[((bgrab_caps.type & VID_TYPE_CHROMAKEY) == 0)]);
  fprintf (stderr," Can subcapture  : %s\n",BooleanText[((bgrab_caps.type & VID_TYPE_SUBCAPTURE) == 0)]);
  
  /* List input channels */
  
  max_tuner=0;
  for (i=0; i<bgrab_caps.channels; i++) {
   bgrab_chnl.channel = i;
   if (ioctl (bgrab->bgrab_dev, VIDIOCGCHAN, &bgrab_chnl) == -1) {
    DBERROR ("ioctl (VIDIOCGCHAN)");
    return 0;
   } else {
    fprintf (stderr," Channel %i: %s ",i,bgrab_chnl.name);
    if ((bgrab_chnl.type & VIDEO_TYPE_TV)==0) {
     fprintf (stderr,"(camera input)\n");
    } else {
     fprintf (stderr,"(TV input)\n");
    } 
    fprintf (stderr,"  Tuners : %i  ",bgrab_chnl.tuners);
    if (bgrab_chnl.tuners>max_tuner) max_tuner=bgrab_chnl.tuners; 
    fprintf (stderr,"  Has audio : %s\n",BooleanText[((bgrab_chnl.flags & VIDEO_VC_AUDIO) == 0)]);
   }
  }
  
  /* Audio channels */
  for (i=0; i<bgrab_caps.audios; i++) {
   bgrab_aud.audio=i;
   if (ioctl (bgrab->bgrab_dev, VIDIOCGAUDIO, &bgrab_aud) == -1) {
    DBERROR ("ioctl (VIDIOCGAUDIO)");
    return 0;
   } else {
    fprintf (stderr," Audio %i: %s\n",i,bgrab_aud.name);
    fprintf (stderr,"  Controllable: ");
    if ((bgrab_aud.flags & VIDEO_AUDIO_MUTABLE) != 0) fprintf (stderr,"Muting ");
    if ((bgrab_aud.flags & VIDEO_AUDIO_VOLUME) != 0) fprintf (stderr,"Volume ");
    if ((bgrab_aud.flags & VIDEO_AUDIO_BASS) != 0) fprintf (stderr,"Bass ");
    if ((bgrab_aud.flags & VIDEO_AUDIO_TREBLE) != 0) fprintf (stderr,"Treble ");
    fprintf (stderr,"\n");
   } 
  }

  /* Retrieve sizes and offsets */
  if (ioctl (bgrab->bgrab_dev, VIDIOCGMBUF, &bgrab->vid_mbuf) == -1) {
   DBERROR ("ioctl (VIDIOCGMBUF)");
   return 0;
  }
  /* Print memory info */
  fprintf (stderr,"Memory Map of %i frames: %i bytes\n",bgrab->vid_mbuf.frames,bgrab->vid_mbuf.size);
  for (i=0; i<bgrab->vid_mbuf.frames; i++) {
   fprintf (stderr," Offset of frame %i: %i\n",i,bgrab->vid_mbuf.offsets[i]);
  }
 }
 
 return 1;
}

/* Read card settings */
int bgrabGetSetting(tSDL_bgrab *bgrab, int which_setting)
{
 struct video_picture bgrab_pict;
 
 DBMESSAGE("Reading setting: %i\n",which_setting);
 //
 if (ioctl (bgrab->bgrab_dev, VIDIOCGPICT, &bgrab_pict) == -1) {
  DBERROR ("ioctl (VIDIOCGPICT)");
  return(0);
 } else {
  switch (which_setting) {
   case SETTING_BRIGHTNESS:
    return(bgrab_pict.brightness);
    break;
   case SETTING_HUE:
    return(bgrab_pict.hue);
    break;
   case SETTING_COLOUR:
    return(bgrab_pict.colour);
    break;
   case SETTING_CONTRAST:
    return(bgrab_pict.contrast);
    break;
  } 
 }
 
 return(1);
}

/* Adjust card settings */
int bgrabSetSetting(tSDL_bgrab *bgrab, int which_setting, int value)
{
 struct video_picture bgrab_pict;

 DBMESSAGE("Adjusting setting: %i to %i\n",which_setting, value);
 //
 if (ioctl (bgrab->bgrab_dev, VIDIOCGPICT, &bgrab_pict) == -1) {
  DBERROR("ioctl (VIDIOCGPICT)");
  return 0;
 } else {
  switch (which_setting) {
   case SETTING_BRIGHTNESS:
    bgrab_pict.brightness=value;
    break;
   case SETTING_HUE:
    bgrab_pict.hue=value;
    break;
   case SETTING_COLOUR:
    bgrab_pict.colour=value;
    break;
   case SETTING_CONTRAST:
    bgrab_pict.contrast=value;
    break;
  } 
  if (ioctl (bgrab->bgrab_dev, VIDIOCSPICT, &bgrab_pict) == -1) {
   DBERROR("ioctl (VIDIOCSPICT)");
   return 0;
  } 
 }
 
 return(1);
}

/* Set the input channel and bgrabmode on card */
int bgrabSetChannel(tSDL_bgrab *bgrab, int channel, int bgrabmode)
{
 struct video_capability bgrab_cap;
 struct video_channel bgrab_chan;
 struct video_tuner tuner;

 DBMESSAGE("Setting channel to %i and mode to %i\n", channel, bgrabmode);
   
 /* Query for number of channels */
 if (ioctl (bgrab->bgrab_dev, VIDIOCGCAP, &bgrab_cap) == -1) {
  DBERROR("ioctl (VIDIOCGCAP)");
  return 0;
 } 

 /* Clip channel to allowed range */
 if (channel<0) {
  channel=0;
  DBMESSAGE("Clipping channel to 0\n");
 } else {
  bgrab_cap.channels--;
  if (channel>bgrab_cap.channels) {
   channel=bgrab_cap.channels;
   DBMESSAGE("Clipping channel to %i\n",bgrab_cap.channels);
  }
 }

 /* Set channel number */
 bgrab_chan.channel=0; 
 if (ioctl (bgrab->bgrab_dev, VIDIOCGCHAN, &bgrab_chan) == -1) {
  DBERROR ("ioctl (VIDIOCGCHAN)");
  return(-1);
 } else {
  bgrab_chan.channel=channel; 
  if (ioctl (bgrab->bgrab_dev, VIDIOCSCHAN, &bgrab_chan) == -1) {
   DBERROR ("ioctl (VIDIOCSCHAN)");
   fprintf (stderr,"Channel was %i\n",channel);
   return(-1);
  } 
 }

 /* Set bgrabmode */
 tuner.tuner=0;
 if (channel==CHANNEL_TUNER) {
  if (ioctl (bgrab->bgrab_dev, VIDIOCGTUNER, &tuner) == -1) {
   DBERROR ("ioctl (VIDIOCGTUNER)");
   return 0;
  }
 }
 tuner.mode=bgrabmode;
 if (ioctl (bgrab->bgrab_dev, VIDIOCSTUNER, &tuner) == -1) {
  DBERROR ("ioctl (VIDIOCSTUNER)");
  return 0;
 }

 return(0);
}

/* Set the tuner frequency */
int bgrabSetFrequency(tSDL_bgrab *bgrab, int region, int index)
{
 int frequency;
 unsigned long adjusted_frequency;

 DBMESSAGE("Setting tuner to region %i index %i\n", region, index);
 // 
 frequency=bgrab_get_frequency (region, index);
 if (frequency>0) {
  adjusted_frequency=frequency*16/1000;
  if (ioctl (bgrab->bgrab_dev, VIDIOCSFREQ, &adjusted_frequency) == -1) {
   DBERROR("ioctl (VIDIOCSFREQ)");
   return 0;
  }
 }
 
 return(1); 
}

/* Self running thread that constantly grabs images */
int grab_images_thread (void *data)
{
 tSDL_bgrab *bgrab=(tSDL_bgrab *)data;
 int in_loop;

 DBMESSAGE ("Starting grabbing loop.\n"); 
 
 /* Loop ... grabbing images */
 in_loop=1;
 while (in_loop) {

  /* Lock buffers and state */
  SDL_LockMutex(bgrab->buffer_mutex);

  /* Advance grab-frame number */
  bgrab->current_grab_number=((bgrab->current_grab_number + 1) % 2);

  /* Unlock buffers and state */
  SDL_UnlockMutex(bgrab->buffer_mutex);

  /* Wait for next image in the sequence to complete grabbing */
  if (ioctl (bgrab->bgrab_dev, VIDIOCSYNC, &bgrab->vid_mmap[bgrab->current_grab_number]) == -1) {
   DBERROR("VIDIOCSYNC");
   perror("VIDIOCSYNC\n");
   continue;
   //return(0);
  }

  /* Issue new grab command for this buffer */
  if (ioctl (bgrab->bgrab_dev, VIDIOCMCAPTURE, &bgrab->vid_mmap[bgrab->current_grab_number]) == -1) {
   DBERROR("VIDIOCMCAPTURE");
   fprintf(stderr, "Error! VIDIOCMCAPTURE\n");
   return(0);
  }

  /* Lock buffers and state */
  SDL_LockMutex(bgrab->buffer_mutex);

  /* Announce that a new frame is available */
  bgrab->have_new_frame=1;

  /* Get loop state */
  in_loop=bgrab->grabbing_active;

  /* Unlock buffers and state */
  SDL_UnlockMutex(bgrab->buffer_mutex);

  /* Signal potentially waiting main thread */
  SDL_CondSignal(bgrab->buffer_cond);   
 }

 DBMESSAGE ("Done grabbing loop.\n"); 
 fprintf(stderr, "Done grabbing loop.\n");

 return 1; 
}

/* Initialize grabber and start grabbing-thread */
int bgrabStart (tSDL_bgrab *bgrab, int width, int height, int bgra)
{
 int i;
 int format, depth;
 
 /* Store variables in structure */
 bgrab->width=width;
 bgrab->height=height;

 /* Always grab at 32bit RGB image */
 bgrab->format=VIDEO_PALETTE_RGB32;
 depth=4;
  
 /* Retrieve buffer size and offsets */
 if (ioctl (bgrab->bgrab_dev, VIDIOCGMBUF, &bgrab->vid_mbuf) == -1) {
  DBERROR("ioctl (VIDIOCGMBUF)");
  return(0);
 }
 DBMESSAGE("Grabber buffer and offsets retrieved.\n");
 
 /* Map grabber memory into user space */
 bgrab->bgrab_map = mmap (0, bgrab->vid_mbuf.size, PROT_READ|PROT_WRITE,MAP_SHARED,bgrab->bgrab_dev,0);
 if ((unsigned char *)-1 == (unsigned char *)bgrab->bgrab_map) {
  DBERROR("mmap()");
  return(0);
 }
 DBMESSAGE("Grabber memory mapped.\n");

 /* Generate mmap records */
 for (i=0; i<2; i++) {
  bgrab->vid_mmap[i].format = bgrab->format;
  bgrab->vid_mmap[i].frame  = i;
  bgrab->vid_mmap[i].width  = width;
  bgrab->vid_mmap[i].height = height;
 }

 #if SDL_BYTEORDER == SDL_BIG_ENDIAN
  DBMESSAGE ("Client is: big-endian\n");
  if (bgra) {
   bgrab->rmask = 0x0000ff00;
   bgrab->gmask = 0x00ff0000;
   bgrab->bmask = 0xff000000;
   bgrab->amask = 0x000000ff;
  } else {
   bgrab->rmask = 0xff000000;
   bgrab->gmask = 0x00ff0000;
   bgrab->bmask = 0x0000ff00;
   bgrab->amask = 0x000000ff;
  }
 #else
  DBMESSAGE ("Client is: little-endian\n");
  if (bgra) {
   bgrab->rmask = 0x00ff0000;
   bgrab->gmask = 0x0000ff00;
   bgrab->bmask = 0x000000ff;
   bgrab->amask = 0xff000000;
  } else {
   bgrab->rmask = 0x000000ff;
   bgrab->gmask = 0x0000ff00;
   bgrab->bmask = 0x00ff0000;
   bgrab->amask = 0xff000000;
  }
 #endif

 /* Create framebuffer */
 bgrab->framebuffer = SDL_CreateRGBSurface(SDL_HWSURFACE,width,height,32,
                                           bgrab->rmask,bgrab->gmask,bgrab->bmask,0);
 SDL_SetAlpha(bgrab->framebuffer,0,0);
 if (bgrab->framebuffer==NULL) {
  DBERROR ("Could not create main framebuffer.\n");
  return 0;
 }
 DBMESSAGE ("Main Framebuffer created.\n");

 /* Create deframebuffer for deinterlacing */
 bgrab->deframebuffer = SDL_CreateRGBSurface(SDL_HWSURFACE,width,height,32,
                                           bgrab->rmask,bgrab->gmask,bgrab->bmask,0);
 SDL_SetAlpha(bgrab->deframebuffer,0,0);
 if (bgrab->deframebuffer==NULL) {
  DBERROR ("Could not create second framebuffer.\n");
  return 0;
 }
 DBMESSAGE ("Second Framebuffer created.\n");

    
 /* Calculate framebuffer size */
 bgrab->image_pixels=width*height;
 bgrab->image_size=bgrab->image_pixels*depth;

 /* Create backbuffer */
 if ((bgrab->backbuffer=(unsigned char *)malloc(bgrab->image_size))==NULL) {
  DBERROR ("Out of memory.\n");
  return 0;
 }
  
 /* Reset grab counter variables */
 bgrab->current_grab_number=0;
 bgrab->totalframecount=0;
 bgrab->have_new_frame=0;
  
 /* Initialize mutex */
 bgrab->buffer_mutex = SDL_CreateMutex();
 if (bgrab->buffer_mutex==NULL) {
  DBERROR("Could not create mutex.\n");
  return 0;
 }

 /* Initialize conditional */
 bgrab->buffer_cond = SDL_CreateCond();
 if (bgrab->buffer_cond==NULL) {
  DBERROR("Could not create conditional.\n");
  return 0;
 }

 /* Initiate capture for frames */
 for (i=0; i<2; i++) {
  /* Sync old images in the sequence to complete grabbing */
  ioctl (bgrab->bgrab_dev, VIDIOCSYNC, &bgrab->vid_mmap[i]);
  /* Start capture */
  if (ioctl (bgrab->bgrab_dev, VIDIOCMCAPTURE, &bgrab->vid_mmap[i]) == -1) {
   DBERROR ("VIDIOCMCAPTURE");
   return 0;
  }
 }
 DBMESSAGE("Capture started.\n");

 /* Set thread loop flag */
 bgrab->grabbing_active=1;

 /* Start grabbing thread */
 bgrab->grab_thread = SDL_CreateThread(grab_images_thread,(void *)bgrab);
 if (bgrab->grab_thread==NULL) {
  DBERROR("Could not create thread.\n");
  return 0;
 }
 
 return 1;
}

/* Stop grabbing thread */
int bgrabStop(tSDL_bgrab *bgrab)
{
 /* Set flag for thread to finish */
 bgrab->grabbing_active=0;

 /* Wait for grabbing thread to exit */
 SDL_WaitThread(bgrab->grab_thread,NULL);

 /* Free image buffers */
 if (bgrab->framebuffer) {
  free(bgrab->framebuffer);
  bgrab->framebuffer=NULL;
 }
 if (bgrab->deframebuffer) {
  free(bgrab->deframebuffer);
  bgrab->deframebuffer=NULL;
 }
 if (bgrab->backbuffer) {
  free(bgrab->backbuffer);
  bgrab->backbuffer=NULL;
 }
 
 /* Unmap memory */
 if (munmap (bgrab->bgrab_map, bgrab->vid_mbuf.size) == -1) {
  DBERROR("munmap()");
  return(0);
 }

 return(1);
}


/* Transfer last grabbed image into external buffer, lock to be processed */ 
int bgrabBlitFramebuffer(tSDL_bgrab *bgrab, SDL_Surface *target, int deinterlace)
{
 /* Check if we are grabbing */
 if (bgrab->grabbing_active) {

  /* Increase counter */
  bgrab->totalframecount++;
   
  /* Lock buffers and state */
  SDL_LockMutex(bgrab->buffer_mutex);
  
  /* Check if a new frame is available */
  if (!bgrab->have_new_frame) {
   /* Wait for signal - unlocking mutex in the process */
   SDL_CondWait(bgrab->buffer_cond,bgrab->buffer_mutex);
  }

  /* Copy framebuffer pixel data from other-than-grab framebuffer into current image */
  memcpy (bgrab->framebuffer->pixels,&bgrab->bgrab_map[bgrab->vid_mbuf.offsets[(bgrab->current_grab_number+1) % 2]],bgrab->image_size);

  /* Mark frame as used */
  bgrab->have_new_frame=0;
  
  /* Unlock buffers and state */
  SDL_UnlockMutex(bgrab->buffer_mutex);

  if (deinterlace) {

   /* Deinterlace */
   switch (deinterlace) {
    case 1:
     bgrab_deinterlace(bgrab, bgrab->framebuffer, bgrab->deframebuffer);
     break;
    case 2:
     bgrab_deinterlace_smart(bgrab, bgrab->framebuffer, bgrab->deframebuffer);
     break;
    case 3:
     bgrab_deinterlace_smooth(bgrab, bgrab->framebuffer, bgrab->deframebuffer);
     break;
    default:
     bgrab_deinterlace(bgrab, bgrab->framebuffer, bgrab->deframebuffer);
     break;
   }
            
   /* Blit this buffer to target */
   SDL_BlitSurface(bgrab->deframebuffer, NULL, target, NULL);

  } else {

   /* Blit this buffer to target */
   SDL_BlitSurface(bgrab->framebuffer, NULL, target, NULL);  

  }
 
  /* FPS calculation */
  bgrab_calc_fps(bgrab);
  
 } else {
  /* No grabbing */
  return 0;
 }

 /* Return success */  
 return 1;
}

int bgrabOpen(tSDL_bgrab *bgrab, char *device)
{

 /* Open the bgrab4linux device */
 bgrab->bgrab_dev = open (device, O_RDWR);
 if (bgrab->bgrab_dev == -1) {
  DBERROR("open()");
  return (0);
 }
 
 /* Reset variables */
 bgrab->grabbing_active=0;
 bgrab->width=-1;
 bgrab->height=-1;
 bgrab->format=-1;
 bgrab->input=-1;
 bgrab->image_size=-1;
 bgrab->image_pixels=-1;
 bgrab->fps_update_interval=-1;

 return(1);
}

int bgrabClose(tSDL_bgrab *bgrab)
{
 
 /* Close bgrab4linux device */
 close(bgrab->bgrab_dev);

 return 1;
}
