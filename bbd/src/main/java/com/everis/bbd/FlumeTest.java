package com.everis.bbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
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
	public static String _f1024 = "C:\\Users\\jsabatep\\Desktop\\ficheros\\f1024.txt";
	public static String _f5120 = "C:\\Users\\jsabatep\\Desktop\\ficheros\\f5120.txt";
	public static String _f25600 = "C:\\Users\\jsabatep\\Desktop\\ficheros\\f25600.txt";
	
	// Path of the file to create and send via Flume.
	public static String _filePath = _f1024;

	// Number of tweets that are going to be created.
	public static int _numberTweetsToCreate = 4000000;

	// Address (IP:PORT) of the Flume agent.
	public static String _agentIP = "7.110.8.20";  // Node 1
	public static int _agentPort = 11011;

	// Directory where the Flume agent is going to create the file received.
	private static String _outputDirectory = "/tmp/testflume/";

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

	/**
	 * Sends a file (line by line) via a RPC client to the agent configured.
	 * 
	 * @return tweets sent successfully.
	 */
	public static boolean readAndSendTweets()
	{		
		// Configuring client.
		RpcClientFacade client = RpcClientFacadeFactory.getClient(RpcClientFacadeFactory.RPC_CLIENT_FACADE);
		if (!client.connect(_agentIP, _agentPort))
		{
			return false;
		}
		client.setOutputDirectory(_outputDirectory );
		
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
				client.sendData(tweet, 0);
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
