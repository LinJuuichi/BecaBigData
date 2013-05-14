package com.everis.bbd.flume;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Logger;
import org.apache.flume.Event;
import com.everis.bbd.snconnector.SNObject;

/**
 * RpcClientFacade wrapper for avoiding connection to any port.
 * Writes to a file the events instead of sending them through a client. 
 */
public class RpcClientFacadeWrapper extends RpcClientFacade 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(RpcClientFacadeWrapper.class.getName());

	/**
	 * Default path to the file.
	 */
	private static String DEFAULT_FILE = "events.txt";
	
	/**
	 * Writer.
	 */
	PrintWriter _writer;
	
	/**
	 * Default constructor.
	 */
	public RpcClientFacadeWrapper() {}

	/**
	 * Default constructor.
	 * 
	 * @param hostname does nothing.
	 * @param port does nothing.
	 */
	public RpcClientFacadeWrapper(String hostname, int port) {}
	
	/**
	 * Open the file.
	 * 
	 * @return always true.
	 */
	@Override
	public boolean connect()
	{
		try 
		{
			_writer = new PrintWriter(DEFAULT_FILE, "UTF-8");
		} 
		catch (FileNotFoundException e) 
		{
			log.severe("File not found");
			return false;
		} 
		catch (UnsupportedEncodingException e) 
		{
			log.severe("Encoding not suported");
			return false;
		}
		return true;
	}
	
	/**
	 * Closes the file.
	 */
	@Override
	public void cleanUp()
	{
		log.warning("Closing  file.");
		_writer.close();
	}

	/**
	 * Open the file.
	 * 
	 * @param hostname does nothing.
	 * @param port does nothing.
	 * @return always true.
	 */
	@Override
	public boolean connect(String hostname, int port)
	{
		this.connect();
		return true;
	}

	/**
	 * Writes to a file the event.
	 * 
	 * @param event to be printed.
	 */
	@Override
	public void sendEvent(Event event)
	{	
		System.out.println(event.toString());
		_writer.println(event.toString());
	}
	
	/**
	 * Writes to a file the event.
	 * 
	 * @param data to be printed.
	 * @param time does nothing.
	 */
	@Override
	public void sendData(String data, Date time)
	{
		System.out.println(data);
		_writer.println(data);
	}

	/**
	 * Writes to a file the event.
	 * 
	 * @param data to be printed.
	 */
	@Override
	public void sendData(SNObject data)
	{
		System.out.println(data.toString());
		_writer.println(data.toString());
	}
}
