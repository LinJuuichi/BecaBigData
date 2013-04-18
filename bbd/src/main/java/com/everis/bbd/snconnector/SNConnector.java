package com.everis.bbd.snconnector;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import org.json.JSONObject;

/**
 * Abstract class for social networks connectors
 * @author Robert
 *
 */
public abstract class SNConnector 
{
	private SynchronousQueue<JSONObject> _results;
	private HashMap<String, String> _configuration;
	
	public SNConnector()
	{
		_results = null;
		_configuration = null;
	}
	
	public void configure(HashMap<String, String> configuration)
	{
		_configuration = configuration;
	}
	
	public synchronized SynchronousQueue getResults()
	{
		return _results;
	}
	
	public synchronized void appendResults(SynchronousQueue<JSONObject> newResults)
	{
		
	}
}
