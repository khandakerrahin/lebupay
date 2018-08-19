/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lebupay.common.MessageUtil;
import com.lebupay.dao.CheckOutDAO;
import com.lebupay.dao.ParameterDAO;
import com.lebupay.model.CheckoutModel;
import com.lebupay.model.ParameterModel;
import com.lebupay.service.CheckOutService;
import com.lebupay.validation.MerchantValidation;

/**
 * This is CheckOutServiceImpl Class implements CheckOutService interface is used to perform operation on CheckOut Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class CheckOutServiceImpl implements CheckOutService {

	private static Logger logger = Logger.getLogger(CheckOutServiceImpl.class);
	
	@Autowired
	private CheckOutDAO checkOutDao;
	
	@Autowired
	private ParameterDAO parameterDao;
	
	@Autowired
	private MerchantValidation merchantValidation;
	
	@Autowired
	private MessageUtil messageUtil;
	
	/**
	 * This method is used for Update the Check out Page.
	 * @param checkoutModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int updateCheckout(CheckoutModel checkoutModel, HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCheckout -- START");
		}
		
		if(!(checkoutModel.getFile() == null || checkoutModel.getFile().isEmpty())){
			
			MultipartFile multipartFile = checkoutModel.getFile();
			merchantValidation.imageFormatValidation(checkoutModel);
			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");
			
			String dirStr = appPath + "resources" + File.separator + "banner";
			File dir = new File(dirStr);
			if(!dir.exists()){
				dir.mkdir();
			}

			// construct the complete absolute path of the file
			String fileName = checkoutModel.getModifiedBy() + multipartFile.getOriginalFilename();
			String fullPath = dirStr + File.separator + fileName;
			
			File file = new File(fullPath);

			multipartFile.transferTo(file);
			
			checkoutModel.setBannerName(fileName);
		}
		
		if(checkoutModel.getBackgroundColour().length() > 7 || !checkoutModel.getBackgroundColour().contains("#")) {
			
			checkoutModel.setBackgroundColour(messageUtil.getBundle("merchant.checkout.bgcolour"));
		}
		
		int result = checkOutDao.updateCheckout(checkoutModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCheckout -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method is used for Inserting the Parameters.
	 * @param parameterModel
	 * @return long
	 * @throws Exception
	 */
	public long insertParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertParameter -- START");
		}
		
		merchantValidation.merchantParameterCreateValidation(parameterModel);
		long result = parameterDao.insertParameter(parameterModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("insertParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Updating the Parameters.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int updateParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateParameter -- START");
		}
		
		merchantValidation.merchantParameterUpdateValidation(parameterModel);
		int result = parameterDao.updateParameter(parameterModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("updateParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for deleting the Parameters.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int deleteParameter(ParameterModel parameterModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteParameter -- START");
		}
		
		int result = parameterDao.deleteParameter(parameterModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteParameter -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for fetching the Parameters w.r.t Merchant Id.
	 * @param merchantId
	 * @return List
	 * @throws Exception
	 */
	public List<ParameterModel> fetchParametersById(long merchantId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParametersById -- START");
		}
		
		List<ParameterModel> parameterModels = parameterDao.fetchParametersById(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParametersById -- END");
		}
		
		return parameterModels;
	}
	
	/**
	 * This method is used for fetching the Checkout Setting w.r.t Merchant Id.
	 * @param merchantId
	 * @return CheckoutModel
	 * @throws Exception
	 */
	public CheckoutModel fetchCheckoutById(long merchantId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCheckoutById -- START");
		}
		
		CheckoutModel checkoutModel = checkOutDao.fetchCheckoutById(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchCheckoutById -- END");
	   }
		
	   return checkoutModel;
	}
	
	/**
	 * This method is used for Fetching the Parameters w.r.t Parameter Id.
	 * @param parameterId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(long parameterId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchParameterById -- START");
		}
		
		ParameterModel parameterModel = parameterDao.fetchParameterById(parameterId);
	
		if (logger.isInfoEnabled()) {
			logger.info("fetchParameterById -- END");
	   }
		
	   return parameterModel;
	}
}
