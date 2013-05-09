package com.everis.bbd.snconnector;

/**
 * Enumeration with the values of the keys of the different fields of SNConnector.
 */
public enum SNConnectorKeys 
{	
	/** EVENT JSON KEYS **/
	
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
	
	/** CONFIGURATION FILE KEYS **/
	
	/**
	 * Configuration key for application.
	 */
	CONF_APPLICATION_KEY("application"),
	
	/**
	 * Configuration key for specifying the connector type.
	 */
	CONF_CONNECTOR_TYPE_KEY("connectorType"),
	
	/**
	 * Configuration key for specifying the list of queries configuration files.
	 */
	CONF_CONFIG_KEY("config"),
	
	/**
	 * Key for accessing the query in the configuration.
	 */
	CONF_QUERY_KEY("query"),
	
	/**
	 * Key for accessing the events count per page in the configuration.
	 */
	CONF_COUNT_KEY("count"),

	/**
	 * Key for accessing the date since in the configuration.
	 */
	CONF_SINCE_KEY("since"),

	/**
	 * Key for accessing the data until in the configuration.
	 */
	CONF_UNTIL_KEY("until"),

	/**
	 * Key for accessing the since event ID in the configuration.
	 */
	CONF_SINCEID_KEY("sinceID"),

	/**
	 * Key for accessing the maximum event ID in the configuration.
	 */
	CONF_MAXID_KEY("maxID"),
	
	/**
	 * Key for accessing the events threshold in the configuration.
	 */
	CONF_EVENTS_THRESHOLD_KEY("eventsThreshold"),
	
	/**
	 * Key for accessing the sleep parameter in the configuration.
	 */
	CONF_SLEEP_KEY("sleep"),
	
	/** OAUTH KEYS **/
	
	/**
	 * Key for accessing the consumer key in the configuration.
	 */
	OAUTH_CONSUMER_KEY("consumerKey"),

	/**
	 * Key for accessing the consumer secret in the configuration.
	 */
	OAUTH_CONSUMER_SECRET("consumerSecret"),

	/**
	 * Key for accessing the access token in the configuration.
	 */
	OAUTH_ACCESS_TOKEN("accessToken"),

	/**
	 * Key for accessing the access token secret in the configuration.
	 */
	OAUTH_ACCESS_TOKEN_SECRET("accessTokenSecret");

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