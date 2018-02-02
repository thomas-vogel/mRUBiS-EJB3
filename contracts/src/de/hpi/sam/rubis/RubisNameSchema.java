package de.hpi.sam.rubis;

/**
 * Name schema for RUBiS defining names or part of names relevant for naming
 * directories, services etc.
 * 
 * @author thomas vogel
 * 
 */
public interface RubisNameSchema {

	/**
	 * String for separating the prefix and the rest of a mapped name.
	 */
	public static String MAPPED_NAME_SEPARATOR = "/";

	/**
	 * Folder in the JNDI for mapped names of enterprise beans being part of
	 * RUBiS.
	 */
	public static String MAPPED_NAME_FOLDER = "mRUBiS";

	/**
	 * Name prefix for mapped names of enterprise beans in the JNDI.
	 */
	public static String MAPPED_NAME_PREFIX = MAPPED_NAME_FOLDER
			+ MAPPED_NAME_SEPARATOR;

	/**
	 * The name of the persistence unit used by the application.
	 */
	public static String PERSISTENCE_UNIT_NAME = "mRUBiS-pu";

}
