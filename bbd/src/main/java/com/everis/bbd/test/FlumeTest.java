package com.everis.bbd.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Properties;

import com.everis.bbd.flume.RpcClientFacade;
import com.everis.bbd.flume.RpcClientFacadeFactory;
import com.everis.bbd.utilities.TweetGenerator;

/**
 * Generates a file with a large number of tweets generated randomly.
 * Reads the file line by line and sends it via RpcClient.
 */
public class FlumeTest 
{
	// Paths of the files.
	private static String _f1024 = "C:\\Users\\rserratm\\Desktop\\ficheros\\f1024.txt";
	private static String _f5120 = "C:\\Users\\rserratm\\Desktop\\ficheros\\f5120.txt";
	private static String _f25600 = "C:\\Users\\rserratm\\Desktop\\ficheros\\f25600.txt";
	
	// Path of the file to create and send via Flume.
	private static String _filePath = _f1024;

	// Number of tweets that are going to be created.
	private static int _numberTweetsToCreate = 100;

	// Address (IP:PORT) of the Flume agent.
	private static String _agentIPNode1 = "7.110.8.20";  // Node 1
	private static int _agentPortNode1 = 11011;
	
	private static String _agentIPNode2 = "7.110.8.21";  // Node 2
	private static int _agentPortNode2 = 11011;
	
	private static String _agentIPNode3 = "7.110.8.22";  // Node 3
	private static int _agentPortNode3 = 11011;
	
	private static String _agentIPNode4 = "7.110.8.23";  // Node 4
	private static int _agentPortNode4 = 11011;
	
	private static int MAX_ATTEMPS = 2;

	// Directory where the Flume agent is going to create the file received.
	private static String _outputDirectory = "/tmp/testflume/";
	
	private static RpcClientFacade _client;

	/**
	 * Creates a file with _numberTweetsToCreate random tweets.
	 * 
	 * @return file created successfully
	 */
	public static boolean createTweetsFile()
	{
		try
		{
			FileWriter fstream = new FileWriter(_filePath);
			BufferedWriter out = new BufferedWriter(fstream);
			for (long i = 0; i < _numberTweetsToCreate; i++)
			{
				out.write(TweetGenerator.generateRandomTweet(i).toString()+"\n");
			}
			out.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean createFailOverClient()
	{
		// Setup properties for the failover
		Properties props = new Properties();
		props.put("client.type", "default_failover");

		// List of hosts (space-separated list of user-chosen host aliases)
		props.put("hosts", "h2 h3");

		// host/port pair for each host alias
		//String host1 = _agentIPNode1+":"+_agentPortNode1;
		String host2 = _agentIPNode2+":"+_agentPortNode2;
		String host3 = _agentIPNode3+":"+_agentPortNode3;
		//String host4 = _agentIPNode4+":"+_agentPortNode4;
		//props.put("hosts.h1", host1);
		props.put("hosts.h2", host2);
		props.put("hosts.h3", host3);
		props.put("max-attempts", MAX_ATTEMPS);
		//props.put("hosts.h3", host3);
		//props.put("hosts.h4", host4);
		
		// create the client with failover properties
		_client = RpcClientFacadeFactory.getClientWithProperties(RpcClientFacadeFactory.RPC_CLIENT_FACADE, props);
		_client.setOutputDirectory(_outputDirectory );
		return true;
	}
	
	public static boolean createClient()
	{
		_client = RpcClientFacadeFactory.getClient(RpcClientFacadeFactory.RPC_CLIENT_FACADE);
		if (!_client.connect(_agentIPNode1, _agentPortNode1))
		{
			return false;
		}
		_client.setOutputDirectory(_outputDirectory );
		return true;
	}

	/**
	 * Sends a file (line by line) via a RPC client to the agent configured.
	 * 
	 * @return tweets sent successfully.
	 */
	public static boolean readAndSendTweets()
	{		
		//createClient();
		createFailOverClient();
	
		try
		{
			//Opening file to send.
			FileInputStream fstream = new FileInputStream(_filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			// Start timing
			long startTime = System.nanoTime();
			
			// Read file line by line
			String tweet;
			while ((tweet = br.readLine()) != null)
			{
				_client.sendData(tweet, 0);
			}
			
			// Finish timing.
			long elapsedTime = System.nanoTime() - startTime;
			
			System.out.println("Ellapsed time: " + elapsedTime);
			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * @param args none.
	 */
	public static void main(String[] args) 
	{
		//createTweetsFile();
		readAndSendTweets();
	}
}
