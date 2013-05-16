package com.everis.bbd.utilities.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Dictionary.
 */
public abstract class Dictionary 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(Dictionary.class.getName());
	
	/**
	 * Path to the config directory from target directory.
	 */
	public static String CONFIG_PATH = "/../config/dictionaries/";
	
	/**
	 * File extension for dictionaries.
	 */
	public static String DICTIONARY_EXTENSION = ".dictionary";
	
	/**
	 * Dictionary name.
	 */
	protected String _dictionaryName;
	
	/**
	 * Path to the dictionary.
	 */
	protected String _dictionaryPath;
	
	/**
	 * 
	 */
	protected Map<String, String> _dictionary;
	
	/**
	 * Constructor.
	 */
	public Dictionary() 
	{
		_dictionaryName = "";
		_dictionaryPath = "";
		_dictionary = new HashMap<String,String>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name dictionary name.
	 */
	public Dictionary(String name) 
	{
		_dictionaryName = name;
		_dictionaryPath = "";
		_dictionary = new HashMap<String,String>();
	}
	
	/**
	 * Sets the path of the dictionary.
	 * 
	 * @return if path is correct.
	 */
	public boolean setPath()
	{
		String basePath = ConfigurationReader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		basePath = basePath.substring(0,basePath.lastIndexOf("/"));
		
		_dictionaryPath = basePath + CONFIG_PATH + _dictionaryName + DICTIONARY_EXTENSION;

		try 
		{
			_dictionaryPath = URLDecoder.decode(_dictionaryPath, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			log.severe("Path error:"+_dictionaryPath+".");
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the path of the dictionary to the specified dictionary.
	 * 
	 * @param name of the dictionary file.
	 * @return if path is correct.
	 */
	public boolean setPath(String name)
	{
		_dictionaryName = name;
		return setPath();
	}
	
	/**
	 * Reads the dictionary from file.
	 * 
	 * @return if dictionary read correctly.
	 */
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
				_dictionary.put(line, "");
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
	
	/**
	 * @return the dictionary.
	 */
	public Map<String, String> getDictionary()
	{
		return _dictionary;
	}
	
	/**
	 * Processes the text.
	 * 
	 * @param text to process.
	 * @return the resulting text after processing it.
	 */
	public abstract String processText(String text);
}
