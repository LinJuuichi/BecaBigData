package com.everis.bbd.utilities;

import java.sql.Timestamp;
import java.util.Random;
import com.everis.bbd.snconnector.SNObjectComment;
import com.everis.bbd.snconnector.SNObjectKeys;

/**
 * Generates random tweets.
 */
public class TweetGenerator 
{

	/**
	 * Alphabet.
	 */
	public static String _alphabet= "abcdefghijklmnopqrstwxyz";
	
	/**
	 * Queries.
	 */
	public static String[] _queries = { "#apple", "#ipod", "#iphone", "#ipad", "#macbook" };
	
	/**
	 * Sources.
	 */
	public static String[] _sources = { "Twitter for iPhone", "Twitter for Android", "Tweetbot", "Web", "Ecophone" };
	
	/**
	 * Users ID.
	 */
	public static long[] _usersID = { 0000011511, 1234567890, 1631734614, 485110600, 814754816, 63515863 };
	
	/**
	 * Users.
	 */
	public static String[] _users = { "linjuuichi", "foxline", "solsolete", "asturiano", "billpuertas", "estebantrabajos" };
	
	/**
	 * @param size of the word.
	 * @return a String containing a word generated randomly.
	 */
	public static String getRandomWord(int size)
	{
		Random r = new Random();
		char[] word = new char[size];
		for (int i = 0; i < size; i++)
		{
			word[i] = _alphabet.charAt(r.nextInt(_alphabet.length()));
		}
		return new String(word);
	}
	
	/**
	 * @param size size of the string.
	 * @param words number of words.
	 * @return a string generated randomly.
	 */
	static public String getRandomPhrase(int size, int words)
	{
		int numberOfChars = size - words - 1;
		int wordSize = numberOfChars/words;
		String phrase = new String();
		for (int i = 0; i < words; ++i)
		{
			phrase = phrase.concat(getRandomWord(wordSize));
			phrase = phrase.concat(" ");
		}
		
		return phrase;
	}
	
	/**
	 * @param max maximum.
	 * @return return an integer lower or equal than max (0 <= i <= max).
	 */
	static public int getRandomInt(int max)
	{
		Random r = new Random();
		return r.nextInt(max+1);
	}
	
	/**
	 * @return return a random double.
	 */
	static public double getRandomDouble()
	{
		Random r = new Random();
		return r.nextDouble();
	}
	
	/**
	 * @return return a random long.
	 */
	static public long getRandomLong()
	{
		Random r = new Random();
		return r.nextLong();
	}
	
	/**
	 * @return a random timestamp.
	 */
	private static Timestamp getRandomTimestamp() 
	{
		long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
		long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
		long diff = end - offset + 1;
		return new Timestamp(offset + (long)(Math.random() * diff));
	}
	
	/**
	 * @param id identifier of the tweet.
	 * @return a tweet generates randomly.
	 */
	static public SNObjectComment generateRandomTweet(long id)
	{
		SNObjectComment tweet = new SNObjectComment();
		
		tweet.setLong(SNObjectKeys.POST_ID_KEY.getId(), id);
		
		String query = _queries[getRandomInt(_queries.length-1)];
		tweet.setString(SNObjectKeys.POST_QUERY_KEY.getId(), query);
		
		int user = getRandomInt(_usersID.length-1);
		tweet.setLong(SNObjectKeys.POST_USERID_KEY.getId(), _usersID[user]);
		tweet.setString(SNObjectKeys.POST_USER_KEY.getId(), _users[user]);
		
		tweet.setString(SNObjectKeys.POST_LANGUAGE_KEY.getId(), getRandomWord(7));
		tweet.setTimestamp(SNObjectKeys.POST_DATE_KEY.getId(), getRandomTimestamp());
		tweet.setString(SNObjectKeys.POST_SOURCE_KEY.getId(), _sources[getRandomInt(_sources.length-1)]);
		tweet.setString(SNObjectKeys.POST_TEXT_KEY.getId(), getRandomPhrase(160,16).concat(query));
		tweet.setDouble(SNObjectKeys.POST_LATITUDE_KEY.getId(), getRandomDouble());
		tweet.setDouble(SNObjectKeys.POST_LONGITUDE_KEY.getId(), getRandomDouble());
		
		return tweet;
	}

}
