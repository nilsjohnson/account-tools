package accounttools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import encryptionfilter.EncryptionFilter;

/*
 * This class handles users. It can create them and authenticate them. 
 */
public class AccountTools
{
	// file for to store users
	private static final String FILE_NAME = "keep/users.dat";
	private static File userList = new File(FILE_NAME);

	// structure to hold users
	private static ArrayList<User> users = null;

	// length of randomly generated keys, if chosen to use.
	private static int KEY_LENGTH = 35;

	// for passwords
	public static final char[] LEGAL_PW_SPECIAL_CHARACTER = { '!', '@', '#', '$', '%', '^', '&', '*' };
	public static final int MIN_PW_LENGTH = 4;
	public static final int MAX_PW_LENGTH = 20;

	// for usernames
	public static final int MIN_NAME_LENGTH = 4;
	public static final int MAX_NAME_LENGTH = 15;
	public static final char[] ILLEGAL_NAME_CHARACTER = { ' ', '!', '#', '%', '^', '&', '*' };

	public static User getUserObject(String enteredUserName, String enteredPassword) throws BadLoginException
	{
		if (users == null)
		{
			loadUserList();
		}

		boolean foundUser = false;
		int i = 0;
		if (isValidUser(enteredUserName, enteredPassword))
		{
			// this block gets the index of the user
			do
			{
				if (users.get(i).getUsername().equals(enteredUserName))
				{
					foundUser = true;
				}
				else
				{
					i++;
				}
			} while (foundUser == false);
			return users.get(i);
		}
		else
		{
			return null;
		}

	}

	public static boolean isValidUser(String enteredUserName, String enteredPassword) throws BadLoginException
	{
		// temp user and index variable to go through array list of users
		User temp = new User();
		String nameInFile, pwCipher;
		String decryptedPw = null;
		int i = 0;

		// load the user list to memory for indexing
		loadUserList();

		// if there are no users in the file, throw exception
		if (users.size() == 0)
		{
			BadLoginException e = new BadLoginException(LoginError.NO_USERS);
			throw (e);
		}
		// otherwise, start comparing users
		else
		{
			do
			{
				temp = users.get(i);
				nameInFile = temp.getUsername();
				pwCipher = temp.getPwCipherText();
				decryptedPw = EncryptionFilter.decrypt(pwCipher, enteredPassword);
				i++;
			} while (i < users.size() && !nameInFile.equals(enteredUserName));
		}

		// there is a match between the username and the password decrypted with the
		// password, return true
		if (temp.getUsername().equals(enteredUserName) && decryptedPw.equals(enteredPassword))
		{
			return true;
		}
		// otherwise, depending on the mismatch, throw an exception
		else
		{
			if (!temp.getUsername().equals(enteredUserName))
			{
				BadLoginException e = new BadLoginException(LoginError.USER_NOT_FOUND);
				throw (e);
			}
			else if (temp.getUsername().equals(enteredUserName) && !decryptedPw.equals(enteredPassword))
			{
				BadLoginException e = new BadLoginException(LoginError.INVALID_PASSWORD);
				throw (e);
			}
		}
		return false;

	}

	public static void makeNewAccount(String enteredUserName, String enteredPassword, String enteredPwConfirm) throws NewUserException, FileNotFoundException, IOException
	{
		// verifiy a legally formatted name is entered
		ArrayList<UsernameError> usernameErrorList = checkUsernameLegality(enteredUserName);

		if (usernameErrorList != null)
		{
			BadUsernameException e = new BadUsernameException(usernameErrorList);
			throw (e);
		}

		// if list hasnt been loaded, loads it.
		if (users == null)
		{
			loadUserList();
		}

		// counts the index when looping through current users

		if (!isAvailable(enteredUserName))
		{
			ArrayList<UsernameError> errors = new ArrayList<>();
			errors.add(UsernameError.UNAVAILABLE);
			NewUserException e = new BadUsernameException(errors);
			throw (e);
		}

		// verify password legality
		ArrayList<PasswordError> pwErrorList = checkPasswordLegality(enteredPassword);

		// if pw was illegal and they match
		if (pwErrorList != null && enteredPassword.equals(enteredPwConfirm))
		{
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}
		// if pw was illegal and they don't match
		if (pwErrorList != null && !enteredPassword.equals(enteredPwConfirm))
		{
			pwErrorList.add(PasswordError.MIS_MATCH);
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}
		// if 'entredPassword' was legal but doesnt match the confirm
		if (!enteredPassword.equals(enteredPwConfirm))
		{
			pwErrorList = new ArrayList<>();
			BadPasswordException e = new BadPasswordException(pwErrorList);
			throw (e);
		}

		// puts user in array list of existing users
		users.add(new User(enteredUserName, EncryptionFilter.encrypt(enteredPassword, enteredPassword)));
		// writes the new list to the file
		writeUsersToFile();

		// TOOD put this not in accounttools
		// make a new file for that user
		File pwListFile = new File("keep/" + enteredUserName + ".dat");
		pwListFile.createNewFile();
	}

	public static boolean isAvailable(String enteredUserName)
	{
		String nameInFile;
		int i = 0;
		while (i < users.size())
		{
			nameInFile = users.get(i).getUsername();
			if (nameInFile.equals(enteredUserName))
			{
				return false;
			}
			i++;
		}
		;
		return true;
	}

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
			if (numUpper + numLower + numDigits + numSpecial != i + 1)
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
			errorList.add(PasswordError.NEEDS_SPECIAL);
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

	public static String getRandomKey()
	{
		StringBuilder key = new StringBuilder(KEY_LENGTH);

		for (int i = 0; i < KEY_LENGTH; i++)
		{
			key.append((char) (new Random().nextInt(255) + 0));
		}

		return key.toString();
	}

	private static void loadUserList()
	{
		if (userList.length() > 0)
		{
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(userList)))
			{
				try
				{
					users = new ArrayList<User>((ArrayList<User>) (input.readObject()));
				}
				catch (ClassNotFoundException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			users = new ArrayList<User>();
		}
	}

	private static void writeUsersToFile()
	{
		System.out.println("Writing all users to file");
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(userList, false));)
		{
			output.writeObject(users);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static int getNumberOfUsers()
	{
		if (users == null)
		{
			loadUserList();
		}
		return users.size();
	}
}
