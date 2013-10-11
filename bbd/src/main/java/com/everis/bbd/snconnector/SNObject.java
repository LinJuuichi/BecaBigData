package com.everis.bbd.snconnector;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Object that contains and embeds the information of a single object of a social network.
 * Like comments, profiles, etc.
 */
public class SNObject 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(SNObject.class.getName());
	
	/**
	 * Separator for every field.
	 */
	protected static String FIELD_SEPARATOR = "\t";
	
	/**
	 * Post information.
	 */
	Map<String, Object> _information;
	
	/**
	 * Contructor.
	 */
	public SNObject()
	{
		_information = new HashMap<String,Object>();
	}
	
	/**
	 * @param key of the value.
	 * @param value String value.
	 */
	public void setString(String key, String value)
	{
		_information.put(key, value.replace("\t","").replace("\n", "").replace("\r", ""));
	}
	
	/**
	 * @param key of the value
	 * @return a String with the value
	 */
	public String getString(String key)
	{
		if (!_information.containsKey(key))
		{
			return null;
		}
		return (String) _information.get(key);
	}
	
	/**
	 * @param key of the value.
	 * @param value int value.
	 */
	public void setInt(String key, int value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return an int with the value
	 */
	public Integer getInt(String key)
	{
		if (!_information.containsKey(key))
		{
			return -1;
		}
		return (Integer)_information.get(key);
	}
	
	/**
	 * @param key of the value.
	 * @param value long value.
	 */
	public void setLong(String key, long value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return a long with the value
	 */
	public Long getLong(String key)
	{
		if (!_information.containsKey(key))
		{
			return -1L;
		}
		return (Long)_information.get(key);
	}
	
	/**
	 * @param key of the value.
	 * @param value Timestamp value.
	 */
	public void setTimestamp(String key, Timestamp value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return a timestamp with the value
	 */
	public Timestamp getTimestamp(String key)
	{
		if (!_information.containsKey(key))
		{
			return null;
		}
		return (Timestamp) _information.get(key);
	}
	
	/**
	 * @param key of the value.
	 * @param value double value.
	 */
	public void setDouble(String key, double value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return a SNObject with the value
	 */
	public SNObject getSNObject(String key)
	{
		if (!_information.containsKey(key))
		{
			return (SNObject) null;
		}
		return ((SNObject)_information.get(key));
	}
	
	/**
	 * @param key of the value.
	 * @param value SNObject value.
	 */
	public void setSNObject(String key, SNObject value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return a double with the value
	 */
	public Double getDouble(String key)
	{
		if (!_information.containsKey(key))
		{
			return -1.;
		}
		return ((Double)_information.get(key));
	}
	
	/**
	 * @param key to check.
	 * @return if the key has a value assigned.
	 */
	public boolean hasValue(String key)
	{
		return _information.containsKey(key);
	}
	
	/**
	 * Returns a String with all the information formated.
	 */
	public String toString()
	{
		//log.info("To string called.");
		return null;
	}
}
