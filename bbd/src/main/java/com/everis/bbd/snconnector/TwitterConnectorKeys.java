package com.everis.bbd.snconnector;

/**
 * Enumeration with the values of the keys of the different fields of TwitterConnector-
 */
public enum TwitterConnectorKeys 
{	
	
	/**
	 * Key for accessing the query in the configuration.
	 */
	CONF_QUERY_KEY("query"),
	
	/**
	 * Key for accessing the tweets count per page in the configuration.
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
	 * Key for accessing the since tweet ID in the configuration.
	 */
	CONF_SINCEID_KEY("sinceID"),

	/**
	 * Key for accessing the maximum tweet ID in the configuration.
	 */
	CONF_MAXID_KEY("maxId");

	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator. 
	 * 
	 * @param id the identifier 
	 */
	private TwitterConnectorKeys(String id) 
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
