package com.everis.bbd.flume;

import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.FlumeException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.TimestampInterceptor;

/**
 * Facade class for RpcClient to send events to Flume sources.
 */
public class RpcClientFacade 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(RpcClientFacade.class.getName());
	
	/**
	 * Client to communicate with Flume source.
	 */
	private RpcClient _client;
	
	
	/**
	 * Hostname of  Flume source.
	 */
	private String _hostname;
	
	
	/**
	 * Port of Flume source.
	 */
	private int _port;

	/**
	 * Creates an empty client.
	 */
	public RpcClientFacade() 
	{ 
		_client = null;
	}

	/**
	 * Creates and initializes the client.
	 * 
	 * @param hostname of source.
	 * @param port of source.
	 */
	public RpcClientFacade(String hostname, int port)
	{
		_client = null;
		_hostname = hostname;
		_port = port;
	}
	
	/**
	 * Connects the client.
	 * 
	 * @return connection established.
	 */
	public boolean connect()
	{
		log.info("Connecting client to "+_hostname+":"+_port+".");
		try
		{
			_client = RpcClientFactory.getDefaultInstance(_hostname, _port);
		}
		catch (FlumeException e)
		{
			log.severe("Could not connect.");
			return false;
		}
		return true;
	}

	/**
	 * Connects the client to hostname and port.
	 * If there is a connection, closes it.
	 * 
	 * @param hostname of source.
	 * @param port of source.
	 * @return connection established.
	 */
	public boolean connect(String hostname, int port)
	{
		if (_client != null)
		{
			log.warning("There is already a connected client to"+_hostname+":"+_port+".");
			_client.close();
		}
		_hostname = hostname;
		_port = port;
		return connect();
	}

	/**
	 * Sends an event to Flumes source connected.
	 * 
	 * @param event to send to the source.
	 */
	public void sendEvent(Event event)
	{
		if (_client != null)
		{
			try
			{
				log.info("Sending event "+event.toString()+".");
				
				TimestampInterceptor.Builder b = new TimestampInterceptor.Builder();
				TimestampInterceptor t = (TimestampInterceptor) b.build();
				_client.append(t.intercept(event));
			}
			catch (EventDeliveryException e) 
			{
				log.warning("Couldn't send event. Reconnecting client...");
				_client.close();
				_client = RpcClientFactory.getDefaultInstance(_hostname, _port);
			}
		}
	}

	/**
	 * Sends data to a Flume source as an event.
	 * 
	 * @param data String to be sent as an event.
	 */
	public void sendData(String data)
	{
		Event event = EventBuilder.withBody(data,Charset.forName("UTF-8"));
		this.sendEvent(event);
	}

	/**
	 * Closes the client.
	 */
	public void cleanUp()
	{
		log.warning("Closing  with"+_hostname+":"+_port+".");
		_client.close();
		_client = null;
	}

	/**
	 * Sets the source hostname.
	 * 
	 * @param hostname source.
	 */
	public void setHostname(String hostname)
	{
		_hostname = hostname;
	}

	/**
	 * @return source hostname.
	 */
	public String getHostname()
	{
		return _hostname;
	}

	/**
	 * Sets the source port.
	 * 
	 * @param port source.
	 */
	public void setPort(int port)
	{
		_port = port;
	}

	/**
	 * @return port source.
	 */
	public int getPort()
	{
		return _port;
	}
}