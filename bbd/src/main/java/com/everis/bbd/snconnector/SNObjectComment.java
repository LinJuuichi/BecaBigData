package com.everis.bbd.snconnector;

/**
 * SNObject for user comments in different social networks.
 * Like tweets, facebook comments, etc.
 */
public class SNObjectComment extends SNObject
{

	@Override
	public String toString() 
	{
		String value = "";
		if (this.hasValue(SNObjectKeys.POST_ID_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_ID_KEY.getId()));
		}
		else
		{
			return null;
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_TEXT_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_TEXT_KEY.getId()));
		}
		else
		{
			return null;
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_USERID_KEY.getId()))
		{
			value.concat(Integer.toString(this.getInt(SNObjectKeys.POST_USERID_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_USER_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_USER_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_LATITUDE_KEY.getId()))
		{
			value.concat(Double.toString(this.getDouble(SNObjectKeys.POST_LATITUDE_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_LONGITUDE_KEY.getId()))
		{
			value.concat(Double.toString(this.getDouble(SNObjectKeys.POST_LONGITUDE_KEY.getId())));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_DATE_KEY.getId()))
		{
			value.concat(this.getTimestamp(SNObjectKeys.POST_DATE_KEY.getId()).toString());
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_SOURCE_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_SOURCE_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		if (this.hasValue(SNObjectKeys.POST_QUERY_KEY.getId()))
		{
			value.concat(this.getString(SNObjectKeys.POST_QUERY_KEY.getId()));
		}
		else
		{
			value.concat("NULL");
		}
		value.concat(FIELD_SEPARATOR);
		
		return value;	
	}

}
