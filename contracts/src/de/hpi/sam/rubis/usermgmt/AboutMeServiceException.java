package de.hpi.sam.rubis.usermgmt;

/**
 * Exception indicating a failure in retrieving information about the currently
 * logged in user.
 * 
 * @author thomas vogel
 * 
 */
public class AboutMeServiceException extends Exception {

	private static final long serialVersionUID = -6480227329257348465L;

	public AboutMeServiceException() {
		super();
	}

	public AboutMeServiceException(String msg) {
		super(msg);
	}

	public AboutMeServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
