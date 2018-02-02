package de.hpi.sam.rubis.filter;

/**
 * Exception indicating a failure in filtering items.
 * 
 * @author thomas vogel
 * 
 */
public class ItemFilterException extends Exception {

	private static final long serialVersionUID = -6063879395584141505L;

	public ItemFilterException() {
		super();
	}

	public ItemFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItemFilterException(String message) {
		super(message);
	}

}