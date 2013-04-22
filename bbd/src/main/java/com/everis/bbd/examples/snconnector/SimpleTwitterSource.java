package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.snconnector.TwitterConnector;

public class SimpleTwitterSource {

	private static String _propertiesFilePath = "/home/training/Desktop/simple_twitter_source.props";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		TwitterConnector twitter = (TwitterConnector) SNConnectorFactory.getConnector(SNConnectorFactory.TWITTER_CONNECTOR);
		if (twitter.configure(_propertiesFilePath))
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
