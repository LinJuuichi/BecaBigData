package com.everis.bbd.mahout;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.PFPGrowth;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import com.everis.bbd.utilities.ConfigurationReader;

/**
 * @author jsabatep
 *
 */
public class FrequentPatternMining 
{
	
	private static Logger log = Logger.getLogger(FrequentPatternMining.class.getName());
	
	private static String CONFIGURATION_PATH = "config/mahoutFPM.properties";
	
	private String _inputPath;
	
	private String _outputPath;
	
	private String _encoding = "UTF-8";
	
	private String _splitterPattern = " ";
	
	private String _minSupport = "3";
	
	private String _maxHeapSize = "50";
	
	private Collection<String> _returnableFeatures; 
	
	private List<Pair<String,TopKStringPatterns>> _results;
	
    /**
     * @param args
     * @throws Exception 
     */
    public static void main( String[] args ) throws Exception
    {
       FrequentPatternMining fpm = new FrequentPatternMining();
       fpm.makeConfiguration();
       fpm.startPFPGrowth();
       fpm.processResults();
       fpm.writeResults();
    }
	
    private void makeConfiguration() 
    {
    	 ConfigurationReader cr = new ConfigurationReader(CONFIGURATION_PATH);
         cr.readConfigurationFile();
         
         _inputPath = cr.getValue("inputPath", "");
         _outputPath = cr.getValue("outputPath", "");
         _encoding = cr.getValue("encoding", _encoding);
         _splitterPattern = cr.getValue("splitterPattern", _splitterPattern);
         _returnableFeatures = cr.getValues("returnableFeatures");
         _minSupport = cr.getValue("minSupport", _minSupport);
         _maxHeapSize = cr.getValue("maxPatternSupport", _maxHeapSize);
    }    
    
    private void startPFPGrowth() throws Exception 
    {
    	Parameters params = new Parameters();
    	
    	params.set("minSupport", _minSupport);
    	params.set("maxHeapSize", _maxHeapSize);
    	params.set("splitPattern", _splitterPattern);
    	params.set("encoding", _encoding);
    
    	Path input = new Path(_inputPath);
    	Path output = new Path(_outputPath);
    	
    	params.set("input", input.toString());
        params.set("output", output.toString());
    	
    	Configuration conf = new Configuration();
    	HadoopUtil.delete(conf, output);
    	
    	PFPGrowth.runPFPGrowth(params);
    }
    
    private void processResults() throws IOException
    {
    	Parameters params = new Parameters();
    	Path output = new Path(_outputPath);
    	params.set(PFPGrowth.OUTPUT, output.toString());
    	
    	_results = PFPGrowth.readFrequentPattern(params);
    	
    	for(Pair<String,TopKStringPatterns> p : _results)
    	{
    		boolean exists = false;
    		for(String s : _returnableFeatures) 
    		{
    			if(p.getFirst().equalsIgnoreCase(s)) 
    			{
    				exists = true;
    				break;
    			}
    		}
    		if(!exists) _results.remove(p);
    	}
    } 
    
    private void writeResults() {
    	for(Pair<String,TopKStringPatterns> p : _results)
    	{
    		log.info(p.getFirst() + " " + p.swap().toString());
    	}
    }
    
}

