package com.everis.bbd.mahout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.Parameters;
import org.apache.mahout.fpm.pfpgrowth.PFPGrowth;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;

import com.everis.bbd.hbase.HBaseWrapper;
import com.everis.bbd.utilities.ConfigurationReader;

/**
 * This class makes easier the calls to frequent pattern mining provided by Mahout. It calculates 
 * frequent patterns of a given file where transactions are stored in different lines.
 *
 */
public class FrequentPatternMining 
{
	
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(FrequentPatternMining.class.getName());
	
	/**
	 * Name of the configuration file.
	 */
	private static String CONFIGURATION_PATH = "mahout_fpm.properties";
	
	
	/**
	 * Path of the input file.
	 */
	private String _inputPath;
	
	/**
	 * Path of the output directory.
	 */
	private String _outputPath;
	
	
	/**
	 * Encoding of the characters.
	 */
	private String _encoding = "UTF-8";
	
	/**
	 * Pattern to split features of a transaction. A transaction consist in a set of features, 
	 * for example each tweet. A feature is a single component for which patterns are mined, for
	 * example each word in a tweet.
	 */
	private String _splitterPattern = " ";
	
	/**
	 * Minimum appearance count that a feature has to have to enter in the processing stage.
	 */
	private String _minSupport = "3";
	
	/**
	 * Maximum number of patterns found for each feature. Only _maxHeapSize first most closed 
	 * patterns will be shown for each feature.
	 */
	private String _maxHeapSize = "50";
	
	/**
	 * Key value to identify the row where the results are inserted in HBase.
	 */
	private String _key;
	
	/**
	 * A list with the keys whose results must be saved. An empty list will save all the 
	 * results calculated.
	 */
	private Collection<String> _returnableFeatures; 
	
	/**
	 * A list containing all the results calculated.
	 */
	private List<Pair<String,TopKStringPatterns>> _results;
	
    /**
     * Execute all the process of frequent pattern mining.
     * @param args Not used.
     * @throws Exception If an exception occurs during a Mahout process.
     */
    public static void main( String[] args ) throws Exception
    {
       FrequentPatternMining fpm = new FrequentPatternMining();
       fpm.makeConfiguration();
       fpm.startPFPGrowth();
       fpm.processResults();
       fpm.writeResults();
    }
	
    /**
     * Constructor of the class. Key identifier is set by default to yesterday.
     */
    public FrequentPatternMining() {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);   
    	Date date = cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    	_key = sdf.format(date); 
    }
    
    /**
     * Reads the configuration from the file.
     */
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
         _key = cr.getValue("key", _key);
    }    
    
    /**
     * Execute the PFPGrowth algorithm provided by Mahout.
     * @throws Exception If an exception occurs during Mahout runPFPGrowth process.
     */
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
    
    /**
     * Process the result obtained to keep only the desired features.
     * @throws IOException If an exception occurs during Mahout readFrequentPatterns process.
     */
    private void processResults() throws IOException
    {
    	Parameters params = new Parameters();
    	Path output = new Path(_outputPath);
    	params.set(PFPGrowth.OUTPUT, output.toString());
    	
    	_results = PFPGrowth.readFrequentPattern(params);
    	
    	if(!_returnableFeatures.isEmpty())
    	{
	    	Iterator<Pair<String,TopKStringPatterns>> iter = _results.iterator();
	    	while(iter.hasNext())
	    	{
	    		Pair<String,TopKStringPatterns> p = iter.next();
	    		boolean exists = false;
	    		for(String s : _returnableFeatures) 
	    		{
	    			if(p.getFirst().equalsIgnoreCase(s)) 
	    			{
	    				exists = true;
	    				break;
	    			}
	    		}
	    		if(!exists) iter.remove();
	    	}
    	}
    } 
    
    /**
     * Write the obtained results in HBase.
     * @throws IOException 
     */
    private void writeResults() throws IOException {
    
    	log.info("Total keys obtained: " + _results.size());
    	log.info(_results.toString());
    	
    	HBaseWrapper hbw = new HBaseWrapper();
    	
    	for(Pair<String,TopKStringPatterns> p : _results)
    	{
    		String key = p.getFirst();
    		List<Pair<List<String>, Long>> patterns = p.getSecond().getPatterns();
    		
    		for(Pair<List<String>, Long> pattern : patterns) 
    		{
    			String qualifier = key;
    			Long value = pattern.getSecond();
    			
    			List<String> features = pattern.getFirst();
    			for(String feature : features)
    			{
    				qualifier = qualifier + "." + feature;
    			}
    			
        		hbw.insertValue("tweets", _key, "patterns", qualifier, value.toString());
    		}
    	}
    	
    	hbw.loadInserts();
    }
    
}

