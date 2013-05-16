package com.everis.bbd.utilities.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Word list dictionary.
 */
public class WordListDictionary extends Dictionary
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(WordListDictionary.class.getName());
	
	/**
	 * Separator token for key-value parameters in lists.
	 */
	private static final String KEYLIST_ASSIGN_TOKEN = "=";

	/**
	 * Word separator token for lists.
	 */
	private static final String LIST_SEPARATOR_TOKEN = ",";
	
	/**
	 * Constructor.
	 */
	public WordListDictionary() 
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param name name of the dictionary.
	 */
	public WordListDictionary(String name)
	{
		super(name);
	}
	
	@Override
	public boolean readDictionary()
	{
		if (_dictionaryPath == "")
		{
			if (!setPath())
			{
				return false;
			}
		}
		
		BufferedReader in;
		try 
		{
			//A File must be opened in order to be able to access Distributed Cache.
			in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(_dictionaryPath))));
			String line;
			while((line = in.readLine()) != null)
			{
				String[] var = line.split(String.valueOf(WordListDictionary.KEYLIST_ASSIGN_TOKEN));
				String[] valuesArray = var[1].split(String.valueOf(WordListDictionary.LIST_SEPARATOR_TOKEN));
				for (String v: valuesArray)
				{
					_dictionary.put(v, var[0]);
				}
			}
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			log.severe("Couldn't read dictionary in path: "+_dictionaryPath+".");
			return false;
		} 
		catch (IOException e) 
		{
			log.severe("Couldn't read dictionary in path: "+_dictionaryPath+".");
			return false;
		}
		return true;
	}

	@Override
	public String processText(String text) 
	{
		for (String word: text.split(" "))
		{
			String replaceWord = _dictionary.get(word);
			if (replaceWord != null)
			{
				text = replaceWord(text, word, replaceWord);
			}
		}
		return text;
	}

}
