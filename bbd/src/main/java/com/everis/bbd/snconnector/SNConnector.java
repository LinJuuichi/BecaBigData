package com.everis.bbd.snconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONObject;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Abstract class for social networks connectors.
 */
public abstract class SNConnector 
{	
	/**
	 * Logger.
	 */
	protected static Logger log;
	
	/**
	 * Path to the configuration file.
	 */
	protected String _propertiesFilePath;

	/**
	 * Connector configuration.
	 */
	protected ConfigurationReader _configuration;
	
	/**
	 * List containing the queries results.
	 */
	protected List<JSONObject> _results;
	
	/**
	 * Word/s to search.
	 */
	protected String _query;
	
	/**
	 * Creator initializing attributes and configuration.
	 * 
	 * @param propertiesFilePath path to the configuration path.
	 */
	public SNConnector(String propertiesFilePath)
	{
		_propertiesFilePath = propertiesFilePath;
	}
	
	/**
	 * Initializes the configuration and the results.
	 * 
	 * @param propertiesFilePath path to the configuration path.
	 * @return initialization success
	 */
	public boolean configure(String propertiesFilePath)
	{
		_results = new ArrayList<JSONObject>();
		_configuration = new ConfigurationReader(propertiesFilePath);
		if (!_configuration.readConfigurationFile())
		{
			log.severe("Could not read "+_propertiesFilePath+". Connector don't configured.");
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the query.
	 * 
	 * @param query word/s to search.
	 */
	public void setQuery(String query)
	{
		_query = query;
	}
	
	/**
	 * Return the results.
	 * 
	 * @return a list with the results query.
	 */
	public List<JSONObject> getResults()
	{
		return _results;
	}
	
	/**
	 * Connects to the social network.
	 * 
	 * @return connection success.
	 */
	public abstract boolean connect();
	
	/**
	 * Closes the connection to the social network.
	 */
	public abstract void close();
	
	/**
	 * Executes the query and saves the results.
	 * 
	 * @return number of results (0 if non and < 0 if error)
	 */
	public abstract int query();
	
	/**
	 * Executes the query and saves the results.
	 * 
	 * @param query word/s to search.
	 * @return number of results (0 if non and < 0 if error)
	 */
	public int query(String query)
	{
		_query = query;
		return query();
	}
	
	/**
	 * Executes the query and saves the results.
	 * 
	 * @return number of results (0 if non and < 0 if error)
	 */
	public abstract int nextQuery();
	
	
	/**
	 * @return if last search has more results to query.
	 */
	public abstract boolean hasNextQuery();
}
