package com.everis.bbd.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.everis.bbd.utilities.dictionary.Dictionary;
import com.everis.bbd.utilities.dictionary.DictionaryFactory;

/**
 * Wrapper for dictionary that process text lines.
 */
public class TextProcessor 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(TextProcessor.class.getName());
	
	/**
	 * Dictionaries.
	 */
	List<Dictionary> _dictionaries;
	
	/**
	 * Constructor.
	 */
	public TextProcessor()
	{
		_dictionaries = new ArrayList<Dictionary>();
	}
	
	/**
	 * Read all the dictionaries specified.
	 * 
	 * @param dictionaries map where the key is the dictionary name and value is the dictionary type.
	 * @param nameIsPath if the dictionaries names are the path to the dictionaries themselves.
	 * @return if all dictionaries have been read.
	 */
	public boolean readDictionaries(Map<String,Integer> dictionaries, boolean nameIsPath)
	{
		for (Entry<String, Integer> dictionary: dictionaries.entrySet())
		{
			Dictionary newDictionary = DictionaryFactory.getDictionary(dictionary.getKey(),dictionary.getValue());
			if (nameIsPath)
			{
				newDictionary.setPath(dictionary.getKey());
			}
			if (newDictionary.readDictionary())
			{
				_dictionaries.add(newDictionary);
			}
			else
			{
				log.warning("Dictionary "+dictionary.getKey()+" could not be read.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Read all the dictionaries specified.
	 * The names specified are not the paths.
	 * 
	 * @param dictionaries map where the key is the dictionary name and value is the dictionary type.
	 * @return if all dictionaries have been read.
	 */
	public boolean readDictionaries(Map<String,Integer> dictionaries)
	{
		return readDictionaries(dictionaries, false);
	}
	
	/**
	 * Process the line.
	 * 
	 * @param line to process.
	 * @param alphanum if true removes all characters non alphanumeric.
	 * @return processed text.
	 */
	public String preProcess(String line, boolean alphanum)
	{
		if (alphanum)
		{
			line = line.replaceAll("[^a-zA-Z0-9\\ ]", "");
		}
		for (Dictionary dictionary: _dictionaries)
		{
			line = dictionary.processText(line);
		}
		return line;
	}
}
