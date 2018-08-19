/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import com.lebupay.model.QuickPayModel;

/**
 * This interface is used to declare methods for QuickPay Database Operations.
 * @author Java-Team
 *
 */
public interface QuickPayDAO {

	
	/**
	 * This method is used for saving the QUICK PAY Details w.r.t Merchant ID.
	 * @param quickPayModel
	 * @return long
	 * @throws Exception
	 */
	public long saveQuickPay(QuickPayModel quickPayModel) throws Exception ;
	
	/**
	 * This method is used for fetching the QuickPay w.r.t Merchant and ID.
	 * @param quickPayId
	 * @param merchantId
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayById(long quickPayId, long merchantId) throws Exception ;
	
	/**
	 * This method is used for fetching the Quick Pay w.r.t Unique Keys.
	 * @param keys
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayByKeys(String keys) throws Exception ;

}