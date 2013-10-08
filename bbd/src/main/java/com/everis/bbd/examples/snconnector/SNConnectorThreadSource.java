package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import com.everis.bbd.examples.snconnector.SNApplicationThread;
import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Example for using SNConnectorSource.
 */
public class SNConnectorThreadSource 
{

	/**
	 * Properties file name.
	 */
	private static String _propertiesFile = "thread.properties";

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
		RpcClientFacade client = RpcClientFacadeFactory.getClient(configuration.getValue("clientType", ""));
		client.setHostname(configuration.getValue("hostname", "localhost"));
		client.setPort(configuration.getIntValue("port", 0));
		if (!client.connect())
		{
			System.out.println("Failed in connect the client.");
			return;
		}

		// reading properties files for each connector.
		List<String> twitterPropertiesFiles = configuration.getValues("config");
		
		// creating a connectorThread for each file read and starting it.
		List<SNApplicationThread> connectors = new ArrayList<SNApplicationThread>();
		for(String configFile: twitterPropertiesFiles)
		{
			SNApplicationThread connectorThread = new SNApplicationThread(configFile);
			if(connectorThread.configure(client))
			{
				connectors.add(connectorThread);
				connectorThread.start();
			}
		}
	}
}
