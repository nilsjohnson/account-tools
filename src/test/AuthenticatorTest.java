package test;


import java.util.ArrayList;

import authentication.Authenticator;
import authentication.BadLoginException;
import authentication.LoginError;
import authentication.NewUserException;
import authentication.PasswordError;
import authentication.UsernameError;



public class AuthenticatorTest
{

	/**
	 * This program demonstrates usage of the Authenticator object
	 * 
	 * @author Nils Johnson
	 * @param args
	 */
	public static void main(String[] args)
	{
		// make authenticator object that works with your object. -> See
		// ExampleUserObject
		Authenticator<ExampleUserObject> authenticator = new Authenticator<>();

		// sample login credentials
		final String USERNAME = "Bryndlle";
		final String PASSWORD = "Pass123sd&";

		// if desired, you may validate legality of username and password using
		// Authenticator's static methods
		ArrayList<PasswordError> pwErrorList = Authenticator.checkPasswordLegality(PASSWORD);
		ArrayList<UsernameError> usernameErrorList = Authenticator.checkUsernameLegality(USERNAME);

		// both lists remain null, there are no errors.
		if (pwErrorList == null && usernameErrorList == null)
		{
			System.out.println("This username/password combination is legal");
			// this is where you should call authenticator.createUser to prevent a frivolous
			// database call.
		}
		else if (pwErrorList != null)
		{
			System.out.println("Password Issues:\n");
			for (PasswordError error : pwErrorList)
			{
				System.out.println(error.toString());
			}
		}
		else if (usernameErrorList != null)
		{
			System.out.println("Username Issues:\n");
			for (UsernameError error : usernameErrorList)
			{
				System.out.println(error.toString());
			}
		}

		// --------Making a new user -------//
		try
		{
			// create an Object to associate with user.
			ExampleUserObject example = new ExampleUserObject(USERNAME, "Blue", 12.9);

			// create this user.
			authenticator.createUser(USERNAME, PASSWORD, PASSWORD, null);
		}
		// catch any NewUserExceptions
		catch (NewUserException e)
		{
			// getErrors returns an array of Enums representing the problems.
			Enum<?>[] errors = e.getErrors();

			for (Enum<?> error : errors)
			{
				if (error instanceof PasswordError)
				{
					System.out.println("Password Error: " + error.toString());

					switch ((PasswordError) error)
					{
					case HAS_ILLEGAL_CHAR:
						break;
					case MIS_MATCH:
						break;
					case NEEDS_LOWER:
						break;
					case NEEDS_NUMBER:
						break;
					case NEEDS_SPECIAL_CHAR:
						break;
					case NEEDS_UPPER:
						break;
					case TOO_LONG:
						break;
					case TOO_SHORT:
						break;
					default:
						break;
					}
				}
				else if (error instanceof UsernameError)
				{
					System.out.println("Username Error: " + error.toString());

					switch ((UsernameError) error)
					{
					case HAS_ILLEGAL_CHAR:
						break;
					case TOO_LONG:
						break;
					case TOO_SHORT:
						break;
					case UNAVAILABLE:
						break;
					default:
						break;
					}
				}
			}

		}
		
		
		// --------Getting a user's object back -------

		// user's object
		ExampleUserObject object = null;

		try
		{
			// if valid credentials are entered, retrieves object.
			object = (ExampleUserObject) authenticator.getUser(USERNAME, PASSWORD);
		}
		catch (BadLoginException ex)
		{	
			LoginError error = ex.getError();
			String errorMessage = ex.getMessage();
			
			System.out.println("Login Error: " + error.toString());
			System.out.println(errorMessage);
			
			switch(error)
			{
			case INVALID_ATTEMPT:
				break;
			case INVALID_PASSWORD:
				break;
			case NO_USERS:
				break;
			case USER_NOT_FOUND:
				break;
			default:
				break;
			}
		}

		// now that you've fetched your user's object, do whatever with it.
		object.favoriteColor = "Orange";
		object.favoriteNumber = 832.567;

		// when it's time, simply save it. 
		authenticator.saveUser(object);
		
		// and if you need to, you can delete this user and their object
		authenticator.deleteUser(object);
	}
}
