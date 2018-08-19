/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.dao.ContentDAO;
import com.lebupay.model.ContentModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;
import com.lebupay.service.ContentService;
import com.lebupay.validation.AdminValidation;

/**
 * This is ContentServiceImpl Class implements ContentService interface is used to perform operation on Content Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class ContentServiceImpl implements ContentService {

	private static Logger logger = Logger.getLogger(ContentServiceImpl.class);
	
	@Autowired
	private ContentDAO contentDao;
	
	@Autowired
	private AdminValidation contentManagementValidation;
	
	
	/**
	 * This method use to create content detail
	 * @param contentModel
	 * @return long
	 * @throws Exception
	 */
	public long saveContent(ContentModel contentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveContent -- START");
		}
		
		contentManagementValidation.contentValidation(contentModel);
		long result = contentDao.insertContent(contentModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("saveContent -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method use to get content detail
	 * @param contentId
	 * @return ContentModel
	 * @throws Exception
	 */
	public ContentModel fetchContentById(long contentId) throws Exception {
			
		if (logger.isInfoEnabled()) {
			logger.info("fetchContentById -- START");
		}
		
		ContentModel contentModel = contentDao.fetchActiveContentByID(contentId);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchContentById -- END");
		}
			
		return contentModel;
	}
	
	
	/**
	 * This method use to get content details
	 * @return List<ContentModel>
	 * @throws Exception
	 */
	public List<ContentModel> fetchContent() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchContent -- START");
		}
		
		List<ContentModel> contentModels = contentDao.fetchAllActiveContents();
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchContent -- END");
	    }
		
		return contentModels;
	}
	
	/**
	 * This method is used for Datatable for the Content.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchContent(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchContents -- START");
		}
		
		List<ContentModel> contentModels = contentDao.fetchAllContentsForAdmin(dataTableModel);
		dataTableModel.setData(contentModels);
		dataTableModel.setRecordsTotal(contentDao.getContentsCountForAdmin(dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchContents -- END");
		}
	}
	
	/**
	 * This method is used for generating the Excel for the Content.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportContentsForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportContentsForExcel -- START");
		}
		
		List<ContentModel> contentModels = contentDao.fetchAllContentsForExportForAdmin(dataTableModel);
		Object[][] objects = new Object[contentModels.size()+1][noOfColumns];
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			/*objects2[j++] = contentModels.get(i).getContentId();*/
			objects2[j++] = contentModels.get(i).getPath();
			objects2[j++] = contentModels.get(i).getContent();
			objects2[j++] = contentModels.get(i).getTypeModel().getTypeName();
			objects2[j] = contentModels.get(i).getStatus().name();
			
			objects[++i] = objects2;
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("exportContentsForExcel -- END");
		}
		
		return objects;
	}
	
	/**
	 * This method is used for Updating the Content.
	 * @param contentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateContent(ContentModel contentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateContent -- START");
		}
		
		contentManagementValidation.contentValidation(contentModel);
		int result = contentDao.updateContent(contentModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateContent -- END");
		}
		
		return result;
	}
	
	/**
	 * This method use to delete content details
	 * @param contentId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteContent(long contentId,long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteContent -- START");
		}
		
		ContentModel contentModel = contentDao.fetchActiveContentByID(contentId);
		contentModel.setaModifiedBy(adminId);
		contentModel.setStatus(Status.DELETE);
		
		int result = contentDao.updateContent(contentModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteContent -- END");
		}
		
		return result;
	}
}
