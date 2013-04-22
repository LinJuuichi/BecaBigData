package com.everis.bbd.snconnector;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONObject;
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

	/**
	 * Path to default configuration path.
	 */
	public static final String DEFAULT_CONFIGURATION_PATH = "";
	
	/**
	 * Key for accessing the tweets count per page in the configuration.
	 */
	private static final String CONF_COUNT_KEY = "count";
	
	/**
	 * Key for accessing the date since in the configuration.
	 */
	private static final String CONF_SINCE_KEY = "since";
	
	/**
	 * Key for accessing the data until in the configuration.
	 */
	private static final String CONF_UNTIL_KEY = "until";
	
	/**
	 * Key for accessing the since tweet ID in the configuration.
	 */
	private static final String CONF_SINCEID_KEY = "sinceID";
	
	/**
	 * Key for accessing the maximum tweet ID in the configuration.
	 */
	private static final String CONF_MAXID_KEY = "maxId";
	
	/**
	 * Key for accessing the consumer key in the configuration.
	 */
	private static final String CONSUMER_KEY = "consumerKey";
	
	/**
	 * Key for accessing the consumer secret in the configuration.
	 */
	private static final String CONSUMER_SECRET = "consumerSecret";
	
	/**
	 * Key for accessing the access token in the configuration.
	 */
	private static final String ACCESS_TOKEN = "accessToken";
	
	/**
	 * Key for accessing the access token secret in the configuration.
	 */
	private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
	
	/**
	 * Twitter instances for querying.
	 */
	private Twitter _twitter;
	
	/**
	 * Query.
	 */
	private Query _twitterQuery;
	
	
	/**
	 * Results for query.
	 */
	private QueryResult _queryResults;
	
	/**
	 * Default configuration file creator
	 */
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
	 * Initializes the configuration and the results.
	 * Also configures the query.
	 */
	@Override
	public boolean configure(String propertiesFilePath)
	{
		if (super.configure(propertiesFilePath))
		{
			_twitterQuery = new Query(_query);
			
			if (_configuration.exists(CONF_COUNT_KEY) <= 0)
			{
				_twitterQuery.setCount(_configuration.getIntValue(CONF_COUNT_KEY, 100));
			}
			
			if (_configuration.exists(CONF_SINCEID_KEY) <= 0)
			{
				_twitterQuery.setSinceId(_configuration.getIntValue(CONF_SINCEID_KEY, -1));
			}
			
			if (_configuration.exists(CONF_MAXID_KEY) <= 0)
			{
				_twitterQuery.setMaxId(_configuration.getIntValue(CONF_MAXID_KEY, -1));
			}
			
			if (_configuration.exists(CONF_SINCE_KEY) <= 0)
			{
				_twitterQuery.setSince(_configuration.getValue(CONF_SINCE_KEY, ""));
			}
			
			if (_configuration.exists(CONF_UNTIL_KEY) <= 0)
			{
				_twitterQuery.setUntil(_configuration.getValue(CONF_UNTIL_KEY, ""));
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean connect()
	{
		this.configure(_propertiesFilePath);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(_configuration.getValue(TwitterConnector.CONSUMER_KEY,""))
		  .setOAuthConsumerSecret(_configuration.getValue(TwitterConnector.CONSUMER_SECRET,""))
		  .setOAuthAccessToken(_configuration.getValue(TwitterConnector.ACCESS_TOKEN,""))
		  .setOAuthAccessTokenSecret(_configuration.getValue(TwitterConnector.ACCESS_TOKEN_SECRET,""));

		TwitterFactory tf = new TwitterFactory(cb.build());
		_twitter = tf.getInstance();
		return true;
	}

	@Override
	public void close() 
	{
		_twitter.shutdown();
		_query = null;
		_configuration.clearConfiguration();
		clearResults();
	}

	@Override
	public int query(boolean appendResults) 
	{
		try 
		{
			_queryResults = _twitter.search(_twitterQuery);
			List<Status> statusList = _queryResults.getTweets();
			int results = 0;
			for (Status status: statusList)
			{
				JSONObject object = statusToJSONObject(status);
				if (object != null)
				{
					_results.add(object);
					results++;
				}
			}
			return results;
		} 
		catch (TwitterException e) 
		{
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int nextQuery()
	{
		if (this.hasNextQuery())
		{
			_twitterQuery = _queryResults.nextQuery();
			return query(true);
		}
		return -1;
	}
	
	@Override
	public boolean hasNextQuery()
	{
		if (_queryResults != null)
		{
			return _queryResults.hasNext();
		}
		return false;
	}
	
	/**
	 * Creates a JSONObject from a Status.
	 * 
	 * @param status tweet to convert to JSONObject.
	 * @return the tweet formatted.
	 */
	private JSONObject statusToJSONObject(Status status)
	{
		JSONObject tweet;
		try 
		{
			tweet = new JSONObject(status.toString());
			return tweet;
		} 
		catch (ParseException e) 
		{
			log.severe("Could not parse status.");
		}
		return null;
	}
}
