package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.snconnector.SNObject;
import com.everis.bbd.snconnector.SNObjectKeys;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Simple example for querying in streaming via Twitter.
 */
public class SimpleTwitterStreamSource
{
	/**
	 * Properties file name.
	 */
	private static String _propertiesFile = "streaming.properties";

	/**
	 * @param args arguments
	 */
	public static void main(String[] args)
	{
		// reading the configuration file.
		ConfigurationReader configuration = new ConfigurationReader(_propertiesFile);
		if (!configuration.readConfigurationFile())
		{
			System.out.println("Failed in reading configuration file.");
			return;
		}

		String twitterPropertiesFile = configuration.getValue("config", "");

		SNConnector connector = SNConnectorFactory.getConnector(SNConnectorFactory.getConnectorId(configuration.getValue("application", "")));
		if (connector.configure(twitterPropertiesFile))
		{
			connector.connect();
			List<SNObject> results = new ArrayList<SNObject>();
			connector.query(true);
			while(true)
			{
				while (connector.getResultSize() >= 1)
				{
					results = connector.getAndClearResults();
					for(SNObject tweet: results)
					{
						System.out.println(tweet.getString(SNObjectKeys.POST_TEXT_KEY.getId()));
					}
				}
			}
		}

	}

}
