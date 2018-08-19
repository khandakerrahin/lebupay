/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.common.MessageUtil;
import com.lebupay.common.SendMail;
import com.lebupay.common.Util;
import com.lebupay.dao.MerchantDao;
import com.lebupay.dao.TransactionDAO;
import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CityBankTransactionModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.LoyaltyModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.TransactionModel;
import com.lebupay.service.LoyaltyService;
import com.lebupay.service.TransactionService;
import com.lebupay.validation.MerchantValidation;

/**
 * This is TransactionServiceImpl Class implements TransactionService interface is used to perform operation on Transaction Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

	private static Logger logger = Logger.getLogger(TransactionServiceImpl.class);
	
	@Autowired
	private TransactionDAO transactionDao;
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private LoyaltyService loyaltyService;
	
	@Autowired
	private MerchantValidation merchantValidation;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private SendMail sendMail;
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id.
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantId (Long merchantId) throws Exception {
			
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionByMerchantId(merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- START");
		}
		
		return transactionModels;
	}
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id for Datatable.
	 * @param dataTableModel
	 * @param merchantId
	 * @throws Exception
	 */
	public void fetchAllTransactionsByMerchantId(DataTableModel dataTableModel, Long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionsByMerchantId(merchantId, dataTableModel);
		dataTableModel.setData(transactionModels);
		dataTableModel.setRecordsTotal(transactionDao.getAllTransactionsCountByMerchantId(merchantId, dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
		}
	}
	
	public List<TransactionModel> fetchAllTransactionsByMerchantIdNew(DataTableModel dataTableModel, Long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}
		
		//merchantValidation.searchTransacionValidation(dataTableModel);
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionByMerchantIdByDate(dataTableModel,merchantId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
		}
		
		return transactionModels;
	}
	
	/**
	 * This method is used for generating the Excel for Datatable of the transaction List w.r.t Merchant Id.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param merchantId
	 * @return Object[][]
	 * @throws Exception
	 */
