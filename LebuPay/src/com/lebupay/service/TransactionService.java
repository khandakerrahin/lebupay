/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.util.List;

import com.lebupay.model.DataTableModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.TransactionModel;

/**
 * This is TransactionService Interface is used to perform operations on Transaction Module.
 * @author Java Team
 *
 */
public interface TransactionService {
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id.
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantId (Long merchantId) throws Exception ;
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id for Datatable.
	 * @param dataTableModel
	 * @param merchantId
	 * @throws Exception
	 */
	public void fetchAllTransactionsByMerchantId(DataTableModel dataTableModel, Long merchantId) throws Exception ;
	
	/**
	 * This method is used for fetching all the Transaction Details By Merchant Id for Datatable.
	 * @param dataTableModel
	 * @param merchantId
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsByMerchantIdNew(DataTableModel dataTableModel, Long merchantId) throws Exception ;
	
	/**
	 * This method is used for generating the Excel for Datatable of the transaction List w.r.t Merchant Id.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param merchantId
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportAllTransactionsForExcelByMerchantId(DataTableModel dataTableModel, int noOfColumns, Long merchantId) throws Exception ;
	
	/**
	 * This method is used for fetching all the Transactions depending upon the status.
	 * @param status
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions(int status) throws Exception ;
	
	/**
	 * This method is used for fetching all the Transactions depending upon the status for the Datatable.
	 * @param dataTableModel
	 * @param status
	 * @throws Exception
	 */
	public void fetchTransactions(DataTableModel dataTableModel, int status) throws Exception ;
	
	/**
	 * This method is used for generating the Excel for the Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @param status
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportTransactionsForExcel(DataTableModel dataTableModel, int noOfColumns, int status) throws Exception ;
	
	/**
	 * This method is used for updating the status of the Transaction in the Admin Section.
	 * @param status
	 * @param transactionId
	 * @param adminID
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(int status, Long transactionId, Long adminID) throws Exception ;
	
	/**
	 * This method is used for Inserting the Transaction Details.
	 * @param transactionModel
	 * @return long
	 * @throws Exception
	 */
	public long insertTransaction(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used to fetch Transaction by Order Id.
	 * @param orderId
	 * @return  TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderId (String orderId) throws Exception ;
	
	/**
	 * When the payment is complete then the total account balance of the merchant along with his/her loyalty points are being updated in this method.
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	//public synchronized int updateTransactionAfterPayment(TransactionModel transactionModel) throws Exception ;
	
	/**
	 * This method is used for updating the transaction used in the Merchant Section.
	 * @param merchantID
	 * @param status
	 * @param transactionId
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(Long merchantID, int status, Long transactionId) throws Exception ;
	
	/**
	 * This method is used for fetching the Transactions w.r.t Transaction ID.
	 * @param transactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionById (long transactionId) throws Exception ;
	
	/**
	 * This method is used for fetching the Transactions w.r.t txnID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByTXNId (String txnID) throws Exception ;
	
	/**
	 * This method is used for fetching the Transactions w.r.t txnID.
	 * @param txnID
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByTXNId_detail (String txnID) throws Exception ;
	
	/**
	 * This method is used for fetching the Transactions w.r.t ORDER_ID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderid (String txnID) throws Exception ;
	
	/**
	 * This method is used for Inserting the Order.
	 * @param paymentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertOrder(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used for updating the Order Table when the Execute Payment APi is hit.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int executePayment(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used for updating the Customer Details present in the Order Table.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderCustomerDetails(PaymentModel paymentModel) throws Exception ;
	
	/**
	 * This method is used for Fetching Order By Token.
	 * @param token
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchOrderByToken(String token) throws Exception ;
	
	/**
	 * This method is used for fetching the Transaction Details w.r.t AccessKey,orderTransactionId.
	 * @param accessKey
	 * @param orderTransactionId
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey_V2(String accessKey, String orderTransactionId) throws Exception ;
	
	/**
	 * This method is used for fetching the Transaction Details w.r.t AccessKey,orderTransactionId.
	 * @param accessKey
	 * @param orderTransactionId
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey(String accessKey, String orderTransactionId) throws Exception ;
	
	/**
	 * To Check that same bKash Id has been used for transaction.
	 * @param bKashId
	 * @return List
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByBKashId (String bKashId) throws Exception;
}