package com.everis.bbd.snconnector;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import org.json.JSONObject;

import twitter4j.Status;

/**
 * Abstract class for social networks connectors.
 *
 */
public abstract class SNConnector 
{
	private List<Status> _results;
	private HashMap<String, String> _configuration;
	
	/**
	 * 
	 */
	public SNConnector()
	{
		_results = null;
		_configuration = null;
	}
	
	/**
	 * Configures the connector with the file.
	 * @param configurationFile the configuration of the connector.
	 */
	public void configure(String configurationFile)
	{
		// #TODO
		//_configuration = configuration;
	}
	
	/**
	 * Return the results of the query.
	 * @return the results of the last executed query.
	 */
	public synchronized List<Status> getResults()
	{
		return _results;
	}
	
	/**
	 * @param newResults
	 */
	public synchronized void appendResults(SynchronousQueue<JSONObject> newResults)
	{
		
	}
}
