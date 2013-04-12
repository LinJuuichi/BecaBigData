package com.everis.bbd.flume;

import java.nio.charset.Charset;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

public class RpcClientFacade 
{
	private RpcClient _client;
	private String _hostname;
	private int _port;

	public RpcClientFacade() 
	{ 
		_client = null;
	}

	public RpcClientFacade(String hostname, int port)
	{
		_client = null;
		init(hostname,port);
	}

	public void init(String hostname, int port)
	{
		_hostname = hostname;
		_port = port;
		if (_client != null)
		{
			_client.close();
			_client = null;
		}
		_client = RpcClientFactory.getDefaultInstance(_hostname, _port);
	}

	public void sendEvent(Event event)
	{
		if (_client != null)
		{
			try
			{
				_client.append(event);
			}
			catch (EventDeliveryException e) 
			{
				_client.close();
				_client = null;
				_client = RpcClientFactory.getDefaultInstance(_hostname, _port);
			}
		}
	}

	public void sendData(String data)
	{
		Event event = EventBuilder.withBody(data,Charset.forName("UTF-8"));
		this.sendEvent(event);
	}

	public void cleanUp()
	{
		_client.close();
	}

	public void setHostname(String hostname)
	{
		_hostname = hostname;
	}

	public String getHostname()
	{
		return _hostname;
	}

	public void setPort(int port)
	{
		_port = port;
	}

	public int getPort()
	{
		return _port;
	}
}