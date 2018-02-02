package de.hpi.sam.rubis.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for converting entity instances to business objects.
 * 
 * @author thomas vogel
 * 
 */
public class DtoHelper {

	/**
	 * Key: identifier; Value: converted regions
	 */
	private Map<Integer, Region> regions;
	/**
	 * Key: identifier; Value: converted categories
	 */
	private Map<Integer, Category> categories;
	/**
	 * Key: identifier; Value: converted users
	 */
	private Map<Integer, User> users;
	/**
	 * Key: identifier; Value: converted items
	 */
	private Map<Integer, Item> items;
	/**
	 * Key: identifier; Value: converted bids
	 */
	private Map<Integer, Bid> bids;
	/**
	 * Key: identifier; Value: converted buy-nows
	 */
	private Map<Integer, BuyNow> buyNows;
	/**
	 * Key: identifier; Value: converted comments
	 */
	private Map<Integer, Comment> comments;
	/**
	 * Key: identifier; Value: converted inventory items
	 */
	private Map<Integer, InventoryItem> inventoryItems;

	/**
	 * Constructor.
	 */
	public DtoHelper() {
		this.regions = new HashMap<Integer, Region>();
		this.categories = new HashMap<Integer, Category>();
		this.users = new HashMap<Integer, User>();
		this.items = new HashMap<Integer, Item>();
		this.bids = new HashMap<Integer, Bid>();
		this.buyNows = new HashMap<Integer, BuyNow>();
		this.comments = new HashMap<Integer, Comment>();
		this.inventoryItems = new HashMap<Integer, InventoryItem>();
	}

	/**
	 * @return the regions
	 */
	public Map<Integer, Region> getRegions() {
		return regions;
	}

	/**
	 * @return the category
	 */
	public Map<Integer, Category> getCategories() {
		return categories;
	}

	/**
	 * @return the users
	 */
	public Map<Integer, User> getUsers() {
		return users;
	}

	/**
	 * @return the items
	 */
	public Map<Integer, Item> getItems() {
		return items;
	}

	/**
	 * @return the bids
	 */
	public Map<Integer, Bid> getBids() {
		return bids;
	}

	/**
	 * @return the buyNows
	 */
	public Map<Integer, BuyNow> getBuyNows() {
		return buyNows;
	}

	/**
	 * @return the comments
	 */
	public Map<Integer, Comment> getComments() {
		return comments;
	}

	/**
	 * @return the inventoryItems
	 */
	public Map<Integer, InventoryItem> getInventoryItems() {
		return inventoryItems;
	}

}
