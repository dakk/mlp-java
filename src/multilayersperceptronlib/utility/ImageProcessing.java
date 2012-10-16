/*
 *     mlp-java, Copyright (C) 2012 Davide Gessa
 * 
 * 	This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package multilayersperceptronlib.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessing 
{

	public static double[] loadImage(String path, int sizex, int sizey, boolean color)
	{
		File imgLoc = new File(path);
		
		BufferedImage bi;	// Immagine formattata
		BufferedImage img; 	// Immagine caricata

		
		if(color)
			bi = new BufferedImage(
					sizex, 
					sizey,
					BufferedImage.TYPE_INT_ARGB);
		else
			bi = new BufferedImage(
		         	sizex, 
		            sizey,
		            BufferedImage.TYPE_BYTE_GRAY);
		
		try 
		{
		   img = ImageIO.read(imgLoc);
		}
		catch (IOException ex) 
		{
			System.out.println(path+" not loaded");
			return null;
		}
	   
		
        Graphics2D g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
	    double[] data = new double[sizex*sizey];
	    
        for(int i = 0; i < sizex; i++)
        	for(int j = 0; j < sizey; j++)
        	{
        		int[] d = new int[3];
        		bi.getRaster().getPixel(i, j, d);
        		
        		data[i*sizex + j] = ((double) d[0]) / 255.0;
        		//System.out.println(data[i*sizex + j]+" -> "+d[0]);
        	}
        		
		return data;		
	}
	

	public static boolean saveImage(String path, double[] data, int sizex, int sizey, boolean color)
	{
		BufferedImage bi;
		
		if(color)
			bi = new BufferedImage(
	            sizex, 
	            sizey,
	            BufferedImage.TYPE_INT_ARGB);
		else
			bi = new BufferedImage(
		            sizex, 
		            sizey,
		            BufferedImage.TYPE_BYTE_GRAY);
				
		for(int i = 0; i < sizex; i++)
			for(int j = 0; j < sizey; j++)
			{
				//bi.setRGB(i, j, (int) data[i*sizex + j] * 255);
				
				int[] d = new int[3];
				d[0] = (int) (data[i*sizex + j] * 255);
				d[1] = 0;
				d[2] = 0;
        		bi.getRaster().setPixel(i, j, d);
        		
        		//System.out.println(data[i*sizex + j]);
        		//System.out.println(data[i*sizex + j]+" -> "+d[0]);
        		//data[i*sizex + j] = ((double) d[0]/255.0);
			}
		
		try 
		{
		    File outputfile = new File(path);
		    ImageIO.write(bi, "png", outputfile);
		    return true;
		} 
		catch (IOException e) 
		{
			return false;
		}
	}
}
