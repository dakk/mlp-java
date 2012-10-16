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

public class SinTest 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int[] layers = new int[]{ 1, 3, 1 };
		
		MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new SigmoidalTransfer());
		
		/* Learning */
		for(int i = 0; i < 10000; i++)
		{
			double[] inputs = new double[]{Math.random()*4};
			double[] output = new double[]{Math.sin(inputs[0])};
			double error;
					

			System.out.println("sin("+inputs[0]+") = "+output[0]);
			
			error = net.backPropagate(inputs, output);
			System.out.println("Error at step "+i+" is "+error);
		}
		
		System.out.println("Learning completed!");
		
		/* Test */
		double[] inputs = new double[]{Math.random()*4};
		double[] output = net.execute(inputs);


		System.out.println("sin("+inputs[0]+") = "+output[0]+" (real is "+Math.sin(inputs[0])+")");	
		
	}
}
