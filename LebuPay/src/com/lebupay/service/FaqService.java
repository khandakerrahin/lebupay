/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;

import com.lebupay.model.DataTableModel;
import com.lebupay.model.FaqModel;

/**
 * This is FaqService Interface is used to perform operations on Faq Module.
 * @author Java Team
 *
 */
public interface FaqService {
	
	/**
	 * This method is used to Insert the FAQ.
	 * @param faqModel
	 * @return long
	 * @throws Exception
	 */
	public long saveFaq(FaqModel faqModel) throws Exception ;
	
	/**
	 * This method is used for Fetching all the FAQ's.
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchFaq() throws Exception ;
	
	/**
	 * This method is used for Fetching the FaQ's for the Datatable.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchFaq(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for generating the Excel for the FAQ's.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportFaqsForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception ;
		
	/**
	 * This method is used for Fetching the FAQ w.r.t Faq Id.
	 * @param FaqId
	 * @return FaqModel
	 * @throws Exception
	 */
	public FaqModel fetchActiveFaqByID(long FaqId) throws Exception ;
	
	/**
	 * This method is used for Updating the FAQ.
	 * @param FaqModel
	 * @return int
	 * @throws Exception
	 */
	public int updateFaq(FaqModel FaqModel) throws Exception ;
	
	/**
	 * This method is used for Deleting the FAQ.
	 * @param faqId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteFaq(long faqId,long adminId) throws Exception ;
	
	/**
	 * This method is used to fetch the Active FAQ's w.r.t User Type.
	 * @param userType
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqsByType(String userType)  throws Exception ;
}
