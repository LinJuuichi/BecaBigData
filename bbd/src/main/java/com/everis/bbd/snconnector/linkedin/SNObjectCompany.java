package com.everis.bbd.snconnector.linkedin;

import java.util.logging.Logger;

import com.everis.bbd.snconnector.SNObject;
import com.everis.bbd.snconnector.SNObjectComment;

/**
 * SNObject for company information from LinkedIn.
 */
public class SNObjectCompany extends SNObject 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(SNObjectCompany.class.getName());

	@Override
	public String toString() 
	{
		log.info("To string called.");
		// TODO Auto-generated method stub
		return null;
	}

}
