package authentication;

/**
 * These are used to represent the possible errors that can occur when creating
 * a user name.
 * 
 * @author Nils
 *
 */
public enum UsernameError
{
	TOO_SHORT,
	TOO_LONG,
	UNAVAILABLE,
	HAS_ILLEGAL_CHAR;
}
