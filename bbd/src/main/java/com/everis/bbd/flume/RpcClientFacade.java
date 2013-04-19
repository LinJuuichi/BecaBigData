package com.everis.bbd.flume;

import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

/**
 * Facade class for RpcClient to send events to Flume sources.
 */
public class RpcClientFacade 
{
	private static Logger log = Logger.getLogger(RpcClientFacade.class.getName());
	
	private RpcClient _client;
	private String _hostname;
	private int _port;

	/**
	 * Creates an empty client.
	 */
	public RpcClientFacade() 
	{ 
		_client = null;
	}

	/**
	 * Creates and connects the client to a hostname and port.
	 * 
	 * @param hostname of source.
	 * @param port of source.
	 */
	public RpcClientFacade(String hostname, int port)
	{
		_client = null;
		init(hostname,port);
	}

	/**
	 * Connects the client to a hostname and port.
	 * 
	 * @param hostname of source.
	 * @param port of source.
	 */
	public void init(String hostname, int port)
	{
		if (_client != null)
		{
			log.warning("There is already a connected client to"+_hostname+":"+_port+".");
			_client.close();
		}
		_hostname = hostname;
		_port = port;
		log.info("Connecting client to"+_hostname+":"+_port+".");
		_client = RpcClientFactory.getDefaultInstance(_hostname, _port);
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
				_client.append(event);
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