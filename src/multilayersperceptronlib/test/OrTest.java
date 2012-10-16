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
import multilayersperceptronlib.transferfunctions.SigmoidalTransfer;

public class OrTest 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int[] layers = new int[]{ 2, 5, 1 };
		
		MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new SigmoidalTransfer());
		
		/* Learning */
		for(int i = 0; i < 10000; i++)
		{
			double[] inputs = new double[]{Math.round(Math.random()), Math.round(Math.random())};
			double[] output = new double[1];
			double error;
			
			
			if((inputs[0] == 1.0) || (inputs[1] == 1.0))
				output[0] = 1.0;
			else
				output[0] = 0.0;
			

			System.out.println(inputs[0]+" or "+inputs[1]+" = "+output[0]);
			
			error = net.backPropagate(inputs, output);
			System.out.println("Error at step "+i+" is "+error);
		}
		
		System.out.println("Learning completed!");
		
		/* Test */
		double[] inputs = new double[]{0.0, 1.0};
		double[] output = net.execute(inputs);

		System.out.println(inputs[0]+" or "+inputs[1]+" = "+Math.round(output[0])+" ("+output[0]+")");
		
		
	}
}
