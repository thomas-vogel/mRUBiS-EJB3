package de.hpi.sam.rubis.filter;

import java.util.List;

import javax.ejb.Remote;

import de.hpi.sam.rubis.RubisNameSchema;
import de.hpi.sam.rubis.entity.Item;
import de.hpi.sam.rubis.entity.User;

/**
 * Filter to remove irrelevant items from a list of items.
 * 
 * @author thomas vogel
 * 
 */
@Remote
public interface ItemFilter {

	/**
	 * Mapped name to find the filter that removes items from sales ending too
	 * soon to make a bid.
	 */
	public static String LAST_SECOND_SALES_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "LastSecondSaleItemFilter";

	/**
	 * Mapped name to find the filter that removes items from past sales.
	 */
	public static String PAST_SALES_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "PastSalesItemsFilter";

	/**
	 * Mapped name to find the filter that removes items that are only sold by
	 * buy-nows.
	 */
	public static String BUY_NOW_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "BuyNowItemsFilter";

	/**
	 * Mapped name to find the filter that removes items that are not available.
	 */
	public static String AVAILABILITY_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "AvailabilityItemFilter";

	/**
	 * Mapped name to find the filter that removes items that are provided on
	 * another continent as the user lives.
	 */
	public static String REGION_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "RegionItemFilter";

	/**
	 * Mapped name to find the filter that removes items that are provided by
	 * sellers with a low reputation.
	 */
	public static String SELLER_REPUTATION_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "SellerReputationItemFilter";

	/**
	 * Mapped name to find the filter that removes items based on user comments
	 * for individual items.
	 */
	public static String COMMENT_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "CommentItemFilter";

	/**
	 * Mapped name to find the filter that removes items based on their
	 * categories.
	 */
	public static String CATEGORY_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "CategoryItemFilter";

	/**
	 * Mapped name to find the filter that removes items based on recommendation
	 * of the buying user.
	 */
	public static String RECOMMENDATION_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "RecommendationItemFilter";

	/**
	 * Mapped name to find the filter that removes items whose auction end after
	 * a certain threshold in the future.
	 */
	public static String FUTURE_SALES_ITEM_FILTER_MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "FutureSalesItemFilter";

	/**
	 * Processes a given list (i.e, the first parameter) and removes items from
	 * this list, which that are not relevant for the given user.
	 * 
	 * @param items
	 *            all items
	 * @param user
	 *            the searching user
	 * @return the list of items that are relevant for the given user. The
	 *         returned list together with the items removed by this filter
	 *         corresponds to the list given as the first parameter.
	 * @throws ItemFilterException
	 *             if a failure occurred in filtering the items.
	 */
	public List<Item> filter(List<Item> items, User user)
			throws ItemFilterException;

}
