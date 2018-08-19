/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.FaqModel;

/**
 * This interface is used to declare methods for FAQ Database Operations.
 * @author Java-Team
 *
 */
public interface FaqDAO {

	
	/**
	 * This method is used for inserting FAQ.
	 * @param faqModel
	 * @return long
	 * @throws Exception
	 */
	public long insertFaq(FaqModel faqModel) throws Exception ;
	
	/**
	 * This method is used for updating the FAQ.
	 * @param faqModel
	 * @return int
	 * @throws Exception
	 */
	public int updateFaq(FaqModel faqModel) throws Exception ;
	
	/**
	 * This method is used for fetching all active FAQS.
	 * @return List
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqs()  throws Exception ;
	
	
	/**
	 * This method is used to fetch all the faq details according to the searching citeria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllFaqsForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	
	/**
	 * This method is used to count the total number of faqs in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getFaqsCountForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the faq list.
	 * @param dataTableModel
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllFaqsForExportForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to fetch Active FAQ by ID.
	 * @param faqId
	 * @return FaqModel
	 * @throws Exception
	 */
	public FaqModel fetchActiveFaqByID(long faqId)  throws Exception ;
	
	
	/**
	 * This method is used to fetch the active FAQ depending upon the user type.
	 * @param userType
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqsByType(String userType)  throws Exception ;

}
