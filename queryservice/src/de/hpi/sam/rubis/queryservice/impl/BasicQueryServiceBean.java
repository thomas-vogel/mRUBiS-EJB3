package de.hpi.sam.rubis.queryservice.impl;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.hpi.sam.rubis.RubisNameSchema;
import de.hpi.sam.rubis.entity.Category;
import de.hpi.sam.rubis.entity.DtoHelper;
import de.hpi.sam.rubis.entity.ECategory;
import de.hpi.sam.rubis.entity.EItem;
import de.hpi.sam.rubis.entity.ERegion;
import de.hpi.sam.rubis.entity.EUser;
import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.entity.Region;
import de.hpi.sam.rubis.entity.User;
import de.hpi.sam.rubis.filter.ItemFilter;
import de.hpi.sam.rubis.filter.ItemFilterException;
import de.hpi.sam.rubis.queryservice.BasicQueryService;
import de.hpi.sam.rubis.queryservice.QueryServiceException;

/**
 * Implementation of the {@link BasicQueryService}.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = BasicQueryService.MAPPED_NAME)
public class BasicQueryServiceBean implements BasicQueryService {

	@PersistenceContext(unitName = RubisNameSchema.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB(mappedName = ItemFilter.LAST_SECOND_SALES_ITEM_FILTER_MAPPED_NAME)
	private ItemFilter firstFilter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> findAllCategories() throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findAllCategories");
			List<ECategory> eCategories = query.getResultList();
			List<Category> categories = new LinkedList<Category>();
			DtoHelper dtoHelper = new DtoHelper();
			for (ECategory eCategory : eCategories) {
				categories.add(eCategory.convertToDTO(dtoHelper));
			}
			return categories;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving all categories.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Region> findAllRegions() throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findAllRegions");
			List<ERegion> eRegions = query.getResultList();
			List<Region> regions = new LinkedList<Region>();
			DtoHelper dtoHelper = new DtoHelper();
			for (ERegion eRegion : eRegions) {
				regions.add(eRegion.convertToDTO(dtoHelper));
			}
			return regions;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving all regions.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findAllUsers");
			List<EUser> eUsers = query.getResultList();
			List<User> users = new LinkedList<User>();
			DtoHelper dtoHelper = new DtoHelper();
			for (EUser eUser : eUsers) {
				users.add(eUser.convertToDTO(dtoHelper));
			}
			return users;
		} catch (Exception e) {
			throw new QueryServiceException("Failure in retrieving all users.",
					e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Item> findAllItems() throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findAllItems");
			List<EItem> eItems = query.getResultList();
			List<Item> items = new LinkedList<Item>();
			DtoHelper dtoHelper = new DtoHelper();
			for (EItem eItem : eItems) {
				items.add(eItem.convertToDTO(dtoHelper));
			}
			return items;
		} catch (Exception e) {
			throw new QueryServiceException("Failure in retrieving all items.",
					e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Item> findPersonalizedItems(User user)
			throws QueryServiceException {
		List<Item> allItems = this.findAllItems();
		try {
			List<Item> result = this.firstFilter.filter(allItems, user);
			return result;
		} catch (ItemFilterException e) {
			throw new QueryServiceException(
					"Failure in retrieving personalized items.", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Category> findCategoriesByName(String categoryName)
			throws QueryServiceException {
		String pattern = "%" + categoryName + "%";
		try {
			Query query = this.em
					.createQuery("SELECT c FROM ECategory c WHERE c.name LIKE :categoryName");
			query = query.setParameter("categoryName", pattern);
			List<ECategory> eCategories = query.getResultList();
			List<Category> categories = new LinkedList<Category>();
			DtoHelper dtoHelper = new DtoHelper();
			for (ECategory eCategory : eCategories) {
				categories.add(eCategory.convertToDTO(dtoHelper));
			}
			return categories;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving all categories whose names are like "
							+ categoryName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category findCategoryById(int categoryId)
			throws QueryServiceException {
		ECategory eCategory = null;
		try {
			eCategory = this.em.find(ECategory.class, categoryId);
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the category with id " + categoryId
							+ ".", e);
		}
		Category category = null;
		if (eCategory != null) {
			category = eCategory.convertToDTO(new DtoHelper());
		}
		return category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category findCategoryByName(String categoryName)
			throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findCategoryByName");
			query = query.setParameter("name", categoryName);
			Object result = query.getSingleResult();
			ECategory eCategory = (ECategory) result;
			return eCategory.convertToDTO(new DtoHelper());
		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the category with name "
							+ categoryName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Item> findItemsByName(String itemName)
			throws QueryServiceException {
		String pattern = "%" + itemName + "%";
		try {
			Query query = this.em
					.createQuery("SELECT i FROM EItem i WHERE i.name LIKE :itemName");
			query = query.setParameter("itemName", pattern);
			List<EItem> eItems = query.getResultList();
			List<Item> items = new LinkedList<Item>();
			DtoHelper dtoHelper = new DtoHelper();
			for (EItem eItem : eItems) {
				items.add(eItem.convertToDTO(dtoHelper));
			}
			return items;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving all items whose names are like "
							+ itemName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Region findRegionById(int regionId) throws QueryServiceException {
		ERegion eRegion = null;
		try {
			eRegion = this.em.find(ERegion.class, regionId);
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the region with id " + regionId
							+ ".", e);
		}
		Region region = null;
		if (eRegion != null) {
			region = eRegion.convertToDTO(new DtoHelper());
		}
		return region;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Region findRegionByName(String regionName)
			throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findRegionByName");
			query = query.setParameter("name", regionName);
			Object result = query.getSingleResult();
			ERegion eRegion = (ERegion) result;
			return eRegion.convertToDTO(new DtoHelper());
		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the region with name " + regionName
							+ ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Region> findRegionsByName(String regionName)
			throws QueryServiceException {
		String pattern = "%" + regionName + "%";
		try {
			Query query = this.em
					.createQuery("SELECT r FROM ERegion r WHERE r.name LIKE :regionName");
			query = query.setParameter("regionName", pattern);
			List<ERegion> eRegions = query.getResultList();
			List<Region> regions = new LinkedList<Region>();
			DtoHelper dtoHelper = new DtoHelper();
			for (ERegion eRegion : eRegions) {
				regions.add(eRegion.convertToDTO(dtoHelper));
			}
			return regions;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving all regions whose names are like "
							+ regionName + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findUserById(int userId) throws QueryServiceException {
		EUser eUser = null;
		try {
			eUser = this.em.find(EUser.class, userId);
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the user with id " + userId + ".", e);
		}
		User user = null;
		if (eUser != null) {
			user = eUser.convertToDTO(new DtoHelper());
		}
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User findUserByNickname(String nickname)
			throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findUserByNickName");
			query = query.setParameter("nickname", nickname);
			Object result = query.getSingleResult();
			EUser eUser = (EUser) result;
			return eUser.convertToDTO(new DtoHelper());
		} catch (NoResultException nre) {
			return null;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the user with nickname " + nickname
							+ ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUserNicknameUsed(String nickname)
			throws QueryServiceException {
		try {
			Query query = this.em.createNamedQuery("findUserByNickName");
			query = query.setParameter("nickname", nickname);
			@SuppressWarnings("unused")
			Object result = query.getSingleResult();
			return true;
		} catch (NoResultException nre) {
			return false;
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in checking the availability of the user nickname "
							+ nickname + ".", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item findItemById(int itemId) throws QueryServiceException {
		EItem eItem = null;
		try {
			eItem = this.em.find(EItem.class, itemId);
		} catch (Exception e) {
			throw new QueryServiceException(
					"Failure in retrieving the item with id " + itemId + ".", e);
		}
		Item item = null;
		if (eItem != null) {
			item = eItem.convertToDTO(new DtoHelper());
		}
		return item;
	}
}