public Object[][] exportAllTransactionsForExcelByMerchantId(DataTableModel dataTableModel, int noOfColumns, Long merchantId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportAllTransactionsForExcel -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionsForExportByMerchantId(merchantId, dataTableModel);
		Object[][] objects = new Object[transactionModels.size()+1][noOfColumns];
		
		String columnNames = (dataTableModel.getColumnNames())+","+"TXN TIME"+","+"TRANSACTION STATUS";
		
		String[] columns = columnNames.split(",");
		objects[0] = columns; 
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}

			objects2[j++] = String.valueOf(transactionModels.get(i).getPaymentModel().getName());
			objects2[j++] = String.valueOf(transactionModels.get(i).getPaymentModel().getOrderTransactionID());
			objects2[j++] =  String.valueOf(transactionModels.get(i).getGrossAmount()); // Txn Amount
			
			objects2[j++] = String.valueOf(transactionModels.get(i).getAmount()); // Net Amount
			objects2[j++] = transactionModels.get(i).getBank(); // Transaction Status
			
			objects2[j++] = transactionModels.get(i).getTxnId();
			String dateTime = transactionModels.get(i).getCreatedDate();
			String arr [] = null;
			String date = "";
			String time = "";
			if(Objects.nonNull(dateTime)){
				 arr = dateTime.split(",");
				 date = arr[0] + arr[1];
				 time = arr[2];
			}
			objects2[j++] = date; // TXN DATE
			objects2[j++] = time; // TXM TIME
			
			objects2[j++] = transactionModels.get(i).getPaymentModel().getTransactionStatus(); // Transaction Status
			
			objects[++i] = objects2;
			
			
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("exportAllTransactionsForExcel -- END");
		}
		
		return objects;
	}
	
	/**
	 * This method is used for fetching all the Transactions depending upon the status.
	 * @param status
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions(int status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactions(status);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
	   }
		
		return transactionModels;
	}
	
	/**
	 * This method is used for fetching all the Transactions depending upon the status for the Datatable.
	 * @param dataTableModel
	 * @param status
	 * @throws Exception
	 */
	public void fetchTransactions(DataTableModel dataTableModel, int status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactions -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactions(status, dataTableModel);
		dataTableModel.setData(transactionModels);
		dataTableModel.setRecordsTotal(transactionDao.getTransactionsCount(status, dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactions -- END");
		}
	}
	
	/**
	 * This method is used for generating the Excel for the Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param status
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportTransactionsForExcel(DataTableModel dataTableModel, int noOfColumns, int status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportTransactionsForExcel -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionsForExport(status, dataTableModel);
		Object[][] objects = new Object[transactionModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			
			objects2[j++] = transactionModels.get(i).getMerchantModel().getFirstName()+" "+transactionModels.get(i).getMerchantModel().getLastName();
			objects2[j++] = transactionModels.get(i).getMerchantModel().getEmailId();
			objects2[j++] = transactionModels.get(i).getMerchantModel().getMobileNo();
			objects2[j++] = transactionModels.get(i).getMerchantModel().getEblId();
			objects2[j++] = String.valueOf(transactionModels.get(i).getBalance());
			objects2[j++] = String.valueOf(transactionModels.get(i).getLoyaltyPoint());
			objects2[j++] = String.valueOf(transactionModels.get(i).getGrossAmount());
			objects2[j++] = String.valueOf(transactionModels.get(i).getAmount());
			objects2[j++] = transactionModels.get(i).getTxnId();
			objects2[j] = transactionModels.get(i).getCreatedDate();
			
			objects[++i] = objects2;
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("exportTransactionsForExcel -- END");
		}
		
		return objects;
	}
	
	/**
	 * This method is used for updating the status of the Transaction in the Admin Section.
	 * @param status
	 * @param transactionId
	 * @param adminID
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(int status, Long transactionId, Long adminID) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- START");
		}
		
		int result = transactionDao.updateTransaction(status, transactionId, adminID);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Inserting the Transaction Details.
	 * @param transactionModel
	 * @return long
	 * @throws Exception
	 */
	public long insertTransaction(TransactionModel transactionModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- START");
		}
		
		TransactionModel transactionModel2 = transactionDao.fetchTransactionByOrderId(transactionModel.getOrder_id());
		if(Objects.nonNull(transactionModel2.getTransactionId()))
			throw new Exception(messageUtil.getBundle("duplicate.orderId"));
		long result = transactionDao.insertTransaction(transactionModel);
				
	
		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- END");
		}
			
		return result;
	}
	
	/**
	 * This method is used to fetch transaction by OrderID.
	 * @param orderId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderId (String orderId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- START");
		}
		
		TransactionModel transactionModel = transactionDao.fetchTransactionByOrderId(orderId);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- END");
		}
			
		return transactionModel;
	}
	
	/**
	 * When the payment is complete then the total account balance of the merchant along with his/her loyalty points are being updated in this method.
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public synchronized int updateTransactionAfterPayment(TransactionModel transactionModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterPayment -- START");
		}
		
		MerchantModel merchantModel = merchantDao.fetchActiveMerchantById(transactionModel.getMerchantModel().getMerchantId());
		TransactionModel transactionModel2 = transactionDao.fetchTransactionById(transactionModel.getTransactionId());
		CardTypePercentageModel cardTypePercentageModel = transactionDao.fetchCardPercentageByMerchantId(transactionModel.getMerchantModel().getMerchantId(), transactionModel.getCard_brand());
		
		transactionModel.setGrossAmount(transactionModel.getAmount());
		double paybaleAmount = 0.0;
		if(Objects.nonNull(cardTypePercentageModel)){
			if(!Util.isEmpty(cardTypePercentageModel.getType())){
				if(cardTypePercentageModel.getType().equalsIgnoreCase("PERCENTAGE")){ // Percentage
					
					paybaleAmount = transactionModel.getAmount() - (transactionModel.getAmount() * Double.valueOf(cardTypePercentageModel.getFinalCardPercentage()))/100;
					
				} else if(cardTypePercentageModel.getType().equalsIgnoreCase("FIXED")){ // Fixed
					
					paybaleAmount = transactionModel.getAmount() - Double.valueOf(cardTypePercentageModel.getFinalCardPercentage());
					
				} else { // For No Card
					
					paybaleAmount = 0.0;
				}
			}
		} else {
			paybaleAmount = transactionModel.getAmount();
		}
		transactionModel2.setAmount(paybaleAmount);
		transactionModel.setAmount(paybaleAmount);
		double balance = merchantModel.getTransactionAmount() + transactionModel2.getAmount();
		transactionModel.setBalance(balance);
		
		LoyaltyModel loyaltyModel = loyaltyService.getA2P();
		Double amount = loyaltyModel.getAmount();
		Double point = loyaltyModel.getPoint();
		double loyaltyPoint = merchantModel.getLoyaltyPoint() + (point * transactionModel2.getAmount() / amount);
		transactionModel.setLoyaltyPoint(loyaltyPoint);
		int result = transactionDao.updateTransactionAfterPayment(transactionModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterPayment -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for updating the transaction used in the Merchant Section.
	 * @param merchantID
	 * @param status
	 * @param transactionId
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(Long merchantID, int status, Long transactionId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- START");
		}
		
		int result = transactionDao.updateTransaction(merchantID, status, transactionId);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- END");
		}
		
		return result;
	}
	
	
	/**
	 * When the payment is complete then the total account balance of the merchant along with his/her loyalty points are being updated in this method.
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public synchronized int updateEblTransactionAfterPayment(TransactionModel transactionModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateEblTransactionAfterPayment -- START");
		}
		
		
		int result = transactionDao.insertEblTransactionAfterPayment(transactionModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterPayment -- END");
		}
		
		return result;
	}
	/**
	 * This method is used for fetching the Transactions w.r.t Transaction ID.
	 * @param transactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionById (long transactionId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionById -- START");
		}
		
		TransactionModel transactionModel = transactionDao.fetchTransactionById(transactionId);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionById -- END");
	   }
		
		return transactionModel;
	}
	
	/**
	 * This method is used for fetching the Transactions w.r.t txnID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByTXNId (String txnID) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByTXNId -- START");
		}
		
		TransactionModel transactionModel = transactionDao.fetchTransactionByTXNId(txnID);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByTXNId -- END");
	   }
		
		return transactionModel;
	}
	
	/**
	 * This method is used for fetching the Transactions w.r.t OREDR_ID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderid (String txnID) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- START");
		}
		
		TransactionModel transactionModel = transactionDao.fetchTransactionByOrderid(txnID);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- END");
	   }
		
		return transactionModel;
	}
	
	/**
	 * This method is used for Inserting the Order.
	 * @param paymentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertOrder(PaymentModel paymentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertOrder -- START");
		}
		
		merchantValidation.createOrderValidation(paymentModel);
		String customerDetails = "{\"firstName\": \""+paymentModel.getFirstName()+"\",  \"lastName\": \""+paymentModel.getLastName()+"\",  \"email\": \""+paymentModel.getEmailId()+"\",  \"mobileNumber\": \""+paymentModel.getMobileNumber()+"\"}";
		paymentModel.setCustomerDetails(customerDetails);
		long result = transactionDao.insertOrder(paymentModel);
	
		if (logger.isInfoEnabled()) {
			logger.info("insertOrder -- END");
		}
			
		return result;
	}
	
	/**
	 * This method is used for updating the Order Table when the Execute Payment APi is hit.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int executePayment(PaymentModel paymentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("insertOrder -- START");
		}
		
		merchantValidation.updateOrderValidation(paymentModel);
		int result = transactionDao.updateOrder(paymentModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("insertOrder -- END");
		}
			
		return result;
	}
	
	/**
	 * This method is used for updating the Customer Details present in the Order Table.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderCustomerDetails(PaymentModel paymentModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- START");
		}
		
		int result = transactionDao.updateOrderCustomerDetails(paymentModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Fetching Order By Token.
	 * @param token
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchOrderByToken(String token) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByToken -- START");
		}
		
		PaymentModel paymentModel = transactionDao.fetchOrderByToken(token);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByToken -- END");
	   }
		
		return paymentModel;
	}
	
	/**
	 * This method is used for fetching the Merchant Details w.r.t AccessKey.
	 * @param accessKey
	 * @param orderTransactionId
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey(String accessKey, String orderTransactionId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchMerchantByAccessKey -- START");
		}
		
		PaymentModel paymentModel = transactionDao.fetchTransactionByAccessKey(accessKey, orderTransactionId);
		
		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchMerchantByAccessKey -- END");
	    }
		
		return paymentModel;
	}
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id.
	 * @param bKashId
	 * @return List
	 * @throws Exception
	 */
	@Override
	public List<TransactionModel> fetchAllTransactionByBKashId (String bKashId) throws Exception {
			
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByBKashId -- START");
		}
		
		List<TransactionModel> transactionModels = transactionDao.fetchAllTransactionByBKashId(bKashId);
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByBKashId -- START");
		}
		
		return transactionModels;
	}
	
	/**
	 * When the citybank payment is complete then the total account balance of the merchant along with his/her loyalty points are being updated in this method.
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public synchronized int updateTransactionAfterCityPayment(TransactionModel transactionModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterCityPayment -- START");
		}
		
		MerchantModel merchantModel = merchantDao.fetchActiveMerchantById(transactionModel.getMerchantModel().getMerchantId());
		TransactionModel transactionModel2 = transactionDao.fetchTransactionById(transactionModel.getTransactionId());
		
		
		CardTypePercentageModel cardTypePercentageModel = transactionDao.fetchCardPercentageByMerchantId(transactionModel.getMerchantModel().getMerchantId(), transactionModel.getCard_brand());
		transactionModel.setGrossAmount(transactionModel.getAmount());
		double paybaleAmount = 0.0;
		if(Objects.nonNull(cardTypePercentageModel)){
			if(!Util.isEmpty(cardTypePercentageModel.getType())){
			
				if(cardTypePercentageModel.getType().equalsIgnoreCase("PERCENTAGE")){ // Percentage
					
					paybaleAmount = transactionModel.getAmount() - (transactionModel.getAmount() * Double.valueOf(cardTypePercentageModel.getFinalCardPercentage()))/100;
					
				} else if(cardTypePercentageModel.getType().equalsIgnoreCase("FIXED")){ // Fixed
					
					paybaleAmount = transactionModel.getAmount() - Double.valueOf(cardTypePercentageModel.getFinalCardPercentage());
					
				} else { // For No Card
					
					paybaleAmount = 0.0;
				}
			}
		} else {
			
			paybaleAmount = transactionModel.getAmount();
			
		}
		
		transactionModel2.setAmount(paybaleAmount);
		transactionModel.setAmount(paybaleAmount);
		
		double balance = merchantModel.getTransactionAmount() + transactionModel2.getAmount();
		transactionModel.setBalance(balance);
		LoyaltyModel loyaltyModel = loyaltyService.getA2P();
		Double amount = loyaltyModel.getAmount();
		Double point = loyaltyModel.getPoint();
		double loyaltyPoint = merchantModel.getLoyaltyPoint() + (point * transactionModel2.getAmount() / amount);
		transactionModel.setLoyaltyPoint(loyaltyPoint);
		
		int result = transactionDao.updateTransactionAfterCityPayment(transactionModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterCityPayment -- END");
		}
		
		return result;
	}
	
	/**
	 * When the citybank payment is cancelled then the total account balance of the merchant along with his/her loyalty points are being updated in this method.
	 * @param transactionModel
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public synchronized int updateCalcelledTransactionAfterCityPayment(TransactionModel transactionModel, int status) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateCancelledTransactionAfterCityPayment -- START");
		}
		
		int result = transactionDao.updateCancelledTransactionAfterCityPayment(transactionModel, status);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateCancelledTransactionAfterCityPayment -- END");
		}
		
		return result;
	}
	
	/**
	 * This method is used for Fetching Order By Token.
	 * @param cityBankTransactionModel
	 * @return PaymentModel
	 * @throws Exception
	 */
	public int saveCityBankTransaction(CityBankTransactionModel cityBankTransactionModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByToken -- START");
		}
		
		int result = transactionDao.saveCityBankTransaction(cityBankTransactionModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByToken -- END");
	   }
		
		return result;
	}
}