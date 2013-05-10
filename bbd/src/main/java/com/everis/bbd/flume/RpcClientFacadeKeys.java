package com.everis.bbd.flume;

/**
 * Enumeration with the values of the keys of the different fields of RpcClientFacade.
 */
public enum RpcClientFacadeKeys 
{
	/** RPC CLIENT KEYS **/ 

	/**
	 * Key for accessing the events threshold in the configuration.
	 */
	CONF_CLIENT_TYPE_KEY("clientType"),
	
	/**
	 * Key for accessing the events threshold in the configuration.
	 */
	CONF_HOSTNAME_KEY("hostname"),
	
	/**
	 * Key for accessing the events threshold in the configuration.
	 */
	CONF_OUTPUT_DIRECTORY_KEY("outputDirectory"),
	
	/**
	 * Key for accessing the events threshold in the configuration.
	 */
	CONF_PORT_KEY("port");
	
	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private RpcClientFacadeKeys(String id) 
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
