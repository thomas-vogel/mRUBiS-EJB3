package de.hpi.sam.rubis.authservice;

import javax.ejb.Remote;

import de.hpi.sam.rubis.RubisNameSchema;
import de.hpi.sam.rubis.entity.User;

/**
 * Service for authenticating a user.
 * 
 * @author thomas vogel
 * 
 */
@Remote
public interface AuthenticationService {

	/**
	 * Mapped name to find the service.
	 */
	public static String MAPPED_NAME = RubisNameSchema.MAPPED_NAME_PREFIX
			+ "AuthenticationService";

	/**
	 * @param nickname
	 *            user nick name
	 * @param password
	 *            user password
	 * @return the authenticated user. If the user cannot be found or
	 *         authenticated, the exception is thrown instead of returning
	 *         <code>null</code>.
	 * @throws AuthenticationServiceException
	 *             if the authentication fails
	 */
	public User authenticate(String nickname, String password)
			throws AuthenticationServiceException;

}
