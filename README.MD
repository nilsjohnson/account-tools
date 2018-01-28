This repository contains code that can be used in an application in which you wish to associate a user with object that can be retrieved with that user's login credentials.

Example useage: A resource game in which the user has an object containing their resources, which you would like to be able to save and reload for them.

Each use's username and password are stored in an SQLlite db. The username is stored as plaintext, and the password is stored as ciphertext with the key being the password itself. The purpose of this encryption is so that someone cant open the database and just see login credentials. This is not a secure system of storage. 

To use: See AuthenticatorExample.java and ExampleUserObject.java. 




