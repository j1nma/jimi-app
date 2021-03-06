package edu.itba.paw.jimi.interfaces.daos;

import edu.itba.paw.jimi.models.User;

import java.util.Collection;
import java.util.Set;

public interface UserDao {

	User findById(final long id);

	/**
	 * Create a new user.
	 *
	 * @param username The name of the user.
	 * @param password The password of the user.
	 * @param roles    A set of roles.
	 * @return The created user.
	 */
	User create(String username, String password, Set<String> roles);


	/**
	 * Finds the User by the username.
	 *
	 * @param username The username to search.
	 * @return the user with said username.
	 */
	User findByUsername(String username);


	/**
	 * Updates all the contents of the user.
	 *
	 * @param user The user to be updated.
	 */
	void update(User user);

	int getTotalUsers();

	/**
	 * Returns all the users paginated.
	 *
	 * @return all the users.
	 */
	Collection<User> findAll(int maxResults, int offset);

	/**
	 * Deletes a user.
	 *
	 * @param id Id of the user.
	 */
	void delete(final long id);
}
