package de.hpi.sam.rubis.itemmgmt;

/**
 * Exception indicating a failure in registering new items.
 * 
 * @author thomas vogel
 * 
 */
public class ItemRegistrationServiceException extends Exception {

	private static final long serialVersionUID = 8039068466675276280L;

	public ItemRegistrationServiceException() {
		super();
	}

	public ItemRegistrationServiceException(String msg) {
		super(msg);
	}

	public ItemRegistrationServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
