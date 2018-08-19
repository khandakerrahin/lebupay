/**
 * @formatter:off
 *
 */
package com.lebupay.service;
import com.lebupay.model.LoyaltyModel;

/**
 * This is LoyaltyService Interface is used to perform operations on Loyalty Module.
 * @author Java Team
 *
 */
public interface LoyaltyService {
	
	/**
	 * This method is used for fetching the active Loyalty by type.
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel getA2P()  throws Exception ;
	
	/**
	 * Amount to Point save or update performs in this method.
	 * @param loyaltyModel
	 * @param adminId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel saveOrUpdateA2PLayalty(LoyaltyModel loyaltyModel,long adminId) throws Exception;
	
	
	/**
	 * This method is used for fetching the Point to Amount Details from Database.
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel getP2A()  throws Exception ;
	
	/**
	 * Point to amount save or update performs in this method.
	 * @param loyaltyModel
	 * @param adminId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel saveOrUpdateP2ALayalty(LoyaltyModel loyaltyModel,long adminId) throws Exception;
	
}
