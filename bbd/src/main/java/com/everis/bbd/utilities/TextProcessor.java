package com.everis.bbd.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.everis.bbd.utilities.dictionary.Dictionary;
import com.everis.bbd.utilities.dictionary.DictionaryFactory;
import com.everis.bbd.utilities.dictionary.DictionaryKeys;

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
				_dictionaries.add(newDictionary.getDictionaryType(), newDictionary);
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
	 * Reads a single dictionary and set into the indicated index.
	 * 
	 * @param dictionaryName dictionary name.
	 * @param dictionaryType dictionary type.
	 * @param index position where the dictionary will be placed.
	 * @param nameIsPath if the dictionary name is the path to the dictionary itself.
	 * @return if the dictionary has been read.
	 */
	public boolean readDictionary(String dictionaryName, int dictionaryType, int index, boolean nameIsPath)
	{
		Dictionary newDictionary = DictionaryFactory.getDictionary(dictionaryName,dictionaryType);
		if (nameIsPath)
		{
			newDictionary.setPath(dictionaryName);
		}
		if (newDictionary.readDictionary())
		{
			_dictionaries.add(index, newDictionary);
		}
		else
		{
			log.warning("Dictionary "+dictionaryName+" could not be read.");
			return false;
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
	 * @param text to process.
	 * @param alphanum if true removes all characters non alphanumeric.
	 * @return processed text.
	 */
	public String preProcess(String text, boolean alphanum)
	{
		text = text.toLowerCase();
		if (alphanum)
		{
			text = text.replaceAll("[^a-zA-Z0-9\\ ]", " ");
		}
		for (Dictionary dictionary: _dictionaries)
		{
			text = dictionary.processText(text);
		}
		return text;
	}
	
	/**
	 * Process the line.
	 * 
	 * @param text to process.
	 * @param alphanum before what dictionary the pre process should remove all characters non alphanumeric. (-1 if not)
	 * @param order list of indexes of the dictionaries in order of execution.
	 * @return processed text.
	 */
	public String preProcess(String text, int alphanum, int[] order)
	{
		text = text.toLowerCase();
		for (int i = 0; i < order.length; ++i)
		{
			if (alphanum == order[i])
			{
				text = text.replaceAll("[^a-zA-Z0-9\\ ]", " ");
			}
			text = _dictionaries.get(order[i]).processText(text);
		}
		return text;
	}
}
