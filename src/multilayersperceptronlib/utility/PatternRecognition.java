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

import multilayersperceptronlib.MultiLayerPerceptron;
import multilayersperceptronlib.TransferFunction;
import multilayersperceptronlib.transferfunctions.SigmoidalTransfer;

public class PatternRecognition 
{
	protected MultiLayerPerceptron fNetwork;
	protected int fNPatterns;
	protected int fImageSize;
	
	
	/**
	 * 
	 * @param imgSize Dimensione immagine
	 * @param nPatterns Numero di patterns
	 * @param learningRate Costante di apprendimento
	 * @param transferFun Funzione di trasferimento
	 */
	public PatternRecognition(int imgSize, int nPatterns, double learningRate, TransferFunction transferFun)
	{
		int[] layers = new int[]{ imgSize * imgSize, imgSize, nPatterns };
		fNPatterns = nPatterns;
		fImageSize = imgSize;
		
		fNetwork = new MultiLayerPerceptron(layers, learningRate, transferFun);		
	}
	
	
	/**
	 * Riconoscere un pattern in un immagine
	 * 
	 * @param imgPath Path dell'immagine da riconoscere
	 * @return Numero del pattern riconosciuto
	 */
	public int recognize(String imgPath)
	{
		double[] inputs = ImageProcessingBW.loadImage(imgPath, fImageSize, fImageSize);
		double[] output = fNetwork.execute(inputs);
		int max = 0;

		for(int i = 1; i < fNPatterns; i++)
		{
			if(output[i] > output[max])
			{
				max = i;
			}
		}
		
		return max;
	}
	

	/**
	 * Riconoscere un pattern in una bitmap
	 * 
	 * @return Numero del pattern riconosciuto
	 */
	public int recognize(boolean[][] bitMap, int sizeX, int sizeY)
	{
		// Riempio l'input con la bitmap
		double[] inputs = new double[fNetwork.getInputLayerSize()];
		
		int x = 0; 
		int y = 0;
		
		for(int i = 0; i < fNetwork.getInputLayerSize(); i++)
		{
			if(bitMap[y][x] == false)
				inputs[y*sizeX + x] = 0.0;
			else
				inputs[y*sizeX + x] = 1.0;
			
			x++;
			
			if(x >= sizeX)
			{
				x = 0;
				y++;
			}
		}
		
		
		double[] output = fNetwork.execute(inputs);
		int max = 0;

		for(int i = 1; i < fNPatterns; i++)
		{
			if(output[i] > output[max])
			{
				max = i;
			}
		}
		
		return max;
	}
	
	/**
	 * Step di learning, backpropaga un solo sample, passato come parametro
	 * 
	 * @param input Input normalizzato in un double[]
	 * @param output Identificatore del pattern
	 * @return Errore del passo
	 */
	public double learningStep(double[] input, int output)
	{
		if(input == null)
		{
			return 0;
		}

		double[] outputs = new double[fNPatterns];

		for(int l = 0; l < fNPatterns; l++)
				outputs[l] = 0.0;
				
		outputs[output] = 1.0;

		return fNetwork.backPropagate(input, outputs);
	}
	
	/**
	 * Step di learning, backpropaga tutti i sample di tutti i patterns 
	 * una volta
	 * 
	 * @param dir Cartella dove trovare le cartelle coi sample
	 * @param imagesPerPattern Numero di samples per cartella
	 * @return Errore relativo del passo
	 */
	public double learningStep(String dir, int imagesPerPattern)
	{
		double error = 0.0;
		for(int k = 1; k < imagesPerPattern; k++)
		{
			for(int j = 1; j < fNPatterns+1; j++)
			{		
				String pattern = dir+"/"+j+"/"+k+".png";
				double[] inputs = ImageProcessingBW.loadImage(pattern, fImageSize, fImageSize);
				
				if(inputs == null)
				{
					continue;	
				}
				double[] output = new double[fNPatterns];

				for(int l = 0; l < fNPatterns; l++)
					output[l] = 0.0;
				
				output[j-1] = 1.0;
				
				error += fNetwork.backPropagate(inputs, output);			
			}
		}
		return (error / (double) (fNPatterns * imagesPerPattern));
	}

}
