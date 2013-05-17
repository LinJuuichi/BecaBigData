package com.everis.bbd.utilities.dictionary;

import java.util.logging.Logger;

/**
 * Factory for dictionaries.
 */
public class DictionaryFactory 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(DictionaryFactory.class.getName());

	/**
	 * Identifier for CharDictionary.
	 */
	public static final int CHAR_DICTIONARY = 1;
	
	/**
	 * Identifier for WordDictionary.
	 */
	public static final int WORD_DICTIONARY = 2;
	
	/**
	 * Identifier for WordListDictionary.
	 */
	public static final int WORD_LIST_DICTIONARY = 3;
	
	/**
	 * Identifier for BlackListDictionary.
	 */
	public static final int BLACK_LIST_DICTIONARY = 4;
	

	/**
	 * DictionaryFactory can't be instantiated.
	 */
	private DictionaryFactory() {}
	
	/**
	 * Returns a dictionary.
	 * 
	 * @param type dictionary.
	 * @param dictionaryName dictionary name.
	 * @return dictionary.
	 */
	public static Dictionary getDictionary(String dictionaryName, int type)
	{
		Dictionary dictionary = null;
		switch (type) 
		{
		case CHAR_DICTIONARY:
			dictionary = new CharDictionary(dictionaryName);
			break;
		case WORD_DICTIONARY:
			dictionary = new WordDictionary(dictionaryName);
			break;
		case WORD_LIST_DICTIONARY:
			dictionary = new WordListDictionary(dictionaryName);
			break;
		case BLACK_LIST_DICTIONARY:
			dictionary = new BlackListDictionary(dictionaryName);
			break;
		default:
			log.warning("Dictionary type does not exist");
			break;
		}
		return dictionary;
	}
}
