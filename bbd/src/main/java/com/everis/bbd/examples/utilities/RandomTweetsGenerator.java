package com.everis.bbd.examples.utilities;

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
public class RandomTweetsGenerator 
{

	public static String _filePath = "/Users/rserratm/Downloads/proba.txt";

	public static int _numberTweets = 100;

	public static String _clientIP = "192.168.216.136";
	public static int _clientPort = 11011;

	private static String _outputDirectory = "/proba/";

	/**
	 * @return file created successfully
	 */
	public static boolean createTweetsFile()
	{
		try
		{
			FileWriter fstream = new FileWriter(_filePath);
			BufferedWriter out = new BufferedWriter(fstream);
			for (long i = 0; i < _numberTweets; i++)
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
	 * @return tweets sent successfully.
	 */
	public static boolean readAndSendTweets()
	{
		// Configuring client.
		RpcClientFacade client = RpcClientFacadeFactory.getClient(RpcClientFacadeFactory.RPC_CLIENT_FACADE);
		if (!client.connect(_clientIP, _clientPort))
		{
			return false;
		}
		client.setOutputDirectory(_outputDirectory );
		try
		{
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
			
			// Finnish timing.
			long elapsedTime = System.nanoTime() - startTime;
			
			System.out.println("Ellapsed time: "+elapsedTime);
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
		createTweetsFile();
		readAndSendTweets();
	}
}
