package com.everis.bbd.examples.utilities;

import com.everis.bbd.utilities.TextProcessDictionary;

/**
 * Simple use case for dictionary. Given a short text and the dictionaries, it processes the text.
 */
public class SimpleDictionaryUseCase 
{	
	
	/**
	 * Text to process.
	 */
	private static String _text = "Bayern won Barcelona last week, but Barça is going to win Munchen on the wednesday match. I like barsa team over the bavarian!";
	
	/**
	 * Dictionary.
	 */
	private static TextProcessDictionary _dictionary;
	
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		_dictionary = new TextProcessDictionary();
		if (!_dictionary.readDictionaries())
		{
			return;
		}
		
		System.out.println("Original text:");
		System.out.println(_text);
		
		_text = _dictionary.preProcess(_text);
		
		System.out.println("Resulting text:");
		System.out.println(_text.replaceAll("week", ""));
	}
}
