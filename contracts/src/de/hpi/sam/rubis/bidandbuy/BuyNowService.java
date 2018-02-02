package de.hpi.sam.rubis.bidandbuy;

import javax.ejb.Remote;

import de.hpi.sam.rubis.RubisNameSchema;
import de.hpi.sam.rubis.entity.BuyNow;

/**
 * Service to buy-now items.
 * 
 * @author thomas vogel
 * 
 */
@Remote
public interface BuyNowService {

	/**
	 * Mapped name to find the service.
	 */
	public static String MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "BuyNowService";

	/**
	 * Buys now an item.
	 * 
	 * @param itemId
	 *            the identifier of the item to buy
	 * @param quantity
	 *            the number of item instances to buy
	 * @param nickname
	 *            the nickname of the user buying the item
	 * @param password
	 *            the password of the user buying the item
	 * @return the buy-now business object
	 * @throws BuyNowServiceException
	 *             if there is a failure in buying the item.
	 */
	public BuyNow buyItemNow(int itemId, int quantity, String nickname,
			String password) throws BuyNowServiceException;

	/**
	 * Returns the number of available instances of the item with the given id.
	 * 
	 * @param itemId
	 *            the id of the item
	 * @return the number of available instances of the item.
	 */
	public int getNumberOfAvailableInstances(int itemId)
			throws BuyNowServiceException;

}
