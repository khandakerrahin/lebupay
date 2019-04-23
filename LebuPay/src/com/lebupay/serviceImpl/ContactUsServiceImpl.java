/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.MessageUtil;
import com.lebupay.common.SendMail;
import com.lebupay.common.SpiderEmailSender;
import com.lebupay.common.Util;
import com.lebupay.dao.ContactUsDAO;
import com.lebupay.daoImpl.BaseDao;
import com.lebupay.exception.EmptyValueException;
import com.lebupay.exception.FormExceptions;
import com.lebupay.model.ContactUsModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.EmailInvoicingModel;
import com.lebupay.service.ContactUsService;

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is ContactUsServiceImpl Class implements ContactUsService interface is used to perform operation on ContactUs Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class ContactUsServiceImpl extends BaseDao implements ContactUsService {

	private static Logger logger = Logger.getLogger(ContactUsServiceImpl.class);
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private ContactUsDAO contactUsDao;
	
	@Autowired
	private SendMail sendMail;
	
	@Autowired
	private SpiderEmailSender spiderEmailSender;
	
	/**
	 * This method is used to Save the Contact us form and send a mail to the user.
	 * @param contactUsModel
	 * @param httpServletRequest
	 * @return long
	 * @throws Exception
	 */
	public long saveContactUs(ContactUsModel contactUsModel, HttpServletRequest httpServletRequest) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveContactUs -- START");
		}
		
		saveContactUsValidation(contactUsModel, httpServletRequest);
		long result = contactUsDao.saveContactUs(contactUsModel);
		if(result > 0) {
			
			try{
				// Send Email
				String action="saveContactUs";
				String header = "Thank you";
				String emailMessageBody = "<p>Hi "+contactUsModel.getName()+"!</p><p>Thanks for your Query. Our system team will get back to you soon.</p> <p>Payment GateWay Team </p>";
				String subject = messageUtil.getBundle("contactus.subject");
				
				EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
				emailInvoicingModel.setAction(action);
				emailInvoicingModel.setHeader(header);
				emailInvoicingModel.setFirstName(contactUsModel.getName());
				emailInvoicingModel.setEmailMessageBody(emailMessageBody);
				emailInvoicingModel.setSubject(subject);
				emailInvoicingModel.setEmailId(contactUsModel.getEmailId());
				emailInvoicingModel.setIsTemplate(true);

				spiderEmailSender.sendEmail(emailInvoicingModel);
				
				
				//sendMail.send(contactUsModel.getEmailId(), messageBody, subject);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("saveContactUs -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for validating the Contact Us Form.
	 * @param contactUsModel
	 * @param httpServletRequest
	 * @throws Exception
	 */
	public void saveContactUsValidation(ContactUsModel contactUsModel, HttpServletRequest httpServletRequest) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("saveContactUsValidation -- START");
		}

		List<String> exe = new ArrayList<String>();

		if (Util.isEmpty(contactUsModel.getName())) {
			exe.add(messageUtil.getBundle("contactus.name.required"));
		}

		if (Util.isEmpty(contactUsModel.getSubject())) {
			exe.add(messageUtil.getBundle("contactus.subject.required"));
		}

		if (Util.isEmpty(contactUsModel.getEmailId())) {
			exe.add(messageUtil.getBundle("contactus.email.required"));
		} else if (Util.emailValidator(contactUsModel.getEmailId())) {
			exe.add(messageUtil.getBundle("contactus.email.invalid"));
		}
		
		if (Util.isEmpty(contactUsModel.getContactUsMessage())) {
			exe.add(messageUtil.getBundle("contactus.message.required"));
		}

		if (exe.size() > 0)
			throw new FormExceptions(exe);

		if (logger.isInfoEnabled()) {
			logger.info("saveContactUsValidation -- END");
		}
	}
	
	/**
	 * This method is used for Fetching all the Contact Us Details.
	 * @return List<ContactUsModel>
	 * @throws Exception
	 */
	public List<ContactUsModel> fetchAllContactUs() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContactUs -- START");
		}
		
		List<ContactUsModel> contactUsModels = contactUsDao.fetchAllContactUs();
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllContactUs -- END");
		}
		
		return contactUsModels;
		
	}
	
	/**
	 * This method is used for fetching the Contact us w.r.t Contact Us Id.
	 * @param contactUsId
	 * @return ContactUsModel
	 * @throws Exception
	 */
	public ContactUsModel fetchContactUsByID(long contactUsId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchContactUsByID -- START");
		}
		
		ContactUsModel contactUsModel = contactUsDao.fetchContactUsByID(contactUsId);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchContactUsByID -- END");
		}
		
		return contactUsModel;
		
	}
	
	/**
	 * This method is used for Replying the Contact Us from the Admin Side.
	 * @param contactUsModel
	 * @return int
	 * @throws Exception
	 */
	public int replyContactUs(ContactUsModel contactUsModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("replyContactUs -- START");
		}
		
		replyContactUsValidation(contactUsModel);
		int result = contactUsDao.replyContactUs(contactUsModel);
		if(result > 0) {
			
			try{
				// Send Email
				String action="replyContactUs";
				String header = "Thank you";
				String emailMessageBody = "<p>Hi "+contactUsModel.getName()+"!</p><p>Your Query was "+contactUsModel.getContactUsMessage()+".</p>"
						+ "<p>"+contactUsModel.getReply()+".</p> "
						+ " <p>Payment GateWay Team </p>";
				String subject = messageUtil.getBundle("contactus.subject");
				
				EmailInvoicingModel emailInvoicingModel = new EmailInvoicingModel();
				emailInvoicingModel.setAction(action);
				emailInvoicingModel.setHeader(header);
				emailInvoicingModel.setFirstName(contactUsModel.getName());
				emailInvoicingModel.setEmailMessageBody(emailMessageBody);
				emailInvoicingModel.setSubject(subject);
				emailInvoicingModel.setQuery(contactUsModel.getContactUsMessage());
				emailInvoicingModel.setReply(contactUsModel.getReply());
				emailInvoicingModel.setEmailId(contactUsModel.getEmailId());
				emailInvoicingModel.setIsTemplate(true);

				spiderEmailSender.sendEmail(emailInvoicingModel);
				
				//sendMail.send(contactUsModel.getEmailId(), messageBody, subject);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("replyContactUs -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used to validate the reply Contact Us Form.
	 * @param contactUsModel
	 * @throws Exception
	 */
	public void replyContactUsValidation(ContactUsModel contactUsModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("replyContactUsValidation -- START");
		}

		Map<String, Exception> exceptions = new HashMap<String, Exception>();

		if (Util.isEmpty(contactUsModel.getName())) {
			
			exceptions.put("contactus.name.required", new EmptyValueException(messageUtil.getBundle("contactus.name.required")));
		}

		if (Util.isEmpty(contactUsModel.getSubject())) {
			
			exceptions.put("contactus.subject.required", new EmptyValueException(messageUtil.getBundle("contactus.subject.required")));
		}

		if (Util.isEmpty(contactUsModel.getEmailId())) {
			
			exceptions.put("contactus.email.required", new EmptyValueException(messageUtil.getBundle("contactus.email.required")));
			
		} else if (Util.emailValidator(contactUsModel.getEmailId())) {
			
			exceptions.put("contactus.email.invalid", new EmptyValueException(messageUtil.getBundle("contactus.email.invalid")));
		}
		
		if (Util.isEmpty(contactUsModel.getContactUsMessage())) {
			
			exceptions.put("contactus.message.required", new EmptyValueException(messageUtil.getBundle("contactus.message.required")));
		}
		
		if (Util.isEmpty(contactUsModel.getReply())) {
			exceptions.put("contactus.reply.required", new EmptyValueException(messageUtil.getBundle("contactus.reply.required")));
		}

		if (exceptions.size() > 0)
			throw new FormExceptions(exceptions);

		if (logger.isInfoEnabled()) {
			logger.info("replyContactUsValidation -- END");
		}
	}
	
	/**
	 * This method is used in the Datatable for the Contact Us.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchContactUsAdmin(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQueryAdmin -- START");
		}
		
		List<ContactUsModel> contactUsModels = contactUsDao.fetchAllContactUsForAdmin(dataTableModel);
		dataTableModel.setData(contactUsModels);
		dataTableModel.setRecordsTotal(contactUsDao.getContactUsCountForAdmin(dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchQueryAdmin -- END");
		}
	}
	
	/**
	 * This method is used to generate the Excel for the Contact Us Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object
	 * @throws Exception
	 */
	public Object[][] exportContactUsForExcelAdmin(DataTableModel dataTableModel, int noOfColumns) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportQueryForExcelAdmin -- START");
		}
		
		List<ContactUsModel> contactUsModels = contactUsDao.fetchAllContactUsForExportForAdmin(dataTableModel);
		Object[][] objects = new Object[contactUsModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			objects2[j++] = contactUsModels.get(i).getEmailId();
			objects2[j++] = contactUsModels.get(i).getSubject();
			objects2[j++] = contactUsModels.get(i).getContactUsMessage();
			objects2[j] = contactUsModels.get(i).getReply();
			
			objects[++i] = objects2;
		}
				
		if (logger.isInfoEnabled()) {
			logger.info("exportQueryForExcelAdmin -- END");
		}
		
		return objects;
	}
}
