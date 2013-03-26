/*******************************************************************************
 * Copyright (c) 2012 Gary F. Pollice
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: gpollice
 *******************************************************************************/

package cs3733.hw1;

import java.util.Iterator;
import java.util.PriorityQueue;
import cs3733.hw1.FrequentFlyerLevel;;
/**
 * Description
 * 
 * @author gpollice
 * @version Mar 19, 2013
 */
public class FrequentFlyer
{
	private final String frequentFlyerId;
	private int pointsAvailable;
	private FrequentFlyerLevel frequentFlyerLevel;
	private PriorityQueue<FFTransaction> transactionHistory;
	

	private float pointsMultiplier;
	
	/**
	 * Constructor. The only identification for a frequent flyer is the ID, which will be used by
	 * clients for various purposes.
	 * 
	 * @param frequentFlyerId
	 *            the frequent flyer ID
	 */
	public FrequentFlyer(String frequentFlyerId)
	{
		this.frequentFlyerId = frequentFlyerId;
		this.pointsAvailable = 0;
		this.frequentFlyerLevel = FrequentFlyerLevel.BASIC;
		this.transactionHistory = new PriorityQueue<FFTransaction>();
		this.pointsMultiplier= 1;
	}

	/**
	 * Record the completion of a flight for the frequent flyer. This should update the flyer's
	 * level and points appropriately. You can assume that the airport codes are valid.
	 * 
	 * This should also create an FFTransaction for this flyer and add it to the end of the
	 * transaction history.
	 * 
	 * @param from
	 *            the source airport's code
	 * @param to
	 *            the destination airport's code
	 * @return the frequent flyer's point level after this flight (truncating any fractions).
	 */
	public int recordFlight(String from, String to)
	{
		// TODO
		return 0;
	}

	/**
	 * Redeem points to pay for a flight. As long as there are enough points in the account to cover
	 * the cost of the flight (10 points per mile), the points are removed from the flyer's
	 * available total.
	 * 
	 * This should also create an FFTransaction for this flyer and add it to the end of the
	 * transaction history.
	 * 
	 * @param from
	 *            the source airport's code
	 * @param to
	 *            the destination airport's code
	 * @returnthe frequent flyer's point level after this redemption (truncating any fractions).
	 * @throws InsufficientPointsException if there not enough points to pay for the flight
	 */
	public int redeemPoints(String from, String to)
			throws InsufficientPointsException
	{
		// TODO
		return 0;
	}
	
	/**
	 * This method is used by the airline to adjust the frequent flyer's points
	 * This can e done to compensate the flyer for inconviences, award bonuses,
	 * or make any other adjustment.
	 * 
	 * This will also add a FFTransaction to the flyer's history. The transaction
	 * will have a <code>from</code> string of "ADJUST" and a null <code>to</code>
	 * string.
	 *
	 * @param adjustment the amount to be added to the points (can be negative)
	 * @return the resulting points available for this flyer, truncating any 
	 * 	fractional points.
	 */
	public int adjustBalance(int adjustment)
	{
		// TODO
		return 0;
	}
	
	/**
	 * Return an iterator to the transaction history. This should return an iterator
	 * to the collection of FFTransactions for this frequent flyer. The iterator should
	 * return the transactions in the order in which they were added to the flyer's 
	 * history (that is the oldest transaction first).
	 * @return
	 */
	public Iterator<FFTransaction> getTransactionHistory()
	{
		return transactionHistory.iterator();
	}
	
	/**
	 * @return the points available for this flyer, truncating any fractional points.
	 */
	public int getPointsAvailable()
	{
		return pointsAvailable;
	}
	
	/**
	 * @return this frequent flyer's current level
	 */
	public FrequentFlyerLevel getFrequentFlyerLevel()
	{
		return frequentFlyerLevel;
	}

	/**
	 * @return the frequent flyer ID
	 */
	public String getFrequentFlyerId()
	{
		return frequentFlyerId;
	}
}
