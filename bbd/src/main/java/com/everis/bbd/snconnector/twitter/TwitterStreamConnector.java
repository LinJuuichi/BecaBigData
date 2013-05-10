package com.everis.bbd.snconnector.twitter;

import java.util.List;
import java.util.logging.Logger;

import com.everis.bbd.snconnector.SNConnectorKeys;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 */
public class TwitterStreamConnector extends AbstractTwitterConnector 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(TwitterStreamConnector.class.getName());

	/**
	 * Twitter instance for streaming.
	 */
	private TwitterStream _twitter;

	/**
	 * Filtering options for tracking tweets.
	 */
	private FilterQuery _twitterQuery;
	
	/**
	 * List of words to be tracked in Twitter.
	 */
	private List<String> _tracks;
	
	/**
	 * Default configuration file constructor.
	 */
	public TwitterStreamConnector()
	{
		this(DEFAULT_CONFIGURATION_PATH);
	}

	/**
	 * Returns a TwitterStreamConnector configured with the properties in
	 * propertiesFile.
	 * 
	 * @param propertiesFile file path with the properties (tokens).
	 */
	public TwitterStreamConnector(String propertiesFile) 
	{
		super(propertiesFile);
	}
	
	@Override
	public boolean configureQuery()
	{
		_twitterQuery = new FilterQuery();
		if (_configuration.exists(SNConnectorKeys.CONF_QUERY_KEY.getId()) > 0)
		{
			_tracks = _configuration.getValues(SNConnectorKeys.CONF_QUERY_KEY.getId());
			String[] tracks = _tracks.toArray(new String[_tracks.size()]);
			_search = tracks.toString();
			_twitterQuery.track(tracks);
		}
		else
		{
			log.severe("Query not specified in file "+_propertiesFile+".");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean connectToTwitter(ConfigurationBuilder cb)
	{
		TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
		_twitter = tf.getInstance();
		return true;
	}

	@Override
	public void close() 
	{
		_twitter.shutdown();
		_configuration.clearConfiguration();
		clearResults();
	}

	@Override
	public int query(boolean appendResults) 
	{
		if(!appendResults)
		{
			clearResults();
		}
		
		StatusListener listener = new StatusListener() 
		{
			public void onStatus(Status status) 
			{
				log.info("New event received.");
				synchronized(_results)
				{
					_results.add(statusToJSONObject(status, _search));
				}
			}
			
			// This listener will ignore everything except for new tweets
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onScrubGeo(long userId, long upToStatusId) {}
			public void onException(Exception ex) 
			{
				ex.printStackTrace();
			}

			public void onStallWarning(StallWarning arg0) {}
		};
		_twitter.addListener(listener);
		
		if (_twitterQuery != null)
		{
			log.info("Starting to sample tweets.");
			_twitter.filter(_twitterQuery);
		}
		else
		{
			log.severe("The twitter query has not been set. ");
		}
		
		return 0;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public int query(String query, boolean appendResults) 
	{
		return 0;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public int nextQuery() 
	{
		return 0;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public boolean hasNextQuery() 
	{
		return false;
	}

}
