/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import com.lebupay.model.CheckoutModel;

/**
 * This Interface is used to declare method for CheckOut Operations.
 * @author Java Team
 *
 */
public interface CheckOutDAO {

	
	/**
	 * This method is used for update the checkout w.r.t Merchant ID.
	 * @param checkoutModel
	 * @return int
	 * @throws Exception
	 */
	public int updateCheckout(CheckoutModel checkoutModel) throws Exception ;
	
	/**
	 * This method is used internally for fetching the Checkout Details as per Merchant during merchant login.
	 * @param merchantId
	 * @return CheckoutModel
	 * @throws Exception
	 */
	public CheckoutModel fetchCheckoutById(long merchantId) throws Exception;

}
