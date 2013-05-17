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
	private static String INPUT_FILE = "events.txt";
	
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
		Map<String, Integer> dictionaries = new HashMap<String, Integer>();
		dictionaries.put("word", DictionaryFactory.WORD_DICTIONARY);
		dictionaries.put("words", DictionaryFactory.WORD_LIST_DICTIONARY);
		dictionaries.put("blacklist", DictionaryFactory.BLACK_LIST_DICTIONARY);
		if (!processor.readDictionaries(dictionaries))
		{
			System.out.println("Could not read dictionaries.");
			return;
		}
		
		String line;
		try 
		{
			while((line = _reader.readLine()) != null)
			{
				String text = line.split("\t")[1];
				text = processor.preProcess(text,true);
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
