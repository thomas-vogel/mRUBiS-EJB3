package de.hpi.sam.rubis.itemmgmt;

import java.util.List;

import javax.ejb.Remote;

import de.hpi.sam.rubis.RubisNameSchema;
import de.hpi.sam.rubis.entity.Category;
import de.hpi.sam.rubis.entity.Item;

/**
 * Service for browsing categories and items.
 * 
 * @author thomas vogel
 * 
 */
@Remote
public interface BrowseCategoriesService {

	/**
	 * Mapped name to find the service.
	 */
	public static String MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "BrowseCategoriesService";

	/**
	 * Retrieves all categories that contain at least one item that is currently
	 * offered or that has been offered in the specified region.
	 * 
	 * @param regionName
	 *            the name of the region
	 * @return a list of categories containing items being offered or having
	 *         been offered in the specified region
	 * @throws BrowseCategoriesServiceException
	 *             if there is a failure in retrieving the categories.
	 */
	public List<Category> getCategoriesInRegion(String regionName)
			throws BrowseCategoriesServiceException;

	/**
	 * Retrieves all categories.
	 * 
	 * @return a list of all categories
	 * @throws BrowseCategoriesServiceException
	 *             if there is a failure in retrieving all categories.
	 */
	public List<Category> getAllCategories()
			throws BrowseCategoriesServiceException;

	/**
	 * Retrieves all categories whose names contain the specified
	 * <code>categoryName</code>.
	 * 
	 * @param categoryName
	 *            the name of the category to search for.
	 * @return a list of all categories whose names contain the
	 *         <code>categoryName</code>.
	 * @throws BrowseCategoriesServiceException
	 *             if there is a failure in retrieving the categories.
	 */
	public List<Category> getCategoriesByName(String categoryName)
			throws BrowseCategoriesServiceException;

	/**
	 * Retrieves all items whose names contain the specified
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the name of the item to search for.
	 * @return a list of all items whose names contain the <code>itemName</code>
	 *         as a part of the name.
	 * @throws BrowseCategoriesServiceException
	 *             if there is a failure in retrieving the items.
	 */
	public List<Item> getItemsByName(String itemName)
			throws BrowseCategoriesServiceException;

	/**
	 * Retrieves a personalized list of items for the user with the given name
	 * and password.
	 * 
	 * @param username
	 *            the user's nickname
	 * @param password
	 *            the user's password
	 * @return the personalized list of items
	 * @throws BrowseCategoriesServiceException
	 *             if there is a failure in retrieving the personalized list.
	 */
	public List<Item> getPersonalizedItems(String username, String password)
			throws BrowseCategoriesServiceException;

}
