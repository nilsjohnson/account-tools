package authentication;

import java.io.FileNotFoundException;
import java.io.IOException;

import authentication.BadLoginException;
import authentication.NewUserException;


/**
 * This interface states which methods the DAO is to implement. This gives us
 * the advantage of being able to work on the program now and have it function as it
 * will, when the database is actually implemented.
 * 
 * @author Nils
 *
 */
public interface IUserDAO<T>
{
	void createUser (String userName, String enteredPassword, String enteredPasswordConfirm, Object object) throws NewUserException;
	void deleteUser(T object);
	Object getUserObject(String enteredUserName, String enteredPassword) throws BadLoginException;
	boolean usernameIsAvailable(String enteredUserName);
	void saveUserObject(T object);
}

