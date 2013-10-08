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

public class TimeLog 
{
	private static class LogEntry
	{
		public String _date;
		public String _time;
		public String _info;
		public String _session;
		public String _function;
	}

	// Entry type for log lines.
	private static final String START_TYPE = "START";
	private static final String END_TYPE = "END";

	// Class parameters.
	private static final String DIRECTORY_ARG = "-d";
	private static final int START_ID = 1;
	private static final int END_ID = 2;

	// Class variables.
	private static BufferedWriter _out;

	private static void writeEntryTiming(LogEntry start, LogEntry end) throws IOException
	{
		String startTime = start._date+" "+start._time.replace(',','.');
		String endTime = end._date+" "+end._time.replace(',','.');
		double time = (Timestamp.valueOf(endTime).getTime()) - (Timestamp.valueOf(startTime).getTime());

		_out.write(start._function+"\t"+start._session+"\t"+start._info+"\t"
				+start._date+"\t"+start._time+"\t"
				+end._date+"\t"+end._time+"\t"
				+String.valueOf(time)+"\n");
	}

	private static boolean entriesCheck(LogEntry start, LogEntry end)
	{
		if (start._session.compareTo(end._session) != 0)
		{
			return false;
		}
		if (start._function.compareTo(end._function) != 0)
		{
			return false;
		}
		return true;
	}

	private static LogEntry lineToLogEntry(String line, int type)
	{
		LogEntry entry = new LogEntry();
		String atDivision[] = line.split("@");
		String doubleDotDivision[] = atDivision[1].split(":");
		entry._function = doubleDotDivision[0].replace(")","");
		String spaceDivision[] = atDivision[0].split(" ");
		entry._date = spaceDivision[0];
		entry._time = spaceDivision[1];
		entry._info = spaceDivision[7];
		entry._session = spaceDivision[8].replace("(", "");
		return entry;
	}

	private static void processFileLog(String inputFilePath) throws IOException
	{
		FileInputStream infstream = new FileInputStream(inputFilePath);
		DataInputStream in = new DataInputStream(infstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		Vector<LogEntry> startEntries = new Vector<LogEntry>(0);
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.matches(".*\\b("+START_TYPE+")\\b.*"))
			{
				startEntries.add(lineToLogEntry(line,START_ID));
			}
			else if (line.matches(".*\\b("+END_TYPE+")\\b.*"))
			{
				LogEntry endEntry = lineToLogEntry(line,END_ID);
				LogEntry startEntry;
				for (int i = startEntries.size()-1; i >= 0; --i)
				{
					startEntry = startEntries.get(i);
					if (entriesCheck(startEntry, endEntry))
					{
						startEntries.remove(i);
						writeEntryTiming(startEntry, endEntry);
						break;
					}
				}
			}
		}

		startEntries.clear();
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