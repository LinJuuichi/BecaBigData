package com.everis.bbd.mapreduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.everis.bbd.utilities.TextProcessor;
import com.everis.bbd.utilities.dictionary.DictionaryFactory;

/**
 *
 *
 */
public class Preprocessing extends Configured implements Tool 
{
	
	/**
	 * 
	 *
	 */
	public static class PreprocessingMapper extends Mapper <LongWritable, Text, Text, NullWritable> 
	{
		/**
		 * 
		 */
		private static TextProcessor _tp;
		private static int[] order = {0, 1, 2, 3};
	    
		@Override
	    protected void setup(Context context) throws IOException 
	    {
	    	_tp = new TextProcessor();
	    	_tp.readDictionary("nourl", DictionaryFactory.NO_URL_DICTIONARY,0,false);
			_tp.readDictionary("blacklist", DictionaryFactory.BLACK_LIST_DICTIONARY,1,false);
			_tp.readDictionary("word", DictionaryFactory.WORD_DICTIONARY,2,false);
			_tp.readDictionary("words", DictionaryFactory.WORD_LIST_DICTIONARY,3,false);
	    }

		@Override
	    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	    {
	        Text processed = new Text();
	        String line = value.toString();
	        processed.set(_tp.preProcess(line, 2, order));
	        context.write(processed, NullWritable.get());
	    }
	}

	public int run(String[] args) throws Exception 
	{
		Job job = new Job(super.getConf());

	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(NullWritable.class);
		
		job.setMapperClass(PreprocessingMapper.class);
		job.setNumReduceTasks(0);
	    
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);

	    FileInputFormat.setInputPaths(job, new Path("hdfs://cdh4-node1/tmp/fpm"));
	    FileOutputFormat.setOutputPath(job, new Path("hdfs://cdh4-node1/tmp/fpm_filtered"));

	    job.setJarByClass(Preprocessing.class);

	    return job.waitForCompletion(true)? 1: 0;
	}
	
	/**
	 * 
	 * @param args Arguments
	 * @return 0 if everything is OK.
	 */
	public static int main(String[] args) 
	{
		//FileUtils.deleteDirectory(new File("/user/training/mapred/textout"));
		Configuration configuration = new Configuration();
		//String[] otherArgs;
		int res = 0;
		try {
			//otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
			res = ToolRunner.run(configuration, new Preprocessing(), args);
		} catch (Exception e) {
			
		}
	    return res;
	}
}