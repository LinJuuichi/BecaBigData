package com.everis.bbd.flume;

import java.util.Date;
import java.util.logging.Logger;
import org.apache.flume.Event;
import org.json.JSONObject;

/**
 * RpcClientFacade wrapper for avoiding connection to any port.
 * Prints the events instead of sending them through a client. 
 */
public class RpcClientFacadeWrapper extends RpcClientFacade 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(RpcClientFacadeWrapper.class.getName());

	/**
	 * Default constructor.
	 */
	public RpcClientFacadeWrapper() {}

	/**
	 * Default constructor.
	 * 
	 * @param hostname does nothing.
	 * @param port dos nothing.
	 */
	public RpcClientFacadeWrapper(String hostname, int port) {}
	
	/**
	 * Does nothing, returns true.
	 * 
	 * @return always true.
	 */
	@Override
	public boolean connect()
	{
		return true;
	}

	/**
	 * Does nothing, returns true.
	 * 
	 * @param hostname does nothing.
	 * @param port does nothing.
	 * @return always true.
	 */
	@Override
	public boolean connect(String hostname, int port)
	{
		return true;
	}

	/**
	 * Prints the event.
	 * 
	 * @param event to be printed.
	 */
	@Override
	public void sendEvent(Event event)
	{	
		log.info("Event: ");
		log.info(event.toString());
	}
	
	/**
	 * Prints the event.
	 * 
	 * @param data to be printed.
	 * @param time does nothing.
	 */
	@Override
	public void sendData(String data, Date time)
	{
		log.info("Event: ");
		log.info(data);
	}

	/**
	 * Prints the event.
	 * 
	 * @param data to be printed.
	 */
	@Override
	public void sendData(JSONObject data)
	{
		log.info("Event: ");
		log.info(data.toString());
	}
}
