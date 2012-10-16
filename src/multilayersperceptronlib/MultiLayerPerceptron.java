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
package multilayersperceptronlib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MultiLayerPerceptron implements Cloneable
{
	protected double			fLearningRate = 0.6;
	protected Layer[]			fLayers;
	protected TransferFunction 	fTransferFunction;

	
	/**
	 * Crea una rete neuronale mlp
	 * 
	 * @param layers Numero di neuroni per ogni layer
	 * @param learningRate Costante di apprendimento
	 * @param fun Funzione di trasferimento
	 */
	public MultiLayerPerceptron(int[] layers, double learningRate, TransferFunction fun)
	{
		fLearningRate = learningRate;
		fTransferFunction = fun;
		
		fLayers = new Layer[layers.length];
		
		for(int i = 0; i < layers.length; i++)
		{			
			if(i != 0)
			{
				fLayers[i] = new Layer(layers[i], layers[i - 1]);
			}
			else
			{
				fLayers[i] = new Layer(layers[i], 0);
			}
		}
	}
	

	
	/**
	 * Esegui la rete
	 * 
	 * @param input Valori di input
	 * @return Valori di output restituiti dalla rete
	 */
	public double[] execute(double[] input)
	{
		int i;
		int j;
		int k;
		double new_value;
		
		double output[] = new double[fLayers[fLayers.length - 1].Length];
		
		// Put input
		for(i = 0; i < fLayers[0].Length; i++)
		{
			fLayers[0].Neurons[i].Value = input[i];
		}
		
		// Execute - hiddens + output
		for(k = 1; k < fLayers.length; k++)
		{
			for(i = 0; i < fLayers[k].Length; i++)
			{
				new_value = 0.0;
				for(j = 0; j < fLayers[k - 1].Length; j++)
					new_value += fLayers[k].Neurons[i].Weights[j] * fLayers[k - 1].Neurons[j].Value;
				
				new_value += fLayers[k].Neurons[i].Bias;
				
				fLayers[k].Neurons[i].Value = fTransferFunction.evalute(new_value);
			}
		}
		
		
		// Get output
		for(i = 0; i < fLayers[fLayers.length - 1].Length; i++)
		{
			output[i] = fLayers[fLayers.length - 1].Neurons[i].Value;
		}
		
		return output;
	}
	
	
	
	/**
	 * Algoritmo di backpropagation per il learning assistito
	 * (Versione multi threads)
	 * 
	 * Convergenza non garantita e molto lenta; utilizzare come criteri
	 * di stop una norma tra gli errori precedente e corrente, ed un
	 * numero massimo di iterazioni.
	 * 
	 * Wikipedia:
	 * 	The training data is broken up into equally large batches for each 
	 * 	of the threads. Each thread executes the forward and backward propagations. 
	 * 	The weight and threshold deltas are summed for each of the threads. 
	 * 	At the end of each iteration all threads must pause briefly for the 
	 * 	weight and threshold deltas to be summed and applied to the neural network. 
	 * 	This process continues for each iteration. 
	 * 
	 * @param input Valori in input
	 * @param output Valori di output atteso
	 * @param nthread Numero di thread da spawnare per il learning
	 * @return Errore delta tra output generato ed output atteso
	 */
	public double backPropagateMultiThread(double[] input, double[] output, int nthread)
	{
		return 0.0;
	}

	
	
	/**
	 * Algoritmo di backpropagation per il learning assistito
	 * (Versione single thread)
	 * 
	 * Convergenza non garantita e molto lenta; utilizzare come criteri
	 * di stop una norma tra gli errori precedente e corrente, ed un
	 * numero massimo di iterazioni.
	 * 
	 * @param input Valori in input (scalati tra 0 ed 1)
	 * @param output Valori di output atteso (scalati tra 0 ed 1)
	 * @return Errore delta tra output generato ed output atteso
	 */
	public double backPropagate(double[] input, double[] output)
	{
		double new_output[] = execute(input);
		double error;
		int i;
		int j;
		int k;
		
		/* doutput = correct output (output) */
		
		// Calcoliamo l'errore dell'output
		for(i = 0; i < fLayers[fLayers.length - 1].Length; i++)
		{
			error = output[i] - new_output[i];
			fLayers[fLayers.length - 1].Neurons[i].Delta = error * fTransferFunction.evaluteDerivate(new_output[i]);
		} 
	
		
		for(k = fLayers.length - 2; k >= 0; k--)
		{
			// Calcolo l'errore dello strato corrente e ricalcolo i delta
			for(i = 0; i < fLayers[k].Length; i++)
			{
				error = 0.0;
				for(j = 0; j < fLayers[k + 1].Length; j++)
					error += fLayers[k + 1].Neurons[j].Delta * fLayers[k + 1].Neurons[j].Weights[i];
								
				fLayers[k].Neurons[i].Delta = error * fTransferFunction.evaluteDerivate(fLayers[k].Neurons[i].Value);				
			}
			
			// Aggiorno i pesi dello strato successivo
			for(i = 0; i < fLayers[k + 1].Length; i++)
			{
				for(j = 0; j < fLayers[k].Length; j++)
					fLayers[k + 1].Neurons[i].Weights[j] += fLearningRate * fLayers[k + 1].Neurons[i].Delta * 
							fLayers[k].Neurons[j].Value;
				fLayers[k + 1].Neurons[i].Bias += fLearningRate * fLayers[k + 1].Neurons[i].Delta;
			}
		}	
		
		// Calcoliamo l'errore 
		error = 0.0;
		
		for(i = 0; i < output.length; i++)
		{
			error += Math.abs(new_output[i] - output[i]);
			
			//System.out.println(output[i]+" "+new_output[i]);
		}

		error = error / output.length;
		return error;
	}
	
	
	/**
	 * Salva una rete MLP su file
	 * 
	 * @param path Path nel quale salvare la rete MLP
	 * @return true se salvata correttamente
	 */
	public boolean save(String path)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
		}
		catch (Exception e) 
		{ 
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Carica una rete MLP da file
	 * @param path Path dal quale caricare la rete MLP
	 * @return Rete MLP caricata dal file o null
	 */
	public static MultiLayerPerceptron load(String path)
	{
		try
		{
			MultiLayerPerceptron net;
			
			FileInputStream fin = new FileInputStream(path);
			ObjectInputStream oos = new ObjectInputStream(fin);
			net = (MultiLayerPerceptron) oos.readObject();
			oos.close();
			
			return net;
		}
		catch (Exception e) 
		{ 
			return null;
		}
	}
	
	

	/**
	 * @return Costante di apprendimento
	 */
	public double getLearningRate()
	{
		return fLearningRate;
	}
	
	
	/**
	 * 
	 * @param rate
	 */
	public void	setLearningRate(double rate)
	{
		fLearningRate = rate;
	}
	
	
	/**
	 * Imposta una nuova funzione di trasferimento
	 * 
	 * @param fun Funzione di trasferimento
	 */
	public void setTransferFunction(TransferFunction fun)
	{
		fTransferFunction = fun;
	}
	
	
	
	/**
	 * @return Dimensione layer di input
	 */
	public int getInputLayerSize()
	{
		return fLayers[0].Length;
	}
	
	
	/**
	 * @return Dimensione layer di output
	 */
	public int getOutputLayerSize()
	{
		return fLayers[fLayers.length - 1].Length;
	}
}

