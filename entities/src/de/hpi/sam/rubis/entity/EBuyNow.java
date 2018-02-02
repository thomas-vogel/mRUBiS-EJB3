package de.hpi.sam.rubis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for buy-nows.
 * 
 * @author thomas vogel
 * 
 */
@Entity
@Table(name = "buy_now")
public class EBuyNow implements Serializable {

	private static final long serialVersionUID = 2044270753380820261L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "buyer_id", nullable = false)
	private EUser buyer;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private EItem item;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "buy_now_date")
	private Date date;

	/**
	 * default constructor.
	 */
	public EBuyNow() {

	}

	/**
	 * @param id
	 * @param buyer
	 * @param item
	 * @param quantity
	 * @param date
	 */
	public EBuyNow(int id, EUser buyer, EItem item, int quantity, Date date) {
		super();
		this.id = id;
		this.buyer = buyer;
		this.item = item;
		this.quantity = quantity;
		this.date = date;
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
	 * @return the buyer
	 */
	public EUser getBuyer() {
		return buyer;
	}

	/**
	 * @param buyer
	 *            the buyer to set
	 */
	public void setBuyer(EUser buyer) {
		this.buyer = buyer;
	}

	/**
	 * @return the item
	 */
	public EItem getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(EItem item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Converts this entity instance of a buy-now to a business object
	 * representing the same buy-now.
	 * 
	 * @param dtoHelper
	 *            conversion helper
	 * @return the buy-now business object of this entity instance.
	 */
	public BuyNow convertToDTO(DtoHelper dtoHelper) {
		if (dtoHelper == null) {
			dtoHelper = new DtoHelper();
		}
		BuyNow buyNow = dtoHelper.getBuyNows().get(this.getId());
		if (buyNow == null) {
			buyNow = new BuyNow(this.getId(), null, null, this.getQuantity(),
					this.getDate());
			dtoHelper.getBuyNows().put(buyNow.getId(), buyNow);

			User buyingUser = this.getBuyer().convertToDTO(dtoHelper);
			buyNow.setBuyer(buyingUser);

			Item boughtItem = this.getItem().convertToDTO(dtoHelper);
			buyNow.setItem(boughtItem);

		}
		return buyNow;
	}

}
