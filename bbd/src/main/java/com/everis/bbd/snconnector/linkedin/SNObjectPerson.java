package com.everis.bbd.snconnector.linkedin;

import java.util.logging.Logger;

import com.everis.bbd.snconnector.SNObject;

/**
 * SNObject for person information from LinkedIn.
 */
public class SNObjectPerson extends SNObject 
{

	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(SNObjectPerson.class.getName());
	
	@Override
	public String toString() 
	{
		log.info("To string called.");
		// TODO Auto-generated method stub
		return null;
	}

}
