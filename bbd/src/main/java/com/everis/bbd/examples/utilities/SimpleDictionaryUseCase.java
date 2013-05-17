package com.everis.bbd.examples.utilities;

import java.util.HashMap;
import java.util.Map;

import com.everis.bbd.utilities.TextProcessor;
import com.everis.bbd.utilities.dictionary.DictionaryFactory;

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
	private static TextProcessor _dictionary;
	
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		_dictionary = new TextProcessor();
		Map<String, Integer> dictionaries = new HashMap<String, Integer>();
		dictionaries.put("word", DictionaryFactory.WORD_DICTIONARY);
		dictionaries.put("list", DictionaryFactory.WORD_LIST_DICTIONARY);
		if (!_dictionary.readDictionaries(dictionaries))
		{
			return;
		}
		
		System.out.println("Original text:");
		System.out.println(_text);
		
		_text = _dictionary.preProcess(_text,true);
		
		System.out.println("Resulting text:");
		System.out.println(_text.replaceAll("week", ""));
	}
}
