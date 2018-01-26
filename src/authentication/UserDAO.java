package authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import authentication.Authenticator;
import authentication.BadLoginException;
import authentication.BadPasswordException;
import authentication.BadUsernameException;
import authentication.EncryptionFilter;

import authentication.NewUserException;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Nils Johnson
 */
// TODO - Try with resources for each call, instead of try-catch-finally
public class UserDAO <T extends IsetUser> extends DAO implements IUserDAO<T>
{

	/**
	 * This constructor will make a table of Users if it does not exist.
	 * 
	 * @throws SQLException
	 */
	public UserDAO() throws SQLException
	{
		super();
	}

	/**
	 * This method inserts a user into the SQLite database
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Override
	public void createUser (String enteredUsername, String enteredPassword, String enteredPwConfirm, Object object) throws NewUserException
	{
		// verify a legally formatted name is entered
		ArrayList<UsernameError> usernameErrorList = Authenticator.checkUsernameLegality(enteredUsername);

		// if there is an error list, throw an exception with that list
		if (usernameErrorList != null)
		{
			BadUsernameException e = new BadUsernameException(usernameErrorList);
			throw (e);
		}

		// check to see if name is available
		if (!usernameIsAvailable(enteredUsername))
		{
			ArrayList<UsernameError> errors = new ArrayList<>();
			errors.add(UsernameError.UNAVAILABLE);
			NewUserException e = new BadUsernameException(errors);
			throw (e);
		}

		// verify password legality
		ArrayList<PasswordError> pwErrorList = Authenticator.checkPasswordLegality(enteredPassword);

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

		// if everything checks out, then put in the database, and make a file for the user's Object
		try
		{
			openConnection();
			String statement = "insert into Users (userName, passwordCipher , filePath) " + "values (?, ?, ?)";
			PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

			preparedInsertStatement.setString(1, enteredUsername);
			preparedInsertStatement.setString(2, EncryptionFilter.encrypt(enteredPassword, enteredPassword));
			preparedInsertStatement.setString(3, enteredUsername + ".dat");

			preparedInsertStatement.executeUpdate();
			
			// make this user a file and serialize their initial object
			// File pwListFile = new File();
			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(enteredUsername + ".dat"));)
			{
				output.writeObject(object);
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
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		
	}

	@Override
	public Object getUserObject(String enteredUsername, String enteredPassword) throws BadLoginException
	{
		// If the input is blatantly invalid, throw exception before making database call.
		if (enteredUsername.length() < Authenticator.MIN_NAME_LENGTH || enteredPassword.length() < Authenticator.MIN_PW_LENGTH)
		{
			throw new BadLoginException(LoginError.INVALID_ATTEMPT);
		}

		// variables used within method to determine the result of the login attempt
		Object user = null;
		String returnedUsername = null;
		String returnedPasswordCipher = null;
		String returnedFilePath = null;

		try
		{
			openConnection();

			String userNameStatement = "SELECT userName, passwordCipher, filePath  FROM Users WHERE userName = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(userNameStatement);
			preparedStatement.setString(1, enteredUsername);

			ResultSet rSet = preparedStatement.executeQuery();

			// if the query returns, the user exists
			if (rSet.next())
			{
				returnedUsername = rSet.getString(1);
				returnedPasswordCipher = rSet.getString(2);
				returnedFilePath = rSet.getString(3);

			}
			else if (returnedUsername == null)
			{
				throw new BadLoginException(LoginError.USER_NOT_FOUND);
			}

			// see if the entered password matches the passwordChipher when decrypted with
			// the provided password
			if (!enteredPassword.equals(EncryptionFilter.decrypt(returnedPasswordCipher, enteredPassword)))
			{
				throw new BadLoginException(LoginError.INVALID_PASSWORD);
			}

			// Deserialize the object found at the path
			// this can throw FileNotFoundException, IOException back to wherever getUser
			// was called from.
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(returnedFilePath));)
			{
				user = input.readObject();
			}
			catch (ClassNotFoundException | IOException e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			

		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return user;
	}

	@Override
	public boolean usernameIsAvailable(String enteredUserName)
	{
		boolean result = true;
		try
		{
			openConnection();

			String statement = "select userName from Users where userName = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, enteredUserName);

			ResultSet rSet = preparedStatement.executeQuery();

			if (rSet.next())
			{
				String returnedName = rSet.getString(1);
				if (returnedName.equals(enteredUserName))
				{
					result = false;
				}
			}
			else
			{
				result = true;
			}

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		return result;
	}

	@Override
	public void deleteUser(T object)
	{
		// name of the user to delete
		String username = object.getUsername();
		
		try
		{
			openConnection();

			String statement = "DELETE from Users where userName = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, username);

			preparedStatement.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		File userFile = new File(username + ".dat");

		if (userFile.delete())
		{
			System.out.println("File delete success for " + username);
		}
		else
		{
			System.out.println("File delete failed for " + username);
		}
	}


	@Override
	public void saveUserObject(T object)
	{
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(object.getUsername() + ".dat"));)
		{
			output.writeObject(object);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}

}
