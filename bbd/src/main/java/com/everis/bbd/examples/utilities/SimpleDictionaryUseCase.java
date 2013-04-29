package com.everis.bbd.examples.utilities;

import java.util.Set;
import com.everis.bbd.utilities.Dictionary;

/**
 * Simple use case for dictionary. Given a short text and the dictionaries, it processes the text.
 */
public class SimpleDictionaryUseCase 
{
	/**
	 * Path to character dictionary.
	 */
	private static String CHAR_PATH = "../config/examples/SimpleDictionaryUseCase/char.dictionary";
	
	/**
	 * Path to word dictionary.
	 */
	private static String WORD_PATH = "../config/examples/SimpleDictionaryUseCase/word.dictionary";
	
	/**
	 * Path to list dictionary.
	 */
	private static String LIST_PATH = "../config/examples/SimpleDictionaryUseCase/list.dictionary";
	
	/**
	 * Text to process.
	 */
	private static String _text = "Bayern won Barcelona last week, but Barça is going to win Munchen on the wednesday match. I like barsa team over the bavarian!";
	
	/**
	 * Dictionary.
	 */
	private static Dictionary _dictionary;
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		_dictionary = new Dictionary();
		
		String charPath = SimpleDictionaryUseCase.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		charPath = charPath + CHAR_PATH;
		
		String wordPath = SimpleDictionaryUseCase.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		wordPath = wordPath + WORD_PATH;
		
		String listPath = SimpleDictionaryUseCase.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		listPath = listPath + LIST_PATH;
	
		if (!_dictionary.readDictionaries(charPath, wordPath, listPath))
		{
			return;
		}
		
		System.out.println("Original text:");
		System.out.println(_text);
		
		Set<String> dict = _dictionary.getCharDictionary();
		for (Object o: dict.toArray())
		{
			String s = (String) o;
			_text = _text.replaceAll("\\"+s, " ");
		}
		//_text = _text.replaceAll("\\"+".", " ");
		dict = _dictionary.getWordDictionary();
		for (Object o: dict.toArray())
		{
			String s = (String) o;
			_text = _text.replaceAll(" "+s+" ", " ");
		}
		
		for (String s: _text.split(" "))
		{
			String replaceWord = _dictionary.replaceWord(s);
			if (replaceWord != null)
			{
				_text = _text.replace(" "+s, " "+replaceWord);
			}
		}
		
		System.out.println("Resulting text:");
		System.out.println(_text.replaceAll("week", ""));
	}
}
