/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.lebupay.model.ContactUsModel;
import com.lebupay.model.DataTableModel;

/**
 * This is ContactUsService Interface is used to perform operations on ContactUs Module.
 * @author Java Team
 *
 */
public interface ContactUsService {
	
	/**
	 * This method is used to Save the Contact us form and send a mail to the user.
	 * @param contactUsModel
	 * @param httpServletRequest
	 * @return long
	 * @throws Exception
	 */
	public long saveContactUs(ContactUsModel contactUsModel, HttpServletRequest httpServletRequest) throws Exception ;
	
	/**
	 * This method is used for validating the Contact Us Form.
	 * @param contactUsModel
	 * @param httpServletRequest
	 * @throws Exception
	 */
	public void saveContactUsValidation(ContactUsModel contactUsModel, HttpServletRequest httpServletRequest) throws Exception ;
	
	/**
	 * This method is used for Fetching all the Contact Us Details.
	 * @return List<ContactUsModel>
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUs() throws Exception ;
	
	/**
	 * This method is used for fetching the Contact us w.r.t Contact Us Id.
	 * @param contactUsId
	 * @return ContactUsModel
	 * @throws Exception
	 */
	public ContactUsModel fetchContactUsByID(long contactUsId) throws Exception ;
	
	/**
	 * This method is used for Replying the Contact Us from the Admin Side.
	 * @param contactUsModel
	 * @return int
	 * @throws Exception
	 */
	public int replyContactUs(ContactUsModel contactUsModel) throws Exception ;
	
	/**
	 * This method is used to validate the reply Contact Us Form.
	 * @param contactUsModel
	 * @throws Exception
	 */
	public void replyContactUsValidation(ContactUsModel contactUsModel) throws Exception ;
	
	/**
	 * This method is used in the Datatable for the Contact Us.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchContactUsAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to generate the Excel for the Contact Us Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return
	 * @throws Exception
	 */
	public Object[][] exportContactUsForExcelAdmin(DataTableModel dataTableModel, int noOfColumns) throws Exception ;
}
