package com.everis.bbd.snconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.everis.bbd.utilities.ConfigurationReader;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Connector for Twitter.
 */
public class TwitterConnector extends SNConnector 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(TwitterConnector.class.getName());
	
	public static final String DEFAULT_CONFIGURATION_PATH = "";
	
	private static final String CONSUMER_KEY = "CONSUMER_KEY";
	private static final String CONSUMER_SECRET = "CONSUMER_SECRET";
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String ACCESS_TOKEN_SECRET = "ACCESS_TOKEN_SECRET";

	private ConfigurationReader _config;
	
	private Twitter _twitter;
	private Query _query;
	private QueryResult _queryResults;
	
	public TwitterConnector()
	{
		super(DEFAULT_CONFIGURATION_PATH);
	}
	
	/**
	 * Returns a TwitterConnector configured with the properties in
	 * propertiesFilePath.
	 * 
	 * @param propertiesFilePath file path with the properties (tokens).
	 */
	public TwitterConnector(String propertiesFilePath)
	{
		super(propertiesFilePath);
	}
	
	/**
	 * Connects Twitter with the the properties in propertiesFilePath.
	 * 
	 * @param propertiesFilePath file path with the properties (tokens).
	 */
	public void connect(String propertiesFilePath)
	{
		_propertiesFilePath = propertiesFilePath;
	}

	/**
	 * Connects to Twitter if a properties file was specified.
	 * 
	 * @return if the connection was successful
	 */
	public boolean connect()
	{
		if (_propertiesFilePath.isEmpty())
		{
			log.warning("Properties file no specified.");
			return false;
		}
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(_config.getValue(TwitterConnector.CONSUMER_KEY,""))
		  .setOAuthConsumerSecret(_config.getValue(TwitterConnector.CONSUMER_SECRET,""))
		  .setOAuthAccessToken(_config.getValue(TwitterConnector.ACCESS_TOKEN,""))
		  .setOAuthAccessTokenSecret(_config.getValue(TwitterConnector.ACCESS_TOKEN_SECRET,""));

		TwitterFactory tf = new TwitterFactory(cb.build());
		_twitter = tf.getInstance();
		return true;
	}

	/**
	 * @return the query specified.
	 */
	public Query getQuery()
	{
		return _query;
	}

	/**
	 * Sets a new Query.
	 * 
	 * @param query String containing the query to execute.
	 */
	public void setQuery(String query)
	{
		_query = new Query(query);
	}

	/**
	 * Executes the setted query. Saves the results. 
	 * 
	 * @return the number of downloaded tweets.
	 */
	public int doQuery()
	{
		try 
		{
			_queryResults = _twitter.search(_query);
			return _queryResults.getCount();
		} 
		catch (TwitterException e) 
		{
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Sets the query and executes it. Saves the results. 
	 * 
	 * @param  query String containing the query to execute.
	 * @return the number of downloaded tweets.
	 */
	public int doQuery(String query)
	{
		this.setQuery(query);
		return this.doQuery();
	}

	/**
	 * Asks if there is more results for the executed query.
	 * 
	 * @return if there is another query.
	 */
	public boolean hasNextQuery()
	{
		if (_queryResults != null)
		{
			return _queryResults.hasNext();
		}
		return false;
	}

	/**
	 * Executes the next query. Saves the results.
	 *  
	 * @return the number of downloaded tweets.
	 */
	public int doNextQuery()
	{
		if (this.hasNextQuery())
		{
			_query = _queryResults.nextQuery();
			return this.doQuery();
		}
		return -1;
	}

	/*public List<JSONObject> getResults()
	{
		ArrayList<JSONObject> results = null;
		if (_queryResults != null)
		{
			return _queryResults.getTweets();
		}
		return results;
	}*/

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int query() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int nextQuery() {
		// TODO Auto-generated method stub
		return 0;
	}
}
