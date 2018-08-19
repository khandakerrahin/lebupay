/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import javax.servlet.http.HttpServletRequest;

import com.lebupay.model.QuickPayModel;

/**
 * This is QuickPayService Interface is used to perform operations on QuickPay Module.
 * @author Java Team
 *
 */
public interface QuickPayService {

	
	/**
	 * This method is use to add Quick Pay details
	 * @param quickPayModel
	 * @param httpServletRequest
	 * @return String
	 * @throws Exception
	 */
	public String createQuickPay(QuickPayModel quickPayModel,HttpServletRequest httpServletRequest) throws Exception ;
	
	
	/**
	 * This method is use to generated HTML from
	 * @param instaBuyModel
	 * @param httpServletRequest
	 * @return String
	 * @throws Exception
	 */
	//private String generatedHTMLFrom(QuickPayModel quickPayModel,HttpServletRequest httpServletRequest) throws Exception;
	
	/**
	 * This method is used for Fetching the Quick Pay Details w.r.t QuickPay Id and Merchant Id.
	 * @param quickPayId
	 * @param merchantId
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayById(long quickPayId, long merchantId) throws Exception ;
	
	/**
	 * This method is used for Fetching the Quick Pay Details w.r.t keys.
	 * @param keys
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayByKeys(String keys) throws Exception ;
	
}
