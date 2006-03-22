#include "SDL_bgrab.h"

int bgrab_deinterlace (tSDL_bgrab *bgrab, SDL_Surface *source, SDL_Surface *target)
{
 int y, ymax, x, xmax;
 int pitch = source->pitch;
 unsigned char *psrc = source->pixels;
 unsigned char *pdst = target->pixels + pitch;
 unsigned char *psrcA = psrc;
 unsigned char *psrcB = psrc +   pitch;
 unsigned char *psrcC = psrc + 2*pitch;


 /* Copy first line */
 memcpy(target->pixels, psrc, pitch);
 /* Deinterlace lines */
 xmax = source->w;
 ymax = source->h-1;
 for (y=1; y<ymax; y++) {
  for (x=0; x<xmax; x++) {

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
   psrcA++;
   psrcB++;
   psrcC++;
   pdst++;

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
   psrcA++;
   psrcB++;
   psrcC++;
   pdst++;

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
         
   psrcA += 2;
   psrcB += 2;
   psrcC += 2;
   pdst += 2;
  }
 }
 /* Copy last line */
 memcpy(pdst, psrcA, pitch);

 return 1;
}

int bgrab_deinterlace_smart (tSDL_bgrab *bgrab, SDL_Surface *source, SDL_Surface *target)
{
 int y, ymax, x, xmax;
 unsigned int diff;
 int pitch = source->pitch;
 unsigned char *psrc = source->pixels;
 unsigned char *pdst = target->pixels + pitch;
 unsigned char *psrcA = psrc;
 unsigned char *psrcB = psrc +   pitch;
 unsigned char *psrcC = psrc + 2*pitch;
 unsigned char *pback = bgrab->backbuffer + pitch;

 /* Copy first line */
 memcpy(target->pixels, psrc, pitch);
 /* Deinterlace lines */
 xmax = source->w;
 ymax = source->h-1;
 for (y=1; y<ymax; y++) {
  for (x=0; x<xmax; x++) {

   /* Determine if we need to deinterlace */
   if (*psrcB<*pback) {
    diff = *pback - *psrcB; 
   } else {
    diff = *psrcB - *pback;
   }
   pback++;
   psrcB++;
   if (*psrcB<*pback) {
    diff += (*pback - *psrcB); 
   } else {
    diff += (*psrcB - *pback);
   }
   pback++;
   psrcB++;
   if (*psrcB<*pback) {
    diff += (*pback - *psrcB); 
   } else {
    diff += (*psrcB - *pback);
   }
   pback += 2;
   psrcB -= 2;

   /* Test threshold */
   if (diff>64) {
    /* Deinterlace */

    *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
    psrcA++;
    psrcB++;
    psrcC++;
    pdst++;
 
    *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
    psrcA++;
    psrcB++;
    psrcC++;
    pdst++;
 
    *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*psrcC) >> 2);
          
    psrcA += 2;
    psrcB += 2;
    psrcC += 2;
    pdst += 2;
   } else {
    /* Just copy */
    *(unsigned int *)pdst = *(unsigned int *)psrcB;
    psrcA += 4;
    psrcB += 4;
    psrcC += 4;
    pdst += 4;        
   }
      
  }
 }
 /* Copy last line */
 memcpy(pdst, psrcA, pitch);

 /* Copy current source to backbuffer */
 memcpy(bgrab->backbuffer,source->pixels,pitch*source->h);
 
 return 1;
}


int bgrab_deinterlace_smooth (tSDL_bgrab *bgrab, SDL_Surface *source, SDL_Surface *target)
{
 int y, ymax, x, xmax;
 int pitch = source->pitch;
 unsigned char *psrc = source->pixels;
 unsigned char *pdst = target->pixels + pitch;
 unsigned char *psrcA = psrc;
 unsigned char *psrcB = psrc +   pitch;
 unsigned char *psrcC = psrc + 2*pitch;
 unsigned char *pback = bgrab->backbuffer + pitch;

 /* Copy first line */
 memcpy(target->pixels, psrc, pitch);
 /* Deinterlace lines */
 xmax = source->w;
 ymax = source->h-1;
 for (y=1; y<ymax; y++) {
  for (x=0; x<xmax; x++) {

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB + (int)*pback + (int)*pback + (int)*psrcC) / 6);
   psrcA++;
   psrcB++;
   psrcC++;
   pdst++;
   pback++;

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB  + (int)*pback + (int)*pback + (int)*psrcC) / 6);
   psrcA++;
   psrcB++;
   psrcC++;
   pdst++;
   pback++;

   *pdst = (unsigned char)( ((int)*psrcA + (int)*psrcB + (int)*psrcB  + (int)*pback + (int)*pback + (int)*psrcC) / 6);
   psrcA += 2;
   psrcB += 2;
   psrcC += 2;
   pdst += 2;
   pback += 2;
  }
 }
 /* Copy last line */
 memcpy(pdst, psrcA, pitch);

 /* Copy current destination to backbuffer */
 memcpy(bgrab->backbuffer,target->pixels,pitch*source->h);
 
 return 1;
}
