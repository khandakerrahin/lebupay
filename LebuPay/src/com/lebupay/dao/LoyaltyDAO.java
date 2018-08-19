/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import com.lebupay.model.LoyaltyModel;

/**
 * This interface is used to declare methods for Loyalty Database Operations.
 * @author Java Team
 *
 */
public interface LoyaltyDAO {

	
	/**
	 * This method is used for inserting Loyalty.
	 * @param loyaltyModel
	 * @return long
	 * @throws Exception
	 */
	public long insertLoyalty(LoyaltyModel loyaltyModel) throws Exception ;
	
	/**
	 * This method is used for updating Loyalty.
	 * @param loyaltyModel
	 * @return int
	 * @throws Exception
	 */
	public int updateLoyalty(LoyaltyModel loyaltyModel) throws Exception ;
	
	/**
	 * This method is used for fetching active Loyalty By LoyalityType.
	 * @param loyalityType
	 * @return List<LoyaltyModel>
	 * @throws Exception
	 */
	public LoyaltyModel fetchActiveLoyaltyByType(String loyalityType)  throws Exception ;
	
	/**
	 * This method is used for fetching Loyalty By Id.
	 * @param loyaltyId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel fetchActiveLoyaltyByID(long loyaltyId)  throws Exception ;

}
