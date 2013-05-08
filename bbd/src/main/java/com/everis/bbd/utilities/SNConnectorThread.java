package com.everis.bbd.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONObject;
import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorFactory;

/**
 * Wrapper for inherited classes from SNConnector.
 */
public class SNConnectorThread extends Thread
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(SNConnectorThread.class.getName());

	/**
	 * RpcClient to flume.
	 */
	private RpcClientFacade _client;
	
	/**
	 * Connector to the social network.
	 */
	private SNConnector _connector;
	
	/**
	 * Configuration.
	 */
	private ConfigurationReader _configuration;
	
	/**
	 * Name of the property file.
	 */
	private String _propertiesFile;
	
	/**
	 * @param propertiesFile Name of the property file.
	 */
	public SNConnectorThread(String propertiesFile)
	{
		_propertiesFile = propertiesFile;
	}
	
	/**
	 * Configures the client.
	 * @return if client has been successfully configured.
	 */
	public boolean configureClient()
	{
		_client = RpcClientFacadeFactory.getClient(_configuration.getValue("clientType", ""));
		_client.setHostname(_configuration.getValue("hostname", "localhost"));
		_client.setPort(_configuration.getIntValue("port", 0));
		return _client.connect();
	}
	
	/**
	 * Creates a connector and a client and configures them.
	 * 
	 * @return if configuration has been successful.
	 */
	public boolean configure()
	{
		_configuration = new ConfigurationReader(_propertiesFile);
		if (!_configuration.readConfigurationFile())
		{
			return false;
		}
		
		_connector = SNConnectorFactory.getConnector(SNConnectorFactory.getConnectorId(_configuration.getValue("application", "")));
		if (!_connector.configure(_configuration))
		{
			return false;
		}
		
		if (!_connector.connect())
		{
			return false;
		}
		
		if (!this.configureClient())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates a connector and a client and configures them.
	 * 
	 * @param client client configured.
	 * @return if configuration has been successful.
	 */
	public boolean configure(RpcClientFacade client)
	{
		_configuration = new ConfigurationReader(_propertiesFile);
		if (!_configuration.readConfigurationFile())
		{
			return false;
		}
		
		_connector = SNConnectorFactory.getConnector(SNConnectorFactory.getConnectorId(_configuration.getValue("application", "")));
		if (!_connector.configure(_configuration))
		{
			return false;
		}
		
		if (!_connector.connect())
		{
			return false;
		}
		
		this.setClient(client);
		
		return true;
	}
	
	/**
	 * Creates a connector and configures it.
	 * 
	 * @param propertiesFile Name of the property file.
	 * @return if configuration has been successful.
	 */
	public boolean configure(String propertiesFile)
	{
		_propertiesFile = propertiesFile;
		return this.configure();
	}
	
	/**
	 * Sets and configure a new client.
	 * 
	 * @param client new client.
	 */
	public void setClient(RpcClientFacade client)
	{
		_client = client;
	}
	
	/**
	 * Runs the connector in streaming and sends events through the client.
	 */
	private void streaming()
	{
		int eventsThreshold = _configuration.getIntValue("eventsThreshold", 10);
		while(true)
		{
			while (_connector.getResultSize() >= eventsThreshold)
			{
				List<JSONObject> results = _connector.getAndClearResults();
				for(JSONObject event: results)
				{
					_client.sendData(event);
				}
			}
		}
	}
	
	/**
	 * Runs the connector looking for pagination and sends events through the client.
	 */
	private void pagination()
	{
		List<JSONObject> results = new ArrayList<JSONObject>();
		int numberResults = _connector.query(false);
		while (numberResults > 0)
		{
			results.addAll(_connector.getAndClearResults());
			for (JSONObject event: results)
			{
				_client.sendData(event);
			}
			if (_connector.hasNextQuery())
			{
				numberResults = _connector.nextQuery();
			}
			else
			{
				numberResults = 0;
			}
		}
	}
	
	@Override
	public void run()
	{
		String type = _configuration.getValue("connectorType", "none");
		if (type.equals("pagination"))
		{
			this.pagination();
		}
		else if (type.equals("streaming"))
		{
			this.streaming();
		}
		else
		{
			log.warning("Connector type does not exists");
		}
	}
}
