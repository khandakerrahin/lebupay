/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.ContactUsModel;
import com.lebupay.model.DataTableModel;

/**
 * This Interface is used to declare methods for Contact Us Database Operation.
 * @author Java-Team
 *
 */
public interface ContactUsDAO {

	
	/**
	 * This method is used to save the contact us.
	 * @param contactUsModel
	 * @return long
	 * @throws Exception
	 */
	public long saveContactUs(ContactUsModel contactUsModel) throws Exception ;
	
	/**
	 * This method is used to fetch all the Contact Us for Admin.
	 * @return List
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUs() throws Exception ;
	
	/**
	 * This method is used for fetching the ContactUs Details w.r.t ID.
	 * @param contactUsId
	 * @return ContactUsModel
	 * @throws Exception
	 */
	public ContactUsModel fetchContactUsByID(long contactUsId) throws Exception ;
	
	/**
	 * This method is used by the Admin to reply the Contact Us.
	 * @param contactUsModel
	 * @return int
	 * @throws Exception
	 */
	public int replyContactUs(ContactUsModel contactUsModel) throws Exception ;
	
	/**
	 * This method is used to fetch all the query details according to the searching criteria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUsForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	
	/**
	 * This method is used to count the total number of query in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getContactUsCountForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the query list.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUsForExportForAdmin(DataTableModel dataTableModel) throws Exception ;

}
