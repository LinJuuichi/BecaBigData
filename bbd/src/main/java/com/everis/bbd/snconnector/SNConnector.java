package com.everis.bbd.snconnector;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import org.json.JSONObject;

import twitter4j.Status;

/**
 * Abstract class for social networks connectors
 * @author Robert
 *
 */
public abstract class SNConnector 
{
	private List<Status> _results;
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
	
	public synchronized List<Status> getResults()
	{
		return _results;
	}
	
	public synchronized void appendResults(SynchronousQueue<JSONObject> newResults)
	{
		
	}
}
