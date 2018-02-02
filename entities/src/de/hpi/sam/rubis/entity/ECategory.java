package de.hpi.sam.rubis.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.hpi.sam.rubis.entity.Category;
import de.hpi.sam.rubis.entity.Item;

/**
 * Entity class for categories.
 * 
 * @author thomas vogel
 * 
 */
@Entity
@NamedQueries( {
		@NamedQuery(name = "findCategoryByName", query = "SELECT DISTINCT c FROM ECategory c WHERE c.name = :name"),
		@NamedQuery(name = "findAllCategories", query = "SELECT c FROM ECategory c") })
@Table(name = "categories")
public class ECategory implements Serializable {

	private static final long serialVersionUID = -106289370321078166L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "name", length = 50)
	private String name;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "category")
	private List<EItem> items;

	/**
	 * default constructor.
	 */
	public ECategory() {

	}

	/**
	 * @param id
	 * @param name
	 * @param items
	 */
	public ECategory(int id, String name, List<EItem> items) {
		super();
		this.id = id;
		this.name = name;
		this.items = items;
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
	 * @return the items
	 */
	public List<EItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<EItem> items) {
		this.items = items;
	}

	/**
	 * Converts this entity instance of a category to a business object
	 * representing the same category.
	 * 
	 * @param dtoHelper
	 *            conversion helper
	 * @return the category business object of this entity instance.
	 */
	public Category convertToDTO(DtoHelper dtoHelper) {
		if (dtoHelper == null) {
			dtoHelper = new DtoHelper();
		}
		Category category = dtoHelper.getCategories().get(this.getId());
		if (category == null) {
			List<Item> items = new LinkedList<Item>();
			category = new Category(this.getId(), this.getName(), items);
			dtoHelper.getCategories().put(category.getId(), category);

			for (EItem eItem : this.getItems()) {
				Item item = eItem.convertToDTO(dtoHelper);
				items.add(item);
				item.setCategory(category);
			}

		}
		return category;

	}

}
