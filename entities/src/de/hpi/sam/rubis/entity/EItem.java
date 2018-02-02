package de.hpi.sam.rubis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for items.
 * 
 * @author thomas vogel
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "findAllItems", query = "SELECT i FROM EItem i") })
@Table(name = "items")
public class EItem implements Serializable {

	private static final long serialVersionUID = 7455029805910727975L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "description", length = 2048)
	private String description;

	@Column(name = "initial_price", nullable = false)
	private float initialPrice;

	@Column(name = "initial_quantity", nullable = false)
	private int initialQuantity;

	@Column(name = "reserve_price")
	private float reservePrice = 0;

	@Column(name = "buy_now_price")
	private float buyNowPrice = 0;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "only_buy_now")
	private boolean onlyBuyNow;

	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	private EUser seller;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private ECategory category;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "item")
	@OrderBy("bidPrice DESC, date DESC")
	private List<EBid> bids;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "item")
	@OrderBy("date DESC")
	private List<EBuyNow> buyNows;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "item")
	@OrderBy("date DESC")
	private List<EComment> comments;

	// fetch type must be lazy! Otherwise low-level exceptions wrt ORB
	// connections and marshalling are thrown!
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "item")
	private EInventoryItem inventoryItem;

	/**
	 * default constructor.
	 */
	public EItem() {

	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param initialPrice
	 * @param initialQuantity
	 * @param reservePrice
	 * @param buyNowPrice
	 * @param startDate
	 * @param endDate
	 * @param onlyBuyNow
	 * @param seller
	 * @param category
	 * @param bids
	 * @param buyNows
	 * @param comments
	 * @param inventoryItem
	 */
	public EItem(int id, String name, String description, float initialPrice,
			int initialQuantity, float reservePrice, float buyNowPrice,
			Date startDate, Date endDate, boolean onlyBuyNow, EUser seller,
			ECategory category, List<EBid> bids, List<EBuyNow> buyNows,
			List<EComment> comments, EInventoryItem inventoryItem) {
		super();
		this.id = id;
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
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the initialPrice
	 */
	public float getInitialPrice() {
		return initialPrice;
	}

	/**
	 * @param initialPrice
	 *            the initialPrice to set
	 */
	public void setInitialPrice(float initialPrice) {
		this.initialPrice = initialPrice;
	}

	/**
	 * @return the initialQuantity
	 */
	public int getInitialQuantity() {
		return initialQuantity;
	}

	/**
	 * @param initialQuantity
	 *            the initialQuantity to set
	 */
	public void setInitialQuantity(int initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	/**
	 * @return the reservePrice
	 */
	public float getReservePrice() {
		return reservePrice;
	}

	/**
	 * @param reservePrice
	 *            the reservePrice to set
	 */
	public void setReservePrice(float reservePrice) {
		this.reservePrice = reservePrice;
	}

	/**
	 * @return the buyNowPrice
	 */
	public float getBuyNowPrice() {
		return buyNowPrice;
	}

	/**
	 * @param buyNowPrice
	 *            the buyNowPrice to set
	 */
	public void setBuyNowPrice(float buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * 
	 * @param onlyBuyNow
	 *            whether the item can only be bought by buy-nows (
	 *            <code>true</code>) or also by auctions (<code>false</code>).
	 */
	public void setOnlyBuyNow(boolean onlyBuyNow) {
		this.onlyBuyNow = onlyBuyNow;
	}

	/**
	 * @return the seller
	 */
	public EUser getSeller() {
		return seller;
	}

	/**
	 * @param seller
	 *            the seller to set
	 */
	public void setSeller(EUser seller) {
		this.seller = seller;
	}

	/**
	 * @return the category
	 */
	public ECategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(ECategory category) {
		this.category = category;
	}

	/**
	 * @return the bids
	 */
	public List<EBid> getBids() {
		return bids;
	}

	/**
	 * @param bids
	 *            the bids to set
	 */
	public void setBids(List<EBid> bids) {
		this.bids = bids;
	}

	/**
	 * @return the comments
	 */
	public List<EComment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<EComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the buyNows
	 */
	public List<EBuyNow> getBuyNows() {
		return buyNows;
	}

	/**
	 * @param buyNows
	 *            the buyNows to set
	 */
	public void setBuyNows(List<EBuyNow> buyNows) {
		this.buyNows = buyNows;
	}

	/**
	 * 
	 * @return the inventory item of this item
	 */
	public EInventoryItem getInventoryItem() {
		return inventoryItem;
	}

	/**
	 * 
	 * @param inventoryItem
	 *            the inventory item to set
	 */
	public void setInventoryItem(EInventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	/**
	 * Converts this entity instance of an item to a business object
	 * representing the same item.
	 * 
	 * @param dtoHelper
	 *            conversion helper
	 * @return the item business object of this entity instance.
	 */
	public Item convertToDTO(DtoHelper dtoHelper) {
		if (dtoHelper == null) {
			dtoHelper = new DtoHelper();
		}
		Item item = dtoHelper.getItems().get(this.getId());
		if (item == null) {
			List<Bid> bids = new LinkedList<Bid>();
			List<BuyNow> buyNows = new LinkedList<BuyNow>();
			List<Comment> comments = new LinkedList<Comment>();

			item = new Item(this.getId(), this.getName(),
					this.getDescription(), this.getInitialPrice(),
					this.getInitialQuantity(), this.getReservePrice(),
					this.getBuyNowPrice(), this.getStartDate(),
					this.getEndDate(), this.isOnlyBuyNow(), null, null, bids,
					buyNows, comments, null);
			dtoHelper.getItems().put(item.getId(), item);

			// category of the item
			Category category = this.getCategory().convertToDTO(dtoHelper);
			item.setCategory(category);

			// seller of the item
			User seller = this.getSeller().convertToDTO(dtoHelper);

			List<Comment> receivedComments = seller.getReceivedComments();
			List<UserInfoComment> userInfoComments = new LinkedList<UserInfoComment>();
			for (Comment comment : receivedComments) {
				userInfoComments.add(new UserInfoComment(comment.getId(),
						comment.getComment(), comment.getRating(), comment
								.getFromUser().getId(), comment.getFromUser()
								.getNickname(), comment.getDate(), comment
								.getItem().getId(),
						comment.getItem().getName(), comment.getItem()
								.getDescription()));
			}

			item.setSellerId(seller);

			// Bids
			for (EBid eBid : this.getBids()) {
				bids.add(eBid.convertToDTO(dtoHelper));
			}

			// Buy-nows
			for (EBuyNow eBuyNow : this.getBuyNows()) {
				BuyNow buyNow = eBuyNow.convertToDTO(dtoHelper);
				buyNows.add(buyNow);
			}

			// Comments
			for (EComment eComment : this.getComments()) {
				comments.add(eComment.convertToDTO(dtoHelper));
			}

			// inventory item
			EInventoryItem eInventoryItem = this.getInventoryItem();
			if (eInventoryItem != null) {
				InventoryItem inventoryItem = eInventoryItem
						.convertToDTO(dtoHelper);
				item.setInventoryItem(inventoryItem);
			}

		}
		return item;

	}

}
