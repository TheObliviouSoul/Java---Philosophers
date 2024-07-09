/**
 * Class BaseThread
 * Simply one customized base class for many of our own threads.
 * <p>
 * An attempt to maintain an automatic unique TID (thread ID)
 * among all the derivatives and allow setting your own if needed.
 * Plus some methods for the sync exercises.
 * <p>
 * $Revision: 1.2 $
 * $Last Revision Date: 2021/05/11 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class BaseThread extends Thread
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * Our Thread ID
	 */
	protected int iTID;

    /*
	 * ------------
	 * Constructors
	 * ------------
	 */

	/**
	 * Sets user-specified TID
	 */
	public BaseThread(final int piTID)
	{
		this.iTID = piTID;
	}

	/**
	 * Retrieves our TID
	 * @return TID, integer
	 */
	public final int getTID()
	{
		return this.iTID;
	}

}

// EOF
