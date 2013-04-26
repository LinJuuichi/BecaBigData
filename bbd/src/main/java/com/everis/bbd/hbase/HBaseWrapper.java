package com.everis.bbd.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.everis.bbd.mahout.FrequentPatternMining;

/**
 * HBaseWrapper is a wrapper of HBase API which makes easier the calls to HBase.
 */
public class HBaseWrapper {
	
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(FrequentPatternMining.class.getName());
	
	/**
	 * In this list are stored all the pending inserts until the function flushInserts is 
	 * executed.
	 */
	private List<Put> _puts = new ArrayList<Put>();
	
	/**
	 * Table which we are working with. Creating HTable is a really expensive process
	 * and should be executed only once for table.
	 */
	private  HTable _table;
	
	/**
	 * Constructor.
	 */	
	public HBaseWrapper()
	{
		
	}
	
	/**
	 * Select the HBase table to work with.
	 * @param table The HBase table.
	 * @return True if the connector is correctly created. False otherwise.
	 */
	public boolean selectTable(String table)
	{
		Configuration conf = HBaseConfiguration.create();
		try {
			_table = new HTable(conf, table);
			_table.setAutoFlush(false);
		} catch (IOException e) {
			log.severe("Failed to create the HBase table connector.");
			return false;
		}
		return true;
	}
	
	/**
	 * Insert a new value in HBase. The values inserted won't be stored in HBase until the 
	 * execution of flushInserts method.
	 * @param key value of the row key identifier.
	 * @param family family column where insert the new value.
	 * @param qualifier qualifier.
	 * @param value value to be stored.
	 */
	public void insertValue(String key, String family, String qualifier, String value)
	{
		insertValue(key, family, qualifier, value, null);
	}
	
	/**
	 * Insert a new value in HBase. The values inserted won't be stored in HBase until the 
	 * execution of flushInserts method.
	 * @param key value of the row key identifier.
	 * @param family family column where insert the new value.
	 * @param qualifier qualifier.
	 * @param value value to be stored.
	 * @param ts timestamp of the new insertion.
	 */
	public void insertValue(String key, String family, String qualifier, String value, Long ts)  
	{
		byte[] keyB = Bytes.toBytes(key);
		Put p = new Put(keyB);
		
		byte[] familyB = Bytes.toBytes(family);
		byte[] qualifierB = Bytes.toBytes(qualifier);
		byte[] valueB = Bytes.toBytes(value);
		
		if(ts == null)
		{
			p.add(familyB, qualifierB, valueB);
		}
		else if(ts < 0)
		{
			log.warning("Trying to set a negative timestamp.");
			p.add(familyB, qualifierB, valueB);
		}
		else {
			p.add(familyB, qualifierB, ts.longValue(), valueB);
		}
		
		_puts.add(p);
	}
		

	/**
	 * Make all the current changes in HBase.
	 * @return True if all changes have been correctly made. False otherwise.
	 */
	public boolean flushInserts()
	{
		if(_table == null) 
		{
			log.severe("First select a correct table.");
			return false;
		}
		try {
			_table.put(_puts);
			_table.flushCommits();
		} catch (IOException e) {
			log.severe("Failed to flush the changes.");
			return false;
		}
		_puts.clear();
		return true;
	}
	

	/**
	 * Close the current HBase table connector.
	 * @return True if the close is successful. False otherwise.
	 */
	public boolean closeTable()
	{
		try {
			_table.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
		
			//seria una buena idea cerrar las HTable cuando ya no se usen con un close().
			//boolean has(byte[] family, byte[] qualifier)
			//boolean has(byte[] family, byte[] qualifier, long ts)
			//boolean has(byte[] family, byte[] qualifier, byte[] value)
			//boolean has(byte[] family, byte[] qualifier, long ts, byte[] value)
}
