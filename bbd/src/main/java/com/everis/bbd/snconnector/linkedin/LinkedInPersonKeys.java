package com.everis.bbd.snconnector.linkedin;

/**
 * JSON LinkedIn person key names enumeration.
 */
public enum LinkedInPersonKeys 
{
	/**
	 * associations key.
	 */
	ASSOCIATIONS_KEY("associations"),
	
	/**
	 * honors key.
	 */
	HONORS_KEY("honors"),
	
	/**
	 * certifications key.
	 */
	CERTIFICATIONS_KEY("certifications"),
	
	/**
	 * location key.
	 */
	LOCATION_KEY("location"),
	
	/**
	 * currentStatus key.
	 */
	CURRENT_STATUS_KEY("currentStatus"),
	
	/**
	 * currentShare key.
	 */
	CURRENT_SHARE_KEY("currentShare"),
	
	/**
	 * numberOfRecommenders key.
	 */
	NUMBER_OF_RECOMMENDERS_KEY("numberOfRecommenders"),
	
	/**
	 * numberOfConnections key.
	 */
	NUMBER_OF_CONNECTIONS_KEY("numberOfConnections"),
	
	/**
	 * birth key.
	 */
	BIRTH_KEY("birth"),
	
	/**
	 * distance key.
	 */
	DISTANCE_KEY("distance"),
	
	/**
	 * firstName key.
	 */
	FIRST_NAME_KEY("firstName"),
	
	/**
	 * lastName key.
	 */
	LAST_NAME_KEY("lastName"),
	
	/**
	 * educations key.
	 */
	EDUCATIONS_KEY("educations"),
	
	/**
	 * headline key.
	 */
	HEADLINE_KEY("headline"),
	
	/**
	 * id key.
	 */
	ID_KEY("id"),
	
	/**
	 * industry key.
	 */
	INDUSTRY_KEY("industry"),
	
	/**
	 * interests key.
	 */
	INTERESTS_KEY("interests"),
	
	/**
	 * languages key.
	 */
	LANGUAGES_KEY("languages");
	
	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private LinkedInPersonKeys(String id) 
	{
		_id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() 
	{
		return _id;
	}	

}
