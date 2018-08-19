/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;

import com.lebupay.model.ContentModel;
import com.lebupay.model.DataTableModel;

/**
 * This is ContentService Interface is used to perform operations on Content Module.
 * @author Java Team
 *
 */
public interface ContentService {
	
	/**
	 * This method use to create content detail
	 * @param contentModel
	 * @return long
	 * @throws Exception
	 */
	public long saveContent(ContentModel contentModel) throws Exception ;
	
	
	/**
	 * This method use to get content detail
	 * @param contentId
	 * @return ContentModel
	 * @throws Exception
	 */
	public ContentModel fetchContentById(long contentId) throws Exception ;
	
	
	/**
	 * This method use to get content details
	 * @return List<ContentModel>
	 * @throws Exception
	 */
	public List<ContentModel> fetchContent() throws Exception ;
	
	/**
	 * This method is used for Datatable for the Content.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchContent(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for generating the Excel for the Content.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportContentsForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception ;
	
	/**
	 * This method is used for Updating the Content.
	 * @param contentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateContent(ContentModel contentModel) throws Exception ;
	
	/**
	 * This method use to delete content details
	 * @param contentId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteContent(long contentId,long adminId) throws Exception ;
}
