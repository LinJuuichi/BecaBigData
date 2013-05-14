package com.everis.bbd.snconnector.linkedin;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Logger;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorKeys;
import com.everis.bbd.snconnector.SNObject;
import com.everis.bbd.snconnector.SNObjectPerson;
import com.google.code.linkedinapi.client.CompaniesApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.PeopleApiClient;
import com.google.code.linkedinapi.client.enumeration.CompanyField;
import com.google.code.linkedinapi.client.enumeration.SearchParameter;
import com.google.code.linkedinapi.schema.Certification;
import com.google.code.linkedinapi.schema.Company;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Language;
import com.google.code.linkedinapi.schema.Location;
import com.google.code.linkedinapi.schema.People;
import com.google.code.linkedinapi.schema.Person;

/**
 * Connector for LinkedIn.
 */
public class LinkedInConnector extends SNConnector 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(LinkedInConnector.class.getName());
	
	/**
	 * Key for company query type.
	 */
	private static final int COMPANY_QUERY = 0;
	
	/**
	 * Key for people query type.
	 */
	private static final int PEOPLE_QUERY = 1;
	
	/**
	 * Key for company query type.
	 */
	private static final String COMPANY_QUERY_KEYWORD = "company";
	
	/**
	 * Key for people query type.
	 */
	private static final String PEOPLE_QUERY_KEYWORD = "people";
	
	/**
	 * Key for query type in the configuration file.
	 */	
	private static final String CONF_QUERY_TYPE_KEY = "search";

	/**
	 * Key for company name in the configuration file.
	 */
	private static final String CONF_COMPANY_NAME_KEY = "company";

	/**
	 * Client for people.
	 */
	private PeopleApiClient _peopleClient;
	
	/**
	 * Client for companies.
	 */
	private CompaniesApiClient _companyClient;
	
	/**
	 * Query type..
	 */
	private int _queryType;
	
	/**
	 * Query filtering parameters.
	 */
	private Map<SearchParameter, String> _queryParameters;
	
	/**
	 * Default configuration file constructor.
	 */
	public LinkedInConnector()
	{
		this(DEFAULT_CONFIGURATION_PATH);
	}
	
	/**
	 * Returns a LinkedInConnector configured with the properties in
	 * propertiesFile.
	 * 
	 * @param propertiesFile file path with the properties (tokens).
	 */
	public LinkedInConnector(String propertiesFile)
	{
		super(propertiesFile);
	}

	@Override
	protected boolean configureQuery() 
	{
		String company = _configuration.getValue(CONF_COMPANY_NAME_KEY, null);
		if (company == null)
		{
			log.severe("Missing or incorrect company property in the properties file.");
			return false;
		}
		_queryParameters = new EnumMap<SearchParameter, String>(SearchParameter.class);
		
		String query = _configuration.getValue(CONF_QUERY_TYPE_KEY, "none");
		if (query.equals(COMPANY_QUERY_KEYWORD))
		{
			_queryType = COMPANY_QUERY;
			_queryParameters.put(SearchParameter.COMPANY_NAME, company);
		}
		else if (query.equals(PEOPLE_QUERY_KEYWORD))
		{
			_queryType = PEOPLE_QUERY;
			_queryParameters.put(SearchParameter.CURRENT_COMPANY, company);
		}
		else
		{
			log.severe("Missing or incorrect search property in the properties file.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean connect()
	{
		if (_configuration == null)
		{
			log.severe("LinkedInConnector must be configured before connection.");
			return false;
		}
		
		LinkedInApiClientFactory factory = 
				LinkedInApiClientFactory.newInstance(
					_configuration.getValue(SNConnectorKeys.OAUTH_CONSUMER_KEY.getId(),""),
					_configuration.getValue(SNConnectorKeys.OAUTH_CONSUMER_SECRET.getId(),"")
				);
		
		String accessToken = _configuration.getValue(SNConnectorKeys.OAUTH_ACCESS_TOKEN.getId(),"");
		String accessSecret = _configuration.getValue(SNConnectorKeys.OAUTH_ACCESS_TOKEN_SECRET.getId(),"");
		
		_companyClient = factory.createCompaniesApiClient(accessToken,accessSecret);
		
		_peopleClient = factory.createPeopleApiClient(accessToken,accessSecret);
		
		return true;
	}

	@Override
	public void close() 
	{
		_companyClient = null;
		_peopleClient = null;
		_results.clear();
		_configuration = null; 
	}

	@Override
	public int query(boolean appendResults) 
	{
		if (appendResults)
		{
			_results.clear();
		}
		
		switch(_queryType)
		{
		case COMPANY_QUERY:
			Company company = _companyClient.getCompanyById("162479",EnumSet.allOf(CompanyField.class));
			_results.add(companyToSNObject(company));
			break;
		case PEOPLE_QUERY:
			People people = _peopleClient.searchPeople(_queryParameters);
			for (Person person: people.getPersonList())
			{
				_results.add(personToSNObject(person));
			}
			break;
		default:
			log.warning("LinkedIn query type does not exist.");
			break;
		}
		return _results.size();
	}

	/**
	 * Converts to JSON a company information.
	 * 
	 * @param company information.
	 * @return company information in JSON format.
	 */
	public SNObject companyToSNObject(Company company)
	{
		SNObject snCompany = new SNObjectCompany();
		
		/** String values **/
		snCompany.setString(LinkedInCompanyKeys.ID_KEY.getId(), company.getId());
		snCompany.setString(LinkedInCompanyKeys.NAME_KEY.getId(), company.getName());
		snCompany.setString(LinkedInCompanyKeys.UNIVERSAL_NAME_KEY.getId(), company.getUniversalName());
		snCompany.setString(LinkedInCompanyKeys.DESCRIPTION_KEY.getId(), company.getDescription());
		snCompany.setString(LinkedInCompanyKeys.SIZE_KEY.getId(), company.getSize());
		snCompany.setString(LinkedInCompanyKeys.EMPLOYEE_COUNT_RANGE_KEY.getId(), company.getEmployeeCountRange().getName());
		snCompany.setString(LinkedInCompanyKeys.CURRENT_STATUS_KEY.getId(), company.getStatus().getName());
		snCompany.setString(LinkedInCompanyKeys.TWITTER_ID_KEY.getId(), company.getTwitterId());
		snCompany.setLong(LinkedInCompanyKeys.END_YEAR_KEY.getId(), company.getEndYear());
		snCompany.setString(LinkedInCompanyKeys.INDUSTRY_KEY.getId(), company.getIndustry());
		snCompany.setString(LinkedInCompanyKeys.TICKER_KEY.getId(), company.getTicker());
		//jCompany.put(LinkedInCompanyKeys.STOCK_EXCHANGE_KEY.getId(), company.getStockExchange().getName());
		
		/** Numeric values **/
		snCompany.setLong(LinkedInCompanyKeys.NUMBER_OF_FOLLOWERS_KEY.getId(), company.getNumFollowers());
		snCompany.setLong(LinkedInCompanyKeys.FOUNDED_YEAR_KEY.getId(), company.getFoundedYear());
		snCompany.setLong(LinkedInCompanyKeys.END_YEAR_KEY.getId(), company.getEndYear());
				
		/** Iterable values **/
		SNObject snEmailDomains = new SNObject();
		for (String email: company.getEmailDomains().getEmailDomainList())
		{
			snEmailDomains.setString(LinkedInCompanyKeys.EMAIL_DOMAIN_KEY.getId(),email);
		}
		snCompany.setSNObject(LinkedInCompanyKeys.EMAIL_DOMAINS_KEY.getId(), snEmailDomains);
		
		SNObject snLocations = new SNObject();
		for (Location location: company.getLocations().getLocationList())
		{
			if (location != null)
			{
				snLocations.setString(LinkedInCompanyKeys.LOCATION_KEY.getId(),location.getDescription());
			}
		}
		snCompany.setSNObject(LinkedInCompanyKeys.LOCATIONS_KEY.getId(), snLocations);
		
		return snCompany;
	}
	
	/**
	 * Converts to JSON a person information.
	 * 
	 * @param person information.
	 * @return person information in JSON format.
	 */
	public SNObject personToSNObject(Person person)
	{
		SNObject snPerson = new SNObjectPerson();
		
		/** String values **/
		snPerson.setString(LinkedInPersonKeys.ID_KEY.getId(), person.getId());
		snPerson.setString(LinkedInPersonKeys.ASSOCIATIONS_KEY.getId(), person.getAssociations());
		snPerson.setString(LinkedInPersonKeys.HONORS_KEY.getId(), person.getHonors());
		snPerson.setString(LinkedInPersonKeys.CURRENT_STATUS_KEY.getId(), person.getCurrentStatus());
		snPerson.setString(LinkedInPersonKeys.FIRST_NAME_KEY.getId(), person.getFirstName());
		snPerson.setString(LinkedInPersonKeys.LAST_NAME_KEY.getId(), person.getLastName());
		snPerson.setString(LinkedInPersonKeys.INDUSTRY_KEY.getId(), person.getIndustry());
		snPerson.setString(LinkedInPersonKeys.INTERESTS_KEY.getId(), person.getInterests());
		snPerson.setString(LinkedInPersonKeys.BIRTH_KEY.getId(), person.getDateOfBirth().toString());
		snPerson.setString(LinkedInPersonKeys.LOCATION_KEY.getId(), person.getLocation().getDescription());
		
		/** Numeric values **/
		snPerson.setLong(LinkedInPersonKeys.DISTANCE_KEY.getId(), person.getDistance());
		snPerson.setLong(LinkedInPersonKeys.NUMBER_OF_RECOMMENDERS_KEY.getId(), person.getNumRecommenders());
		snPerson.setLong(LinkedInPersonKeys.NUMBER_OF_CONNECTIONS_KEY.getId(), person.getNumConnections());
		
		/** Iterable values **/
		SNObject snCerts = new SNObject();
		for (Certification cert: person.getCertifications().getCertificationList())
		{
			snCerts.setString(cert.getId(),cert.getName());
		}
		snPerson.setSNObject(LinkedInPersonKeys.CERTIFICATIONS_KEY.getId(), snCerts);
		
		SNObject snEdu = new SNObject();
		for (Education edu: person.getEducations().getEducationList())
		{
			snEdu.setString(edu.getId(),edu.getSchoolName());
		}
		snPerson.setSNObject(LinkedInPersonKeys.EDUCATIONS_KEY.getId(), snEdu);
		
		SNObject snLan = new SNObject();
		for (Language lan: person.getLanguages().getLanguageList())
		{
			snLan.setString(lan.getId(), lan.getLanguage().getName());
		}
		snPerson.setSNObject(LinkedInPersonKeys.LANGUAGES_KEY.getId(), snLan);
					
		return snPerson;
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public int query(String query, boolean appendResults)
	{
		return 0;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public int nextQuery() 
	{
		return 0;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public boolean hasNextQuery() 
	{
		return false;
	}

}
