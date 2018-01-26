package authentication;

public enum LoginError
{
	/**
	 * Password does not decyrpt to be the entered password
	 */
	INVALID_PASSWORD,

	/**
	 * Username match not found
	 */
	USER_NOT_FOUND,

	/**
	 * There are no users
	 */
	NO_USERS,

	/**
	 * User input for name or password is too short. Flags condition in which a database
	 * call should not be made, due to obvious login issue. Example useage: User simply didnt enter a password.
	 */
	INVALID_ATTEMPT
}