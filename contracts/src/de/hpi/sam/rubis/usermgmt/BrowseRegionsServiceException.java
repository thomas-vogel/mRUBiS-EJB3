package de.hpi.sam.rubis.usermgmt;

/**
 * Exception indicating a failure in browsing the regions.
 * 
 * @author thomas vogel
 * 
 */
public class BrowseRegionsServiceException extends Exception {

	private static final long serialVersionUID = 3268247766180582192L;

	public BrowseRegionsServiceException() {
		super();
	}

	public BrowseRegionsServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrowseRegionsServiceException(String message) {
		super(message);
	}

	public BrowseRegionsServiceException(Throwable cause) {
		super(cause);
	}

}
