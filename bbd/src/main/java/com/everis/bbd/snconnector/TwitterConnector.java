package com.everis.bbd.snconnector;

import java.util.ArrayList;
import java.util.List;
import com.everis.bbd.utilities.ConfigurationReader;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnector extends SNConnector 
{
	private static final String PROP_PATH = "twitterconnector";
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
		_config = new ConfigurationReader(TwitterConnector.PROP_PATH);
		init(
				_config.getValue(TwitterConnector.CONSUMER_KEY),
				_config.getValue(TwitterConnector.CONSUMER_SECRET),
				_config.getValue(TwitterConnector.ACCESS_TOKEN),
				_config.getValue(TwitterConnector.ACCESS_TOKEN_SECRET)
			);
	}

	public void init(String consumerKey, String consumerSecret, String accessToken, String accesTokenSecret)
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret)
		  .setOAuthAccessToken(accessToken)
		  .setOAuthAccessTokenSecret(accesTokenSecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		_twitter = tf.getInstance();
	}

	public Query getQuery()
	{
		return _query;
	}

	public void setQuery(String query)
	{
		_query = new Query(query);
	}

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

	public int doQuery(String query)
	{
		this.setQuery(query);
		return this.doQuery();
	}

	public boolean hasNextQuery()
	{
		if (_queryResults != null)
		{
			return _queryResults.hasNext();
		}
		return false;
	}

	public int doNextQuery()
	{
		if (this.hasNextQuery())
		{
			_query = _queryResults.nextQuery();
			return this.doQuery();
		}
		return -1;
	}

	public List<Status> getResults()
	{
		ArrayList<Status> results = null;
		if (_queryResults != null)
		{
			return _queryResults.getTweets();
		}
		return results;
	}
}
