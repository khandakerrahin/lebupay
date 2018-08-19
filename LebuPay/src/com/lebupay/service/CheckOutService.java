/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lebupay.model.CheckoutModel;
import com.lebupay.model.ParameterModel;

/**
 * This is CheckOutService Interface is used to perform operations on CheckOut Module.
 * @author Java Team
 *
 */
public interface CheckOutService {
	
	
	/**
	 * This method is used for Update the Check out Page.
	 * @param checkoutModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int updateCheckout(CheckoutModel checkoutModel, HttpServletRequest request) throws Exception ;
	
	
	/**
	 * This method is used for Inserting the Parameters.
	 * @param parameterModel
	 * @return long
	 * @throws Exception
	 */
	public long insertParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This method is used for Updating the Parameters.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int updateParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This methos is used for Deleting the Parameters.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int deleteParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This method is used for fetching the Parameters w.r.t Merchant Id.
	 * @param merchantId
	 * @return List<ParameterModel>
	 * @throws Exception
	 */
	public List<ParameterModel> fetchParametersById(long merchantId) throws Exception;
	
	/**
	 * This method is used for fetching the Checkout Setting w.r.t Merchant Id.
	 * @param merchantId
	 * @return CheckoutModel
	 * @throws Exception
	 */
	public CheckoutModel fetchCheckoutById(long merchantId) throws Exception;
	
	/**
	 * This method is used for Fetching the Parameters w.r.t Parameter Id.
	 * @param parameterId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(long parameterId) throws Exception;
}
