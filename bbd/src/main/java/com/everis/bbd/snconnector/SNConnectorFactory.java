package com.everis.bbd.snconnector;

import java.util.logging.Logger;

/**
 * Factory for social network connectors.
 *
 */
public class SNConnectorFactory 
{
	private static Logger log = Logger.getLogger(SNConnectorFactory.class.getName());

	private static final int TWITTER_CONNECTOR = 1;
	//private static final int FACEBOOK_CONNECTOR = 2;
	//private static final int LINKEDIN_CONNECTOR = 3;
	
	private SNConnectorFactory() { }
	
	/**
	 * Returns the connector for the social network type.
	 * @param type social network.
	 * @return connector (SNConnector).
	 */
	public static SNConnector getConnector(int type)
	{
		SNConnector connector = null;
		switch (type) {
		case TWITTER_CONNECTOR:
			connector = new TwitterConnector();
			break;
		default:
			log.warning("Connector type does not exist");
			break;
		}
		return connector;
	}
}
