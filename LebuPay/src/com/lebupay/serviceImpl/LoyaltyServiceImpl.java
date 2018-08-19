/**
 * @formatter:off
 *
 */

package com.lebupay.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.MessageUtil;
import com.lebupay.common.Util;
import com.lebupay.dao.LoyaltyDAO;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.model.LoyaltyModel;
import com.lebupay.model.Status;
import com.lebupay.service.LoyaltyService;

/**
 * This is LoyaltyServiceImpl Class implements LoyaltyService interface is used to perform operation on Loyalty Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class LoyaltyServiceImpl implements LoyaltyService {
	
	private static Logger logger = Logger.getLogger(LoyaltyServiceImpl.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private LoyaltyDAO loyaltyDao;
	
	
	/**
	 * This method is used for fetching the active Loyalty by type.
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel getA2P()  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getA2P -- START");
		}
		
		String loyaltyType = messageUtil.getBundle("layalty.a2p");
		LoyaltyModel a2p = loyaltyDao.fetchActiveLoyaltyByType(loyaltyType.trim());
		
		if (logger.isInfoEnabled()) {
			logger.info("getA2P -- START");
		}
		
		return a2p;
	}
	
	/**
	 * Amount to Point save or update performs in this method.
	 * @param loyaltyModel
	 * @param adminId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel saveOrUpdateA2PLayalty(LoyaltyModel loyaltyModel,long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveOrUpdateA2PLayalty -- START");
		}
		
		String loyaltyType = messageUtil.getBundle("layalty.a2p");
		LoyaltyModel a2p = loyaltyDao.fetchActiveLoyaltyByType(loyaltyType.trim());
		
		Map<String, Exception> exceptions = new HashMap<String, Exception>();
		if(Util.isEmpty(loyaltyModel.getAmount())){
			
			exceptions.put("loyalty.amount.required", new EmptyValueException(messageUtil.getBundle("loyalty.amount.required")));
		} else if(!Util.checkDecialDigit(String.valueOf(loyaltyModel.getAmount()))){
			
			exceptions.put("loyalty.amount.valid",  new EmptyValueException(messageUtil.getBundle("loyalty.amount.valid")));
		}
		
		if(Util.isEmpty(loyaltyModel.getPoint())){
			
			exceptions.put("loyalty.point.required",  new EmptyValueException(messageUtil.getBundle("loyalty.point.required")));
		} else if(!Util.checkDecialDigit(String.valueOf(loyaltyModel.getPoint()))){
			
			exceptions.put("loyalty.point.valid",  new EmptyValueException(messageUtil.getBundle("loyalty.point.valid")));
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(Objects.isNull(a2p)){
			loyaltyModel.setLoyaltyType(loyaltyType);
			long result = loyaltyDao.insertLoyalty(loyaltyModel);
			
			a2p = loyaltyDao.fetchActiveLoyaltyByID(result);
			a2p.setMessage("Successfully Created");
			
		} else {
			
			a2p.setAmount(loyaltyModel.getAmount());
			a2p.setPoint(loyaltyModel.getPoint());
			a2p.setModifiedBy(adminId);
			a2p.setLoyaltyType(loyaltyType);
			a2p.setStatus(Status.ACTIVE);
			
			loyaltyDao.updateLoyalty(a2p);
			
			a2p = loyaltyDao.fetchActiveLoyaltyByID(a2p.getLoyaltyId());
			a2p.setMessage("Successfully Updated");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("saveOrUpdateA2PLayalty -- START");
		}
		
		return a2p;
	}
	
	
	/**
	 * This method is used for fetching the Point to Amount Details from Database.
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel getP2A()  throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("getP2A -- START");
		}
		String loyaltyType = messageUtil.getBundle("layalty.p2a");
		LoyaltyModel p2a = loyaltyDao.fetchActiveLoyaltyByType(loyaltyType.trim());
		
		if (logger.isInfoEnabled()) {
			logger.info("getP2A -- START");
		}
		
		return p2a;
	}
	
	/**
	 * Point to amount save or update performs in this method.
	 * @param loyaltyModel
	 * @param adminId
	 * @return LoyaltyModel
	 * @throws Exception
	 */
	public LoyaltyModel saveOrUpdateP2ALayalty(LoyaltyModel loyaltyModel,long adminId) throws Exception{
		
		if (logger.isInfoEnabled()) {
			logger.info("saveOrUpdateA2PLayalty -- START");
		}
		
		String loyaltyType =messageUtil.getBundle("layalty.p2a");
		LoyaltyModel p2a = loyaltyDao.fetchActiveLoyaltyByType(loyaltyType);
		
		Map<String, Exception> exceptions = new HashMap<String, Exception>();
		if(Util.isEmpty(loyaltyModel.getAmount())){
			
			exceptions.put("loyalty.amount.required",  new EmptyValueException(messageUtil.getBundle("loyalty.amount.required")));
		}  else if(!Util.checkDecialDigit(String.valueOf(loyaltyModel.getAmount()))){
			
			exceptions.put("loyalty.amount.valid",  new EmptyValueException(messageUtil.getBundle("loyalty.amount.valid")));
		}
		
		if(Util.isEmpty(loyaltyModel.getPoint())){
			
			exceptions.put("loyalty.point.required",  new EmptyValueException(messageUtil.getBundle("loyalty.point.required")));
		} else if(!Util.checkDecialDigit(String.valueOf(loyaltyModel.getPoint()))){
			
			exceptions.put("loyalty.point.valid",  new EmptyValueException(messageUtil.getBundle("loyalty.point.valid")));
		}
		
		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);
		
		if(Objects.isNull(p2a)){
			loyaltyModel.setLoyaltyType(loyaltyType);
			long result = loyaltyDao.insertLoyalty(loyaltyModel);
			
			p2a = loyaltyDao.fetchActiveLoyaltyByID(result);
			p2a.setMessage("Successfully Created");
		} else {
			
			p2a.setAmount(loyaltyModel.getAmount());
			p2a.setPoint(loyaltyModel.getPoint());
			p2a.setModifiedBy(adminId);
			p2a.setLoyaltyType(loyaltyType);
			p2a.setStatus(Status.ACTIVE);
			
			loyaltyDao.updateLoyalty(p2a);
			
			p2a = loyaltyDao.fetchActiveLoyaltyByID(p2a.getLoyaltyId());
			p2a.setMessage("Successfully Updated");
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("saveOrUpdateA2PLayalty -- START");
		}
		
		return p2a;
	}
	
}
