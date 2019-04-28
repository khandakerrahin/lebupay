/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.TicketModel;

/**
 * This is MerchantService Interface is used to perform operations on Merchant
 * Module.
 * 
 * @author Java Team
 *
 */
public interface MerchantService {

	/**
	 * This method is use to create merchant
	 * 
	 * @param merchantModel
	 * @param httpServletRequest
	 * @return long
	 * @throws Exception
	 */
	public long create(MerchantModel merchantModel, HttpServletRequest httpServletRequest) throws Exception;

	/**
	 * This method is use to verified mobile no and if mobile no is present then
	 * send a sms code in provided mobile no.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int phoneVerification(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is use to check login of the Merchant.
	 * 
	 * @param merchantModel
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel login(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is use to check if user is registered or not.
	 * 
	 * @param merchantModel
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel checkMerchant(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is use to check forgot password and if email id is present then
	 * send a link in provided email id.
	 * 
	 * @param merchantModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(MerchantModel merchantModel, HttpServletRequest request) throws Exception;

	/**
	 * This method is use in the forgot password of the Merchant.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int forgotPasswordChange(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is use to change password of the Merchant.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used for Updating the Profile of the Merchant.
	 * 
	 * @param merchantModel
	 * @return long
	 * @throws Exception
	 */
	public long updateProfile(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used for fetching the Active Merchant w.r.t Merchant Id.
	 * 
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantById(long merchantId) throws Exception;

	/**
	 * This method is used to skip the COMPANY DETAILS.
	 * 
	 * @param merchantID
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int skipCompany(long merchantID, int status) throws Exception;

	/**
	 * This method is use to resent mobile code of the Merchant.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int resend(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used to create the Company of the Merchant.
	 * 
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long createCompany(CompanyModel companyModel) throws Exception;

	/**
	 * This method is use to save merchant ticket details
	 * 
	 * @param ticketModel
	 * @param userId
	 * @return long
	 * @throws Exception
	 */
	public long saveTicket(TicketModel ticketModel, long userId) throws Exception;

	/**
	 * This method is used for fetching the Tickets w.r.t Merchant Id.
	 * 
	 * @param userId
	 * @return List
	 * @throws Exception
	 */
	public List<TicketModel> fetchTickets(long userId) throws Exception;

	/**
	 * This method is used for Fetching Tickets for the Datatable w.r.t Merchant Id.
	 * 
	 * @param dataTableModel
	 * @param userId
	 * @throws Exception
	 */
	public void fetchTickets(DataTableModel dataTableModel, long userId) throws Exception;

	/**
	 * This method is used for generating Excel for the Tickets Datatable.
	 * 
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param userId
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportTicketsForExcel(DataTableModel dataTableModel, int noOfColumns, long userId)
			throws Exception;

	/**
	 * This method is used to fetch the Company Details w.r.t Merchant Id.
	 * 
	 * @param merchantId
	 * @return CompanyModel
	 * @throws Exception
	 */
	public CompanyModel fetchCompanyByMerchantId(long merchantId) throws Exception;

	/**
	 * This method is used to Udpate the Company Details.
	 * 
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long updateCompany(CompanyModel companyModel) throws Exception;

	/**
	 * This method is used to Sending the Email Invoicing Details.
	 * 
	 * @param emailInvoicingModel
	 * @return int
	 * @throws Exception
	 */
	public int emailInvoicing(EmailInvoicingModel emailInvoicingModel) throws Exception;

	/**
	 * This method is used to create the Link for Payment, send it via mail or sms
	 * and save in the Order Table.
	 * 
	 * @param emailInvoicingModel
	 * @param request
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> link(EmailInvoicingModel emailInvoicingModel, HttpServletRequest request)
			throws Exception;

	/**
	 * This method is used for fetching the Merchant Details w.r.t AccessKey.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantByAccessKey(String accessKey) throws Exception;

	/**
	 * This method is used for fetching the Merchant Details w.r.t AccessKey.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchMerchantByAccessKey(String accessKey) throws Exception;

	/**
	 * This method is used to view Merchant Card Percentage in Merchant Profile.
	 * 
	 * @param merchantId
	 * @return List<CardTypePercentageModel>
	 * @throws Exception
	 */
	public List<CardTypePercentageModel> getAllCardPercentageByMerchantId(Long merchantId) throws Exception;
}