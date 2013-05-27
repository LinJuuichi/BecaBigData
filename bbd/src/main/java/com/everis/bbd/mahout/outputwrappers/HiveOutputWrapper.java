package com.everis.bbd.mahout.outputwrappers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 *
 */
class HiveOutputWrapper extends OutputWrapper {

	/**
	 * 
	 */
	public static final String OUTPUTFILE = "/output.txt";
	/**
	 * 
	 */
	private BufferedWriter _br;
	
	/**
	 * 
	 */
	public HiveOutputWrapper()
	{
		_name = "Hive";
	}
	
	@Override
	public void before() throws OutputWrapperException {
        try {
        	Configuration conf = new Configuration();
        	conf.addResource(new Path("/usr/lib/hadoop/conf/core-site.xml"));
            conf.addResource(new Path("/usr/lib/hadoop/conf/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);
			/*if (fs.exists(pt))
			{
				//remove the file
				fs.delete(pt, true);
			}*/
			Path pt = new Path(OUTPUTFILE);
			_br = new BufferedWriter(new OutputStreamWriter(fs.create(pt, true)));
		} catch (IOException e) {
			throw new OutputWrapperException(e.getMessage());
		}
	}

	@Override
	public void after() throws OutputWrapperException {
		try {
			_br.close();
		} catch (IOException e) {
			throw new OutputWrapperException(e.getMessage());
		}
	}

	@Override
	public void saveFPMresult(String date, String key, String pattern, Long repetitions) throws OutputWrapperException {                       
        String line;
        line = date + '\t' + key + '\t' + pattern + '\t' + repetitions.toString() + '\n';
        try {
			_br.write(line);
		} catch (IOException e) {
			throw new OutputWrapperException(e.getMessage());
		}
    }

}
