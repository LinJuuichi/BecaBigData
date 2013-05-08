package com.everis.bbd.snconnector;

/**
 * Enumeration with the values of the keys of the different fields of TwitterConnector-
 */
public enum SNConnectorKeys 
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
	 * Search key.
	 */
	POST_QUERY_KEY("query"),

	/**
	 * Key for accessing the consumer key in the configuration.
	 */
	CONSUMER_KEY("consumerKey"),

	/**
	 * Key for accessing the consumer secret in the configuration.
	 */
	CONSUMER_SECRET("consumerSecret"),

	/**
	 * Key for accessing the access token in the configuration.
	 */
	ACCESS_TOKEN("accessToken"),

	/**
	 * Key for accessing the access token secret in the configuration.
	 */
	ACCESS_TOKEN_SECRET("accessTokenSecret");

	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private SNConnectorKeys(String id) 
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