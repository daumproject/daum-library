package org.daum.library.replicav2;

/**
 * Enumerates the four different outcomes of comparing two VectorClocks.
 * 
 * @author Frits de Nijs
 * @author Peter Dijkshoorn
 */
public enum VectorComparison
{
	GREATER, EQUAL, SMALLER, SIMULTANEOUS
}