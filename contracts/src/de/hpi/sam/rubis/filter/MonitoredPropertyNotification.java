package de.hpi.sam.rubis.filter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Notification (message) if monitored property of a filter bean has changed in
 * the running system.
 * 
 * @author thomas vogel
 *
 */
public class MonitoredPropertyNotification implements Serializable {

	private static final long serialVersionUID = 6700462953802864513L;
	public static final String IDENTIFIER_PROPERTY = "identifier";
	public static final String LEVEL_PROPERTY = "level";
	public static final String APP_SPECIFIC_LEVEL = "app-specific";
	public static final String LEVEL_SELECTOR = "level = 'app-specific'";

	private String notificationId;
	private String ejbId;
	private String propertyName;
	private String newPropertyValue;
	private String oldPropertyValue;
	private String propertyType;
	private String propertyUnit;
	private String propertyDescription;

	public MonitoredPropertyNotification(String ejbId, String propertyName, String newPropertyValue,
			String oldPropertyValue, String propertyType, String propertyUnit, String propertyDescription) {
		super();
		this.notificationId = UUID.randomUUID() + "_MonitoredPropertyNotification";
		this.ejbId = ejbId;
		this.propertyName = propertyName;
		this.newPropertyValue = newPropertyValue;
		this.oldPropertyValue = oldPropertyValue;
		this.propertyType = propertyType;
		this.propertyUnit = propertyUnit;
		this.propertyDescription = propertyDescription;
	}

	/**
	 * @return the ejbId
	 */
	public String getEjbId() {
		return ejbId;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the newPropertyValue
	 */
	public String getNewPropertyValue() {
		return newPropertyValue;
	}

	/**
	 * @return the oldPropertyValue
	 */
	public String getOldPropertyValue() {
		return oldPropertyValue;
	}

	/**
	 * @return the propertyType
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * @return the propertyUnit
	 */
	public String getPropertyUnit() {
		return propertyUnit;
	}

	/**
	 * @return the propertyDescription
	 */
	public String getPropertyDescription() {
		return propertyDescription;
	}

	/**
	 * @return the notificationId
	 */
	public String getNotificationId() {
		return notificationId;
	}

}
