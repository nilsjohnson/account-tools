package authentication;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This class sets requirements for usernames/passwords and defines methods for validating conformity. 
 * @author Nils Johnson
 *
 */
public class Authenticator <T extends IsetUser>
{
	public UserDAO<T> userDAO;
	
	public static final char[] LEGAL_PW_SPECIAL_CHARACTER = { '!', '@', '#', '$', '^', '&', '*' };
	
	// if setting MIN_PW_LENGTH higher, verify that existing users will not be
	// refused login - see UserDAO.getUser
	public static final int MIN_PW_LENGTH = 4;
	public static final int MAX_PW_LENGTH = 20;

	// if setting the MIN_NAME_LENGTH higher, verify that current users will not be
	// refused login - see UserDAO.getUser
	public static final int MIN_NAME_LENGTH = 4;
	public static final int MAX_NAME_LENGTH = 15;
	public static final char[] ILLEGAL_NAME_CHARACTER = { ' ', '#', '%', '^', '&', '*', '{', '}', '$' };

	
	public Authenticator()
	{
		try
		{
			userDAO = new UserDAO();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method takes a String and returns null if it is a legally formatted
	 * password, otherwise it returns a list of the problems it has.
	 * 
	 * @param enteredPassword - The password being checked.
	 * @return List of problems found, or null if no problems found.
	 */
	public static ArrayList<PasswordError> checkPasswordLegality(String enteredPassword)
	{
		// new array list with space for 6 elements
		ArrayList<PasswordError> errorList = new ArrayList<PasswordError>(7);
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
	 * This method checks to see if a username is legally formatted. 
	 * 
	 * @param username The name being checked.
	 * @return errorList or null
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
	
	
	public void createUser(String enteredUsername, String enteredPassword, String enteredPwConfirm, Object object) throws NewUserException
	{
		userDAO.createUser(enteredUsername, enteredPassword, enteredPwConfirm, object);
	}
	
	public Object getUser(String enteredUsername, String enteredPassword) throws BadLoginException
	{
		return userDAO.getUserObject(enteredUsername, enteredPassword);
	}
	
	public void deleteUser(T object)
	{
		userDAO.deleteUser(object);
	}
	
	public void saveUser(T object)
	{
		userDAO.saveUserObject(object);
	}
}
