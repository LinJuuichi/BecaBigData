package com.everis.bbd.snconnector;

import org.json.JSONObject;

import com.everis.bbd.snconnector.SNConnector.SNConnectorKeys;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public abstract class AbstractTwitterConnector extends SNConnector
{

	public AbstractTwitterConnector() 
	{
		this(DEFAULT_CONFIGURATION_PATH);
	}
	
	public AbstractTwitterConnector(String propertiesFile) 
	{
		super(propertiesFile);
	}
	
	public abstract boolean connectToTwitter(ConfigurationBuilder cb);
	
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

		return connectToTwitter(cb);
	}

	/**
	 * Creates a JSONObject from a Status.
	 * 
	 * @param status tweet to convert to JSONObject.
	 * @param search query executed for getting the tweet.
	 * @return the tweet formatted.
	 */
	protected JSONObject statusToJSONObject(Status status, String search)
	{
		JSONObject jTweet;
		jTweet = new JSONObject();
 		jTweet.put(SNConnectorKeys.POST_ID_KEY.getId(), status.getId());
		jTweet.put(SNConnectorKeys.POST_USERID_KEY.getId(), status.getUser().getId());
		jTweet.put(SNConnectorKeys.POST_USER_KEY.getId(), status.getUser().getName());
		jTweet.put(SNConnectorKeys.POST_SOURCE_KEY.getId(), status.getSource());
		jTweet.put(SNConnectorKeys.POST_DATE_KEY.getId(), status.getCreatedAt().toString());

		GeoLocation geo = status.getGeoLocation();
		if (geo != null)
		{
			jTweet.put(SNConnectorKeys.POST_LATITUDE_KEY.getId(), geo.getLatitude());
			jTweet.put(SNConnectorKeys.POST_LONGITUDE_KEY.getId(), geo.getLongitude());
		}
		jTweet.put(SNConnectorKeys.POST_TEXT_KEY.getId(), status.getText());
		return jTweet;
	}
}
