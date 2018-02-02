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
 * Entity class for comments.
 * 
 * @author thomas vogel
 * 
 */
@Entity
@Table(name = "comments")
public class EComment implements Serializable {

	private static final long serialVersionUID = -1629759246875016975L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "from_user_id", nullable = false)
	private EUser fromUser;

	@ManyToOne
	@JoinColumn(name = "to_user_id", nullable = false)
	private EUser toUser;

	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false)
	private EItem item;

	@Column(name = "rating")
	private int rating;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "comment_date")
	private Date date;

	@Column(name = "comment", length = 2048)
	private String comment;

	/**
	 * default constructor.
	 */
	public EComment() {

	}

	/**
	 * @param id
	 * @param fromUser
	 * @param toUser
	 * @param item
	 * @param rating
	 * @param date
	 * @param comment
	 */
	public EComment(int id, EUser fromUser, EUser toUser, EItem item,
			int rating, Date date, String comment) {
		super();
		this.id = id;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.item = item;
		this.rating = rating;
		this.date = date;
		this.comment = comment;
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
	 * @return the fromUser
	 */
	public EUser getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser
	 *            the fromUser to set
	 */
	public void setFromUser(EUser fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return the toUser
	 */
	public EUser getToUser() {
		return toUser;
	}

	/**
	 * @param toUser
	 *            the toUser to set
	 */
	public void setToUser(EUser toUser) {
		this.toUser = toUser;
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
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Converts this entity instance of a comment to a business object
	 * representing the same comment.
	 * 
	 * @param dtoHelper
	 *            conversion helper
	 * @return the comment business object of this entity instance.
	 */
	public Comment convertToDTO(DtoHelper dtoHelper) {
		if (dtoHelper == null) {
			dtoHelper = new DtoHelper();
		}
		Comment comment = dtoHelper.getComments().get(this.getId());
		if (comment == null) {
			comment = new Comment(this.getId(), null, null, null, this
					.getRating(), this.getDate(), this.getComment());
			dtoHelper.getComments().put(comment.getId(), comment);

			User fromUser = this.getFromUser().convertToDTO(dtoHelper);
			comment.setFromUser(fromUser);

			User toUser = this.getToUser().convertToDTO(dtoHelper);
			comment.setToUser(toUser);

			Item item = this.getItem().convertToDTO(dtoHelper);
			comment.setItem(item);

		}
		return comment;
	}

}
