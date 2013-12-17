package org.knoesis.dbpedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class checks what are the types of DBPedia entities
 *  that are passed to it and filters out only Books.
 * 
 * @author sanjaya@knoesis.org
 */
public class EntityTypeChecker
{

	public static final String BOOKLIST_FILENAME = "data/books.uri";

	private HashMap<String, ArrayList<String>> bookNames;

	/**
	 * Default Constructor
	 */
	public EntityTypeChecker()
	{
		this.bookNames = new HashMap<String, ArrayList<String>>();
		initializeKeywordsMap();
	}

	/**
	 * This method initializes the hashmap. All book names 
	 * are index on their first letter to efficiently access them.
	 */
	private void initializeKeywordsMap()
	{
		if ( this.bookNames == null )
		{
			this.bookNames = new HashMap<String, ArrayList<String>>();
		}
		this.bookNames.put( "a", new ArrayList<String>() );
		this.bookNames.put( "b", new ArrayList<String>() );
		this.bookNames.put( "c", new ArrayList<String>() );
		this.bookNames.put( "d", new ArrayList<String>() );
		this.bookNames.put( "e", new ArrayList<String>() );
		this.bookNames.put( "f", new ArrayList<String>() );
		this.bookNames.put( "g", new ArrayList<String>() );
		this.bookNames.put( "h", new ArrayList<String>() );
		this.bookNames.put( "i", new ArrayList<String>() );
		this.bookNames.put( "j", new ArrayList<String>() );
		this.bookNames.put( "k", new ArrayList<String>() );
		this.bookNames.put( "l", new ArrayList<String>() );
		this.bookNames.put( "m", new ArrayList<String>() );
		this.bookNames.put( "n", new ArrayList<String>() );
		this.bookNames.put( "o", new ArrayList<String>() );
		this.bookNames.put( "p", new ArrayList<String>() );
		this.bookNames.put( "q", new ArrayList<String>() );
		this.bookNames.put( "r", new ArrayList<String>() );
		this.bookNames.put( "s", new ArrayList<String>() );
		this.bookNames.put( "t", new ArrayList<String>() );
		this.bookNames.put( "u", new ArrayList<String>() );
		this.bookNames.put( "v", new ArrayList<String>() );
		this.bookNames.put( "w", new ArrayList<String>() );
		this.bookNames.put( "x", new ArrayList<String>() );
		this.bookNames.put( "y", new ArrayList<String>() );
		this.bookNames.put( "z", new ArrayList<String>() );
		populateBookNamesHashMap();
	}

	public void populateBookNamesHashMap()
	{
		this.populateBookNamesHashMap( EntityTypeChecker.BOOKLIST_FILENAME );
	}

	/**
	 * This method populates the hashmap with actual book names by reading the input file
	 * 
	 * @param filePath
	 */
	public void populateBookNamesHashMap( String filePath )
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( filePath ) );
			String line = null;
			String dataFields[] = null;
			String firstString = null;

			ArrayList<String> bookNames = null;

			while ( ( line = reader.readLine() ) != null )
			{
				dataFields = line.split( "\t" );
				if ( dataFields.length > 0 )
				{
					firstString = Character.toString( dataFields[0].charAt( 0 ) ).toLowerCase();
					if ( firstString != null )
					{
						bookNames = this.bookNames.get( firstString );
						// In case a bookname starts with a number or some other character than a letter.
						if ( bookNames == null )
						{
							bookNames = new ArrayList<String>();
						}
						bookNames.add( dataFields[0] );
						this.bookNames.put( firstString, bookNames );
					}
				}
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method returns whether a given entity is of type Book or not.
	 * 
	 * @param entityName
	 * @return
	 */
	public boolean isABookType( String entityName )
	{
		String firstString = Character.toString( entityName.charAt( 0 ) ).toLowerCase();
		ArrayList<String> bookNames = this.bookNames.get( firstString );
		if ( bookNames != null )
		{
			return bookNames.contains( entityName );
		}
		return false;
	}

	public HashMap<String, ArrayList<String>> getBookNames()
	{
		return bookNames;
	}

	public void setBookNames( HashMap<String, ArrayList<String>> bookNames )
	{
		this.bookNames = bookNames;
	}

	public static void main( String args[] )
	{
		EntityTypeChecker typeChecker = new EntityTypeChecker();
		System.out.println( typeChecker.isABookType( "Bhagavad_Gita_(Sargeant)" ) );
	}

}
