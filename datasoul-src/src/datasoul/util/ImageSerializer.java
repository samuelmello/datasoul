/* 
 * Copyright 2005-2010 Samuel Mello
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

package datasoul.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *
 * @author samuel
 */
public class ImageSerializer {

    public static String imageToStr(BufferedImage img){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( img, "png", baos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        byte[] ba = baos.toByteArray();

        int len=ba.length,j;
        StringBuffer sb=new StringBuffer(len*2);
        for (j=0;j<len;j++) {
            String sByte=Integer.toHexString((int)(ba[j] & 0xFF));
            if (sByte.length()!=2){
                sb.append('0');
            }
            sb.append(sByte);
        }
        return sb.toString();
    }

    public static BufferedImage strToImage(String strImage){

        String str="";
        int intAux=0;
        byte[] bytes = new byte[strImage.length()/2];
        for(int i=0; i< strImage.length()-1;i=i+2){
            str = strImage.substring(i,i+2);
            intAux = Integer.parseInt(str,16);
            bytes[i/2]=(byte)intAux;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(bais);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

