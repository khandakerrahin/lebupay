/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.MessageUtil;
import com.lebupay.dao.QuickPayDAO;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.MerchantSessionModel;
import com.lebupay.model.QuickPayModel;
import com.lebupay.service.QuickPayService;
import com.lebupay.validation.MerchantValidation;

/**
 * This is QuickPayServiceImpl Class implements QuickPayService interface is used to perform operation on QuickPay Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class QuickPayServiceImpl implements QuickPayService {

	private static Logger logger = Logger.getLogger(QuickPayServiceImpl.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private MerchantValidation quickPayValidation;
	
	@Autowired
	private QuickPayDAO quickPayDao;
	
	
	/**
	 * This method is use to add Quick Pay details
	 * @param quickPayModel
	 * @param httpServletRequest
	 * @return String
	 * @throws Exception
	 */
	public String createQuickPay(QuickPayModel quickPayModel,HttpServletRequest httpServletRequest) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("createQuickPay -- START");
		}
		
		quickPayValidation.createQuickPayValidation(quickPayModel);
		
		String generatedHTML = generatedHTMLFrom(quickPayModel,httpServletRequest);
		
		if (logger.isInfoEnabled()) {
			logger.info("createQuickPay -- END");
		}
		
		return generatedHTML;
	}
	
	
	/**
	 * This method is use to generated HTML from
	 * @param quickPayModel
	 * @param httpServletRequest
	 * @return String
	 * @throws Exception
	 */
	private String generatedHTMLFrom(QuickPayModel quickPayModel,HttpServletRequest httpServletRequest) throws Exception{
		if (logger.isInfoEnabled()) {
			logger.info("generatedHTMLFrom -- END");
		}
		
		String path = httpServletRequest.getContextPath();
		String basePath = httpServletRequest.getScheme() + "://"+ httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + path + "/";
		
		String URl = basePath+"quickpay-checkout";
		
		/**
		 * generated unique Key
		 */
		SecureRandom random = new SecureRandom();
		String uniqueKey = new BigInteger(130, random).toString(32);
		
		String html = "<form action='"+URl+"' method='post'>"
				+ "<input type='hidden' name='keys' value='"+uniqueKey+"' />"
				+ "<div style='width: 100px; text-align: center;'>"
				+ "<input type='submit' style='background:#ffffff url("+basePath+"resources/insta-buy/logo_btn.png) no-repeat center center; border:0; cursor:pointer;padding:6px 18px; color:#5cd986; font-size: 15px; width: 108px; font-size:0; border-radius:6px; line-height:27px; display:inline-block; text-align:center; text-decoration:none; font-weight:800;' ></form>";
		
		MerchantSessionModel merchantSessionModel = (MerchantSessionModel) httpServletRequest.getSession().getAttribute("merchantModel");
		
		MerchantModel model = new MerchantModel();
		model.setMerchantId(merchantSessionModel.getMerchantId());
		
		quickPayModel.setMerchantModel(model);
		quickPayModel.setCreatedBy(merchantSessionModel.getMerchantId());
		quickPayModel.setKeys(uniqueKey);
		
		long result =quickPayDao.saveQuickPay(quickPayModel);
		
		if(result > 0){
			
			return html;
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("generatedHTMLFrom -- END");
		}
		
		return null;
	}
	
	/**
	 * This method is used for Fetching the Quick Pay Details w.r.t QuickPay Id and Merchant Id.
	 * @param quickPayId
	 * @param merchantId
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayById(long quickPayId, long merchantId) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayById -- END");
		}
		
		QuickPayModel quickPayModel = quickPayDao.fetchQuickPayById(quickPayId, merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayById -- END");
		}
		
		return quickPayModel;
	}
	
	/**
	 * This method is used for Fetching the Quick Pay Details w.r.t keys.
	 * @param keys
	 * @return QuickPayModel
	 * @throws Exception
	 */
	public QuickPayModel fetchQuickPayByKeys(String keys) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayByKeys -- START");
		}
		
		QuickPayModel quickPayModel = quickPayDao.fetchQuickPayByKeys(keys);
			
		if (logger.isInfoEnabled()) {
			logger.info("fetchQuickPayByKeys -- END");
		}
			
		return quickPayModel;
	}
	
}
