package com.synalogik.wordcountproject;

/**
 * @author NicholasChristou
 * 
 * Encapsulates number of occurrences of specified word length
 *
 */
public class NumberOfWordLengthEntry implements Comparable<NumberOfWordLengthEntry>{
	
		private int wordLength = 0;
		private int occurences = 0;
		
		public NumberOfWordLengthEntry(int wordLength, int occurences) {
			this.wordLength = wordLength;
			this.occurences = occurences;
		}
		
		public Integer getWordLength() {
			return wordLength;
		}
		public void setWordLength(int wordLength) {
			this.wordLength = wordLength;
		}
		public int getOccurrences() {
			return occurences;
		}
		public void setOccurences(int occurences) {
			this.occurences = occurences;
		}

		//Order using word length
		@Override
		public int compareTo(NumberOfWordLengthEntry o) {
			return this.getWordLength().compareTo(o.getWordLength());
		}
		
}
