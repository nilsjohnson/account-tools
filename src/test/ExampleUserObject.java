package test;

import java.io.Serializable;

import authentication.IsetUser;

/**
 * This class demonstrates how to extend user. Since User is serialized,
 * remember that members of this class must also implement serializable.
 * 
 * @author Nils Johnson
 */
public class ExampleUserObject implements Serializable, IsetUser
{
	public String username;
	public String favoriteColor;
	public double favoriteNumber;

	public ExampleUserObject(String username, String favoriteColor, double favoriteNumber)
	{
		setUsername(username);
		this.favoriteColor = favoriteColor;
		this.favoriteNumber = favoriteNumber;
	}

	@Override
	public void setUsername(String username)
	{
		this.username = username;
	}

	@Override
	public String getUsername()
	{
		return this.username;
	}
}
