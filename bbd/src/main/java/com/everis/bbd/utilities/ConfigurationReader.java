package com.everis.bbd.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


public class ConfigurationReader 
{
	private static Logger log = Logger.getLogger(ConfigurationReader.class.getName());
	
	public static char LIST_SEPARATOR_TOKEN = ' ';
	public static char KEYVALUE_ASSIGN_TOKEN = '=';
	public static char COMMENT_TOKEN = '#';
	
	private String _filePath;
	HashMap<String, List<String>> _configuration;
	
	public ConfigurationReader(String filePath)
	{
		_filePath = filePath;
		_configuration = new HashMap<String, List<String>>();
	}
	
	public boolean readConfigurationFile()
	{
		_configuration = new HashMap<String, List<String>>();
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(_filePath));
			
			String line;
			while((line = in.readLine()) != null)
			{
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
			log.warning("WARNING - ConfigurationReader.readConfigurationFile() - File does not exist");
			e.printStackTrace();
			return false;
		}
		catch (IOException e) 
		{
			log.warning("WARNING - ConfigurationReader.readConfigurationFile()");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int exists(String key)
	{
		if (_configuration.containsValue(key))
		{
			return _configuration.get(key).size();
		}
		return 0;
	}
	
	public List<String> getValues(String key)
	{
		return _configuration.get(key);
	}
	
	public String getValue(String key, String defaultValue)
	{
		int number = exists(key);
		if (number == 0) return defaultValue;
		else if (number == 1) return _configuration.get(key).get(0);
		else 
		{
			log.warning("WARNING - ConfigurationReader.getvalue() - The key "+key+" has more than one value.");
			return null;
		}
	}
	
	public boolean readConfigurationFile(String filePath)
	{
		_filePath = filePath;
		return readConfigurationFile();
	}
	
	public HashMap<String, List<String>> getConfiguration()
	{
		return _configuration;
	}
}
