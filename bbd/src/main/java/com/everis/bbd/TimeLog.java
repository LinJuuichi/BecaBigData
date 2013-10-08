package com.everis.bbd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.sql.Timestamp;

public class TimeLog 
{
	private static final int DATE_INDEX = 0;
	private static final int HOUR_INDEX = 1;
	private static final int INFO_INDEX = 2;
	private static final int SESION_INDEX = 3;
	private static final int FUNCTION_INDEX = 4;
	private static final int TYPE_INDEX = 5;
	
	private static final int LIMIT_OF_READ_LINES = 50000;
	
	private static final String START_TYPE = "START";
	private static final String END_TYPE = "END";
	
	private static Vector<String[]> log = new Vector<String[]>(0);
	private static BufferedWriter out;
	
	
	public static boolean processLog()
	{
		for(int i = 0; i < log.size()-1; ++i)
		{
			String[] entry = log.get(i);
			if (entry[TYPE_INDEX].compareTo(START_TYPE) == 0)
			{
				for (int j = i+1; j < log.size(); ++j)
				{
					String[] nextEntry = log.get(j);
					if (entry[FUNCTION_INDEX].compareTo(nextEntry[FUNCTION_INDEX]) == 0
							&& entry[SESION_INDEX].compareTo(nextEntry[SESION_INDEX]) == 0)
					{
						if (nextEntry[TYPE_INDEX].compareTo(START_TYPE) == 0)
						{
							break;
						}
						else if (nextEntry[TYPE_INDEX].compareTo(END_TYPE) == 0)
						{
							//double time = Double.valueOf(nextEntry[HOUR_INDEX].replace(',','.')) - Double.valueOf(entry[HOUR_INDEX].replace(',','.'));
							String time1 = entry[DATE_INDEX]+" "+entry[HOUR_INDEX].replace(',','.');
							String time2 = nextEntry[DATE_INDEX]+" "+nextEntry[HOUR_INDEX].replace(',','.');
							double time = (Timestamp.valueOf(time2).getTime()) - (Timestamp.valueOf(time1).getTime());

							
							try
							{
								out.write(entry[FUNCTION_INDEX]+"\t"+entry[INFO_INDEX]
												+"\t"+entry[SESION_INDEX]+"\t"+entry[DATE_INDEX]+"\t"+entry[HOUR_INDEX]
												+"\t"+nextEntry[DATE_INDEX]+"\t"+nextEntry[HOUR_INDEX]+"\t"
												+String.valueOf(time)+"\n");
							} 
							catch (IOException e) 
							{
								System.err.println("Error: " + e.getMessage());
								return false;
							}
						}
					}
				}
			}
		}
		log.clear();
		return true;
	}
	
	public static void main(String[] args) 
	{
		
		if (args.length != 2)
		{
			return;
		}
		String _inputFilePath = args[0];
		String _outputFilePath = args[1];
		try
		{
			FileInputStream infstream = new FileInputStream(_inputFilePath);
			DataInputStream in = new DataInputStream(infstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			FileWriter outfstream = new FileWriter(_outputFilePath);
			out = new BufferedWriter(outfstream);
			
			long startTime = System.nanoTime();
			
			String line;
			int readLines = 0;
			while ((line = br.readLine()) != null)
			{
				String[] parsedLine = line.split(" ");
				log.add(parsedLine);
				readLines++;
				if (readLines > LIMIT_OF_READ_LINES && parsedLine[TYPE_INDEX].compareTo(START_TYPE) == 0)
				{
					int lol = 7;
					lol = lol+3;
					if (!processLog())
					{
						br.close();
						in.close();
						out.close();
						return;
					}
					readLines = 0;
					log.add(parsedLine);
					readLines++;
				}
			}
			if (!processLog())
			{
				in.close();
				return ;
			}
			in.close();
			out.close();
			long elapsedTime = System.nanoTime() - startTime;
			
			System.out.println("Ellapsed time (in seconds): " + (double)(elapsedTime/1000000000));
			
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			return;
		}
	}
}