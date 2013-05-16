package com.everis.bbd.utilities.dictionary;

import java.util.logging.Logger;

/**
 * Word dictionary.
 */
public class WordDictionary extends Dictionary 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(WordDictionary.class.getName());
	
	/**
	 * Constructor.
	 */
	public WordDictionary() 
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param name name of the dictionary.
	 */
	public WordDictionary(String name)
	{
		super(name);
	}

	@Override
	public String processText(String text) 
	{
		text = text.toLowerCase();
		for (String word: _dictionary.keySet())
		{
			text = replaceWord(text, word, "");
		}
		return text;
	}
}
