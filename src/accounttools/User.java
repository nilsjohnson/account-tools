package accounttools;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable
{

	private static final long serialVersionUID = 1L;
	protected String userName;
	private String pwCipherText;
	private transient  String pwPlainText;
	

	public User(String userName, String pwCipherText)
	{
		this.userName = userName;
		this.pwCipherText = pwCipherText;
	
	}
	
	public User(User u)
	{
		this(u.userName, u.pwCipherText);
	}
	
	public User()
	{
		this.userName = null;
		this.pwCipherText = null;
	}

	public String getUsername()
	{
		return this.userName;
	}

	public String getPwCipherText()
	{
		return pwCipherText;
	}

	public void setPwPlainText(String pwPlainText)
	{
		this.pwPlainText = pwPlainText;
	}
	
	public String getPwPlainText()
	{
		return pwPlainText;
	}

	// TODO fix this, put elsewhere
	


}
