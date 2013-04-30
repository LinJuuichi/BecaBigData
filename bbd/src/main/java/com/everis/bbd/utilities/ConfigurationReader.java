package com.everis.bbd.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Map for reading and obtaining configuration files.
 * Every line is a parameter <key-value>, unless it starts with {@link ConfigurationReader#COMMENT_TOKEN}.
 * Parameter keys are separated of their values by {@link ConfigurationReader#KEYVALUE_ASSIGN_TOKEN}.
 * Parameter values can contain a list of values, with every value separated by {@link ConfigurationReader#LIST_SEPARATOR_TOKEN}.
 */
public class ConfigurationReader 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(ConfigurationReader.class.getName());
	
	/**
	 * Path to the config directory from target directory.
	 */
	public static String CONFIG_PATH = "/../config/";
	
	/**
	 * Separator for parameter values in lists.
	 */
	public static char LIST_SEPARATOR_TOKEN = ' ';
	
	/**
	 * Separator token for key-value parameters.
	 */
	public static char KEYVALUE_ASSIGN_TOKEN = '=';
	
	/**
	 * Starting text token for value parameters.
	 */
	public static char KEYVALUE_TEXT_TOKEN = '"';
	
	/**
	 * Starting token for comments.
	 */
	public static char COMMENT_TOKEN = '#';
	
	/**
	 * Path to the configuration directory to read.
	 */
	private String _configPath;
	
	/**
	 * Path to the configuration file to read.
	 */
	private String _filePath;
	
	/**
	 * Configuration parameters.
	 */
	private Map<String, List<String>> _configuration;
	
	/**
	 * Creates a ConfigurationReader.
	 * 
	 * @param fileName configuration file path.
	 */
	public ConfigurationReader(String fileName)
	{
		_configuration = new HashMap<String, List<String>>();
		setFile(fileName);
	}
	
	/**
	 * Sets the complete config path for the file fileName.
	 * 
	 * @param fileName name of the file to read.
	 * @return path completion success.
	 */
	public boolean setFile(String fileName)
	{
		_configPath = ConfigurationReader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		_configPath = _configPath.substring(0,_configPath.lastIndexOf("/")) + CONFIG_PATH + fileName;
		try 
		{
			_filePath = URLDecoder.decode(_configPath, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			log.severe("Path error:"+_configPath+".");
			return false;
		}
		return true;
	}
	
	/**
	 * Reads the configuration file in the specified path.
	 * 
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
				if (!line.isEmpty() && line.charAt(0) != ConfigurationReader.COMMENT_TOKEN)
				{
					String[] var = line.split(String.valueOf(ConfigurationReader.KEYVALUE_ASSIGN_TOKEN));
					List<String> values = new ArrayList<String>();
					
					if (var[1].charAt(0) == ConfigurationReader.KEYVALUE_TEXT_TOKEN)
					{
						values.add(var[1].subSequence(1, var[1].length()-1).toString());
					}
					else
					{
						String[] valuesArray = var[1].split(String.valueOf(ConfigurationReader.LIST_SEPARATOR_TOKEN));
						for (String v: valuesArray)
						{
							values.add(v);
						}
					}
					log.info("Key: "+var[0]);
					log.info("Values: "+values.toString());
					_configuration.put(var[0], values);
				}
			}
			
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			log.warning("File "+_configPath+" does not exist.");
			return false;
		}
		catch (IOException e) 
		{
			log.warning("Error reading "+_configPath+".");
			_configuration = new HashMap<String, List<String>>();
			return false;
		}
		return true;
	}
	
	/**
	 * Reads the configuration file in the specified path.
	 * 
	 * @param fileName configuration file path.
	 * @return if it has read successfully the configuration file.
	 */
	public boolean readConfigurationFile(String fileName)
	{
		setFile(fileName);
		_configuration = new HashMap<String, List<String>>();
		return readConfigurationFile();
	}
	
	/**
	 * Clears the configuration.
	 */
	public void clearConfiguration()
	{
		_configuration.clear();
	}
	
	/**
	 * @return the configuration.
	 */
	public Map<String, List<String>> getConfiguration()
	{
		return _configuration;
	}
	
	/**
	 * Check if key exists in the configuration and how many values contains.
	 * 
	 * @param key String to check.
	 * @return how many values contains the key (0 if not exists).
	 */
	public int exists(String key)
	{
		if (_configuration.containsKey(key)) 
		{
			return _configuration.get(key).size();
		}
		return 0;
	}
	
	/**
	 * Get a list with the values in the configuration for key.
	 * 
	 * @param key parameter key.
	 * @return a list with the values. (empty list if there is no configuration or existing key)
	 */
	public List<String> getValues(String key)
	{
		if (!_configuration.containsKey(key))
		{
			log.info("Key "+key+" has no values.");
			return new ArrayList<String>();
		}
		return _configuration.get(key);
	}
	
	/**
	 * Get the value in the configuration for key. If key does not exist returns the default value.
	 * 
	 * @param key parameter key.
	 * @param defaultValue in case the key does not exist.
	 * @return a single value.
	 */ 
	public String getValue(String key, String defaultValue)
	{
		int number = exists(key);
		if (number == 0) 
		{
			log.info("Key "+key+" has no values.");
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
	
	/**
	 * Get the value in the configuration for key as an integer. 
	 * If key does not exist or is not a parsable integer returns the default value.
	 * 
	 * @param key parameter key.
	 * @param defaultValue in case the key does not exist.
	 * @return a single value.
	 */
	public int getIntValue(String key, int defaultValue)
	{
		String value = getValue(key,String.valueOf(defaultValue));
		int result;
		try
		{
			result = Integer.parseInt(value);
		} 
		catch (NumberFormatException  e) 
		{
			log.info("Key "+key+" has not contain a parsable integer. Value = "+value+".");
			return defaultValue;
		}
		return result;
	}
}
