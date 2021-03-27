package com.synalogik.wordcountproject;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller class
 * 
 * I've broken the task down into 4 main bits of functionality as HTTP GET requests
 * 
 * -Word count
 * -Average word length
 * -List of word lengths and occurrences
 * -List of max word length occurrences and their respective lengths
 * 
 * Output of these calls aims to match the example output given in brief.
 *
 */
@Controller
public class WordCountController {

	//String templates
	private static final String wordCountTemplate = "Word count = %s";
	private static final String averageLengthTemplate = "Average word length = %s";
	private static final String numberOfWordTemplate = "Number of words of length ";
	private static final String wordLengthMaxTemplate = "The most frequently occurring word length is ";

	//Ticks upwards with each call made
	private final AtomicLong executionId = new AtomicLong();
	

	/**
	 * WORD COUNT CALL
	 */
	@GetMapping("/wordcount")	
	public WordCount wordcount(@RequestParam(value = "path", defaultValue = "") String path, Model model) {

		//Init output and text
		String output = "";
		String text = FileReadUtil.fileRead(FileReadUtil.constructPath(path));
				
		//If failed to read, output as response
		if (text.contains("Failed to read")) 
		{
			output = text;
		}
		//Else perform function
		else 
		{
			output = String.format(wordCountTemplate, WordCount.countWords(text));
		}
				
		model.addAttribute("result", output);
		
		return new WordCount(executionId.incrementAndGet(), output);
	}
	
	/**
	 * AVERAGE WORD LENGTH CALL
	 */
	@GetMapping("/averagelength")	
	public WordCount averagelength(@RequestParam(value = "path", defaultValue = "") String path, Model model) {
		
		//Init output and text
		String output = "";
		String text = FileReadUtil.fileRead(FileReadUtil.constructPath(path));
				
		//If failed to read, output as response
		if (text.contains("Failed to read")) 
		{
			output = text;
		}
		//Else perform function
		else 
		{
			output = String.format(averageLengthTemplate, WordCount.averageWordLength(text));
		}
		
		model.addAttribute("result", output);
		return new WordCount(executionId.incrementAndGet(), output);
	}
	
	/**
	 * WORD LENGTHS CALL
	 */
	@GetMapping("/wordlengths")	
	public WordCount wordlengths(@RequestParam(value = "path", defaultValue = "") String path, Model model) {
		
		//Init output and text
		String output = "";
		String text = FileReadUtil.fileRead(FileReadUtil.constructPath(path));
				
		//If failed to read, output as response
		if (text.contains("Failed to read")) 
		{
			output = text;
		}
		//Else perform function
		else 
		{
		
			List<NumberOfWordLengthEntry> wordLengths = WordCount.numberOfWordsOfLength(text);
		
			for (NumberOfWordLengthEntry wordLength : wordLengths) {
				output += numberOfWordTemplate + wordLength.getWordLength() + 
						" is " + wordLength.getOccurrences() + System.getProperty("line.separator");
			}
		}
		
		model.addAttribute("result", output);
		
		return new WordCount(executionId.incrementAndGet(), output);
	}
	
	/**
	 * MAX OCCURING WORD LENGTHS CALL
	 */
	@GetMapping("/wordlengthmax")	
	public WordCount wordlengthmax(@RequestParam(value = "path", defaultValue = "") String path, Model model) {
		
		//Init output and text
		String output = "";
		String text = FileReadUtil.fileRead(FileReadUtil.constructPath(path));
		
		//If failed to read, output as response
		if (text.contains("Failed to read")) 
		{
			output = text;
		}
		//Else perform function
		else 
		{
			
			List<NumberOfWordLengthEntry> wordLengths = WordCount.wordLengthMaxOccurringSummary(WordCount.numberOfWordsOfLength(text));

			//If list returns null or empty (eg. no words)
			if (wordLengths == null || wordLengths.isEmpty()) 
			{
				output += "The input text returned 0 word occurences";
			}
			//If list size is 1, simply print word occurrences and word length
			else if (wordLengths.size() == 1)
			{
				output += wordLengthMaxTemplate + wordLengths.get(0).getOccurrences() + 
						", for the word length " + wordLengths.get(0).getWordLength();
			}
			//Print all word lengths, including "&" once before list end 
			else {
				int listCount = 0;
				//selecting first in list is fine, since all should have same number of occurrences
				output += wordLengthMaxTemplate + wordLengths.get(0).getOccurrences() + ", for word lengths of ";
			
				//For each word occurrences entry
				for (NumberOfWordLengthEntry wordLength : wordLengths) {
					
					//2nd from end of list - no comma
					if (listCount == wordLengths.size()-2)
						output += wordLength.getWordLength() + " ";
					
					//last in list - add "&" and last value
					else if (listCount == wordLengths.size()-1)
						output += "& " + wordLength.getWordLength();

					//Otherwise insert value and comma
					else
						output += wordLength.getWordLength() + ", ";
					
					listCount++;
				}//end for loop
			}//end else block
		}
		
		model.addAttribute("result", output);
		
		return new WordCount(executionId.incrementAndGet(), output);
	}
		
}
