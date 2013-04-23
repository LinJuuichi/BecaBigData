package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Simple use example for TwitterConnector.
 */
public class SimpleTwitterSource 
{	
	/**
	 * Properties file name.
	 */
	private static String _propertiesFile = "flume.properties";
	
	/**
	 * @param args arguments.
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
		
		// creating client
		RpcClientFacade client = new RpcClientFacade(configuration.getValue("hostname", ""),configuration.getIntValue("port", 0));
		if (!client.connect())
		{
			System.out.println("Failed in connect the client.");
			return;
		}
		
		String twitterPropertiesFile = configuration.getValue("config", "");
		
		SNConnector connector = SNConnectorFactory.getConnector(SNConnectorFactory.getConnectorId(configuration.getValue("application", "")));
		if (connector.configure(twitterPropertiesFile))
		{
			connector.connect();
			int numberResults = connector.query(false);
			List<JSONObject> results = new ArrayList<JSONObject>();
			while (numberResults > 0)
			{
				results.addAll(connector.getResults());
				if (connector.hasNextQuery())
				{
					numberResults = 0;
					//numberResults = connector.nextQuery();
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
				client.sendData(tweet.toString());
			}
		}
	}
}
