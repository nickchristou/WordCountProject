package com.synalogik.wordcountproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordCount {

		private final long executionId;
		private String content;

		public WordCount(long id, String content) {
			this.executionId = id;
			this.content = content;
		}

		public String getContent() {
			return content;
		}

		public long getId() {
			return executionId;
		}
		
		/**
		 * Takes input string and returns as an array of separate words
		 * 
		 * ASSUMPTIONS MADE: from the expected output given in the example
		 * I've made the assumption that certain characters (eg. the '&') are to be counted
		 * towards the word count total. As well as this, certain characters 
		 * (such as the slashes in the date) are to be counted towards word length but others 
		 * (such as the period). For this reason I am stripping out the following:
		 * 
		 * - .
		 * - ,
		 * - :
		 * - ;
		 * 
		 * As well as this, I have assumed from the given clue  regarding 
		 * formatted numbers and the example text that '*' 
		 * is not to be counted so I am stripping this out as well
		 * 
		 * With more time and a more specific brief, this could either be enhanced by using 
		 * a REGEX to filter out unwanted chars, or could also add a range of different REGEX filters
		 * to allow the user to define which characters to use/discount with a config param
		 * 
		 * @param input - String to separate
		 * @return - word array
		 */
		public static String[] separateWords(String input) 
		{
			
			//Trim specific chars
			input = input.replaceAll("\\.", "");
			input = input.replaceAll("\\,", "");
			input = input.replaceAll("\\;", "");
			input = input.replaceAll("\\:", "");
			input = input.replaceAll("\\*", "");


			//Trim content to ensure no whitespace counted at beginning/end
		    String trimmedContent = input.trim();

		    //Split content into words
		    return Pattern.compile("\\s+").split(trimmedContent);
		}
		
	     /**
		 * Counts words of input string
		 * 
		 * This assumes that any sequence of characters (including numbers and certain chars)
		 * counts as 1 word. Whitespace at beginning or end of word is discounted
		 * 
		 * @param input - text to word count
		 * @return - word count as an int
		 */
		public static int countWords(String input)
		{
			//Length of array = no. of words
			return separateWords(input).length;
		}
		
		/**
		 * Finds average word length of all words in input string
		 * 
		 * This again assumes that any sequence of characters (including numbers and certain chars)
		 * counts as 1 word. Whitespace at beginning or end of word is discounted
		 * 
		 * If no words found returns 0
		 * 
		 * @param input - text to analyse
		 * @return - average as a double
		 */
		public static double averageWordLength(String input) 
		{
			//Init average
			double sum = 0.0d;

		    //Split content into words
		    String[] words = separateWords(input);
		    
		    //find sum
		    for (String word : words) {
		    	sum += word.length();
		    }
			
		    //Prevent divide by 0 error
		    if (words.length != 0)
		    	//average = sum/total words
		    	return sum/words.length;
		    
		    //Return 0
		    else
		    	return 0.0d;
		}
		
		/**
		 * Returns a list of word lengths with number of occurrences
		 * 
		 * This again assumes that any sequence of characters (including numbers and certain chars)
		 * counts as 1 word. Whitespace at beginning or end of word is discounted
		 * 
		 * It was unspecified whether or not this needs to be presented in order, this method orders list
		 * by the word length as presented in the example output
		 * 
		 * 
		 * @param input - text to analyse
		 * @return list of word lengths - this is then used to format response
		 */
		public static List<NumberOfWordLengthEntry> numberOfWordsOfLength(String input)
		{
			//Init List
			List<NumberOfWordLengthEntry> wordLengthList = new ArrayList<>();

		    //Split content into words
		    String[] words = separateWords(input);
		    
		    for (String word : words) {
		    	
		    	//Search if list already contains entry of same length
		    	NumberOfWordLengthEntry wordEntry = wordLengthList.stream().filter(wordLengthEntry -> wordLengthEntry.getWordLength().equals(word.length())).findFirst().orElse(null);
		    	
		    	//If not, add entry with occurrence of 1
		    	if(wordEntry == null) {
		    		wordLengthList.add(new NumberOfWordLengthEntry(word.length(), 1));
		    	}
		    	//Else add 1 to occurrences counter
		    	else {
		    		wordEntry.setOccurences(wordEntry.getOccurrences()+1);
		    	}
		    }
		    
		    //Wasn't specified whether or not this needs to be in order but do this here anyway
		    Collections.sort(wordLengthList);
		    
			return wordLengthList;
			
		}
		
		/**
		 * Returns a filtered list of word lengths containing the word lengths with the max occurrences
		 * 
		 * @param wordLengths - List of word lengths
		 * @return - filtered list
		 */
		public static List<NumberOfWordLengthEntry> wordLengthMaxOccurringSummary(List<NumberOfWordLengthEntry> wordLengths)
		{	
			//Stream and return max by occurrences
			return wordLengths.stream().collect(
					Collectors.groupingBy(NumberOfWordLengthEntry::getOccurrences, TreeMap::new, Collectors.toList()))
					.lastEntry()
                    .getValue();
		}
	
}
