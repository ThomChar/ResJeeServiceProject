package com.instablog.connexion;


/**
 * L'exception InstaException est levee lorsqu'une transaction est inadequate.
 */
public class InstaException extends Exception{

	private static final long serialVersionUID = 1L;

    public InstaException(String message)
    {
        super(message);
    }
}
