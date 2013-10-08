package com.everis.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class LogFilter extends Configured implements Tool 
{
	
	@SuppressWarnings("rawtypes")
	public static class LogFilterKey implements WritableComparable 
	{
		private String method_name;
		private String timestamp;
		private String start_or_end;
		
		public LogFilterKey()
		{
			
		}
		
		public LogFilterKey(String method_name, String timestamp, String start_or_end)
		{
			this.method_name = method_name;
			this.timestamp = timestamp;
			this.start_or_end = start_or_end;
		}
		
		public String getMethodName()
		{
			return method_name;
		}
		
		public String getTimeStamp()
		{
			return timestamp;
		}
		
		@Override
		public void readFields(DataInput in) throws IOException 
		{
			timestamp = WritableUtils.readString(in);
			method_name = WritableUtils.readString(in);
			start_or_end = WritableUtils.readString(in);
		}

		@Override
		public void write(DataOutput out) throws IOException 
		{
			WritableUtils.writeString(out, timestamp);
			WritableUtils.writeString(out, method_name);
			WritableUtils.writeString(out, start_or_end);
		}

		@Override
		public int compareTo(Object o) {
			LogFilterKey lfk = (LogFilterKey)o;
			int result = method_name.compareTo(lfk.method_name);
			if(result == 0) result = timestamp.compareTo(lfk.timestamp);
			if(result == 0) result = start_or_end.compareTo(lfk.start_or_end)*-1;
			return result;
		}
		
		@Override 
		public String toString()
		{
			return method_name;
		}
	}

	public static class LogFilterValue implements Writable
	{
		private String timestamp;
		private String function;
		private String start_or_end;
		private String session_id;
		private String timestamp_start;
		private String difference;
		
		private static final String SEPARATOR = "\t";
		
		public LogFilterValue()
		{
			
		}
		
		public LogFilterValue(String timestamp, String function, String start_or_end, String session_id)
		{
			this.timestamp = timestamp;
			this.function = function;
			this.start_or_end = start_or_end;
			this.session_id = session_id;
		}
		
		public String getTimeStamp()
		{
			return timestamp;
		}
		
		public String getStartOrEnd()
		{
			return start_or_end;
		}
		
		public String getSessionId()
		{
			return session_id;
		}
		
		public void preWrite(String timestamp_start, String difference)
		{
			this.timestamp_start = timestamp_start;
			this.difference = difference;
		}
		
		@Override
		public String toString()
		{
			return(new StringBuilder()).append(timestamp_start).append(SEPARATOR)
					.append(timestamp).append(SEPARATOR)
					.append(function).append(SEPARATOR)
					.append(session_id).append(SEPARATOR)
					//.append(start_or_end).append(SEPARATOR)
					.append(difference).toString();
		}
		
		@Override
		public void readFields(DataInput in) throws IOException 
		{
			timestamp = WritableUtils.readString(in);
			function = WritableUtils.readString(in);
			session_id = WritableUtils.readString(in);
			start_or_end = WritableUtils.readString(in);
		}

		@Override
		public void write(DataOutput out) throws IOException 
		{
			WritableUtils.writeString(out, timestamp);
			WritableUtils.writeString(out, function);
			WritableUtils.writeString(out, session_id);
			WritableUtils.writeString(out, start_or_end);
		}
	}
	
	public static class LogFilterPartitioner extends Partitioner<LogFilterKey, LogFilterValue>
	{
		HashPartitioner<Text, NullWritable> hashPartitioner = new HashPartitioner<Text, NullWritable>();
		Text new_key = new Text();
		
		@Override
		public int getPartition(LogFilterKey key, LogFilterValue value, int numReduceTasks) {
			try
			{
				new_key.set(key.getMethodName());
				NullWritable nullValue = NullWritable.get();
				return hashPartitioner.getPartition(new_key,  nullValue,  numReduceTasks);
			}
			catch(Exception e)
			{
				return (int) (Math.random() * numReduceTasks);
			}
		}
	}
	
	public static class LogFilterGroupingComparator extends WritableComparator 
	{
		protected LogFilterGroupingComparator() 
		{
			super(LogFilterKey.class, true);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable w1, WritableComparable w2)
		{
			LogFilterKey key1 = (LogFilterKey) w1;
			LogFilterKey key2 = (LogFilterKey) w2;
			
			return key1.getMethodName().compareTo(key2.getMethodName());	
		}
		
	}
	
	public static class LogFilterMapper extends Mapper<LongWritable, Text, LogFilterKey, LogFilterValue> 
	{		
		@Override
		public void map(LongWritable key, Text value, Context context)
		{
			String line = value.toString();
			
			if(!line.matches(".*\\b(START|END)\\b.*")) return;
			
			try {		
				String[] aux_0 = line.split("(\\(|\\))");
				String[] aux_1 = aux_0[0].split(" ");
				String[] aux_2 = aux_0[1].split(" ");
				String[] aux_3 = aux_0[2].split(" ");
				
				String timestamp = aux_1[0] + " " + aux_1[1];
				String function = aux_1[aux_1.length - 1];
				String session_id = aux_2[0];
				String method_name = aux_2[2];
				for(int i = 3; i < aux_2.length; i++) 
					method_name = method_name + " " +  aux_2[i];
				String start_or_end = aux_3[1];
				
				LogFilterKey map_key = new LogFilterKey(method_name, timestamp, start_or_end);
				LogFilterValue map_value = new LogFilterValue(timestamp, function, start_or_end, session_id);
				
				context.write(map_key, map_value);		 
			}
			catch (Exception e) 
			{
				
			}
		}
	}
	
	public static class LogFilterReducer extends Reducer<LogFilterKey, LogFilterValue, LogFilterKey, LogFilterValue> 
	{
		HashMap<String, String> entries;
		
		public void reduce(LogFilterKey key, Iterable<LogFilterValue> values, Context context) throws IOException, InterruptedException
		{
			entries = new HashMap<String, String> ();
			
			Iterator<LogFilterValue> iter = values.iterator();
			while(iter.hasNext())
	        {
				LogFilterValue lfv = iter.next();

				if(lfv.getStartOrEnd().compareTo("START") == 0) entries.put(lfv.getSessionId(), lfv.getTimeStamp());
				else
				{
					String start_time = entries.get(lfv.getSessionId());
					String difference;
					if(start_time != null)
					{
						try
						{
							double diff = (Timestamp.valueOf(lfv.getTimeStamp().replace(',', '.')).getTime()) - (Timestamp.valueOf(start_time.replace(',', '.')).getTime());
							difference = String.valueOf(diff);
						}
						catch(Exception e)
						{
							start_time = "0";
							difference = "EXCEPTION: " + e.getMessage();
						}
					}
					else
					{
						start_time = lfv.getSessionId();
						difference = entries.keySet().toString();
						//start_time = "0";
						//difference = "END sin START????";
					}
					lfv.preWrite(start_time, difference);
		        	context.write(key, lfv);
				}
	        }
		}
	}
	
	static int printUsage()
	{
		System.out.println("logfilter [-r <reducers>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}
	
	@Override
	public int run(String[] args) throws Exception 
	{
		Job job = Job.getInstance(getConf(), "logfilter");
		job.setJarByClass(LogFilter.class);

		job.setOutputKeyClass(LogFilterKey.class);
		job.setOutputValueClass(LogFilterValue.class);
		
		job.setMapperClass(LogFilterMapper.class);
		//job.setCombinerClass(LogFilterReducer.class);
		job.setReducerClass(LogFilterReducer.class);
		
		job.setPartitionerClass(LogFilterPartitioner.class);
		job.setGroupingComparatorClass(LogFilterGroupingComparator.class);
		
		List<String> other_args = new ArrayList<String>();
		for(int i = 0; i < args.length; i++)
		{
			try
			{
				if("-r".equals(args[i])) job.setNumReduceTasks(Integer.parseInt(args[++i]));
				else other_args.add(args[i]);
			}
			catch(NumberFormatException e)
			{
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				return printUsage();
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("ERROR: Required parameter missing from " + args[i - 1]);
				return printUsage();
			}
		}
		
		if(other_args.size() != 2)
		{
			System.out.println("ERROR: Wrong number of parameters: " + other_args.size() +
					" instead of 2.");
			return printUsage();
		}
	    
		FileInputFormat.setInputPaths(job, other_args.get(0));
		FileOutputFormat.setOutputPath(job, new Path(other_args.get(1)));
		
	    return (job.waitForCompletion(true) ? 0 : -1);
	}
	
	public static void main(String[] args) throws Exception 
	{
		int res = ToolRunner.run(new Configuration(), new LogFilter(), args);
		System.exit(res);
	}
}
