package de.hpi.sam.rubis.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Business object for representing an item.
 * 
 * @author thomas vogel
 * 
 */
public class Item implements Serializable {

	private static final long serialVersionUID = 2459866242487110233L;
	private Integer id;
	private String name;
	private String description;
	private float initialPrice;
	private int initialQuantity;
	private float reservePrice = 0;
	private float buyNowPrice = 0;
	private Date startDate;
	private Date endDate;
	private boolean onlyBuyNow;
	private User seller;
	private Category category;
	private List<Bid> bids;
	private List<BuyNow> buyNows;
	private List<Comment> comments;
	private InventoryItem inventoryItem;

	/**
	 * @param id
	 *            identifier of the item
	 * @param name
	 *            name of the item
	 * @param description
	 *            description of the item
	 * @param initialPrice
	 *            the initial price of the item
	 * @param initialQuantity
	 *            the initial quantity of the item
	 * @param reservePrice
	 *            the price for which an instance of the item is reserved
	 * @param buyNow
	 *            the price for which the item can be immediately bought
	 * @param startDate
	 *            the date the bidding starts
	 * @param endDate
	 *            the date the bidding ends
	 * @param onlyBuyNow
	 *            <code>true</code> if the item can only be bought by a buy-now
	 *            but not by an auction, or <code>false</code> if the item can
	 *            be bought by a buy-now and an auction.
	 * @param seller
	 *            the user selling this item
	 * @param category
	 *            the category of the item
	 * @param bids
	 *            all the bids users have given to the item
	 * @param buyNows
	 *            all the buy-nows users have given to the item
	 * @param comments
	 *            The comments buyers have given for this item and for the
	 *            seller
	 * @param inventoryItem
	 *            the inventory item of this item
	 */
	public Item(int id, String name, String description, float initialPrice,
			int initialQuantity, float reservePrice, float buyNowPrice,
			Date startDate, Date endDate, boolean onlyBuyNow, User seller,
			Category category, List<Bid> bids, List<BuyNow> buyNows,
			List<Comment> comments, InventoryItem inventoryItem) {
		super();
		this.id = new Integer(id);
		this.name = name;
		this.description = description;
		this.initialPrice = initialPrice;
		this.initialQuantity = initialQuantity;
		this.reservePrice = reservePrice;
		this.buyNowPrice = buyNowPrice;
		this.startDate = startDate;
		this.endDate = endDate;
		this.onlyBuyNow = onlyBuyNow;
		this.seller = seller;
		this.category = category;
		this.bids = bids;
		this.buyNows = buyNows;
		this.comments = comments;
		this.inventoryItem = inventoryItem;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the initialPrice
	 */
	public float getInitialPrice() {
		return this.initialPrice;
	}

	/**
	 * @return the initialQuantity
	 */
	public int getInitialQuantity() {
		return this.initialQuantity;
	}

	/**
	 * @return the reservePrice
	 */
	public float getReservePrice() {
		return this.reservePrice;
	}

	/**
	 * @return the buyNowPrice
	 */
	public float getBuyNowPrice() {
		return this.buyNowPrice;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * 
	 * @return <code>true</code> if the item can only be bought by a buy-now but
	 *         not by an auction, or <code>false</code> if the item can be
	 *         bought by a buy-now and an auction.
	 */
	public boolean isOnlyBuyNow() {
		return onlyBuyNow;
	}

	/**
	 * @return the seller
	 */
	public User getSeller() {
		return this.seller;
	}

	/**
	 * @param seller
	 *            the seller to set
	 */
	void setSellerId(User seller) {
		this.seller = seller;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return this.category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the bids
	 */
	public List<Bid> getBids() {
		if (this.bids == null) {
			return Collections.unmodifiableList(new LinkedList<Bid>());
		} else {
			return Collections.unmodifiableList(this.bids);
		}
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		if (this.comments == null) {
			return Collections.unmodifiableList(new LinkedList<Comment>());
		} else {
			return Collections.unmodifiableList(this.comments);
		}
	}

	/**
	 * @return the buyNows
	 */
	public List<BuyNow> getBuyNows() {
		if (this.buyNows == null) {
			return Collections.unmodifiableList(new LinkedList<BuyNow>());
		} else {
			return Collections.unmodifiableList(this.buyNows);
		}
	}

	/**
	 * 
	 * @return the inventory item
	 */
	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	/**
	 * 
	 * @param inventoryItem
	 *            the inventory item of this item
	 */
	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	/**
	 * 
	 * @return a String representation of this item.
	 */
	public String infoString() {
		StringBuffer sb = new StringBuffer(this.getClass().getSimpleName()
				+ ": ID: " + this.getId() + "\n");
		sb.append("  Name: " + this.getName() + ", " + this.getDescription()
				+ "\n");
		sb.append("  Initial quantity: " + this.getInitialQuantity()
				+ ", Initial Price: " + this.getInitialPrice()
				+ ", Reserve Price: " + this.getReservePrice()
				+ ", Buy-Now Price: " + this.getBuyNowPrice() + "\n");
		sb.append("  Start Date: " + this.getStartDate() + ", End Date: "
				+ this.getEndDate() + "\n");
		sb.append("  Selling User: Id = " + this.getSeller().getId() + ")\n");
		sb.append("  Category: " + this.getCategory().getName()
				+ " (Category ID: " + this.getCategory().getId() + ")\n");

		sb.append("  Bids: " + this.getBids().size() + " bids on this item:\n");
		for (Bid b : this.getBids()) {
			sb.append("    - " + b.infoString());
		}

		sb.append("  Buy-Nows: " + this.getBuyNows().size()
				+ " buy-nows for this item:\n");
		for (BuyNow b : this.getBuyNows()) {
			sb.append("    - " + b.infoString());
		}

		sb.append("  Comments: " + this.getComments().size()
				+ " comments for this item:\n");
		for (Comment c : this.getComments()) {
			sb.append("    - " + c.infoString());
		}

		if (this.inventoryItem != null) {
			sb.append("  Inventory: " + this.inventoryItem.infoString());
		}

		sb.append("\n");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		return (this.getClass().getCanonicalName() + this.id).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final Item other = (Item) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

}
