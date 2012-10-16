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

public interface TransferFunction 
{
	/**
	 * Funzione di trasferimento 
	 * @param value Valore in input
	 * @return Valore funzione
	 */
	public double evalute(double value);
	
	
	/**
	 * Funzione derivata
	 * @param value Valore in input
	 * @return Valore funzione derivata
	 */
	public double evaluteDerivate(double value);
}
