/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.ContentModel;
import com.lebupay.model.DataTableModel;

/**
 * This interface is used to declare methods for Content Database Operations.
 * @author Java-Team
 *
 */
public interface ContentDAO {

	
	/**
	 * This method is used for inserting Content.
	 * @param contentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertContent(ContentModel contentModel) throws Exception ;
	
	/**
	 * This method is used for updating the Content.
	 * @param contentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateContent(ContentModel contentModel) throws Exception ;
	
	/**
	 * This method is used for fetch all the active Content Details.
	 * @return List<ContentModel>
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllActiveContents()  throws Exception ;
	
	
	/**
	 * This method is used to fetch all the content details according to the searching criteria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllContentsForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	
	/**
	 * This method is used to count the total number of contents in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getContentsCountForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the content list.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<ContentModel> fetchAllContentsForExportForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for fetching the active Content By ID.
	 * @param contentId
	 * @return ContentModel
	 * @throws Exception
	 */
	public ContentModel fetchActiveContentByID(long contentId)  throws Exception ;

}
