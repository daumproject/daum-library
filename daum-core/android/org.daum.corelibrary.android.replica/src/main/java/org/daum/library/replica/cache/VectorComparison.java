package org.daum.library.replica.cache;

/**
 * Enumerates the four different outcomes of comparing two VectorClocks.
 */
public enum VectorComparison
{
	GREATER, EQUAL, SMALLER, SIMULTANEOUS
}