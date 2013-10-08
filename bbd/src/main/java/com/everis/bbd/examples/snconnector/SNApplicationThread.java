package com.everis.bbd.examples.snconnector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.flume.RpcClientFacadeKeys;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorFactory;
import com.everis.bbd.snconnector.SNConnectorKeys;
import com.everis.bbd.snconnector.SNObject;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * Wrapper for inherited classes from SNConnector.
 */
public class SNApplicationThread extends Thread
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(SNApplicationThread.class.getName());

	/**
	 * Key for default mode.
	 */
	private static String DEFAULT_MODE_KEY = "none";
	
	/**
	 * Key for pagination.
	 */
	private static String PAGINATION_KEY = "pagination";
	
	/**
	 * Key for streaming.
	 */
	private static String STREAMING_KEY = "streaming";
	
	/**
	 * Default event threshold.
	 */
	private static int DEFAULT_THRESHOLD = 10;
	
	/**
	 * Default sleep value.
	 */
	private static long DEFAULT_SLEEP = 5000;
	
	/**
	 * Default sleep value.
	 */
	private static String DEFAULT_HOSTNAME = "localhost";
	
	/**
	 * Default sleep value.
	 */
	private static int DEFAULT_PORT = 0;
	
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
	public SNApplicationThread(String propertiesFile)
	{
		_propertiesFile = propertiesFile;
	}
	
	/**
	 * Configures the client.
	 * 
	 * @return if client has been successfully configured.
	 */
	public boolean configureClient()
	{
		String clientType = _configuration.getValue(RpcClientFacadeKeys.CONF_CLIENT_TYPE_KEY.getId(), "");
		_client = RpcClientFacadeFactory.getClient(clientType);
		if (_client == null)
		{
			return false;
		}
		
		String hostname = _configuration.getValue(RpcClientFacadeKeys.CONF_HOSTNAME_KEY.getId(), DEFAULT_HOSTNAME);
		_client.setHostname(hostname);
		
		int port = _configuration.getIntValue(RpcClientFacadeKeys.CONF_PORT_KEY.getId(), DEFAULT_PORT);
		_client.setPort(port);
		
		String outputDir = _configuration.getValue(RpcClientFacadeKeys.CONF_OUTPUT_DIRECTORY_KEY.getId(), "");
		if (!outputDir.equals(""))
		{
			_client.setOutputDirectory(outputDir);
		}
		
		return _client.connect();
	}
	
	/**
	 * Creates a connector and configures it.
	 * 
	 * @return if configuration has been successful.
	 */
	public boolean configureConnector()
	{
		_configuration = new ConfigurationReader(_propertiesFile);
		if (!_configuration.readConfigurationFile())
		{
			return false;
		}
		
		int connectorType = SNConnectorFactory.getConnectorId(_configuration.getValue(SNConnectorKeys.CONF_APPLICATION_KEY.getId(), ""));

		_connector = SNConnectorFactory.getConnector(connectorType);
		
		if (!_connector.configure(_configuration))
		{
			return false;
		}
		
		if (!_connector.connect())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates a connector and a client and configures them.
	 * 
	 * @return if configuration has been successful.
	 */
	public boolean configure()
	{
		if (!this.configureConnector())
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
		if (!this.configureConnector())
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
		// Number of milliseconds in sleep.
		long sleep = _configuration.getLongValue(SNConnectorKeys.CONF_SLEEP_KEY.getId(), DEFAULT_SLEEP);
		// Sets how many events must have received to start sending the data.
		int eventsThreshold = _configuration.getIntValue(SNConnectorKeys.CONF_EVENTS_THRESHOLD_KEY.getId(), DEFAULT_THRESHOLD);
		//TODO: Add a condition to end the streaming.
		
		_connector.query(false);
		
		while(true)
		{
			// If there are enough results, send them via client.
			if (_connector.getResultSize() >= eventsThreshold)
			{
				List<SNObject> results = _connector.getAndClearResults();
				for(SNObject event: results)
				{
					_client.sendData(event);
				}
			}
			// Else, sleep the thread.
			else
			{
				try
				{
					sleep(sleep);
				} 
				catch (InterruptedException e) 
				{
					log.info("Exception in wait execution.");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Runs the connector looking for pagination and sends events through the client.
	 */
	private void pagination()
	{
		// List to store the results.
		List<SNObject> results = new ArrayList<SNObject>();
		// Executes the query.
		int numberResults = _connector.query(false);
		// While there are results, keeps getting the query pages.
		while (numberResults > 0)
		{
			// Gets and sends the events through the client.
			results.addAll(_connector.getAndClearResults());
			for (SNObject event: results)
			{
				//log.info("Sending event");
				_client.sendData(event);
			}
			// If there are more results (pages), then executes the query.
			if (_connector.hasNextQuery())
			{
				numberResults = _connector.nextQuery();
			}
			// Else, there are no results.
			else
			{
				numberResults = 0;
			}
		}
	}
	
	@Override
	public void run()
	{
		// Getting which type of thread must be executed: pagination or streaming.
		String type = _configuration.getValue(SNConnectorKeys.CONF_CONNECTOR_TYPE_KEY.getId(), DEFAULT_MODE_KEY);
		if (type.equals(PAGINATION_KEY))
		{
			// Launches pagination.
			log.info("Starting connector thread in pagination mode.");
			this.pagination();
		}
		else if (type.equals(STREAMING_KEY))
		{
			// Launches streaming.
			log.info("Starting connector thread in streaming mode.");
			this.streaming();
		}
		else
		{
			log.warning("Connector type does not exists");
		}
		_client.cleanUp();
	}
}