/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CityBankTransactionModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.TransactionModel;

/**
 * This interface is used to declare methods for Transaction Database Operations.
 * @author Java-Team
 *
 */
@Repository
public interface TransactionDAO {

	
	/**
	 * This method is used by Merchant for fetching the full Transaction List.
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantId (Long merchantId) throws Exception ;
	
	/**
	 * This method is used by Merchant for fetching the full Transaction List.
	 * @param dataTableModel
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantIdByDate (DataTableModel dataTableModel,Long merchantId) throws Exception ;
	
	
	/**
	 * This method is used to fetch all the transaction details according to the searching citeria for Customer, Merchant and Admin.
	 * @param merchantId
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to count the total number of transactions in Database during AJAX Call.
	 * @param merchantId
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getAllTransactionsCountByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the transaction list.
	 * @param merchantId
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsForExportByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for update the transaction List for claiming purpose by Merchant.
	 * @param merchantID
	 * @param status
	 * @param transactionId
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(Long merchantID, int status, Long transactionId) throws Exception ;
	
	/**
	 * This method is used by Admin for fetching the Transaction List according to the status.
	 * 0 in status means Transaction Done.
	 * 1 in status means Claimed By Merchant.
	 * 2 in status means Disburse from Admin.
	 * @param status
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions (int status) throws Exception ;
	
	
	/**
	 * This method is used to fetch all the transaction details according to the searching citeria for Customer, Merchant and Admin.
	 * @param status
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions(int status, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to count the total number of transactions in Database during AJAX Call.
	 * @param status
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getTransactionsCount(int status, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the transaction list.
	 * @param status
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsForExport(int status, DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for update the transaction List for claiming purpose by Admin.
	 * @param status
	 * @param transactionId
	 * @param adminID
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(int status, Long transactionId,Long adminID) throws Exception ;
	
	/**
	 * This method is used by the Merchant before hitting the Bank's Site.
	 * 3 in status means Transaction Ongoing/Failed.
	 * @param transactionModel
	 * @return long
	 * @throws Exception
	 */
	public long insertTransaction(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used by the Merchant after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int updateTransactionAfterPayment(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used to fetch the transaction w.r.t ID.
	 * @param transactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionById (long transactionId) throws Exception ;
	
	/**
	 * This method is used to Fetch the Transaction By Order Id.
	 * @param orderId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderId (String orderId) throws Exception ;
	
	/**
	 * This method is used for fetching Transaction by TXNID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByTXNId (String txnID) throws Exception ;
	
	/**
	 * This method is used for fetching Transaction by Order_ID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderid (String txnID) throws Exception ;
	
	/**
	 * This method is used to fetch the Latest TXNID.
	 * @return String
	 * @throws Exception
	 */
	public String fetchLatestTXNId() throws Exception ;
	
	/**
	 * This method is used for INSERTING Order.
	 * @param paymentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertOrder(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used for Fetching Order By Token.
	 * @param token
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchOrderByToken(String token) throws Exception ;
	
	/**
	 * This method is used for Fetching Order By Order Transaction ID.
	 * @param orderTransactionId
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchOrderByOrderTransactionId(String orderTransactionId) throws Exception ;
	
	/**
	 * This method is used for Updating Order.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrder(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used for Updating the Customer Details in the Order Table.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderCustomerDetails(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used to fetch the Transaction Details according to his/her ACCESSKEY, orderTransactionId.
	 * @param accessKey
 	 * @param orderTransactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey(String accessKey,String orderTransactionId) throws Exception ;
	
	
	/**
	 * This method is used to fetch card percentage
	 * @param merchantId
	 * @param cardType
	 * @return CardTypePercentageModel
	 * @throws Exception
	 */
	public CardTypePercentageModel fetchCardPercentageByMerchantId (Long merchantId,String cardType) throws Exception;
	
	/**
	 * To Check that same bKash Id has been used for transaction.
	 * @param bKashId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByBKashId (String bKashId) throws Exception;
	
	/**
	 * This method is used by the Merchant after successful citybank transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int updateTransactionAfterCityPayment(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used by the Merchant after successful citybank transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int updateCancelledTransactionAfterCityPayment(TransactionModel transactionModel,int status) throws Exception ;
	
	/**
	 * This method is used to save City Bank transaction data
	 * @param cityBankTransactionModel
	 * @return int
	 * @throws Exception
	 */
	public int saveCityBankTransaction(CityBankTransactionModel cityBankTransactionModel) throws Exception;
	
	/**
	 * This method is used to insert in EBL_TRANSACTION_MASTER table after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int insertEblTransactionAfterPayment(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used to insert in SEBL_TRANSACTION_MASTER table after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int insertSEblTransactionAfterPayment(TransactionModel transactionModel) throws Exception ;
}
