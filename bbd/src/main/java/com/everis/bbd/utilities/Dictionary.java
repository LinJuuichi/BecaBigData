package com.everis.bbd.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Contains three dictionaries:
 * Characters dictionary: lists characters.
 * Words dictionary: lists strings.
 * List dictionary: is set from a file containin lists like <key>=<word1,word2...wordN>. For each wordN, returns key. 
 */
public class Dictionary 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(Dictionary.class.getName());
	
	/**
	 * Default path for character dictionary.
	 */
	public static String CHAR_DICTIONARY_PATH = "";
	
	/**
	 * Default path for word dictionary.
	 */
	public static String WORD_DICTIONARY_PATH = "";
	
	/**
	 * Default path for list dictionary.
	 */
	public static String LIST_DICTIONARY_PATH = "";
	
	/**
	 * Separator token for key-value parameters in lists.
	 */
	private static final String KEYLIST_ASSIGN_TOKEN = "=";

	/**
	 * Word separator token for lists.
	 */
	private static final String LIST_SEPARATOR_TOKEN = ",";
	
	/**
	 * Character dictionary.
	 */
	private Set<String> _charDictionary;
	
	/**
	 * Word dictionary.
	 */
	private Set<String> _wordDictionary;
	
	/**
	 * List dictionary.
	 */
	private Map<String,String> _listDictionary;
	
	/**
	 * Constructor.
	 */
	public Dictionary()
	{
		init();
	}
	
	/**
	 * Initializes the dictionaries.
	 */
	public void init()
	{
		_charDictionary = new HashSet<String>();
		_wordDictionary = new HashSet<String>();
		_listDictionary = new HashMap<String,String>();
	}
	
	/**
	 * Reads the three dictionaries.
	 * 
	 * @return if succeeded.
	 */
	public boolean readDictionaries()
	{
		return readDictionary(_charDictionary,CHAR_DICTIONARY_PATH) 
				&& readDictionary(_wordDictionary,WORD_DICTIONARY_PATH)
				&& readListDictionary(_listDictionary,LIST_DICTIONARY_PATH);
	}
	
	/**
	 * Reads the three dictionaries.
	 * 
	 * @param charPath path for character dictionary.
	 * @param wordPath path for word dictionary.
	 * @param listPath path for list dictionary.
	 * @return if succeeded.
	 */
	public boolean readDictionaries(String charPath, String wordPath, String listPath)
	{
		return readDictionary(_charDictionary,charPath) 
				&& readDictionary(_wordDictionary,wordPath)
				&& readListDictionary(_listDictionary,listPath);
	}
	
	/**
	 * Reads a list dictionary.
	 * 
	 * @param dictionary to store.
	 * @param dictionaryPath to read.
	 * @return if succeeded.
	 */
	public boolean readDictionary(Set<String> dictionary, String dictionaryPath)
	{
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(dictionaryPath));
			String line;
			while((line = in.readLine()) != null)
			{
				dictionary.add(line);
			}
		} 
		catch (FileNotFoundException e) 
		{
			log.severe("Couldn't read dictionary in path: "+dictionaryPath+".");
			return false;
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't read dictionary in path: "+dictionaryPath+".");
			return false;
		}
		return true;
	}
	
	/**
	 * Reads a list dictionary.
	 * 
	 * @param dictionary to store.
	 * @param dictionaryPath to read.
	 * @return if succeeded.
	 */
	public boolean readListDictionary(Map<String, String> dictionary, String dictionaryPath)
	{
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(dictionaryPath));
			String line;
			while((line = in.readLine()) != null)
			{
				String[] var = line.split(String.valueOf(Dictionary.KEYLIST_ASSIGN_TOKEN));
				String[] valuesArray = var[1].split(String.valueOf(Dictionary.LIST_SEPARATOR_TOKEN));
				for (String v: valuesArray)
				{
					dictionary.put(v, var[0]);
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			log.severe("Couldn't read dictionary in path: "+dictionaryPath+".");
			return false;
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't read dictionary in path: "+dictionaryPath+".");
			return false;
		}
		return true;
	}
	
	/**
	 * @param c character to search.
	 * @return if c exists in character dictionary.
	 */
	public boolean existsCharacter(char c)
	{
		return _charDictionary.contains(c);
	}
	
	/**
	 * @param c character to search.
	 * @return if c exists in character dictionary.
	 */
	public boolean existsCharacter(String c)
	{
		return _charDictionary.contains(c);
	}
	
	/**
	 * @param w word to search.
	 * @return if w exists in word dictionary.
	 */
	public boolean existsWord(String w)
	{
		return _wordDictionary.contains(w);
	}
	
	/**
	 * @param w word to search.
	 * @return word that replaces w (null if none).
	 */
	public String replaceWord(String w)
	{
		return _listDictionary.get(w);
	}
	
	/**
	 * @return character dictionary.
	 */
	public Set<String> getCharDictionary()
	{
		return _charDictionary;
	}
	
	/**
	 * @return word dictionary.
	 */
	public Set<String> getWordDictionary()
	{
		return _wordDictionary;
	}
}
