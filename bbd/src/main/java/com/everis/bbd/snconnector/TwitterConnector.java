package com.everis.bbd.snconnector;

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
	 * Default configuration file constructor.
	 */
	public TwitterConnector()
	{
		this(DEFAULT_CONFIGURATION_PATH);
	}

	/**
	 * Returns a TwitterConnector configured with the properties in
	 * propertiesFile.
	 * 
	 * @param propertiesFile file path with the properties (tokens).
	 */
	public TwitterConnector(String propertiesFile)
	{
		super(propertiesFile);
	}

	/**
	 * Initializes the configuration and the results.
	 * Also configures the query.
	 */
	@Override
	public boolean configure(String propertiesFile)
	{
		if (super.configure(propertiesFile))
		{
			_twitterQuery = new Query();
			
			if (_configuration.exists(TwitterConnectorKeys.CONF_QUERY_KEY.getId()) > 0)
			{
				_twitterQuery.setQuery(_configuration.getValue(TwitterConnectorKeys.CONF_QUERY_KEY.getId(), ""));
			}
			else
			{
				log.severe("Query not specified in file "+_propertiesFile+".");
				return false;
			}

			if (_configuration.exists(TwitterConnectorKeys.CONF_COUNT_KEY.getId()) > 0)
			{
				_twitterQuery.setCount(_configuration.getIntValue(TwitterConnectorKeys.CONF_COUNT_KEY.getId(), 100));
			}

			if (_configuration.exists(TwitterConnectorKeys.CONF_SINCEID_KEY.getId()) > 0)
			{
				_twitterQuery.setSinceId(_configuration.getIntValue(TwitterConnectorKeys.CONF_SINCEID_KEY.getId(), -1));
			}

			if (_configuration.exists(TwitterConnectorKeys.CONF_MAXID_KEY.getId()) > 0)
			{
				_twitterQuery.setMaxId(_configuration.getIntValue(TwitterConnectorKeys.CONF_MAXID_KEY.getId(), -1));
			}

			if (_configuration.exists(TwitterConnectorKeys.CONF_SINCE_KEY.getId()) > 0)
			{
				_twitterQuery.setSince(_configuration.getValue(TwitterConnectorKeys.CONF_SINCE_KEY.getId(), ""));
			}

			if (_configuration.exists(TwitterConnectorKeys.CONF_UNTIL_KEY.getId()) > 0)
			{
				_twitterQuery.setUntil(_configuration.getValue(TwitterConnectorKeys.CONF_UNTIL_KEY.getId(), ""));
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean connect()
	{
		if (_configuration == null)
		{
			this.configure(_propertiesFile);
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(_configuration.getValue(SNConnectorKeys.CONSUMER_KEY.getId(),""))
		.setOAuthConsumerSecret(_configuration.getValue(SNConnectorKeys.CONSUMER_SECRET.getId(),""))
		.setOAuthAccessToken(_configuration.getValue(SNConnectorKeys.ACCESS_TOKEN.getId(),""))
		.setOAuthAccessTokenSecret(_configuration.getValue(SNConnectorKeys.ACCESS_TOKEN_SECRET.getId(),""));

		TwitterFactory tf = new TwitterFactory(cb.build());
		_twitter = tf.getInstance();
		return true;
	}

	@Override
	public void close() 
	{
		_twitter.shutdown();
		_twitterQuery = null;
		_configuration.clearConfiguration();
		clearResults();
	}

	@Override
	public int query(boolean appendResults) 
	{
		try 
		{
			log.info("Searching: "+_twitterQuery.getQuery());
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
			log.warning("Search failed.");
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int query(String query, boolean appendResults) 
	{
		_twitterQuery.setQuery(query);
		return this.query(appendResults);
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
		JSONObject jTweet;
		jTweet = new JSONObject();
		jTweet.put(SNConnectorKeys.POST_ID_KEY.getId(), status.getId());
		jTweet.put(SNConnectorKeys.POST_USERID_KEY.getId(), status.getCurrentUserRetweetId());
		jTweet.put(SNConnectorKeys.POST_USER_KEY.getId(), status.getUser().getName());
		jTweet.put(SNConnectorKeys.POST_SOURCE_KEY.getId(), status.getSource());
		jTweet.put(SNConnectorKeys.POST_DATE_KEY.getId(), status.getCreatedAt());
		jTweet.put(SNConnectorKeys.POST_LOCATION_KEY.getId(), status.getGeoLocation());
		jTweet.put(SNConnectorKeys.POST_TEXT_KEY.getId(), status.getText());
		return jTweet;
	}

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
}
