package com.everis.bbd.flume;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.FlumeException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import org.json.JSONObject;

import com.everis.bbd.snconnector.SNConnector.SNConnectorKeys;

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
	 * Key for the header value of the timestamp.
	 */
	private static String TIMESTAMP_HEADER = "timestamp";
	
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
	 * @param time timestamp to add in the event header.
	 */
	public void sendData(String data, Date time)
	{
		Event event = EventBuilder.withBody(data,Charset.forName("UTF-8"));
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(TIMESTAMP_HEADER, String.valueOf(time.getTime()));
		event.setHeaders(headers);
		
		this.sendEvent(event);
	}

	/**
	 * Sends data to a Flume source as an event with a timestamp value in the header.
	 * JSONObject should have a parameter with key {@link SNConnectorKeys#POST_DATE_KEY} that represents the posted date of data.
	 * If not, timestamp will be set to the execution date.
	 * 
	 * @param data JSONObject to be sent as an event.
	 */
	public void sendData(JSONObject data)
	{
		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
		Date date = new Date();
		try 
		{
			date = dateFormat.parse(data.getString(SNConnectorKeys.POST_DATE_KEY.getId()));
		} 
		catch (NoSuchElementException e)
		{
			log.warning("Date not found in data.");
		} 
		catch (ParseException e) 
		{
			log.warning("Date not found in data.");
		}
		finally
		{
			this.sendData(data.toString(),date);
		}
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