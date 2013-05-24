package com.everis.bbd.mahout.outputwrappers;

/**
 * 
 *
 */
public class OutputWrapperFactory {
	
	/**
	 * 
	 */
	public static final int HBASE = 1;
	/**
	 * 
	 */
	public static final int HIVE = 2;
	
	/**
	 * 
	 */
	private OutputWrapperFactory() 
	{
		
	}

	/**
	 * @param wrapperName d
	 * @return d
	 * @throws OutputWrapperException d
	 */
	public static OutputWrapper getOutputWrapper(String wrapperName) throws OutputWrapperException
	{
		if(wrapperName.compareToIgnoreCase("HBASE") == 0) 
			return getOutputWrapper(HBASE);
		else if(wrapperName.compareToIgnoreCase("HIVE") == 0)
			return getOutputWrapper(HIVE);
		else throw new OutputWrapperException("This wrapperName does not correspond to any wrapper");
			
	}	
	
	/**
	 * @param wrapperID d
	 * @return d
	 * @throws OutputWrapperException d
	 */
	public static OutputWrapper getOutputWrapper(int wrapperID) throws OutputWrapperException
	{
		switch(wrapperID)
		{
			case HBASE:
				return new HBaseOutputWrapper();
			case HIVE:
				return new HiveOutputWrapper();
			default:
				throw new OutputWrapperException("This wrapperID does not correspond to any wrapper");
		}
	}
	
}
