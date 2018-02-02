package de.hpi.sam.rubis.authservice;

/**
 * Exception indicating a failure in authenticating a user.
 * 
 * @author thomas vogel
 * 
 */
public class AuthenticationServiceException extends Exception {

	private static final long serialVersionUID = -4471922924425215310L;

	public AuthenticationServiceException() {
		super();
	}

	public AuthenticationServiceException(String msg) {
		super(msg);
	}

	public AuthenticationServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
