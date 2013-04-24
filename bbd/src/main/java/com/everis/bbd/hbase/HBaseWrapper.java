package com.everis.bbd.hbase;

import java.io.IOException;
import java.util.HashMap;
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
	 * Tables which we are working with. Creating HTable is a really expensive process
	 * and should be executed only once for table.
	 */
	private static HashMap<String, HTable> _tables = new HashMap<String, HTable>();
	
	/**
	 * Rows which we are inserting into a specific table. The values inserted won't be stored 
	 * in HBase until the execution of loadInserts method.
	 */
	private HashMap<String, HashMap<String, Put>> _puts = new HashMap<String, HashMap<String, Put>>();
	
	/**
	 * It returns the HTable with the name table or will create a new one if it doesn't exists.
	 * Creating HTable is a really expensive process and should be executed only once for table.
	 * @param table name of the HBase table.
	 * @return HBase table with the specified name.
	 * @throws IOException Exception during the creation of HTable.
	 */
	private static HTable searchTable(String table) throws IOException 
	{
		if(_tables.containsKey(table)) return _tables.get(table);
		else 
		{
			Configuration conf = HBaseConfiguration.create();
			HTable t = new HTable(conf, table);
			_tables.put(table, t);
			return t;
		}
	}
	
	/**
	 * It returns the present row where we are inserting values.
	 * @param table name of the HBase table.
	 * @param key key value of the row.
	 * @return Put representing the row which we are inserting values.
	 */
	private Put searchPut(String table, String key) 
	{
		if(!_puts.containsKey(table)) _puts.put(table, new HashMap<String, Put>());
		if(_puts.get(table).containsKey(key)) return _puts.get(table).get(key);
		else 
		{
			byte[] keyB = Bytes.toBytes(key);
			Put p = new Put(keyB);
			_puts.get(table).put(key, p);
			return p;
		}
	}
	
	/**
	 * Insert a new value in HBase. The values inserted won't be stored in HBase until the 
	 * execution of loadInserts method.
	 * @param table name of the HBase table.
	 * @param key value of the row.
	 * @param family name of the family column where insert the new value.
	 * @param qualifier name of the qualifier.
	 * @param value value to be stored.
	 */
	public void insertValue(String table, String key, String family, String qualifier, String value)
	{
		insertValue(table, key, family, qualifier, value, null);
	}
	
	/**
	 * Insert a new value in HBase. The values inserted won't be stored in HBase until the 
	 * execution of loadInserts method.
	 * @param table name of the HBase table.
	 * @param key value of the row.
	 * @param family name of the family column where insert the new value.
	 * @param qualifier name of the qualifier.
	 * @param value value to be stored.
	 * @param ts timestamp of the new insertion.
	 */
	public void insertValue(String table, String key, String family, String qualifier, String value, Long ts)  
	{
		Put p = searchPut(table, key);
		
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
	}
		
	/**
	 * Make all the current changes in HBase.
	 * @throws IOException Exception during the creation of the table or the insertion.
	 */
	public void loadInserts() throws IOException
	{
		for(String table : _puts.keySet())
		{
			HTable ht = searchTable(table);
			for(Put put : _puts.get(table).values())
			{
				ht.put(put);
			}
		}
	}
		

			//boolean has(byte[] family, byte[] qualifier)
			//boolean has(byte[] family, byte[] qualifier, long ts)
			//boolean has(byte[] family, byte[] qualifier, byte[] value)
			//boolean has(byte[] family, byte[] qualifier, long ts, byte[] value)
}
