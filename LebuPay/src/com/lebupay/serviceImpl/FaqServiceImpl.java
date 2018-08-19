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

import com.lebupay.dao.FaqDAO;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.FaqModel;
import com.lebupay.model.Status;
import com.lebupay.service.FaqService;
import com.lebupay.validation.AdminValidation;

/**
 * This is FaqServiceImpl Class implements FaqService interface is used to perform operation on Faq Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class FaqServiceImpl implements FaqService {

	private static Logger logger = Logger.getLogger(FaqServiceImpl.class);
	
	@Autowired
	private FaqDAO faqDao;
	
	@Autowired
	private AdminValidation faqValidation;
	
	/**
	 * This method is used to Insert the FAQ.
	 * @param faqModel
	 * @return long
	 * @throws Exception
	 */
	public long saveFaq(FaqModel faqModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveFaq -- START");
		}
		
		faqValidation.faqValidation(faqModel);
		long result = faqDao.insertFaq(faqModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("saveFaq -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Fetching all the FAQ's.
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchFaq() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaq -- START");
		}
		
		List<FaqModel> FaqModels = faqDao.fetchAllActiveFaqs();
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaq -- END");
		}
			
		return FaqModels;
	}
	
	/**
	 * This method is used for Fetching the FaQ's for the Datatable.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchFaq(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaqs -- START");
		}
		
		List<FaqModel> faqModels = faqDao.fetchAllFaqsForAdmin(dataTableModel);
		dataTableModel.setData(faqModels);
		dataTableModel.setRecordsTotal(faqDao.getFaqsCountForAdmin(dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaqs -- END");
		}
	}
	
	/**
	 * This method is used for generating the Excel for the FAQ's.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportFaqsForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportFaqsForExcel -- START");
		}
		
		List<FaqModel> faqModels = faqDao.fetchAllFaqsForExportForAdmin(dataTableModel);
		Object[][] objects = new Object[faqModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			/*objects2[j++] = faqModels.get(i).getFaqId();*/
			objects2[j++] = faqModels.get(i).getQuestion();
			objects2[j++] = faqModels.get(i).getAnswer();
			objects2[j++] = faqModels.get(i).getTypeModel().getTypeName();
			objects2[j] = faqModels.get(i).getStatus().name();
			
			objects[++i] = objects2;
		}
		
				
		if (logger.isInfoEnabled()) {
			logger.info("exportFaqsForExcel -- END");
		}
		
		return objects;
	}
		
	/**
	 * This method is used for Fetching the FAQ w.r.t Faq Id.
	 * @param FaqId
	 * @return FaqModel
	 * @throws Exception
	 */
	public FaqModel fetchActiveFaqByID(long FaqId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaqById -- START");
		}
		
		FaqModel faqModel = faqDao.fetchActiveFaqByID(FaqId);
					
		if (logger.isInfoEnabled()) {
			logger.info("fetchFaqById -- END");
		}
			
		return faqModel;
	}
	
	/**
	 * This method is used for Updating the FAQ.
	 * @param FaqModel
	 * @return int
	 * @throws Exception
	 */
	public int updateFaq(FaqModel FaqModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateFaq -- START");
		}
		
		faqValidation.faqValidation(FaqModel);
		int result = faqDao.updateFaq(FaqModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateFaq -- END");
		}
		
		return result;
		
	}
	
	/**
	 * This method is used for Deleting the FAQ.
	 * @param faqId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteFaq(long faqId,long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteFaq -- START");
		}
		
		FaqModel faqModel = faqDao.fetchActiveFaqByID(faqId);
		faqModel.setStatus(Status.DELETE);
		faqModel.setaModifiedBy(adminId);
		
		int result = faqDao.updateFaq(faqModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteFaq -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to fetch the Active FAQ's w.r.t User Type.
	 * @param userType
	 * @return List<FaqModel>
	 * @throws Exception
	 */
	public List<FaqModel> fetchAllActiveFaqsByType(String userType)  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqsByType -- START");
		}
		
		List<FaqModel> faqModels = faqDao.fetchAllActiveFaqsByType(userType);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveFaqsByType -- END");
		}
		
		return faqModels;
	}
}
