/*******************************************************************************
 * Copyright (c) 2012 Gary F. Pollice
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: gpollice, Alex Solomon, Rob Dabrowski, Gonzo
 *******************************************************************************/

package cs3733.hw1;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Description
 * 
 * @author Alex Solomon and Rob Dabrowski
 * @version Mar 19, 2013
 */
public class FrequentFlyer
{
	private final String frequentFlyerId;
	private double pointsAvailable;
	private int milesFlown; 
	private FrequentFlyerLevel frequentFlyerLevel;
	private ArrayList<FFTransaction> transactionHistory;
	private DistanceTable distTable = DistanceTable.getInstance();
	
	// These are the minimum number of miles needed to gain a level
	@SuppressWarnings("unused")
	private final static int basicMiles = 0;
	private final static int silverMiles = 25000;
	private final static int goldMiles = 50000;
	private final static int platMiles = 100000;
	
	// These are the multipliers associated with the different levels
	private final static double basicMult = 1;
	private final static double silverMult = 1.25;
	private final static double goldMult = 1.5;
	private final static double platMult = 2;
	

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
		this.transactionHistory = new ArrayList<FFTransaction>();
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
		double startingPoints = pointsAvailable;
		int miles = distTable.getDistance(from, to); // Miles for current flight in question
		if (miles > 0 && frequentFlyerLevel == FrequentFlyerLevel.BASIC) { 
			if (silverMiles - milesFlown <= miles) { // Indicates that a level has been earned
				int milesEarnedAtThisLevel =  (miles - (silverMiles - milesFlown)); // Miles flow at this level
				miles -= milesEarnedAtThisLevel; // Update remaining miles from current flight
				pointsAvailable +=  milesEarnedAtThisLevel*basicMult ; // update points earned at this level
				frequentFlyerLevel = FrequentFlyerLevel.SILVER; // update level
			} else {
				pointsAvailable += miles*basicMult; // update points
				miles = 0; // All miles entered
			}
		}
		if(miles > 0 && frequentFlyerLevel == FrequentFlyerLevel.SILVER){
			if (goldMiles - milesFlown <= miles) { // Indicates that a level has been earned
				int milesEarnedAtThisLevel =  (miles - (goldMiles - milesFlown)); // Miles flow at this level
				miles -= milesEarnedAtThisLevel; // Update remaining miles from current flight
				pointsAvailable +=  milesEarnedAtThisLevel*silverMult ; // update points earned at this level
				frequentFlyerLevel = FrequentFlyerLevel.GOLD; // update level
			} else {
				pointsAvailable += miles*silverMult; // update points
				miles = 0; // All miles entered
			}
		}
		if(miles > 0 && frequentFlyerLevel == FrequentFlyerLevel.GOLD){
			if (platMiles - milesFlown <= miles) { // Indicates that a level has been earned
				int milesEarnedAtThisLevel =  (miles - (platMiles - milesFlown)); // Miles flow at this level
				miles -= milesEarnedAtThisLevel; // Update remaining miles from current flight
				pointsAvailable +=  milesEarnedAtThisLevel*goldMult ; // update points earned at this level
				frequentFlyerLevel = FrequentFlyerLevel.PLATINUM; // update level
			} else {
				pointsAvailable += miles*goldMult; // update points
				miles = 0; // All miles entered
			}
		}
		if(miles > 0 && frequentFlyerLevel == FrequentFlyerLevel.PLATINUM){
			pointsAvailable += miles*platMult; // update all points
			miles = 0; // all miles have been added
		}
		
		// Storing the History Log
		transactionHistory.add(new FFTransaction(from, to, pointsAvailable - startingPoints));
		
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
	 * @return the frequent flyer's point level after this redemption (truncating any fractions).
	 * @throws InsufficientPointsException if there not enough points to pay for the flight
	 */
	public int redeemPoints(String from, String to)
			throws InsufficientPointsException
	{
		int pointsNeeded = distTable.getDistance(from, to) * 10;
		if (pointsAvailable < pointsNeeded) throw new InsufficientPointsException("Insufficient points");
		
		pointsAvailable -= pointsNeeded;
		transactionHistory.add(new FFTransaction(from, to, -pointsNeeded));
				
		return (int) pointsAvailable;
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
		pointsAvailable += adjustment;
		transactionHistory.add(new FFTransaction("ADJUST", null, adjustment));
		return (int) pointsAvailable;
	}
	
	/**
	 * This method is used by the airline to adjust the number of miles flown by the
	 * frequent flyer. It's similar to adjustBalance, but it adjusts the miles rather
	 * than the points. This does, however, adjust the points (and possibly the 
	 * level) appropriately. 
	 * 
	 * @param the mileage adjustment
	 * @return the resulting points (not miles) available for this flyer, truncating
	 * 	any fractional points.
	 */
	public int adjustMilesFlown(int adjustment)
	{
		milesFlown += adjustment;
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
		return (int) pointsAvailable;
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
