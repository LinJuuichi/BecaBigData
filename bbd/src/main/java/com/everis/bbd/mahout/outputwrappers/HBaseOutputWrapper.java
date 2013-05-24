package com.everis.bbd.mahout.outputwrappers;

import com.everis.bbd.hbase.HBaseWrapper;

/**
 * 
 *
 */
public class HBaseOutputWrapper extends OutputWrapper {

	/**
	 * 
	 */
	private HBaseWrapper _hbw;
	
	/**
	 * 
	 */
	public HBaseOutputWrapper()
	{
		_name = "HBase";
	}
	
	@Override
	public void saveFPMresult(String date, String key, String pattern, Long repetitions) throws OutputWrapperException 
	{
		_hbw.insertValue(date, "patterns", key + "." + pattern, repetitions.toString());
	}

	@Override
	public void before() throws OutputWrapperException {
		_hbw = new HBaseWrapper();
    	_hbw.selectTable("tweets");
	}

	@Override
	public void after() throws OutputWrapperException {
    	_hbw.flushInserts();
    	_hbw.closeTable();
	}

}
