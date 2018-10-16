package authentication;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class sets requirements for usernames/passwords and defines methods for
 * validating conformity.
 * 
 * @author Nils Johnson
 *
 */
public class Authenticator<T extends IsetUser>
{
	private UserDAO<T> userDAO;

	public static final char[] LEGAL_PW_SPECIAL_CHARACTER = { '!', '@', '#', '$', '^', '&', '*' };
	public static final char[] ILLEGAL_NAME_CHARACTER = { ' ', '#', '%', '^', '&', '*', '{', '}', '$', '+', '[', ']' };

	// if changing any length requirement, verify that existing users will not be
	// refused authentication - see UserDAO.getUser, where it throws exception with
	// error of "INVALID_ATTEMPT"
	public static final int MIN_PW_LENGTH = 4;
	public static final int MAX_PW_LENGTH = 20;
	public static final int MIN_NAME_LENGTH = 4;
	public static final int MAX_NAME_LENGTH = 15;
	

	/**
	 * Makes a new Authenticator Object
	 */
	public Authenticator()
	{
		try
		{
			userDAO = new UserDAO<T>();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Takes a String and returns null if it is a legally formatted
	 * password, otherwise it returns a list of the problems it has.
	 */
	public static ArrayList<PasswordError> checkPasswordLegality(String enteredPassword)
	{
		ArrayList<PasswordError> errorList = new ArrayList<PasswordError>();
		int numUpper = 0, numLower = 0, numDigits = 0, numSpecial = 0, numIllegalChar = 0;
		boolean isValidPassword = true;

		if (enteredPassword.length() < MIN_PW_LENGTH)
		{
			errorList.add(PasswordError.TOO_SHORT);
			isValidPassword = false;
		}
		else if (enteredPassword.length() > MAX_PW_LENGTH)
		{
			errorList.add(PasswordError.TOO_LONG);
			isValidPassword = false;
		}

		// go over the whole password, and track the requirements.
		for (int i = 0; i < enteredPassword.length(); i++)
		{
			if (enteredPassword.charAt(i) >= 'A' && enteredPassword.charAt(i) <= 'Z')
			{
				numUpper++;
			}
			if (enteredPassword.charAt(i) >= 'a' && enteredPassword.charAt(i) <= 'z')
			{
				numLower++;
			}
			if (enteredPassword.charAt(i) >= '0' && enteredPassword.charAt(i) <= '9')
			{
				numDigits++;
			}

			if (isLegalSpecialChar(enteredPassword.charAt(i)))
			{
				numSpecial++;
			}
			if (numUpper + numLower +
					numDigits +
					numSpecial != i + 1)
			{
				numIllegalChar++;
			}
		}

		if (numIllegalChar != 0)
		{
			errorList.add(PasswordError.HAS_ILLEGAL_CHAR);
			isValidPassword = false;
		}
		if (numUpper == 0)
		{
			errorList.add(PasswordError.NEEDS_UPPER);
			isValidPassword = false;
		}
		if (numLower == 0)
		{
			errorList.add(PasswordError.NEEDS_LOWER);
			isValidPassword = false;
		}
		if (numDigits == 0)
		{
			errorList.add(PasswordError.NEEDS_NUMBER);
			isValidPassword = false;
		}
		if (numSpecial == 0)
		{
			errorList.add(PasswordError.NEEDS_SPECIAL_CHAR);
			isValidPassword = false;
		}
		if (isValidPassword)
		{
			return null;
		}
		else
		{
			return errorList;
		}
	}

	private static boolean isLegalSpecialChar(char ch)
	{
		for (int i = 0; i < LEGAL_PW_SPECIAL_CHARACTER.length; i++)
		{
			if (ch == LEGAL_PW_SPECIAL_CHARACTER[i])
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Takes a String and returns null if it is a legally formatted
	 * username, otherwise it returns a list of the problems it has.
	 */
	public static ArrayList<UsernameError> checkUsernameLegality(String username)
	{
		ArrayList<UsernameError> errorList = new ArrayList<UsernameError>(4);
		boolean isValidUsername = true;
		boolean isIllegalChar;
		int numIllegalChar = 0;

		if (username.length() < MIN_NAME_LENGTH)
		{
			errorList.add(UsernameError.TOO_SHORT);
			isValidUsername = false;
		}
		else if (username.length() > MAX_NAME_LENGTH)
		{
			errorList.add(UsernameError.TOO_LONG);
			isValidUsername = false;
		}

		for (int i = 0; i < username.length(); i++)
		{
			isIllegalChar = false;
			for (int j = 0; !isIllegalChar && j < ILLEGAL_NAME_CHARACTER.length; j++)
			{
				if (username.charAt(i) == ILLEGAL_NAME_CHARACTER[j])
				{
					isIllegalChar = true;
					numIllegalChar++;
				}
			}
		}

		if (numIllegalChar > 0)
		{
			errorList.add(UsernameError.HAS_ILLEGAL_CHAR);
			isValidUsername = false;
		}
		if (isValidUsername)
		{
			return null;
		}
		else
		{
			return errorList;
		}
	}

	/**
	 * Makes a new user with an associated object.
	 * @param enteredUsername - desired username
	 * @param enteredPassword - desired password
	 * @param enteredPwConfirm - password confirm	
	 * @param object - an object of the type to be associated with the user. Not null.
	 * @throws NewUserException - Exception which describes possible issues.
	 */
	public void createUser(String enteredUsername, String enteredPassword, String enteredPwConfirm, Object object) throws NewUserException
	{
		userDAO.createUser(enteredUsername, enteredPassword, enteredPwConfirm, object);
	}

	/**
	 * Reads and returns users associated Object from file.
	 * 
	 * @param enteredUsername - the username
	 * @param enteredPassword - the password
	 * @return The user's Object
	 * @throws BadLoginException - Exception which details a failed object retrival. 
	 */
	public Object getUser(String enteredUsername, String enteredPassword) throws BadLoginException
	{
		return userDAO.getUserObject(enteredUsername, enteredPassword);
	}

	/**
	 * Deletes user's login credentials from database and associated object.
	 * 
	 * @param object - the user's associated object.
	 */
	public void deleteUser(T object)
	{
		userDAO.deleteUser(object);
	}

	/**
	 * Writes user's object to a file.
	 * @param object
	 */
	public void saveUserObject(T object)
	{
		userDAO.saveUserObject(object);
	}
}
