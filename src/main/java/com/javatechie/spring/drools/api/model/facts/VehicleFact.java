package com.javatechie.spring.drools.api.model.facts;

import java.util.List;

/**
 * @author frank
 * @since Jun 8, 2008
 */
public interface VehicleFact extends Fact {
	
	public int getMakeCharacteristicId();
	
	public List<Integer> getCharacteristicIds();

	public boolean containsCharacteristicId( Integer charId );

	public boolean isModelYearInFromRange( int fromModelYear );

	public boolean isModelYearInToRange( int toModelYear );

	/**
	 * the vehicle age is calculated based on the vehicles registration date (VIN table, column registrationDate = first Registration Date).
	 * If no registration date is given, the model year is used to caluclate the age
	 * 
	 * @param fromVehicleAge
	 *            The vehicle age in years
	 */
	public boolean isVehicleAgeOlderOrSame( int fromVehicleAge, int fromVehicleMonth );

	/**
	 * the vehicle age is calculated based on the vehicles registration date (VIN table, column registrationDate = first Registration Date).
	 * If no registration date is given, the model year is used to caluclate the age
	 * 
	 * @param toVehicleAge
	 *            The vehicle age in years
	 */
	public boolean isVehicleAgeYoungerOrSame( int toVehicleAge, int toVehicleMonth );

}