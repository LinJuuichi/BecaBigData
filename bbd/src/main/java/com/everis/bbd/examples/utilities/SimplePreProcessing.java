package com.everis.bbd.examples.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.everis.bbd.utilities.TextProcessor;
import com.everis.bbd.utilities.dictionary.DictionaryFactory;

/**
 * Example for TextProcessor.
 */
public class SimplePreProcessing 
{
	/**
	 * Default path to the input file.
	 */
	private static String INPUT_FILE = "C:\\Users\\rserratm\\Desktop\\tweets.txt";
	
	/**
	 * Default path to the output file.
	 */
	private static String OUTPUT_FILE = "processed-events.txt";

	/**
	 * Reader.
	 */
	private static BufferedReader _reader;
	
	/**
	 * Writer.
	 */
	private static PrintWriter _writer;
	
	/**
	 * Connects reader and writer.
	 * @return if both reader and writer are connected.
	 */
	private static boolean connect()
	{
		try 
		{
			_reader = new BufferedReader(new FileReader(INPUT_FILE));
			_writer = new PrintWriter(OUTPUT_FILE, "UTF-8");
		} 
		catch (FileNotFoundException e) 
		{
			return false;
		} 
		catch (UnsupportedEncodingException e) 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Disconnects reader and writer.
	 */
	private static void disconnect()
	{
		_writer.close();
		try 
		{
			_reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args arguments.
	 */
	public static void main(String[] args) 
	{
		if(!connect())
		{
			System.out.println("Could not open files.");
			return;
		}
		
		TextProcessor processor = new TextProcessor();

		processor.readDictionary("nourl", DictionaryFactory.NO_URL_DICTIONARY,0,false);
		processor.readDictionary("blacklist", DictionaryFactory.BLACK_LIST_DICTIONARY,1,false);
		processor.readDictionary("word", DictionaryFactory.WORD_DICTIONARY,2,false);
		processor.readDictionary("words", DictionaryFactory.WORD_LIST_DICTIONARY,3,false);

		String line;
		int[] order = {0,1,2,3};
		try 
		{
			while((line = _reader.readLine()) != null)
			{
				String text = processor.preProcess(line, 2, order);
				_writer.println(text);
				System.out.println(text);
			}
			disconnect();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
