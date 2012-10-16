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
package multilayersperceptronlib.test;

import multilayersperceptronlib.MultiLayerPerceptron;
import multilayersperceptronlib.transferfunctions.HyperbolicTransfer;

public class MassiveTest 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int[] layers = new int[]{ 16*16, 16*16/2, 40 };
		
		MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new HyperbolicTransfer());
		
		/* Learning */
		for(int i = 0; i < 100000; i++)
		{
			double[] inputs = new double[16*16];
			double[] output = new double[40];
			double error;
			
			for(int j = 0; j < inputs.length; j++)
				inputs[j] = 0;
			
			error = net.backPropagate(inputs, output);
			System.out.println("Error at step "+i+" is "+error);
		}
		
		System.out.println("Learning completed!");
		
		
		
	}
}
