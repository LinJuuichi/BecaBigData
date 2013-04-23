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
	protected String _propertiesFile;

	/**
	 * Connector configuration.
	 */
	protected ConfigurationReader _configuration;
	
	/**
	 * List containing the queries results.
	 */
	protected List<JSONObject> _results;
	
	/**
	 * Creator initializing attributes and configuration.
	 * 
	 * @param propertiesFile path to the configuration path.
	 */
	public SNConnector(String propertiesFile)
	{
		_propertiesFile = propertiesFile;
	}
	
	/**
	 * Initializes the configuration and the results.
	 * 
	 * @param propertiesFile path to the configuration path.
	 * @return initialization success
	 */
	public boolean configure(String propertiesFile)
	{
		_propertiesFile = propertiesFile;
		_results = new ArrayList<JSONObject>();
		_configuration = new ConfigurationReader(propertiesFile);
		
		if (!_configuration.readConfigurationFile())
		{
			log.severe("Could not read file: "+_propertiesFile+". Connector don't configured.");
			return false;
		}
		
		return true;
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
	 * Clears the results obtained.
	 */
	public void clearResults()
	{
		_results.clear();
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
	 * @param appendResults append the results of the new query to existing ones (if exist)
	 * @return number of results (0 if non and < 0 if error)
	 */
	public abstract int query(boolean appendResults);
	
	/**
	 * Executes the query and saves the results.
	 * 
	 * @param query word/s to search.
	 * @param appendResults append the results of the new query to existing ones (if exist)
	 * @return number of results (0 if non and < 0 if error)
	 */
	public abstract int query(String query, boolean appendResults);
	
	/**
	 * Executes the query and saves the results.
	 * Append the results to the existing ones.
	 * 
	 * @return number of results (0 if non and < 0 if error)
	 */
	public abstract int nextQuery();
	
	
	/**
	 * @return if last search has more results to query.
	 */
	public abstract boolean hasNextQuery();
}
