/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.synalogik.wordcountproject;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.synalogik.wordcountproject.FileReadUtil;
import com.synalogik.wordcountproject.NumberOfWordLengthEntry;
import com.synalogik.wordcountproject.WordCount;
import com.synalogik.wordcountproject.WordCountController;


@WebMvcTest(controllers = WordCountController.class)
public class WordCountApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Home page content
	 */
	@Test
	public void homePage() throws Exception {
		mockMvc.perform(get("/index.html"))
				.andExpect(content().string(containsString("Word Count Project")));
	}

	/**
	 * Word Count call
	 */
	@Test
	public void wordCountCall() throws Exception {
		mockMvc.perform(get("/wordcount"))
				.andExpect(content().string(containsString("Word count")));
	}

	/**
	 * Average Lengths call
	 */
	@Test
	public void averageLengthsCall() throws Exception {
		mockMvc.perform(get("/averagelength"))
				.andExpect(content().string(containsString("Average word length")));
	}
	
	/**
	 * Word Lengths call
	 */
	@Test
	public void wordLengthsCall() throws Exception {
		mockMvc.perform(get("/wordlengths"))
				.andExpect(content().string(containsString("Number of words of length")));
	}
	
	/**
	 * Word Lengths Max call
	 */
	@Test
	public void wordLengthMaxCall() throws Exception {
		mockMvc.perform(get("/wordlengthmax"))
				.andExpect(content().string(containsString("The most frequently occurring word")));
	}
	
	/**
	 * Ensure file read as string
	 */
	@Test
	public void fileReadTest() {
			assertEquals("Test 123", FileReadUtil.fileRead("src/test/resources/test.txt"));
	}
	
	/**
	 * Ensure file read exception handled
	 */
	@Test
	public void fileReadExceptionTest() {
		assertTrue(FileReadUtil.fileRead("nonsense").contains("Failed to read"));
	}
	

	/**
	 * Ensures the expected output of each word counting function
	 * using input given in example
	 */
	@Test
	public void expectedOutputTest() {
		String testInput = "Hello world & good morning. The date is 18/05/2016";
		double average = WordCount.averageWordLength(testInput);
		List<NumberOfWordLengthEntry> wordLengthList = WordCount.numberOfWordsOfLength(testInput);
		List<NumberOfWordLengthEntry> maxWordLengthList = WordCount.wordLengthMaxOccurringSummary(wordLengthList);
		
		//Assert word count = 9
		assertEquals(9, WordCount.countWords(testInput));
		
		//Assert average length as 4.5 recurring
		assertTrue(4.555d < average, "found average: " + average);
		assertTrue(4.6d > average, "found average: " + average);
		
		//Assert number of word lengths list entry as given in output (7 total lengths)
		assertEquals(7, wordLengthList.size());
		
		//Word length 1
		assertEquals(1, wordLengthList.get(0).getWordLength());
		assertEquals(1, wordLengthList.get(0).getOccurrences());
		
		//Word length 2
		assertEquals(2, wordLengthList.get(1).getWordLength());
		assertEquals(1, wordLengthList.get(1).getOccurrences());
		
		//Word length 3
		assertEquals(3, wordLengthList.get(2).getWordLength());
		assertEquals(1, wordLengthList.get(2).getOccurrences());
		
		//Word length 4
		assertEquals(4, wordLengthList.get(3).getWordLength());
		assertEquals(2, wordLengthList.get(3).getOccurrences());
		
		//Word length 5
		assertEquals(5, wordLengthList.get(4).getWordLength());
		assertEquals(2, wordLengthList.get(4).getOccurrences());
		
		//Word length 7
		assertEquals(7, wordLengthList.get(5).getWordLength());
		assertEquals(1, wordLengthList.get(5).getOccurrences());
		
		//Word length 10
		assertEquals(10, wordLengthList.get(6).getWordLength());
		assertEquals(1, wordLengthList.get(6).getOccurrences());

		//Assert max occurrences of word lengths (should be lengths 4 & 5)
		assertEquals(2, maxWordLengthList.size());
		assertEquals(2, maxWordLengthList.get(0).getOccurrences());
		assertEquals(4, maxWordLengthList.get(0).getWordLength());
		assertEquals(5, maxWordLengthList.get(1).getWordLength());
		
	}
	
	/**
	 * Ensure extra whitespace will not count as words
	 */
	@Test
	public void wordCountWhitespaceTest() {
		assertEquals(5, WordCount.countWords(" Hello I'm  a  ginger     guy"));
	}
	
	
	/**
	 * Ensure newline will not effect word count
	 */
	@Test
	public void wordCountNewLineTest() {
		assertEquals(5, WordCount.countWords(" Hello I'm a   " + System.getProperty("line.separator") + "      ginger guy"));
	}
	
	/**
	 * Average test
	 */
	@Test
	public void averageWordTest() {
				assertEquals(3.6d, WordCount.averageWordLength(" Hello I'm a   " + System.getProperty("line.separator") + "      ginger guy"));
	}
	
	/**
	 * No words average test
	 */
	@Test
	public void averageWordZeroTest() {
		assertEquals(0.0d, WordCount.averageWordLength("            "));
	}
	
	/**
	 * Word Lengths test
	 */
	@Test
	public void wordLengthsTest() {
		List<NumberOfWordLengthEntry> wordLengthList = WordCount.numberOfWordsOfLength(" Hello I'm a   " + System.getProperty("line.separator") + "      ginger guy");
		
		//List size
		assertEquals(4, wordLengthList.size());
		
		//1st entry
		assertEquals(1, wordLengthList.get(0).getWordLength());
		assertEquals(1, wordLengthList.get(0).getOccurrences());
		
		//2nd entry
		assertEquals(3, wordLengthList.get(1).getWordLength());
		assertEquals(2, wordLengthList.get(1).getOccurrences());
		
		//3rd entry
		assertEquals(5, wordLengthList.get(2).getWordLength());
		assertEquals(1, wordLengthList.get(2).getOccurrences());
		
		//4th entry
		assertEquals(6, wordLengthList.get(3).getWordLength());
		assertEquals(1, wordLengthList.get(3).getOccurrences());

	}
	
	/**
	 * Max occurrence test
	 */
	@Test
	public void wordLengthsMaxTest() 
	{
		//Perform methods
		List<NumberOfWordLengthEntry> wordLengthList = WordCount.numberOfWordsOfLength(" Hello I'm a   " + System.getProperty("line.separator") + "      ginger guy");
		List<NumberOfWordLengthEntry> maxOccurringList = WordCount.wordLengthMaxOccurringSummary(wordLengthList);

		//Assert
		assertEquals(1, maxOccurringList.size());
		assertEquals(2, maxOccurringList.get(0).getOccurrences());
		assertEquals(3, maxOccurringList.get(0).getWordLength());

	}
	
}
