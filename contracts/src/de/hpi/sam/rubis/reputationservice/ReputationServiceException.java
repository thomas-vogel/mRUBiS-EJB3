package de.hpi.sam.rubis.reputationservice;

/**
 * Exception indicating a failure of the reputation service.
 * 
 * @author thomas vogel
 * 
 */
public class ReputationServiceException extends Exception {

	private static final long serialVersionUID = -3159390918658037889L;

	public ReputationServiceException() {
		super();
	}

	public ReputationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReputationServiceException(String message) {
		super(message);
	}

	public ReputationServiceException(Throwable cause) {
		super(cause);
	}

}
