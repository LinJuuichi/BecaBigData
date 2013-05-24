package com.everis.bbd.mahout.outputwrappers;

/**
 *
 *
 */
public abstract class OutputWrapper 
{
	
	/**
	 * 
	 */
	protected String _name;
	
	/**
	 * @return d
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * @throws OutputWrapperException d
	 */
	public abstract void before() throws OutputWrapperException;
	
	/**
	 * @throws OutputWrapperException d
	 */
	public abstract void after() throws OutputWrapperException;
	
	/**
	 * @param date d
	 * @param key d
	 * @param pattern d
	 * @param repetitions d
	 * @throws OutputWrapperException d
	 */
	public abstract void saveFPMresult(String date, String key, String pattern, Long repetitions) throws OutputWrapperException;
}
