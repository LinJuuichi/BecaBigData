package com.everis.bbd.snconnector.linkedin;

/**
 * JSON LinkedIn company key names enumeration.
 */
public enum LinkedInCompanyKeys 
{
	/**
	 * type key.
	 */
	TYPE_KEY("type"),
	
	/**
	 * name key.
	 */
	NAME_KEY("name"),
	
	/**
	 * universalName key.
	 */
	UNIVERSAL_NAME_KEY("universalName"),
	
	/**
	 * description key.
	 */
	DESCRIPTION_KEY("description"),
	
	/**
	 * emailDomains key.
	 */
	EMAIL_DOMAINS_KEY("emailDomains"),
	
	/**
	 * emailDomains key.
	 */
	EMAIL_DOMAIN_KEY("emailDomain"),
	
	/**
	 * numberOfFollowers key.
	 */
	NUMBER_OF_FOLLOWERS_KEY("numberOfFollowers"),
	
	/**
	 * size key.
	 */
	SIZE_KEY("size"),
	
	/**
	 * employeeCountRange key.
	 */
	EMPLOYEE_COUNT_RANGE_KEY("employeeCountRange"),
	
	/**
	 * foundedyear key.
	 */
	FOUNDED_YEAR_KEY("foundedyear"),
	
	/**
	 * specialities key.
	 */
	SPECIALITIES_KEY("specialities"),
	
	/**
	 * endYear key.
	 */
	END_YEAR_KEY("endYear"),
	
	/**
	 * status key.
	 */
	CURRENT_STATUS_KEY("currentStatus"),
	
	/**
	 * id key.
	 */
	ID_KEY("id"),
	
	/**
	 * twitterId key.
	 */
	TWITTER_ID_KEY("twitterId"),
	
	/**
	 * industry key.
	 */
	INDUSTRY_KEY("industry"),
	
	/**
	 * ticker key.
	 */
	TICKER_KEY("ticker"),
	
	/**
	 * stockExchange key.
	 */
	STOCK_EXCHANGE_KEY("stockExchange"),
	
	/**
	 * location key.
	 */
	LOCATION_KEY("location"),
	
	/**
	 * locations key.
	 */
	LOCATIONS_KEY("locations");
	
	/**
	 * Key value.
	 */
	private String _id = null;

	/**
	 * Creator.
	 * 
	 * @param id the identifier 
	 */
	private LinkedInCompanyKeys(String id) 
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