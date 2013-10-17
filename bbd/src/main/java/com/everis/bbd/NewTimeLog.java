package com.everis.bbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.sql.Timestamp;

public class NewTimeLog 
{

	// Class parameters.
	private static final String DIRECTORY_ARG = "-d";
	private static final int START_ID = 1;
	private static final int END_ID = 2;

	// Class variables.
	private static BufferedWriter _out;

	private static void processFileLog(String inputFilePath) throws IOException
	{
		FileInputStream infstream = new FileInputStream(inputFilePath);
		DataInputStream in = new DataInputStream(infstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = br.readLine()) != null)
		{
			String[] splitLine = line.split(";");
			if (splitLine.length > 10)
			{
				_out.write(splitLine[0]+"\t"+splitLine[1]+"\t"+splitLine[2]+"\t"
								+splitLine[3]+"\t"+splitLine[4]+"\t"
								+splitLine[5]+"\t"+splitLine[6]+"\t"
								+splitLine[7]+"\t"+splitLine[8]+"\t"
								+splitLine[9]+"\t"+splitLine[10]+"\n");
			}
		}
		br.close();
	}
	public static void main(String[] args) 
	{
		// Starting timing.
		long startTime = System.nanoTime();

		// Checking arguments.
		if (args.length != 2 && args.length != 3)
		{
			System.err.println("Error: invalid number of parameters.");
			System.err.println("TimeLog receives three arguments: inputPath, outputPath and [-d] only if inputPath is directory.");
			return;
		}

		// Input path file/directory.
		String _inputPath = args[0];
		boolean is_directory = false;
		// Output path file.
		String _outputFilePath = args[1];

		if (args.length == 3)
		{
			if (args[2].compareTo(DIRECTORY_ARG) == 0)
			{
				is_directory = true;
			}
		}

		try 
		{
			// Creating output buffer.
			FileWriter outfstream = new FileWriter(_outputFilePath);
			_out = new BufferedWriter(outfstream);

			if (is_directory)
			{
				// Reading directory.
				File folder = new File(_inputPath);
				File[] listOfFiles = folder.listFiles();

				// For each file in the directory.
				for (int i = 0; i < listOfFiles.length; i++) 
				{
					if (listOfFiles[i].isFile()) 
					{
						processFileLog(listOfFiles[i].getAbsolutePath());
					}
				}
			}
			else
			{
				processFileLog(_inputPath);
			}

			// Closing output buffer.
			_out.close();
		} 
		catch (IOException e) 
		{
			System.err.println("Error: " + e.getMessage());
		}

		// Stop timing.
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Ellapsed time (in seconds): " + (double)(elapsedTime/1000000000));
	}
}