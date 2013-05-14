package com.everis.bbd.snconnector;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Object that contains and embeds the information of a single post of a social network.
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
	 * 
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
		_information.put(key, value);
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
	public int getInt(String key)
	{
		if (!_information.containsKey(key))
		{
			return (Integer) null;
		}
		return ((Integer)_information.get(key));
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
	public long getLong(String key)
	{
		if (!_information.containsKey(key))
		{
			return (Long) null;
		}
		return ((Long)_information.get(key));
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
	public void setdouble(String key, double value)
	{
		_information.put(key, value);
	}
	
	/**
	 * @param key of the value
	 * @return a double with the value
	 */
	public double getDouble(String key)
	{
		if (!_information.containsKey(key))
		{
			return (Double) null;
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
		String value = "";
		if (this.hasValue(SNObjectKeys.POST_ID_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_ID_KEY.getId()));
		}
		else
		{
			return null;
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_TEXT_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_TEXT_KEY.getId()));
		}
		else
		{
			return null;
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_USERID_KEY.getId()))
		{
			value.concat(Integer.toString(this.getInt(SNObjectKeys.POST_USERID_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_USER_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_USER_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_LATITUDE_KEY.getId()))
		{
			value.concat(Double.toString(this.getDouble(SNObjectKeys.POST_LATITUDE_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_LONGITUDE_KEY.getId()))
		{
			value.concat(Double.toString(this.getDouble(SNObjectKeys.POST_LONGITUDE_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_DATE_KEY.getId()))
		{
			value.concat(this.getTimestamp(SNObjectKeys.POST_DATE_KEY.getId()).toString());
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_SOURCE_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_SOURCE_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_QUERY_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_QUERY_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		return null;	
	}
}
