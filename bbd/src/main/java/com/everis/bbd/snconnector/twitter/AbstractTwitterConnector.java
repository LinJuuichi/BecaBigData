package com.everis.bbd.snconnector.twitter;

import java.sql.Timestamp;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorKeys;
import com.everis.bbd.snconnector.SNObject;
import com.everis.bbd.snconnector.SNObjectComment;
import com.everis.bbd.snconnector.SNObjectKeys;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Abstract implementation for Twitter connectors with common functions.
 */
public abstract class AbstractTwitterConnector extends SNConnector
{

	/**
	 * Default constructor.
	 */
	public AbstractTwitterConnector() 
	{
		this(DEFAULT_CONFIGURATION_PATH);
	}
	
	/**
	 * @param propertiesFile path to the configuration path.
	 */
	public AbstractTwitterConnector(String propertiesFile) 
	{
		super(propertiesFile);
	}
	
	/**
	 * Connects the Twitter connector to Twitter.
	 * 
	 * @param cb configuration builder (with the OAuth tokens set).
	 * @return if the connection has been established.
	 */
	public abstract boolean connectToTwitter(ConfigurationBuilder cb);
	
	@Override
	public boolean connect() 
	{
		if (_configuration == null)
		{
			if (!this.configure(_propertiesFile))
			{
				log.severe("Could not configure the connector. Stop connecting.");
				return false;
			}
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
			.setOAuthConsumerKey(_configuration.getValue(SNConnectorKeys.OAUTH_CONSUMER_KEY.getId(),""))
			.setOAuthConsumerSecret(_configuration.getValue(SNConnectorKeys.OAUTH_CONSUMER_SECRET.getId(),""))
			.setOAuthAccessToken(_configuration.getValue(SNConnectorKeys.OAUTH_ACCESS_TOKEN.getId(),""))
			.setOAuthAccessTokenSecret(_configuration.getValue(SNConnectorKeys.OAUTH_ACCESS_TOKEN_SECRET.getId(),""));

		return connectToTwitter(cb);
	}

	/**
	 * Creates a JSONObject from a Status.
	 * 
	 * @param status tweet to convert to JSONObject.
	 * @param search query executed for getting the tweet.
	 * @return the tweet formatted.
	 */
	protected SNObject statusToSNObject(Status status, String search)
	{
		SNObject tweet = new SNObjectComment();
		tweet.setLong(SNObjectKeys.POST_ID_KEY.getId(), status.getId());
		
		User user = status.getUser();
		if (user != null)
		{
			tweet.setLong(SNObjectKeys.POST_USERID_KEY.getId(), user.getId());
			tweet.setString(SNObjectKeys.POST_USER_KEY.getId(), user.getName());
			tweet.setString(SNObjectKeys.POST_LANGUAGE_KEY.getId(), user.getLang());
		}
		else
		{
			tweet.setLong(SNObjectKeys.POST_USERID_KEY.getId(), -1);
			tweet.setString(SNObjectKeys.POST_USER_KEY.getId(), "NULL");
			tweet.setString(SNObjectKeys.POST_LANGUAGE_KEY.getId(), "NULL");
		}
		
		Timestamp date = new Timestamp(status.getCreatedAt().getTime());
		tweet.setTimestamp(SNObjectKeys.POST_DATE_KEY.getId(), date);
		
		tweet.setString(SNObjectKeys.POST_QUERY_KEY.getId(), search);
		

		tweet.setString(SNObjectKeys.POST_SOURCE_KEY.getId(), status.getSource());
		
		GeoLocation geo = status.getGeoLocation();
		if (geo != null)
		{
			tweet.setDouble(SNObjectKeys.POST_LATITUDE_KEY.getId(), geo.getLatitude());
			tweet.setDouble(SNObjectKeys.POST_LONGITUDE_KEY.getId(), geo.getLongitude());
		}
		
		// @TODO: remove all spacial characters
		tweet.setString(SNObjectKeys.POST_TEXT_KEY.getId(), status.getText().replaceAll("\t", "").replaceAll("\n", ""));
		
		return tweet;
	}
}
