/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.DataTableModel;
import com.lebupay.model.TicketModel;

/**
 * This interface is used to declare methods for Ticket Database Operations.
 * @author Java-Team
 *
 */
public interface TicketDAO {

	
	/**
	 * This method is used for saving the ticket for both Merchant and Customer.
	 * @param ticketModel
	 * @param userType
	 * @param userId
	 * @return long
	 * @throws Exception
	 */
	public long saveTicket(TicketModel ticketModel, String userType, long userId) throws Exception ;
	
	/**
	 * This method is used to fetch all the ticket details for Customer, Merchant and Admin.
	 * @param userType
	 * @param userId
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets(String userType, long userId) throws Exception ;
	
	/**
	 * This method is used to fetch all the ticket details according to the searching criteria for Customer, Merchant and Admin.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTickets(String userType, long userId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to count the total number of tickets in Database during AJAX Call.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getTicketsCount(String userType, long userId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the ticket list.
	 * @param userType
	 * @param userId
	 * @param dataTableModel
	 * @return List<TicketModel>
	 * @throws Exception
	 */
	public List<TicketModel> fetchAllTicketsForExport(String userType, long userId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for fetching the tickets by there ID.
	 * @param userType
	 * @param userId
	 * @param ticketId
	 * @return TicketModel
	 * @throws Exception
	 */
	public TicketModel fetchTicketById(String userType, int userId, int ticketId) throws Exception ;
	
	/**
	 * This method is used for the admin to reply any ticket.
	 * @param ticketModel
	 * @return int
	 * @throws Exception
	 */
	public int replyTicket(TicketModel ticketModel) throws Exception ;

}
