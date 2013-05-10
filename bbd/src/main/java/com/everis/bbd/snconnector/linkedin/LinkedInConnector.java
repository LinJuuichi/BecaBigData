package com.everis.bbd.snconnector.linkedin;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONObject;
import com.everis.bbd.snconnector.SNConnector;
import com.everis.bbd.snconnector.SNConnectorKeys;
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
			_results.add(companyToJsonObject(company));
			break;
		case PEOPLE_QUERY:
			People people = _peopleClient.searchPeople(_queryParameters);
			for (Person person: people.getPersonList())
			{
				_results.add(personToJsonObject(person));
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
	public JSONObject companyToJsonObject(Company company)
	{
		JSONObject jCompany = new JSONObject();
		
		/** String values **/
		jCompany.put(LinkedInCompanyKeys.ID_KEY.getId(), company.getId());
		jCompany.put(LinkedInCompanyKeys.NAME_KEY.getId(), company.getName());
		jCompany.put(LinkedInCompanyKeys.UNIVERSAL_NAME_KEY.getId(), company.getUniversalName());
		jCompany.put(LinkedInCompanyKeys.DESCRIPTION_KEY.getId(), company.getDescription());
		jCompany.put(LinkedInCompanyKeys.SIZE_KEY.getId(), company.getSize());
		jCompany.put(LinkedInCompanyKeys.EMPLOYEE_COUNT_RANGE_KEY.getId(), company.getEmployeeCountRange().getName());
		jCompany.put(LinkedInCompanyKeys.CURRENT_STATUS_KEY.getId(), company.getStatus().getName());
		jCompany.put(LinkedInCompanyKeys.TWITTER_ID_KEY.getId(), company.getTwitterId());
		jCompany.put(LinkedInCompanyKeys.END_YEAR_KEY.getId(), company.getEndYear());
		jCompany.put(LinkedInCompanyKeys.INDUSTRY_KEY.getId(), company.getIndustry());
		jCompany.put(LinkedInCompanyKeys.TICKER_KEY.getId(), company.getTicker());
		//jCompany.put(LinkedInCompanyKeys.STOCK_EXCHANGE_KEY.getId(), company.getStockExchange().getName());
		
		/** Numeric values **/
		jCompany.put(LinkedInCompanyKeys.NUMBER_OF_FOLLOWERS_KEY.getId(), company.getNumFollowers());
		jCompany.put(LinkedInCompanyKeys.FOUNDED_YEAR_KEY.getId(), company.getFoundedYear());
		jCompany.put(LinkedInCompanyKeys.END_YEAR_KEY.getId(), company.getEndYear());
				
		/** Iterable values **/
		JSONObject jEmailDomains = new JSONObject();
		for (String email: company.getEmailDomains().getEmailDomainList())
		{
			jEmailDomains.put(LinkedInCompanyKeys.EMAIL_DOMAIN_KEY.getId(),email);
		}
		jCompany.put(LinkedInCompanyKeys.EMAIL_DOMAINS_KEY.getId(), jEmailDomains);
		
		JSONObject jLocations = new JSONObject();
		for (Location location: company.getLocations().getLocationList())
		{
			if (location != null)
			{
				jLocations.put(LinkedInCompanyKeys.LOCATION_KEY.getId(),location.getDescription());
			}
		}
		jCompany.put(LinkedInCompanyKeys.LOCATIONS_KEY.getId(), jLocations);
		
		return jCompany;
	}
	
	/**
	 * Converts to JSON a person information.
	 * 
	 * @param person information.
	 * @return person information in JSON format.
	 */
	public JSONObject personToJsonObject(Person person)
	{
		JSONObject jPerson = new JSONObject();
		
		/** String values **/
		jPerson.put(LinkedInPersonKeys.ID_KEY.getId(), person.getId());
		jPerson.put(LinkedInPersonKeys.ASSOCIATIONS_KEY.getId(), person.getAssociations());
		jPerson.put(LinkedInPersonKeys.HONORS_KEY.getId(), person.getHonors());
		jPerson.put(LinkedInPersonKeys.CURRENT_STATUS_KEY.getId(), person.getCurrentStatus());
		jPerson.put(LinkedInPersonKeys.FIRST_NAME_KEY.getId(), person.getFirstName());
		jPerson.put(LinkedInPersonKeys.LAST_NAME_KEY.getId(), person.getLastName());
		jPerson.put(LinkedInPersonKeys.INDUSTRY_KEY.getId(), person.getIndustry());
		jPerson.put(LinkedInPersonKeys.INTERESTS_KEY.getId(), person.getInterests());
		jPerson.put(LinkedInPersonKeys.BIRTH_KEY.getId(), person.getDateOfBirth().toString());
		jPerson.put(LinkedInPersonKeys.LOCATION_KEY.getId(), person.getLocation().getDescription());
		
		/** Numeric values **/
		jPerson.put(LinkedInPersonKeys.DISTANCE_KEY.getId(), person.getDistance());
		jPerson.put(LinkedInPersonKeys.NUMBER_OF_RECOMMENDERS_KEY.getId(), person.getNumRecommenders());
		jPerson.put(LinkedInPersonKeys.NUMBER_OF_CONNECTIONS_KEY.getId(), person.getNumConnections());
		
		/** Iterable values **/
		JSONObject jCerts = new JSONObject();
		for (Certification cert: person.getCertifications().getCertificationList())
		{
			jCerts.put(cert.getId(),cert.getName());
		}
		jPerson.put(LinkedInPersonKeys.CERTIFICATIONS_KEY.getId(), jCerts);
		
		JSONObject jEdu = new JSONObject();
		for (Education edu: person.getEducations().getEducationList())
		{
			jEdu.put(edu.getId(),edu.getSchoolName());
		}
		jPerson.put(LinkedInPersonKeys.EDUCATIONS_KEY.getId(), jEdu);
		
		JSONObject jLan = new JSONObject();
		for (Language lan: person.getLanguages().getLanguageList())
		{
			jLan.put(lan.getId(), lan.getLanguage().getName());
		}
		jPerson.put(LinkedInPersonKeys.LANGUAGES_KEY.getId(), jLan);
					
		return jPerson;
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
