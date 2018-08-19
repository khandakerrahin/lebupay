/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.lebupay.model.AdminModel;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.MerchantCardPercentageModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.Status;
import com.lebupay.model.TicketModel;

/**
 * This is AdminService Interface is used to perform operations on Admin Module.
 * @author Java Team
 *
 */
public interface AdminService {
	
	/**
	 * This method is used for Login Check of the Admin.
	 * @param adminModel
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel login(AdminModel adminModel) throws Exception ;
	
	/**
	 * This method is used for email sending in the Forgot password of the Admin.
	 * @param adminModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(AdminModel adminModel, HttpServletRequest request) throws Exception ;
	
	/**
	 * This method is used for the forgot password section of the Admin.
	 * @param adminModel
	 * @return int
	 * @throws Exception
	 */
	public int forgotPasswordChange(AdminModel adminModel) throws Exception ;
	
	/**
	 * This method is used for fetching the Details of the Admin w.r.t Admin Id.
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel fetchAdminById(long adminId) throws Exception ;
	
	/**
	 * This method is used to Update the profile of the Admin.
	 * @param adminModel
	 * @return long
	 * @throws Exception
	 */
	public long updateProfile(AdminModel adminModel) throws Exception ;
	
	/**
	 * This method is used to change password of the Admin.
	 * @param adminModel
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(AdminModel adminModel) throws Exception ;

	/**
	 * This method is used to show  in active merchant list
	 * @param status
	 * @return List
	 * @throws Exception
	 */
	public List<MerchantModel> fetchAllActiveMerchants(Status status) throws Exception ;
	
	/**
	 * This method is used to fetch the Active Merchant List.
	 * @param dataTableModel
	 * @param status
	 * @throws Exception
	 */
	public void fetchActiveMerchantList(DataTableModel dataTableModel, Status status) throws Exception ;
	
	/**
	 * This method is used for generating the Excel of the Merchant List in the Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param status
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportActiveMerchantListForExcel(DataTableModel dataTableModel, int noOfColumns, Status status) throws Exception ;
	
	/**
	 * This method is used for Activate the Merchant.
	 * @param merchantId
	 * @param adminId
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int activateMerchant(long merchantId, long adminId, Status status) throws Exception ;
	
	/**
	 * This method is used for Fetching the Active and Activated Merchant List.
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveAndActivateMerchantById(long merchantId) throws Exception;
	
	/**
	 * This method is used for Update the Profile of the Admin.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfileByAdmin(MerchantModel merchantModel) throws Exception ;
	
	/**
	 * This method is used for Fetching the Ticket Details.
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets() throws Exception ;
	
	/**
	 * This method is used for Fetching the Ticket Details of the Datatable.
	 * @param dataTableModel
	 * @param userId
	 * @throws Exception
	 */
	public void fetchTickets(DataTableModel dataTableModel, long userId) throws Exception ;
	
	/**
	 * This method is used for Generating the Excel of the Ticket Details.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param userId
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportTicketsForExcel(DataTableModel dataTableModel, int noOfColumns, long userId) throws Exception ;
	
	/**
	 * This method is used for Fetching the Tickets w.r.t Ticket Id.
	 * @param ticketId
	 * @return TicketModel
	 * @throws Exception
	 */
	public TicketModel fetchTicketById(int ticketId) throws Exception ;
	
	/**
	 * This method is used for Replying the Ticket.
	 * @param ticketModel
	 * @return int
	 * @throws Exception
	 */
	public int replyTicket(TicketModel ticketModel) throws Exception ;
	
	/**
	 * This method is used to Create userid and password.
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int createUserIdAndPassword(MerchantModel merchantModel) throws Exception ;
	
	/**
	 * This method is used to fetch CardType List
	 * @return List
	 * @throws Exception
	 */
	public List<CardTypeModel> fetchCardTypeList() throws Exception;
	
	/**
	 * This method is used to get List of Merchant Card Percentage by Merchant.
	 * @param merchantId
	 * @return List
	 * @throws Exception
	 */
	public List<MerchantCardPercentageModel> fetchCardPercentageByMerchantId(String merchantId) throws Exception;
	
	/**
	 * This method is used to insert Card Percentage by Merchant
	 * @param merchantCardPercentageModel
	 * @return long
	 * @throws Exception
	 */
	public long saveCardPercentageByMerchantId(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception ;
	
	/**
	 * This method is used to update Card Percentage by Merchant
	 * @param merchantCardPercentageModel
	 * @return long
	 * @throws Exception
	 */
	public long updateCardPercentageByMerchantId(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception ;
}
