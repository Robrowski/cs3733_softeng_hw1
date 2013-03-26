/*******************************************************************************
 * Copyright (c) 2012 Gary F. Pollice
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    gpollice
 *******************************************************************************/

package cs3733.hw1;

import static org.junit.Assert.*;
import java.util.Iterator;
import org.junit.*;

/**
 * Description
 *
 * @author gpollice
 * @version Mar 19, 2013
 */
public class FrequentFlyerStarterTest
{
	private FrequentFlyer flyer1, flyer2;

	/**
	 * Put some things into the distanceTable;
	 */
	@BeforeClass
	public static void setupContext()
	{
		final DistanceTable dt = DistanceTable.getInstance();
		dt.clear();
		dt.addAirportPair("BOS", "JFK", 187);
		dt.addAirportPair("EWR", "BOS", 200);
		dt.addAirportPair("ATL", "BOS", 946);
		dt.addAirportPair("JFK", "SFO", 2578);
		dt.addAirportPair("DEN", "SEA", 1022);
		dt.addAirportPair("ORD", "ORD", 885);
		dt.addAirportPair("ORD", "BOS", 864);
	}
	
	/**
	 * Create frequent flyers for the test.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		flyer1 = new FrequentFlyer("F0001");
		flyer2 = new FrequentFlyer("F0002");
		FFTransaction.resetTransactionNumbers();
	}

	@Test
	public void idIsCorrect()
	{
		assertEquals("F0001", flyer1.getFrequentFlyerId());
	}
	
	@Test
	public void basicMemberFlightUpdatesPoints()
	{
		assertEquals(2578, flyer1.recordFlight("JFK", "SFO"));
	}

	@Test
	public void initialLevelIsBasic()
	{
		assertEquals(FrequentFlyerLevel.BASIC, flyer2.getFrequentFlyerLevel());
	}
	
	@Test
	public void enterFlightChangingLevels()
	{
		flyer1.adjustMilesFlown(24900);
		flyer1.recordFlight("BOS", "EWR");
		assertEquals(FrequentFlyerLevel.SILVER, flyer1.getFrequentFlyerLevel());
		assertEquals(25125, flyer1.getPointsAvailable());
	}
	
	@Test
	public void historyTransactionIsCorrect()
	{
		flyer1.recordFlight("BOS", "EWR");
		Iterator<FFTransaction> history = flyer1.getTransactionHistory();
		assertTrue(history.hasNext());
		FFTransaction fft = history.next();
		assertEquals("BOS", fft.getFrom());
		assertEquals("EWR", fft.getTo());
		assertEquals(1, fft.getTransactionNumber());
		assertEquals(200.0, fft.getTransactionAmount(), 0.0001);
	}

	/** Tests bidirectional adjustment to ensure that the same miles adds/subtracts the 
	 *  same number of points, whether positive or negative. 
	 * 
	 * This is a fully test of the adjustMilesFlown method because it exercises all 
	 *  frequent flyer levels and mathematic functions in the method.
	 */
	@Test
	public void bidirectionalAdjustmentTest(){
		System.out.println("Points after: "+ flyer1.getPointsAvailable());
		flyer1.adjustMilesFlown(250000); // an amount that spans all levels
		System.out.println("Points after: "+ flyer1.getPointsAvailable());
		flyer1.adjustMilesFlown(-250000);// Adjust backwards
		System.out.println("Points after: "+ flyer1.getPointsAvailable());
		assertTrue(flyer1.getPointsAvailable() ==0); // It should be back to zero...
		assertTrue(flyer1.getMilesFlown()== 0);		
	}
	
	
	
	
	/** Checks ordering of transactions
	 * 
	 */
	@Test
	public void largeHistoryTransactionIsCorrect()
	{
		flyer1.recordFlight("BOS", "EWR");
		flyer1.recordFlight("JFK", "SFO");
		flyer1.recordFlight("ATL", "BOS");
		flyer1.recordFlight("ORD", "BOS");
		flyer1.recordFlight("ORD", "BOS");
		flyer1.recordFlight("BOS", "JFK");
		
		Iterator<FFTransaction> history = flyer1.getTransactionHistory();
		assertTrue(history.hasNext());
		FFTransaction fft = history.next();
		assertEquals("BOS", fft.getFrom());
		assertEquals("EWR", fft.getTo());
		
		assertTrue(history.hasNext());
		fft = history.next();
		assertEquals("JFK", fft.getFrom());
		assertEquals("SFO", fft.getTo());
		
		assertTrue(history.hasNext());
		fft = history.next();
		assertEquals("ATL", fft.getFrom());
		assertEquals("BOS", fft.getTo());
		
		assertTrue(history.hasNext());
		fft = history.next();
		assertEquals("ORD", fft.getFrom());
		assertEquals("BOS", fft.getTo());
		
		assertTrue(history.hasNext());
		fft = history.next();
		assertEquals("ORD", fft.getFrom());
		assertEquals("BOS", fft.getTo());
		
		assertTrue(history.hasNext());
		fft = history.next();
		assertEquals("BOS", fft.getFrom());
		assertEquals("JFK", fft.getTo());
	}
}
