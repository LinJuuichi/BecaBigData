package com.everis.bbd.snconnector;

/**
 * Enumeration with the values of the keys of the different fields of SNConnector.
 */
public enum SNObjectKeys 
{	
	/**
	 * ID key.
	 */
	POST_ID_KEY("id"),
	
	/**
	 * text key.
	 */
	POST_TEXT_KEY("text"),
	
	/**
	 * Timestamp/date key.
	 */
	POST_DATE_KEY("posted"),
	
	/**
	 * Username key.
	 */
	POST_USER_KEY("username"),
	
	/**
	 * UserID key.
	 */
	POST_USERID_KEY("userId"),
	
	/**
	 * Longitude key.
	 */
	POST_LONGITUDE_KEY("longitude"),
	
	/**
	 * Latitude key.
	 */
	POST_LATITUDE_KEY("latitude"),
	
	/**
	 * Source key.
	 */
	POST_SOURCE_KEY("source"),
	
	/**
	 * Language key.
	 */
	POST_LANGUAGE_KEY("language"),
	
	/**
	 * Search key.
	 */
	POST_QUERY_KEY("query");

	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private SNObjectKeys(String id) 
	{
		_id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() 
	{
		return _id;
	}		
}