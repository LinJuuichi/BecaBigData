package com.everis.bbd.mahout;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.Pair;

public class FrequentPatternMining 
{

	private String _inputPath;
	
	private String _outputPath;
	
	private String _encoding = "UTF-8";
	
	private String _splitterPattern = " ";
	
	private Collection<?> _returnableFeatures; 
	
    public static void main( String[] args )
    {
       FrequentPatternMining fpm = new FrequentPatternMining();
       fpm.makeConfiguration();
       fpm.runFPGrowth();
    }
	
    private void makeConfiguration() 
    {
    	 ConfigurationReader cr = new ConfigurationReader(path);
         cr.readConfigurationFile();
         
         _inputPath = cr.getValue.get("inputPath");
         _outputPath = cr.getValue.get("outputPath");
         _encoding = cr.getValue("encoding", _encoding);
         _splitterPattern = cr.getValue("splitterPattern", _splitterPattern);
         _returnableFeatures = cr.getValues("returnableFeatures");
    }     
    
     
    
    private void runFPGrowth() 
    {
    	Path input = new Path(_inputPath);
    	Path output = new Path(_outputPath);
    	FSDataInputStream inputStream = null;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(output.toUri(), conf);
        Charset encoding = Charset.forName(_encoding);
    	Iterator<Pair<List<?>, Long>> transactionStream;
    	
        new StringRecordIterator(new FileLineIterable(inputStream, encoding, false), pattern),

        
        
        
        
        int maxHeapSize = Integer.valueOf(params.get("maxHeapSize", "50"));
        int minSupport = Integer.valueOf(params.get("minSupport", "3"));
        
        
        FileSystem fs = FileSystem.get(output.toUri(), conf);
   }
    }
    
}

