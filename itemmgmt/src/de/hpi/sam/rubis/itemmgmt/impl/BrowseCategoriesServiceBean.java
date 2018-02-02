package de.hpi.sam.rubis.itemmgmt.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.hpi.sam.rubis.authservice.AuthenticationService;
import de.hpi.sam.rubis.authservice.AuthenticationServiceException;
import de.hpi.sam.rubis.entity.Category;
import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.itemmgmt.BrowseCategoriesService;
import de.hpi.sam.rubis.itemmgmt.BrowseCategoriesServiceException;
import de.hpi.sam.rubis.queryservice.BasicQueryService;
import de.hpi.sam.rubis.queryservice.QueryService;
import de.hpi.sam.rubis.queryservice.QueryServiceException;

/**
 * Implementation of the {@link BrowseCategoriesService}.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = BrowseCategoriesService.MAPPED_NAME)
public class BrowseCategoriesServiceBean implements BrowseCategoriesService {

	@EJB(mappedName = QueryService.MAPPED_NAME)
	private QueryService queryService;

	@EJB(mappedName = BasicQueryService.MAPPED_NAME)
	private BasicQueryService basicQueryService;

	@EJB(mappedName = AuthenticationService.MAPPED_NAME)
	private AuthenticationService authenticationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Category> getAllCategories()
			throws BrowseCategoriesServiceException {

		try {
			List<Category> allCategories = this.basicQueryService
					.findAllCategories();
			return allCategories;
		} catch (QueryServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Error in retrieving all categories.", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Category> getCategoriesInRegion(String regionName)
			throws BrowseCategoriesServiceException {
		try {
			List<Category> categories = this.queryService
					.findCategoriesInRegion(regionName);
			return categories;
		} catch (QueryServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Error in retrieving all categories in region "
							+ regionName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Category> getCategoriesByName(String categoryName)
			throws BrowseCategoriesServiceException {
		try {
			List<Category> categories = this.basicQueryService
					.findCategoriesByName(categoryName);
			return categories;
		} catch (QueryServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Error in retrieving categories with name " + categoryName
							+ ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getItemsByName(String itemName)
			throws BrowseCategoriesServiceException {
		try {
			List<Item> items = this.basicQueryService.findItemsByName(itemName);
			return items;
		} catch (QueryServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Error in retrieving items with name " + itemName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getPersonalizedItems(String username, String password)
			throws BrowseCategoriesServiceException {
		try {
			User authenticatedUser = this.authenticationService.authenticate(
					username, password);
			List<Item> result = this.basicQueryService
					.findPersonalizedItems(authenticatedUser);
			return result;
		} catch (AuthenticationServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Failure in retrieving the personalized list of items for user "
							+ username, e);
		} catch (QueryServiceException e) {
			throw new BrowseCategoriesServiceException(
					"Failure in retrieving the personalized list of items for user "
							+ username, e);
		}
	}

}
