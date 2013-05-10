package com.everis.bbd.snconnector;

import java.util.logging.Logger;
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
public class TwitterConnector extends AbstractTwitterConnector 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(TwitterConnector.class.getName());

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
	
	@Override
	protected boolean configureQuery()
	{
		_twitterQuery = new Query();
		
		if (_configuration.exists(SNConnectorKeys.CONF_QUERY_KEY.getId()) > 0)
		{
			_search = _configuration.getValue(SNConnectorKeys.CONF_QUERY_KEY.getId(), "");
			_twitterQuery.setQuery(_search);
		}
		else
		{
			log.severe("Query not specified in file "+_propertiesFile+".");
			return false;
		}

		if (_configuration.exists(SNConnectorKeys.CONF_COUNT_KEY.getId()) > 0)
		{
			_twitterQuery.setCount(_configuration.getIntValue(SNConnectorKeys.CONF_COUNT_KEY.getId(), 100));
		}

		if (_configuration.exists(SNConnectorKeys.CONF_SINCEID_KEY.getId()) > 0)
		{
			_twitterQuery.setSinceId(_configuration.getIntValue(SNConnectorKeys.CONF_SINCEID_KEY.getId(), -1));
		}

		if (_configuration.exists(SNConnectorKeys.CONF_MAXID_KEY.getId()) > 0)
		{
			_twitterQuery.setMaxId(_configuration.getIntValue(SNConnectorKeys.CONF_MAXID_KEY.getId(), -1));
		}

		if (_configuration.exists(SNConnectorKeys.CONF_SINCE_KEY.getId()) > 0)
		{
			_twitterQuery.setSince(_configuration.getValue(SNConnectorKeys.CONF_SINCE_KEY.getId(), ""));
		}

		if (_configuration.exists(SNConnectorKeys.CONF_UNTIL_KEY.getId()) > 0)
		{
			_twitterQuery.setUntil(_configuration.getValue(SNConnectorKeys.CONF_UNTIL_KEY.getId(), ""));
		}
		return true;
	}

	@Override
	public boolean connectToTwitter(ConfigurationBuilder cb)
	{
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
			log.info("Querying: "+_twitterQuery.getQuery());
			_queryResults = _twitter.search(_twitterQuery);
			int results = 0;
			for (Status status: _queryResults.getTweets())
			{
				_results.add(statusToJSONObject(status,_search));
				results++;
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
		_search = query;
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
}