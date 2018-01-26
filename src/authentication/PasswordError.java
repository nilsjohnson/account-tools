package authentication;

/**
 * These are the possible errors that can be describe an incorrectly formatted
 * password.
 * 
 * @author Nils Johnson
 */
public enum PasswordError
{
	TOO_SHORT,
	NEEDS_UPPER,
	NEEDS_LOWER,
	NEEDS_NUMBER,
	NEEDS_SPECIAL_CHAR,
	HAS_ILLEGAL_CHAR,
	TOO_LONG,
	MIS_MATCH;
}
