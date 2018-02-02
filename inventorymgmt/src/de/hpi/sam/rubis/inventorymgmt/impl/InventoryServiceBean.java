package de.hpi.sam.rubis.inventorymgmt.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.hpi.sam.rubis.entity.InventoryItem;
import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.inventorymgmt.InventoryService;
import de.hpi.sam.rubis.inventorymgmt.InventoryServiceException;
import de.hpi.sam.rubis.persistenceservice.BusinessObjectsPersistenceService;
import de.hpi.sam.rubis.persistenceservice.BusinessObjectsPersistenceServiceException;
import de.hpi.sam.rubis.queryservice.QueryService;
import de.hpi.sam.rubis.queryservice.QueryServiceException;

/**
 * Implementation of the {@link InventoryService}.
 * 
 * @author thomas vogel
 * 
 */
@Stateless(mappedName = InventoryService.MAPPED_NAME)
public class InventoryServiceBean implements InventoryService {

	@EJB(mappedName = QueryService.MAPPED_NAME)
	private QueryService queryService;

	@EJB(mappedName = BusinessObjectsPersistenceService.MAPPED_NAME)
	private BusinessObjectsPersistenceService persistenceService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int checkAvailabilityOfItem(Item item)
			throws InventoryServiceException {
		try {
			InventoryItem ii = this.queryService
					.retrieveAvailabilityOfItem(item.getId());

			// System.out.println("InventoryServiceBean: "
			// + ii.getItem().infoString() + " // "
			// + ii.getAvailableQuantity());

			return ii.getAvailableQuantity();
		} catch (QueryServiceException e) {
			throw new InventoryServiceException(
					"Failure in checking availability of item " + item, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int checkAvailabilityOfItem(int itemId)
			throws InventoryServiceException {
		boolean dummy = true;
		if (dummy) {
			// avoids the non-robust Derby database
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
			return 1;
		} else {
			try {
				InventoryItem ii = this.queryService
						.retrieveAvailabilityOfItem(itemId);
				return ii.getAvailableQuantity();
			} catch (QueryServiceException e) {
				throw new InventoryServiceException(
						"Failure in checking availability of item with id "
								+ itemId, e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean reserveItem(Item item, int numberOfItems)
			throws InventoryServiceException {
		int availableInstances = this.checkAvailabilityOfItem(item);
		if (numberOfItems > availableInstances) {
			return false;
		} else {
			try {
				boolean isReserved = this.persistenceService
						.reduceInventoryItem(item, numberOfItems);
				return isReserved;
			} catch (BusinessObjectsPersistenceServiceException e) {
				// Item cannot be reserved
				return false;
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelReservedItem(Item item, int numberOfItems)
			throws InventoryServiceException {
		try {
			@SuppressWarnings("unused")
			InventoryItem ii = this.persistenceService.increaseInventoryItem(
					item, numberOfItems);
		} catch (BusinessObjectsPersistenceServiceException e) {
			throw new InventoryServiceException(
					"Failure in increasing the instances of item " + item
							+ " in the inventory.", e);
		}
	}

}
