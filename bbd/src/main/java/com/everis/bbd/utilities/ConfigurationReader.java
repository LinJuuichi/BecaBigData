package com.everis.bbd.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Map for reading and obtaining configuration files.
 * Every line is a parameter <key-value>, unless it starts with the COMMENT_TOKEN.
 * Parameter keys are separated of their values by KEYVALUE_ASSIGN_TOKEN.
 * Parameter values can contain a list of values, with every value separated by LIST_SEPARATOR_TOKEN.
 *
 */
public class ConfigurationReader 
{
	private static Logger log = Logger.getLogger(ConfigurationReader.class.getName());
	
	/**
	 * Separator for parameter values in lists.
	 */
	public static char LIST_SEPARATOR_TOKEN = ' ';
	
	/**
	 * Separator token for key-value parameters.
	 */
	public static char KEYVALUE_ASSIGN_TOKEN = '=';
	
	/**
	 * Starting token for comments.
	 */
	public static char COMMENT_TOKEN = '#';
	
	private String _filePath;
	private HashMap<String, List<String>> _configuration;
	
	/**
	 * Creates and initializes a ConfigurationReader.
	 * @param filePath configuration file path.
	 */
	public ConfigurationReader(String filePath)
	{
		_filePath = filePath;
		readConfigurationFile();
	}
	
	/**
	 * Reads the configuration file in the specified path.
	 * @return if it has read successfully the configuration file.
	 */
	public boolean readConfigurationFile()
	{
		_configuration = new HashMap<String, List<String>>();
		try 
		{
			log.info("Reading "+_filePath+".");
			BufferedReader in = new BufferedReader(new FileReader(_filePath));
			
			String line;
			while((line = in.readLine()) != null)
			{
				log.info("Line - "+line+".");
				if (line.charAt(0) != ConfigurationReader.COMMENT_TOKEN)
				{
					String[] var = line.split(String.valueOf(ConfigurationReader.KEYVALUE_ASSIGN_TOKEN));
					List<String> values = new ArrayList<String>();
					
					String[] valuesArray = var[1].split(String.valueOf(ConfigurationReader.LIST_SEPARATOR_TOKEN));
					for (String v: valuesArray)
					{
						values.add(v);
					}
					_configuration.put(var[0], values);
				}
			}
			
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			log.warning("readConfigurationFile() - File "+_filePath+" does not exist.");
			return false;
		}
		catch (IOException e) 
		{
			log.warning("readConfigurationFile() - Error reading "+_filePath+".");
			_configuration = new HashMap<String, List<String>>();
			return false;
		}
		return true;
	}
	
	/**
	 * Reads the configuration file in the specified path.
	 * @param filePath configuration file path.
	 * @return if it has read successfully the configuration file.
	 */
	public boolean readConfigurationFile(String filePath)
	{
		_filePath = filePath;
		return readConfigurationFile();
	}
	
	/**
	 * @return the configuration.
	 */
	public HashMap<String, List<String>> getConfiguration()
	{
		return _configuration;
	}
	
	/**
	 * Check if key exists in the configuration and how many values contains.
	 * @param key String to check.
	 * @return how many values contains the key (0 if not exists).
	 */
	public int exists(String key)
	{
		if (!_configuration.isEmpty() || _configuration.containsKey(key)) 
		{
			return _configuration.get(key).size();
		}
		return 0;
	}
	
	/**
	 * Get a list with the values in the configuration for key.
	 * @param key parameter key.
	 * @return a list with the values. (null if there is no configuration or existing key)
	 */
	public List<String> getValues(String key)
	{
		if (_configuration.isEmpty() || !_configuration.containsKey(key))
		{
			log.warning("No configuration or no key with value: "+key+".");
			return null;
		}
		return _configuration.get(key);
	}
	
	/**
	 * Get the value (if key exists) in the configuration for key.
	 * @param key parameter key.
	 * @return a single value (null if there is no configuration or existing key).
	 */
	public String getValue(String key)
	{
		int number = exists(key);
		if (number == 1) 
		{
			return _configuration.get(key).get(0);
		}
		else if (number == 0) 
		{
			log.warning("Key "+key+" is empty or does not exist.");
			return null;
		}
		else 
		{
			log.warning("Key "+key+" has more than one value.");
			return _configuration.get(key).get(0);
		}
	}
	
	/**
	 * Get the value in the configuration for key. If key does not exist returns the default value.
	 * @param key parameter key.
	 * @param defaultValue in case the key does not exist.
	 * @return a single value.
	 */
	public String getValue(String key, String defaultValue)
	{
		int number = exists(key);
		if (number == 0) 
		{
			return defaultValue;
		}
		else if (number == 1) 
		{
			return _configuration.get(key).get(0);
		}
		else 
		{
			log.warning("Key "+key+" has more than one value.");
			return _configuration.get(key).get(0);
		}
	}
}
