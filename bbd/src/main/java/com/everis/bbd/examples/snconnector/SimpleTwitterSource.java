package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.snconnector.TwitterConnector;

/**
 * Simple use example for TwitterConnector.
 */
public class SimpleTwitterSource 
{

	/**
	 * Properties file name.
	 */
	private static String _propertiesFile = "simple_twitter_source.properties";
	
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		TwitterConnector twitter = (TwitterConnector) SNConnectorFactory.getConnector(SNConnectorFactory.TWITTER_CONNECTOR);
		if (twitter.configure(_propertiesFile))
		{
			twitter.connect();
			int numberResults = twitter.query(false);
			List<JSONObject> results = new ArrayList<JSONObject>();
			while (numberResults > 0)
			{
				results.addAll(twitter.getResults());
				if (twitter.hasNextQuery())
				{
					//numberResults = twitter.nextQuery();
					numberResults = 0;
				}
				else
				{
					numberResults = 0;
				}
			}
			
			System.out.println("Tweets descargados: "+results.size());
			System.out.println("-----------------------------------");
			
			for (JSONObject tweet: results)
			{
				System.out.println(tweet.toString());
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
			}
		}
	}

}
