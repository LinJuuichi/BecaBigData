package com.everis.bbd.utilities.dictionary;

/**
 * Keys for dictionaries.
 */
public enum DictionaryKeys 
{
	/** DICTIONARY KEYS **/ 

	/**
	 * Identifier for CharDictionary.
	 */
	CHAR_DICTIONARY(1),
	
	/**
	 * Identifier for WordDictionary.
	 */
	WORD_DICTIONARY(2),
	
	/**
	 * Identifier for WordListDictionary.
	 */
	WORD_LIST_DICTIONARY(3),
	
	/**
	 * Identifier for BlackListDictionary.
	 */
	BLACK_LIST_DICTIONARY(4),
	
	/**
	 * Identifier for NoURLDictionary.
	 */
	NO_URL_DICTIONARY(5);
	
	/**
	 * Key value.
	 */
	private int _id;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private DictionaryKeys(int id) 
	{
		_id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() 
	{
		return _id;
	}		
}
