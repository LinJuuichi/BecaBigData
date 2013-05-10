package com.everis.bbd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.flume.RpcClientFacadeKeys;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Application that connects to social networks and downloads information.
 */
public class SNApplication 
{
	/**
	 * Properties file name.
	 */
	private static String _propertiesFile = "snapplication.properties";

	/**
	 * Properties file name.
	 */
	private static String _propertiesDir = "snconnector";

	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		// reading the configuration file.
		ConfigurationReader configuration = new ConfigurationReader(_propertiesDir+"/"+_propertiesFile);
		if (!configuration.readConfigurationFile())
		{
			System.out.println("Failed in reading configuration file.");
			return;
		}

		// creating client
		String clientType = configuration.getValue("clientType", "");
		RpcClientFacade client = null;
		boolean generalClient = !clientType.equals("");
		if (generalClient)
		{
			client = RpcClientFacadeFactory.getClient(clientType);
			client.setHostname(configuration.getValue(RpcClientFacadeKeys.CONF_HOSTNAME_KEY.getId(), "localhost"));
			client.setPort(configuration.getIntValue(RpcClientFacadeKeys.CONF_PORT_KEY.getId(), 0));
			
			String outputDir = configuration.getValue(RpcClientFacadeKeys.CONF_OUTPUT_DIRECTORY_KEY.getId(), "");
			if (!outputDir.equals(""))
			{
				client.setHostname(outputDir);
			}
			
			if (!client.connect())
			{
				System.out.println("Failed in connect the client.");
				return;
			}
		}
		
		// getting properties files for each query.
		String queriesPropertiesDir = configuration.getValue("config","");
		if (queriesPropertiesDir.equals(""))
		{
			System.out.println("No queries properties directory specified.");
			return;
		}
		File folder = new File(configuration.getConfigFilePath().substring(0,configuration.getConfigFilePath().lastIndexOf("/")) + "/" + queriesPropertiesDir);
		String[] queriesPropertiesFiles = folder.list(null);

		// creating a connectorThread for each file read and starting it.
		List<SNApplicationThread> connectors = new ArrayList<SNApplicationThread>();
		for(String configFile: queriesPropertiesFiles)
		{
			SNApplicationThread connectorThread = new SNApplicationThread(_propertiesDir+"/"+queriesPropertiesDir+"/"+configFile);
			boolean configured = false;
			if (generalClient)
			{
				configured = connectorThread.configure(client);
			}
			else
			{
				configured = connectorThread.configure();
			}
			
			if(configured)
			{
				connectors.add(connectorThread);
				connectorThread.start();
			}
		}
	}

}
