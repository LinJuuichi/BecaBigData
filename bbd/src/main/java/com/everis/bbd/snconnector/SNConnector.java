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
	 * Path to default configuration path.
	 */
	public static final String DEFAULT_CONFIGURATION_PATH = "";
	
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
	 * Terms to query.
	 */
	protected String _search;
	
	/**
	 * Constructor initializing attributes and configuration.
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
	 * Initializes the configuration and the results.
	 * 
	 * @param configuration configuration.
	 * @return initialization success
	 */
	public boolean configure(ConfigurationReader configuration)
	{
		_results = new ArrayList<JSONObject>();
		_configuration = configuration;
		_propertiesFile = _configuration.getConfigFileName();
		return true;
	}
	
	/**
	 * Return the results.
	 * 
	 * @return a list with the results query.
	 */
	public synchronized List<JSONObject> getResults()
	{
		return _results;	
	}
	
	/**
	 * @return the number of the results stored.
	 */
	public synchronized int getResultSize()
	{
		return _results.size();
	}
	
	/**
	 * Clears the results obtained.
	 */
	public synchronized void clearResults()
	{
		_results.clear();
	}
	
	/**
	 * Return the results and then clear.
	 * 
	 * @return the results in JSON format.
	 */
	public synchronized List<JSONObject> getAndClearResults()
	{
		List<JSONObject> results = new ArrayList<JSONObject>();
		results.addAll(_results);
		_results.clear();
		return results;
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
}
