package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * 
 */
class SNConnectorThread extends Thread
{
	/**
	 * Client to send information to flume.
	 */
	private RpcClientFacade _client;
	
	/**
	 * Connector for querying.
	 */
	private SNConnector _connector;
	
	/**
	 * Connector configuration file.
	 */
	private String _configFile;
	
	/**
	 * @param configFile Connector configuration file.
	 * @param client Client to send information to flume.
	 */
	public SNConnectorThread(String configFile, RpcClientFacade client)
	{
		_configFile = configFile;
		_client = client;
	}
	
	public void run()
	{
		// reading the configuration file.
		ConfigurationReader configuration = new ConfigurationReader(_configFile);
		if (!configuration.readConfigurationFile())
		{
			System.out.println("Failed in reading configuration file.");
			return;
		}
		
		_connector = SNConnectorFactory.getConnector(SNConnectorFactory.getConnectorId(configuration.getValue("application", "")));
		if (_connector.configure(configuration))
		{
			if (_connector.connect())
			{
				int numberResults = _connector.query(false);
				List<JSONObject> results = new ArrayList<JSONObject>();
				while (numberResults > 0)
				{
					results.addAll(_connector.getResults());
					for (JSONObject tweet: results)
					{
						_client.sendData(tweet);
					}
					if (_connector.hasNextQuery())
					{
						//numberResults = 0;
						results.clear();
						numberResults = _connector.nextQuery();
					}
					else
					{
						numberResults = 0;
					}
				}
			}
		}
	}
}


/**
 * Example for making multiple queries in different TwitterConnectors and join the results into one RpcClientFacade.
 */
public class MultipleTwitterSource 
{

	/**
	 * Application properties file.
	 */
	private static String _propertiesFile = "multiple_sources.properties";
	
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
		
		// reading properties files for each connector.
		List<String> twitterPropertiesFiles = configuration.getValues("config");
		
		// creating a connectorThread for each file read and starting it.
		List<SNConnectorThread> connectors = new ArrayList<SNConnectorThread>();
		for(String configFile: twitterPropertiesFiles)
		{
			SNConnectorThread connectorThread = new SNConnectorThread(configFile,client);
			connectors.add(connectorThread);
			connectorThread.start();
		}
	}
}
