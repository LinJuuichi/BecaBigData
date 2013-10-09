package com.everis.bbd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.flume.RpcClientFacadeKeys;
import com.everis.bbd.snconnector.SNConnectorThread;
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
				client.setOutputDirectory(outputDir);
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
		File[] queriesPropertiesFiles = folder.listFiles();
		
		// creating a connectorThread for each file read and starting it.
		List<SNConnectorThread> connectors = new ArrayList<SNConnectorThread>();
		for(File configFile: queriesPropertiesFiles)
		{
			String configFileName = configFile.getName();
			if (configFileName.endsWith(".properties"))
			{
				SNConnectorThread connectorThread = new SNConnectorThread(_propertiesDir+"/"+queriesPropertiesDir+"/"+configFileName);
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
}
