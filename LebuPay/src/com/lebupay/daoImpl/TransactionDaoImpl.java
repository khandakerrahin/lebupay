/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.lebupay.common.Util;
import com.lebupay.dao.TransactionDAO;
import com.lebupay.logwriter.Logwriter;
import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CityBankTransactionModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.PaymentModel;
import com.lebupay.model.Status;
import com.lebupay.model.TransactionModel;
 
/**
 * This is TransactionDaoImpl extends BaseDao and Implements TransactionDAO Interface used to perform operation on Transaction.
 * @author Java Team
 *
 */
@Repository
public class TransactionDaoImpl extends BaseDao implements TransactionDAO {

	private static Logger logger = Logger.getLogger(TransactionDaoImpl.class);
	//public Logwriter logwrite=new Logwriter();

	/**
	 * This method is used by Merchant for fetching the full Transaction List.
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantId (Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		try {
			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "om.CUTOMER_DETAILS, " // 14
					+ "om.ORDER_TRANSACTION_ID, " //15
					+ "tm.GROSS_AMOUNT, " // 16
					+ "tm.BANK " // 17
					+ "from TRANSACTION_MASTER tm, ORDER_MASTER om "
					+ "where tm.ORDER_ID = om.ORDER_ID and tm.MERCHANT_ID =:MERCHANT_ID and tm.status != 4 order by tm.MODIFIED_DATE desc";

			System.out.println("Fetch All Transaction By Merchant Id ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));

				String date_m = rs.getString(12); 
				SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date_modified = dt2.parse(date_m); 
				SimpleDateFormat dt3 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String modifiedDate = dt3.format(date_modified);

				transactionModel.setModifiedDate(modifiedDate);

				transactionModel.setModifiedBy(rs.getLong(13));

				PaymentModel paymentModel = new PaymentModel();
				String customerDetails = rs.getString(14);
				if(Objects.nonNull(customerDetails)){

					Gson gson = new Gson();
					// JSON to Java object, read it from a Json String.
					paymentModel = gson.fromJson(customerDetails, PaymentModel.class);
					if(Objects.isNull(paymentModel.getName()))
						paymentModel.setName(paymentModel.getFirstName() +" "+paymentModel.getLastName());
				}
				paymentModel.setOrderTransactionID(rs.getString(15));	
				transactionModel.setPaymentModel(paymentModel);
				transactionModel.setGrossAmount(rs.getDouble(16));
				transactionModel.setBank(rs.getString(17));
				transactionModels.add(transactionModel);
			}
			
		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- END");
		}

		return transactionModels;
	}


	/**
	 * This method is used by Merchant for fetching the full Transaction List.
	 * @param dataTableModel
	 * @param merchantId
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionByMerchantIdByDate (DataTableModel dataTableModel,Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		try {
			String sql = "";
			if(!Util.isEmpty(dataTableModel.getToDate()) && !Util.isEmpty(dataTableModel.getFromDate())) {
				sql = "select tm.TRANSACTION_ID," // 1
						+ "tm.AMOUNT," // 2
						+ "tm.BALANCE," // 3
						+ "tm.TXN_ID," // 4
						+ "tm.MERCHANT_ID," // 5
						+ "tm.LOYALTY_POINT," // 6
						+ "tm.RESPONSE_MESSAGE," // 7
						+ "tm.RESPONSE_CODE," // 8
						+ "tm.STATUS," // 9
						+ "tm.CREATED_DATE," // 10
						+ "tm.CREATED_BY," // 11
						+ "tm.MODIFIED_DATE," // 12
						+ "tm.MODIFIED_BY, " // 13
						+ "om.CUTOMER_DETAILS, " // 14
						+ "om.ORDER_TRANSACTION_ID, " //15
						+ "tm.GROSS_AMOUNT, " // 16
						+ "tm.BANK " //17
						+ "from TRANSACTION_MASTER tm, ORDER_MASTER om "
						+ "where tm.ORDER_ID = om.ORDER_ID and tm.MERCHANT_ID =:MERCHANT_ID and tm.status != 4 and (tm.MODIFIED_DATE between TO_TIMESTAMP_TZ('"+dataTableModel.getToDate()+" 01:00:00','YYYY-MM-DD HH:MI:SS') AND "
						+ "TO_TIMESTAMP_TZ('"+dataTableModel.getFromDate()+" 23:59:59','YYYY-MM-DD HH24:MI:SS')) order by tm.MODIFIED_DATE desc";
			} else if(!Util.isEmpty(dataTableModel.getToDate())) {
				sql = "select tm.TRANSACTION_ID," // 1
						+ "tm.AMOUNT," // 2
						+ "tm.BALANCE," // 3
						+ "tm.TXN_ID," // 4
						+ "tm.MERCHANT_ID," // 5
						+ "tm.LOYALTY_POINT," // 6
						+ "tm.RESPONSE_MESSAGE," // 7
						+ "tm.RESPONSE_CODE," // 8
						+ "tm.STATUS," // 9
						+ "tm.CREATED_DATE," // 10
						+ "tm.CREATED_BY," // 11
						+ "tm.MODIFIED_DATE," // 12
						+ "tm.MODIFIED_BY, " // 13
						+ "om.CUTOMER_DETAILS, " // 14
						+ "om.ORDER_TRANSACTION_ID, " //15
						+ "tm.GROSS_AMOUNT, " // 16
						+ "tm.BANK " //17
						+ "from TRANSACTION_MASTER tm, ORDER_MASTER om "
						+ "where tm.ORDER_ID = om.ORDER_ID and tm.MERCHANT_ID =:MERCHANT_ID and tm.status != 4 and (tm.MODIFIED_DATE >= TO_TIMESTAMP_TZ('"+dataTableModel.getToDate()+" 01:00:00','YYYY-MM-DD HH:MI:SS') "
						+ ") order by tm.MODIFIED_DATE desc";
			} else if(!Util.isEmpty(dataTableModel.getFromDate())){
				sql = "select tm.TRANSACTION_ID," // 1
						+ "tm.AMOUNT," // 2
						+ "tm.BALANCE," // 3
						+ "tm.TXN_ID," // 4
						+ "tm.MERCHANT_ID," // 5
						+ "tm.LOYALTY_POINT," // 6
						+ "tm.RESPONSE_MESSAGE," // 7
						+ "tm.RESPONSE_CODE," // 8
						+ "tm.STATUS," // 9
						+ "tm.CREATED_DATE," // 10
						+ "tm.CREATED_BY," // 11
						+ "tm.MODIFIED_DATE," // 12
						+ "tm.MODIFIED_BY, " // 13
						+ "om.CUTOMER_DETAILS, " // 14
						+ "om.ORDER_TRANSACTION_ID, " //15
						+ "tm.GROSS_AMOUNT, " // 16
						+ "tm.BANK " //17
						+ "from TRANSACTION_MASTER tm, ORDER_MASTER om "
						+ "where tm.ORDER_ID = om.ORDER_ID and tm.MERCHANT_ID =:MERCHANT_ID and tm.status != 4 and (tm.MODIFIED_DATE <= "
						+ "TO_TIMESTAMP_TZ('"+dataTableModel.getFromDate()+" 23:59:59','YYYY-MM-DD HH24:MI:SS')) order by tm.MODIFIED_DATE desc";
			}
			System.out.println("Fetch All Transaction By Date range ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));

				String date_m = rs.getString(12); 
				SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date_modified = dt2.parse(date_m); 
				SimpleDateFormat dt3 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String modifiedDate = dt3.format(date_modified);

				transactionModel.setModifiedDate(modifiedDate);
				transactionModel.setModifiedBy(rs.getLong(13));

				PaymentModel paymentModel = new PaymentModel();
				String customerDetails = rs.getString(14);
				if(Objects.nonNull(customerDetails)){

					Gson gson = new Gson();
					// JSON to Java object, read it from a Json String.
					paymentModel = gson.fromJson(customerDetails, PaymentModel.class);
					if(Objects.isNull(paymentModel.getName()))
						paymentModel.setName(paymentModel.getFirstName() +" "+paymentModel.getLastName());
				}
				paymentModel.setOrderTransactionID(rs.getString(15));	
				transactionModel.setPaymentModel(paymentModel);
				transactionModel.setGrossAmount(rs.getDouble(16));
				transactionModel.setBank(rs.getString(17));

				transactionModels.add(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByMerchantId -- END");
		}

		return transactionModels;
	}


	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQueryAllTransactionsByMerchantId(DataTableModel dataTableModel, boolean isCount) {
		String[] searchColumns = {"tm.TXN_ID"};

		String sql = "from TRANSACTION_MASTER tm, ORDER_MASTER om";
		if(searchColumns.length > 0){
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if(i != 0){
					sql += " OR";
				}
				sql += " "+ searchColumns[i] +" like :searchstr";
			}
			sql += ")";
		}


		if(!isCount){
			sql = "select tm.TRANSACTION_ID,tm.AMOUNT,tm.BALANCE,tm.TXN_ID,tm.MERCHANT_ID,tm.LOYALTY_POINT,tm.RESPONSE_MESSAGE,tm.RESPONSE_CODE,tm.STATUS,tm.CREATED_DATE,tm.CREATED_BY,tm.MODIFIED_DATE,tm.MODIFIED_BY,om.CUTOMER_DETAILS,om.ORDER_TRANSACTION_ID,tm.CARD_BRAND,tm.GROSS_AMOUNT,tm.BANK  "+sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}

	/**
	 * This method is used to fetch all the transaction details according to the searching Criteria for Customer, Merchant and Admin.
	 * @param merchantId
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsByMerchantId -- START");
		}

		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
			String sql= getSearchQueryAllTransactionsByMerchantId(dataTableModel, false)+" AND tm.MERCHANT_ID =:MERCHANT_ID AND tm.ORDER_ID = om.ORDER_ID AND tm.status != 4 order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";


			System.out.println("Fetch Transactions: "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				PaymentModel paymentModel = new PaymentModel();
				String customerDetails = rs.getString(14);
				if(Objects.nonNull(customerDetails)){

					Gson gson = new Gson();
					// JSON to Java object, read it from a Json String.
					paymentModel = gson.fromJson(customerDetails, PaymentModel.class);
					if(Objects.isNull(paymentModel.getName()))
						paymentModel.setName(paymentModel.getFirstName() +" "+paymentModel.getLastName());
				}
				paymentModel.setOrderTransactionID(rs.getString(15));	
				transactionModel.setGrossAmount(rs.getDouble(17));
				transactionModel.setBank(rs.getString(18));
				transactionModel.setPaymentModel(paymentModel);
				transactionModels.add(transactionModel);
			}
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsByMerchantId -- END");
		}

		return transactionModels;

	}

	/**
	 * This method is used to count the total number of transactions in Database during AJAX Call.
	 * @param merchantId
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getAllTransactionsCountByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("getAllTransactionsCountByMerchantId -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
			String sql= getSearchQueryAllTransactionsByMerchantId(dataTableModel, true)+" AND tm.MERCHANT_ID =:MERCHANT_ID AND tm.ORDER_ID = om.ORDER_ID AND tm.status != 4 ";


			System.out.println("Transactions Count: "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			ResultSet rs =  pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("getAllTransactionsCountByMerchantId -- END");
		}

		return result;

	}

	/**
	 * This method is used during the export of the transaction list.
	 * @param merchantId
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsForExportByMerchantId(Long merchantId, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsForExportByMerchantId -- START");
		}

		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {

			String sql= getSearchQueryAllTransactionsByMerchantId(dataTableModel, false)+" AND tm.MERCHANT_ID =:MERCHANT_ID AND tm.ORDER_ID = om.ORDER_ID AND tm.status != 4 order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy();


			System.out.println("Fetch Transactions For Export: "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				if(!Util.isEmpty(rs.getString(16))) {

					CardTypePercentageModel cardTypePercentageModel = fetchCardPercentageByMerchantId(merchantId, rs.getString(16));

					if(Objects.nonNull(cardTypePercentageModel)) {

						if(!Util.isEmpty(cardTypePercentageModel.getFinalCardPercentage())){
							transactionModel.setType(cardTypePercentageModel.getType());
							transactionModel.setCardPercentage(String.valueOf(cardTypePercentageModel.getFinalCardPercentage()));// Set Final Card Percentage
						}
						else {
							transactionModel.setType(cardTypePercentageModel.getType());
							transactionModel.setCardPercentage(String.valueOf("0"));// Set Final Card Percentage
						}
					}
				} else {
					transactionModel.setType("NOCARD");
					transactionModel.setCardPercentage(String.valueOf("0"));// Set Final Card Percentage
				}
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				PaymentModel paymentModel = new PaymentModel();
				String customerDetails = rs.getString(14);
				if(Objects.nonNull(customerDetails)){

					Gson gson = new Gson();
					// JSON to Java object, read it from a Json String.
					paymentModel = gson.fromJson(customerDetails, PaymentModel.class);
					if(Objects.isNull(paymentModel.getName()))
						paymentModel.setName(paymentModel.getFirstName() +" "+paymentModel.getLastName());
				}

				int status = rs.getInt(9);

				if(status == 0 || status == 1 || status == 2) {
					paymentModel.setTransactionStatus("Success");
				}
				else if(status == 3) {
					paymentModel.setTransactionStatus("Failed");
				}
				else if(status == 4) {
					paymentModel.setTransactionStatus("Failed");
				}



				paymentModel.setOrderTransactionID(rs.getString(15));
				transactionModel.setGrossAmount(rs.getDouble(17));
				transactionModel.setBank(rs.getString(18));
				transactionModel.setPaymentModel(paymentModel);
				transactionModels.add(transactionModel);
			}
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsForExportByMerchantId -- END");
		}

		return transactionModels;

	}

	@Override
	public CardTypePercentageModel fetchCardPercentageByMerchantId (Long merchantId,String cardType) throws Exception {
		//TODO
		System.out.println("fetchCardPercentageByMerchantId -- START cardType:merchantId "+cardType+" : "+merchantId);

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		CardTypePercentageModel cardTypePercentageModel = null;
		try {
			String sql = "select mcp.CARD_PER_ID, mcp.PERCENTAGE, mcp.MERCHANT_ID, mcp.FIXED, ctm.CARD_TYPE from MERCHANT_CARD_PERCENTAGE mcp, "
					+ " CARD_TYPE_MASTER ctm where mcp.CARD_TYPE_ID = ctm.CARD_TYPE_ID AND UPPER(ctm.CARD_TYPE) = :CARD_TYPE and "
					+ " mcp.MERCHANT_ID =:MERCHANT_ID";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			pst.setStringAtName("CARD_TYPE", cardType.toUpperCase()); // cardType
			//				pst.setStringAtName("CARD_TYPE", cardType==null?"NULL":cardType.toUpperCase()); // cardType
			ResultSet rs =  pst.executeQuery();
			if(rs.next()){

				cardTypePercentageModel = new CardTypePercentageModel();
				cardTypePercentageModel.setCardPerId(rs.getLong(1));
				cardTypePercentageModel.setCardPercentage(rs.getDouble(2));
				cardTypePercentageModel.setMerchantId(rs.getLong(3));
				cardTypePercentageModel.setFixed(rs.getDouble(4));
				cardTypePercentageModel.setCardType(rs.getString(5));

				if(cardTypePercentageModel.getCardPercentage() == 0){
					cardTypePercentageModel.setFinalCardPercentage(cardTypePercentageModel.getFixed());
					cardTypePercentageModel.setType("FIXED");
				} else if(cardTypePercentageModel.getCardPercentage() > 0 && cardTypePercentageModel.getFixed() > 0){
					cardTypePercentageModel.setFinalCardPercentage(cardTypePercentageModel.getCardPercentage());
					cardTypePercentageModel.setType("PERCENTAGE");
				} else {
					cardTypePercentageModel.setFinalCardPercentage(cardTypePercentageModel.getCardPercentage());
					cardTypePercentageModel.setType("PERCENTAGE");
				}										
			}				
		}catch(Exception e) {
			//TODO remove the catch block 
			e.printStackTrace();
			if (logger.isInfoEnabled()) {
				logger.info("fetchCardPercentageByMerchantId catch block "+e);
			}
		}
		finally {		
			try{           
				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{
				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchCardPercentageByMerchantId -- END");
		}

		return cardTypePercentageModel;
	}

	/**
	 * This method is used for update the transaction List for claiming purpose by Merchant.
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

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update TRANSACTION_MASTER set STATUS =:STATUS,MODIFIED_DATE = localtimestamp(0),MODIFIED_BY =:MODIFIED_BY where MERCHANT_ID =:MERCHANT_ID and TRANSACTION_ID =:TRANSACTION_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", status); // STATUS
			pst.setLongAtName("MODIFIED_BY", merchantID); // MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", merchantID); // MERCHANT_ID
			pst.setLongAtName("TRANSACTION_ID", transactionId); // TRANSACTION_ID

			System.out.println("Update Transaction By Merchant==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1; 
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- END");
		}

		return result;
	}

	/**
	 * This method is used by Admin for fetching the Transaction List according to the status.
	 * 0 in status means Transaction Done.
	 * 1 in status means Claimed By Merchant.
	 * 2 in status means Disburse from Admin.
	 * @param status
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions (int status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		try {
			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "m.FIRST_NAME," // 14
					+ "m.LAST_NAME," // 15
					+ "m.EMAIL_ID," // 16
					+ "m.MOBILE_NO," // 17
					+ "m.EBL_USER_NAME," // 18
					+ "m.EBL_PASSWORD," // 19
					+ "m.EBL_ID , "  // 20
					+ "tm.GROSS_AMOUNT, " // 21
					+ "tm.BANK ," // 22

					+ "m.SEBL_USER_NAME," // 23
					+ "m.SEBL_PASSWORD," // 24
					+ "m.SEBL_ID  "  // 25
					+ " from TRANSACTION_MASTER tm, MERCHANT_MASTER m where tm.MERCHANT_ID = m.MERCHANT_ID and tm.STATUS =:STATUS order by tm.CREATED_DATE desc";

			System.out.println("Fetch All Transactions ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", status); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();
				MerchantModel merchantModel = new MerchantModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				merchantModel.setEblUserName(rs.getString(18));
				merchantModel.setEblPassword(rs.getString(19));
				merchantModel.setEblId(rs.getString(20));
				transactionModel.setGrossAmount(rs.getDouble(21));
				transactionModel.setMerchantModel(merchantModel);
				transactionModel.setBank(rs.getString(22));

				merchantModel.setSeblUserName(rs.getString(23));
				merchantModel.setSeblPassword(rs.getString(24));
				merchantModel.setSeblId(rs.getString(25));

				transactionModels.add(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
		}

		return transactionModels;
	}


	/**
	 * This method is used internally to generate the search query.
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQuery(DataTableModel dataTableModel, boolean isCount) {

		String[] searchColumns = {"m.FIRST_NAME", "m.LAST_NAME", "m.EMAIL_ID", "m.MOBILE_NO", "m.EBL_USER_NAME" , "m.EBL_ID", "tm.TXN_ID","tm.GROSS_AMOUNT "};

		String sql = " from TRANSACTION_MASTER tm, MERCHANT_MASTER m";
		if(searchColumns.length > 0){
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if(i != 0){
					sql += " OR";
				}
				sql += " "+ searchColumns[i] +" like :searchstr";
			}
			sql += ")";
		}


		if(!isCount){
			sql = "select tm.TRANSACTION_ID,tm.AMOUNT,tm.BALANCE,tm.TXN_ID,tm.MERCHANT_ID,tm.LOYALTY_POINT,tm.RESPONSE_MESSAGE,tm.RESPONSE_CODE,tm.STATUS,tm.CREATED_DATE,tm.CREATED_BY,tm.MODIFIED_DATE,tm.MODIFIED_BY,m.FIRST_NAME,m.LAST_NAME,m.EMAIL_ID,m.MOBILE_NO,m.EBL_USER_NAME,m.EBL_PASSWORD,m.EBL_ID,tm.GROSS_AMOUNT " +sql;
		}
		else{
			sql = "select COUNT(*)as total "+sql;
		}
		return sql;
	}

	/**
	 * This method is used to fetch all the l details according to the searching citeria for Customer, Merchant and Admin.
	 * @param status
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactions(int status, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- START");
		}

		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
			String sql= getSearchQuery(dataTableModel, false)+" AND tm.STATUS =:STATUS AND tm.MERCHANT_ID = m.MERCHANT_ID order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy() +" OFFSET "+ dataTableModel.getStart() +" ROWS FETCH NEXT "+ dataTableModel.getLength() +" ROWS ONLY";


			System.out.println("Fetch Transactions: "+sql);
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", status); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();
				MerchantModel merchantModel = new MerchantModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(Util.twoDecimalFormatString(rs.getDouble(3)));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));
				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				merchantModel.setEblUserName(rs.getString(18));
				merchantModel.setEblPassword(rs.getString(19));
				merchantModel.setEblId(rs.getString(20));
				transactionModel.setGrossAmount(rs.getDouble(21));
				transactionModel.setMerchantModel(merchantModel);

				transactionModels.add(transactionModel);
			}
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactions -- END");
		}

		return transactionModels;

	}

	/**
	 * This method is used to count the total number of transactions in Database during AJAX Call.
	 * @param status
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getTransactionsCount(int status, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("getTransactionsCount -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
			String sql= getSearchQuery(dataTableModel, true)+" AND tm.STATUS =:STATUS AND tm.MERCHANT_ID = m.MERCHANT_ID";


			System.out.println("Transactions Count: "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", status); // Status
			ResultSet rs =  pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("getTransactionsCount -- END");
		}

		return result;

	}

	/**
	 * This method is used during the export of the transaction list.
	 * @param status
	 * @param dataTableModel
	 * @return List<TransactionModel>
	 * @throws Exception
	 */
	public List<TransactionModel> fetchAllTransactionsForExport(int status, DataTableModel dataTableModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsForExport -- START");
		}

		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {
			String sql= getSearchQuery(dataTableModel, false)+" AND tm.STATUS =:STATUS AND tm.MERCHANT_ID = m.MERCHANT_ID order by "+ dataTableModel.getOrderColumn()+" "+ dataTableModel.getOrderBy();


			System.out.println("Fetch Transactions For Export: "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr","%"+dataTableModel.getSearchString()+"%");
			pst.setIntAtName("STATUS", status); // Status
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();
				MerchantModel merchantModel = new MerchantModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));
				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				merchantModel.setEblUserName(rs.getString(18));
				merchantModel.setEblPassword(rs.getString(19));
				merchantModel.setEblId(rs.getString(20));
				transactionModel.setGrossAmount(rs.getDouble(21));
				transactionModel.setMerchantModel(merchantModel);

				transactionModels.add(transactionModel);
			}
		}	

		finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionsForExport -- END");
		}

		return transactionModels;

	}

	/**
	 * This method is used for update the transaction List for claiming purpose by Admin.
	 * @param status
	 * @param transactionId
	 * @param adminID
	 * @return int
	 * @throws Exception
	 */
	public int updateTransaction(int status, Long transactionId,Long adminID) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update TRANSACTION_MASTER set STATUS =:STATUS,A_MODIFIED_DATE = localtimestamp(0),A_MODIFIED_BY =:A_MODIFIED_BY where TRANSACTION_ID =:TRANSACTION_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", status); // STATUS
			pst.setLongAtName("A_MODIFIED_BY", adminID); // A_MODIFIED_BY
			pst.setLongAtName("TRANSACTION_ID", transactionId); // TRANSACTION_ID

			System.out.println("Update Transaction By Admin==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1; 
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateTransaction -- END");
		}

		return result;
	}

	/**
	 * This method is used by the Merchant before hitting the Bank's Site.
	 * 3 in status means Transaction Ongoing/Failed.
	 * @param transactionModel
	 * @return long
	 * @throws Exception
	 */
	public long insertTransaction(TransactionModel transactionModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- START");
		}

		int min = 2;
		int max = 100000;
		long result = 0;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

		try {

			String latestTXNID = fetchLatestTXNId();
			if(Objects.isNull(latestTXNID))
				transactionModel.setTxnId("LEBUPAY"+randomNum);
			else {

				Long txnID = Long.valueOf(latestTXNID.substring(7, latestTXNID.length()));
				txnID = txnID + 1;
				transactionModel.setTxnId("LEBUPAY"+txnID);
			}

		} catch (Exception e) {
			//e.printStackTrace();
			transactionModel.setTxnId("LEBUPAY"+randomNum);
		}


		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into TRANSACTION_MASTER (TRANSACTION_ID,AMOUNT,MERCHANT_ID,STATUS,CREATED_BY,CREATED_DATE,TXN_ID,ORDER_ID,GROSS_AMOUNT) values(TRANSACTION_MASTER_SEQ.nextval,"
					+ ":AMOUNT,"
					+ ":MERCHANT_ID,"
					+ ":STATUS,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0),:TXN_ID,:ORDER_ID,:GROSS_AMOUNT) ";

			String pk[] = {"TRANSACTION_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setDoubleAtName("AMOUNT", transactionModel.getAmount()); // AMOUNT
			pst.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setIntAtName("STATUS", 4); // STATUS
			pst.setLongAtName("CREATED_BY", transactionModel.getMerchantModel().getMerchantId()); // CREATED_BY
			pst.setStringAtName("TXN_ID", transactionModel.getTxnId()); // TXN_ID
			pst.setStringAtName("ORDER_ID", transactionModel.getOrder_id()); // ORDER_ID
			pst.setDoubleAtName("GROSS_AMOUNT", transactionModel.getAmount()); // GROSS AMOUNT
			System.out.println("Insert Transaction By Merchant==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for insertTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for insertTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertTransaction -- END");
		}

		return result;
	}

	/**
	 * This method is used by the Merchant after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int updateTransactionAfterPayment(TransactionModel transactionModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterPayment -- START");
		}

		int result = 0;
		int result1 = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		OraclePreparedStatement pst1 = null;
		try {

			String sql = "update TRANSACTION_MASTER set TXN_ID =:TXN_ID, BALANCE =:BALANCE, LOYALTY_POINT =:LOYALTY_POINT, STATUS =:STATUS, BKASHID=:BKASHID, BANK=:BANK, "
					+ "AUTHORIZATION_RESPONSE_DATE=:AUTHORIZATION_RESPONSE_DATE,FUNDING_METHOD=:FUNDING_METHOD,ACQUIRER_MESSAGE=:ACQUIRER_MESSAGE,FINANCIAL_NETWORK_CODE=:FINANCIAL_NETWORK_CODE,TRANSACTION_IDENTIFIER=:TRANSACTION_IDENTIFIER,GROSS_AMOUNT=:GROSS_AMOUNT,AMOUNT=:AMOUNT, " // 5
					+ "NAME_ON_CARD=:NAME_ON_CARD,CARD_EXPIRY_YEAR=:CARD_EXPIRY_YEAR,AUTHORIZATION_RESPONSE_TIME=:AUTHORIZATION_RESPONSE_TIME,SECURE_ID=:SECURE_ID,ACQUIRER_CODE=:ACQUIRER_CODE,AUTHORIZATION_RESPONSE_STAN=:AUTHORIZATION_RESPONSE_STAN," // 6
					+ "BANK_MERCHANT_ID=:BANK_MERCHANT_ID,TOTAL_AUTHORIZED_AMOUNT=:TOTAL_AUTHORIZED_AMOUNT,PROVIDED_CARD_NUMBER=:PROVIDED_CARD_NUMBER,CARD_SECURITY_CODE=:CARD_SECURITY_CODE,AUTHENTICATION_TOKEN=:AUTHENTICATION_TOKEN," // 5
					+ "TRANSACTION_RECEIPT=:TRANSACTION_RECEIPT,RESPONSE_GATEWAY_CODE=:RESPONSE_GATEWAY_CODE,ORDER_STATUS=:ORDER_STATUS,ACQUIRER_DATE=:ACQUIRER_DATE,VERSION=:VERSION,COMMERCIAL_CARD_INDICATOR=:COMMERCIAL_CARD_INDICATOR," // 7
					+ "CARD_BRAND=:CARD_BRAND,SOURCE_OF_FUNDS_TYPE=:SOURCE_OF_FUNDS_TYPE,CUSTOMER_FIRSTNAME=:CUSTOMER_FIRSTNAME,DEVICE_BROWSER=:DEVICE_BROWSER,DEVICE_IPADDRESS=:DEVICE_IPADDRESS,ACSECI_VALUE=:ACSECI_VALUE,ACQUIRER_ID=:ACQUIRER_ID,SETTLEMENT_DATE=:SETTLEMENT_DATE," // 8
					+ "TRANSACTION_SOURCE=:TRANSACTION_SOURCE,RESULT=:RESULT,CREATION_TIME=:CREATION_TIME,CUSTOMER_LASTNAME=:CUSTOMER_LASTNAME,TOTAL_REFUNDED_AMOUNT=:TOTAL_REFUNDED_AMOUNT,ACQUIRER_BATCH=:ACQUIRER_BATCH,DESCRIPTION=:DESCRIPTION,TRANSACTION_TYPE=:TRANSACTION_TYPE," // 8
					+ "FINANCIAL_NETWORK_DATE=:FINANCIAL_NETWORK_DATE,RESPONSE_CODE=:RESPONSE_CODE,TRANSACTION_FREQUENCY=:TRANSACTION_FREQUENCY,TRANSACTION_TERMINAL=:TRANSACTION_TERMINAL,AUTHORIZATION_CODE=:AUTHORIZATION_CODE,AUTHENTICATION_STATUS=:AUTHENTICATION_STATUS,PROCESSING_CODE=:PROCESSING_CODE," // 7
					+ "EXPIRY_MONTH=:EXPIRY_MONTH,SECURE_XID=:SECURE_XID,ENROLLMENT_STATUS=:ENROLLMENT_STATUS,CARD_SECURITY_CODE_ERROR=:CARD_SECURITY_CODE_ERROR,TIME_ZONE=:TIME_ZONE,GATEWAY_ENTRY_POINT=:GATEWAY_ENTRY_POINT,MODIFIED_DATE=CURRENT_TIMESTAMP " // 7
					+ "where MERCHANT_ID =:MERCHANT_ID and TRANSACTION_ID =:TRANSACTION_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("TXN_ID", transactionModel.getTxnId()); // TXN_ID
			pst.setDoubleAtName("BALANCE", transactionModel.getBalance()); // BALANCE
			pst.setDoubleAtName("LOYALTY_POINT", transactionModel.getLoyaltyPoint()); // LOYALTY_POINT
			pst.setLongAtName("STATUS", 0); // STATUS

			pst.setStringAtName("AUTHORIZATION_RESPONSE_DATE", transactionModel.getAuthorizationResponse_date());
			pst.setStringAtName("FUNDING_METHOD", transactionModel.getFundingMethod());
			pst.setStringAtName("ACQUIRER_MESSAGE", transactionModel.getAcquirerMessage());
			pst.setStringAtName("FINANCIAL_NETWORK_CODE", transactionModel.getFinancialNetworkCode());
			pst.setStringAtName("TRANSACTION_IDENTIFIER", transactionModel.getTransactionIdentifier());
			pst.setStringAtName("NAME_ON_CARD", transactionModel.getNameOnCard());
			pst.setStringAtName("CARD_EXPIRY_YEAR", transactionModel.getCard_expiry_year());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_TIME", transactionModel.getAuthorizationResponse_time());
			pst.setStringAtName("SECURE_ID", transactionModel.getSecureId());
			pst.setStringAtName("ACQUIRER_CODE", transactionModel.getAcquirerCode());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_STAN", transactionModel.getAuthorizationResponse_stan());
			pst.setStringAtName("BANK_MERCHANT_ID", transactionModel.getMerchantId());
			pst.setStringAtName("TOTAL_AUTHORIZED_AMOUNT", transactionModel.getTotalAuthorizedAmount());
			pst.setStringAtName("PROVIDED_CARD_NUMBER", transactionModel.getProvided_card_number());//TODO
			pst.setStringAtName("CARD_SECURITY_CODE", transactionModel.getCardSecurityCode_gatewayCode());
			pst.setStringAtName("AUTHENTICATION_TOKEN", transactionModel.getAuthenticationToken());
			pst.setStringAtName("TRANSACTION_RECEIPT", transactionModel.getTransaction_receipt());
			pst.setStringAtName("RESPONSE_GATEWAY_CODE", transactionModel.getResponse_gatewayCode());
			pst.setStringAtName("ORDER_STATUS", transactionModel.getOrder_status());
			pst.setStringAtName("ACQUIRER_DATE", transactionModel.getAcquirer_date());
			pst.setStringAtName("VERSION", transactionModel.getVersion());
			pst.setStringAtName("COMMERCIAL_CARD_INDICATOR", transactionModel.getCommercialCardIndicator());
			pst.setStringAtName("CARD_BRAND", transactionModel.getCard_brand());
			pst.setStringAtName("SOURCE_OF_FUNDS_TYPE", transactionModel.getSourceOfFunds_type());
			pst.setStringAtName("CUSTOMER_FIRSTNAME", transactionModel.getCustomer_firstName());
			pst.setStringAtName("DEVICE_BROWSER", transactionModel.getDevice_browser());
			pst.setStringAtName("DEVICE_IPADDRESS", transactionModel.getDevice_ipAddress());
			pst.setStringAtName("ACSECI_VALUE", transactionModel.getAcsEci_value());
			pst.setStringAtName("ACQUIRER_ID", transactionModel.getAcquirer_id());
			pst.setStringAtName("SETTLEMENT_DATE", transactionModel.getSettlementDate());
			pst.setStringAtName("TRANSACTION_SOURCE", transactionModel.getTransaction_source());
			pst.setStringAtName("RESULT", transactionModel.getResult());
			pst.setStringAtName("CREATION_TIME", transactionModel.getCreationTime());
			pst.setStringAtName("CUSTOMER_LASTNAME", transactionModel.getCustomer_lastName());
			pst.setStringAtName("TOTAL_REFUNDED_AMOUNT", transactionModel.getTotalRefundedAmount());
			pst.setStringAtName("ACQUIRER_BATCH", transactionModel.getAcquirer_batch());
			pst.setStringAtName("DESCRIPTION", transactionModel.getDescription());
			pst.setStringAtName("TRANSACTION_TYPE", transactionModel.getTransaction_type());
			pst.setStringAtName("FINANCIAL_NETWORK_DATE", transactionModel.getFinancialNetworkDate());
			pst.setStringAtName("RESPONSE_CODE", transactionModel.getResponseCode());
			pst.setStringAtName("TRANSACTION_FREQUENCY", transactionModel.getTransaction_frequency());
			pst.setStringAtName("TRANSACTION_TERMINAL", transactionModel.getTransaction_terminal());
			pst.setStringAtName("AUTHORIZATION_CODE", transactionModel.getAuthorizationCode());
			pst.setStringAtName("AUTHENTICATION_STATUS", transactionModel.getAuthenticationStatus());
			pst.setStringAtName("PROCESSING_CODE", transactionModel.getProcessingCode());
			pst.setStringAtName("EXPIRY_MONTH", transactionModel.getExpiry_month());
			pst.setStringAtName("SECURE_XID", transactionModel.getSecure_xid());
			pst.setStringAtName("ENROLLMENT_STATUS", transactionModel.getEnrollmentStatus());
			pst.setStringAtName("CARD_SECURITY_CODE_ERROR", transactionModel.getCardSecurityCodeError());
			pst.setStringAtName("TIME_ZONE", transactionModel.getTimeZone());
			pst.setStringAtName("GATEWAY_ENTRY_POINT", transactionModel.getGatewayEntryPoint());

			pst.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setLongAtName("TRANSACTION_ID", transactionModel.getTransactionId()); // TRANSACTION_ID
			pst.setDoubleAtName("GROSS_AMOUNT", transactionModel.getGrossAmount());
			pst.setDoubleAtName("AMOUNT", transactionModel.getAmount());
			pst.setStringAtName("BANK", transactionModel.getBank());
			if(transactionModel.getCard_brand().equals("bkash"))
				pst.setStringAtName("BKASHID", transactionModel.getBkashTrxId());
			else
				pst.setStringAtName("BKASHID", null);

			System.out.println("Update Transaction By Merchant After Successful Transaction==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0) {

				String sql1 = "update MERCHANT_MASTER set TRANSACTION_AMOUNT =:TRANSACTION_AMOUNT, LOYALTY_POINT =:LOYALTY_POINT where MERCHANT_ID =: MERCHANT_ID";
				pst1 = (OraclePreparedStatement) connection.prepareStatement(sql1);
				pst1.setDoubleAtName("TRANSACTION_AMOUNT", transactionModel.getBalance()); // TRANSACTION_AMOUNT
				pst1.setDoubleAtName("LOYALTY_POINT", transactionModel.getLoyaltyPoint()); // LOYALTY_POINT
				pst1.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
				System.out.println("Merchant Balance + Loyalty Point Complete => "+sql1);
				result1 = pst1.executeUpdate();
				if (result1 > 0)
					result = 1; 
				else
					result = 0; 

			}
			connection.commit();

		} finally {

			try{

				if(pst1 != null)
					if(!pst1.isClosed())
						pst1.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransactionAfterPayment");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransactionAfterPayment"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterPayment -- END");
		}

		return result;
	}

	/**
	 * This method is used to fetch the transaction w.r.t ID.
	 * @param transactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionById (long transactionId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionById -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = new TransactionModel();

		try {

			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "m.FIRST_NAME," // 14
					+ "m.LAST_NAME," // 15
					+ "m.EMAIL_ID," // 16
					+ "m.MOBILE_NO," // 17
					+ "tm.ORDER_ID," // 18
					+ "m.EBL_USER_NAME," // 19
					+ "m.EBL_PASSWORD," // 20
					+ "m.EBL_ID, " // 21
					+ "tm.GROSS_AMOUNT, " //22
					+ "m.CITYBANK_MERCHANT_ID," // 23
					+ "om.CUTOMER_DETAILS, " // 24
					//WASIF 20181114
					+ "m.SEBL_USER_NAME," // 25
					+ "m.SEBL_PASSWORD," // 26
					+ "m.SEBL_ID " // 27
					/*
					+ " , m.NOTIFICATION_SMS," //28
					+ "m.NOTIFICATION_EMAIL " //29 /**/
					
					+ "from TRANSACTION_MASTER tm, ORDER_MASTER om, MERCHANT_MASTER m where tm.MERCHANT_ID = m.MERCHANT_ID and tm.ORDER_ID = om.ORDER_ID and tm.TRANSACTION_ID =:TRANSACTION_ID order by tm.CREATED_DATE desc";

			System.out.println("Fetch Transaction By ID ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("TRANSACTION_ID", transactionId); // TRANSACTION_ID
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				MerchantModel merchantModel = new MerchantModel();
				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(rs.getDouble(6));
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));
				transactionModel.setTransactionStatus(rs.getInt(9));
				
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy#hh:mm:ss a");

				transactionModel.setCreatedDate(dt1.format(date));
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				transactionModel.setOrder_id(rs.getString(18));

				merchantModel.setEblUserName(rs.getString(19));
				merchantModel.setEblPassword(rs.getString(20));
				merchantModel.setEblId(rs.getString(21));
				transactionModel.setGrossAmount(rs.getDouble(22));
				merchantModel.setCityMerchantId(rs.getString(23));
				transactionModel.setCustomerDetails(rs.getString(24));
				//WASIF 20181114
				merchantModel.setSeblUserName(rs.getString(25));
				merchantModel.setSeblPassword(rs.getString(26));
				merchantModel.setSeblId(rs.getString(27));
				//TODO
				/*
				merchantModel.setNotification_sms(rs.getString(28));
				merchantModel.setNotification_email(rs.getString(29));/**/
				//.set(rs.getString(27));
				
				transactionModel.setMerchantModel(merchantModel);

			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionById -- END");
		}

		return transactionModel;
	}

	/**
	 * This method is used to Fetch the Transaction By Order Id.
	 * @param orderId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderId (String orderId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = new TransactionModel();

		try {

			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "m.FIRST_NAME," // 14
					+ "m.LAST_NAME," // 15
					+ "m.EMAIL_ID," // 16
					+ "m.MOBILE_NO," // 17
					+ "tm.ORDER_ID" // 18
					+ " from TRANSACTION_MASTER tm, MERCHANT_MASTER m where tm.MERCHANT_ID = m.MERCHANT_ID and tm.ORDER_ID =:ORDER_ID order by tm.CREATED_DATE desc";

			System.out.println("Fetch Transaction By Order ID ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ORDER_ID", orderId); // TRANSACTION_ID
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				MerchantModel merchantModel = new MerchantModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				merchantModel.setMerchantId(rs.getLong(5));
				transactionModel.setLoyaltyPoint(rs.getDouble(6));
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy#hh:mm:ss a");

				transactionModel.setCreatedDate(dt1.format(date));
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				transactionModel.setOrder_id(rs.getString(18));
				transactionModel.setMerchantModel(merchantModel);

			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- END");
		}

		return transactionModel;
	}

	/**
	 * This method is used for fetching Transaction by TXNID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByTXNId (String txnID) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByTXNId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = null;

		try {

			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "m.FIRST_NAME," // 14
					+ "m.LAST_NAME," // 15
					+ "m.EMAIL_ID," // 16
					+ "m.MOBILE_NO," // 17
					+ "m.MERCHANT_ID," // 18
					+ "om.SUCCESS_URL," // 19
					+ "om.FAILURE_URL," // 20
					+ "om.CUTOMER_DETAILS," // 21
					+ "om.AMOUNT," // 22

					+ "m.EBL_USER_NAME," // 23
					+ "m.EBL_PASSWORD," // 24
					+ "m.EBL_ID, " // 25
					+ "tm.GROSS_AMOUNT ,"//26
					//WASIF 20181114
					+ "m.SEBL_USER_NAME," // 27
					+ "m.SEBL_PASSWORD," // 28
					+ "m.SEBL_ID, " // 29
					+ "m.CITYBANK_MERCHANT_ID, " // 30
					//TODO add citybank_merchant_id
					//WASIF 20181114
					//WASIF 20190304 
					+ "om.NOTIFICATION_EMAIL," // 31
					+ "om.NOTIFICATION_SMS" // 32
					
					+ " from TRANSACTION_MASTER tm, MERCHANT_MASTER m, ORDER_MASTER om "
					+ "where tm.MERCHANT_ID = m.MERCHANT_ID and tm.TXN_ID =:TXN_ID and tm.ORDER_ID = om.ORDER_ID order by tm.CREATED_DATE desc";

			System.out.println("Fetch Transaction By TXNID ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("TXN_ID", txnID); // TXN_ID
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				transactionModel = new TransactionModel();
				MerchantModel merchantModel = new MerchantModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(rs.getDouble(6));
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy#hh:mm:ss a");

				transactionModel.setCreatedDate(dt1.format(date));
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				merchantModel.setMerchantId(rs.getLong(18));


				PaymentModel paymentModel = new PaymentModel();
				paymentModel.setSuccessURL(rs.getString(19));
				paymentModel.setFailureURL(rs.getString(20));
				paymentModel.setCustomerDetails(rs.getString(21));
				paymentModel.setAmount(rs.getDouble(22));
				//TODO Wasif 20190304
				paymentModel.setNotification_email(rs.getString(31));
				paymentModel.setNotification_sms(rs.getString(32));
				/**/


				merchantModel.setEblUserName(rs.getString(23));
				merchantModel.setEblPassword(rs.getString(24));
				merchantModel.setEblId(rs.getString(25));
				transactionModel.setGrossAmount(rs.getDouble(26));
				//WASIF 20181114
				merchantModel.setSeblUserName(rs.getString(27));
				merchantModel.setSeblPassword(rs.getString(28));
				merchantModel.setSeblId(rs.getString(29));
				//TODO
				merchantModel.setCityMerchantId(rs.getString(30));
				//WASIF 20181114
				transactionModel.setMerchantModel(merchantModel);

				transactionModel.setPaymentModel(paymentModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByTXNId -- END");
		}

		return transactionModel;
	}


	/**
	 * This method is used for fetching Transaction by TXNID.
	 * @param txnID
	 * @return TransactionModel
	 * @throws Exception
	 */
	public TransactionModel fetchTransactionByOrderid (String txnID) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOrderId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = null;

		try {

			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY, " // 13
					+ "m.FIRST_NAME," // 14
					+ "m.LAST_NAME," // 15
					+ "m.EMAIL_ID," // 16
					+ "m.MOBILE_NO," // 17
					+ "m.MERCHANT_ID," // 18
					+ "om.SUCCESS_URL," // 19
					+ "om.FAILURE_URL," // 20
					+ "om.CUTOMER_DETAILS," // 21
					+ "om.AMOUNT," // 22

					+ "m.EBL_USER_NAME," // 23
					+ "m.EBL_PASSWORD," // 24
					+ "m.EBL_ID," // 25

					+ "m.SEBL_USER_NAME," // 26
					+ "m.SEBL_PASSWORD," // 27
					+ "m.SEBL_ID" // 28

					+ " from TRANSACTION_MASTER tm, MERCHANT_MASTER m, ORDER_MASTER om "
					+ "where tm.MERCHANT_ID = m.MERCHANT_ID and tm.ORDER_ID =:ORDER_ID and tm.ORDER_ID = om.ORDER_ID order by tm.CREATED_DATE desc";

			System.out.println("Fetch Transaction By OrderId ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ORDER_ID", txnID); // TXN_ID
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				transactionModel = new TransactionModel();
				MerchantModel merchantModel = new MerchantModel();


				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(rs.getDouble(6));
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));

				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy#hh:mm:ss a");

				transactionModel.setCreatedDate(dt1.format(date));
				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				merchantModel.setFirstName(rs.getString(14));
				merchantModel.setLastName(rs.getString(15));
				merchantModel.setEmailId(rs.getString(16));
				merchantModel.setMobileNo(rs.getString(17));
				merchantModel.setMerchantId(rs.getLong(18));


				PaymentModel paymentModel = new PaymentModel();
				paymentModel.setSuccessURL(rs.getString(19));
				paymentModel.setFailureURL(rs.getString(20));
				paymentModel.setCustomerDetails(rs.getString(21));
				paymentModel.setAmount(rs.getDouble(22));


				merchantModel.setEblUserName(rs.getString(23));
				merchantModel.setEblPassword(rs.getString(24));
				merchantModel.setEblId(rs.getString(25));
				merchantModel.setSeblUserName(rs.getString(26));
				merchantModel.setSeblPassword(rs.getString(27));
				merchantModel.setSeblId(rs.getString(28));
				transactionModel.setMerchantModel(merchantModel);

				transactionModel.setPaymentModel(paymentModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByOredrId -- END");
		}

		return transactionModel;
	}

	/**
	 * This method is used to fetch the Latest TXNID.
	 * @return String
	 * @throws Exception
	 */
	public String fetchLatestTXNId() throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchLatestTXNId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		String txnID = "";

		try {

			String sql = "select TXN_ID from TRANSACTION_MASTER order by CREATED_DATE desc";

			System.out.println("Fetch Latest TXN ID ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			ResultSet rs =  pst.executeQuery();
			if(rs.next()){

				txnID = rs.getString(1);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchLatestTXNId -- END");
		}

		return txnID;
	}

	/**
	 * This method is used for INSERTING Order.
	 * @param paymentModel
	 * @return long
	 * @throws Exception
	 */
	public long insertOrder(PaymentModel paymentModel) throws Exception {

		if (logger.isInfoEnabled()) {
			//logger.info("insertOrder -- START");
			//TODO 20190220
			logger.info("insertOrder -- START for "+paymentModel.getOrderTransactionID() +" Amount: "+paymentModel.getAmount());
		}
		long result = 0;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
/*
			String sql = "insert into ORDER_MASTER (ORDER_ID,AMOUNT,MERCHANT_ID,SUCCESS_URL,FAILURE_URL,ORDER_TRANSACTION_ID,CUTOMER_DETAILS,TOKEN,STATUS,CREATED_BY,CREATED_DATE) values(ORDER_MASTER_SEQ.nextval,"
					+ ":AMOUNT,"
					+ ":MERCHANT_ID,"
					+ ":SUCCESS_URL,"
					+ ":FAILURE_URL,"
					+ ":ORDER_TRANSACTION_ID,"
					+ ":CUTOMER_DETAILS,"					
					+ ":TOKEN,"
					+ ":STATUS,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0)"
					+ ") ";/**/
			//TODO 20190220
		
			String sql = "insert into ORDER_MASTER (ORDER_ID,AMOUNT,MERCHANT_ID,SUCCESS_URL,FAILURE_URL,ORDER_TRANSACTION_ID,CUTOMER_DETAILS,TOKEN,STATUS,CREATED_BY,CREATED_DATE,NOTIFICATION_SMS,NOTIFICATION_EMAIL) values(ORDER_MASTER_SEQ.nextval,"
					+ ":AMOUNT,"
					+ ":MERCHANT_ID,"
					+ ":SUCCESS_URL,"
					+ ":FAILURE_URL,"
					+ ":ORDER_TRANSACTION_ID,"
					+ ":CUTOMER_DETAILS,"					
					+ ":TOKEN,"
					+ ":STATUS,"
					+ ":CREATED_BY,"
					+ "localtimestamp(0),"
					+ ":NOTIFICATION_SMS,"
					+ ":NOTIFICATION_EMAIL"
					+ ") "; /**/

			String pk[] = {"ORDER_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setDoubleAtName("AMOUNT", paymentModel.getAmount()); // AMOUNT
			pst.setLongAtName("MERCHANT_ID", paymentModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setStringAtName("SUCCESS_URL", paymentModel.getSuccessURL()); // SUCCESS_URL
			pst.setStringAtName("FAILURE_URL", paymentModel.getFailureURL()); // FAILURE_URL
			pst.setStringAtName("ORDER_TRANSACTION_ID", paymentModel.getOrderTransactionID()); // ORDER_TRANSACTION_ID
			pst.setObjectAtName("CUTOMER_DETAILS", paymentModel.getCustomerDetails()); // CUTOMER_DETAILS
			pst.setStringAtName("TOKEN", paymentModel.getToken()); // TOKEN
			pst.setIntAtName("STATUS", 1); // STATUS
			pst.setLongAtName("CREATED_BY", paymentModel.getMerchantModel().getMerchantId()); // CREATED_BY
       //TODO 20190220
		    pst.setStringAtName("NOTIFICATION_SMS",paymentModel.getNotification_sms());
            pst.setStringAtName("NOTIFICATION_EMAIL",paymentModel.getNotification_email());/**/
			System.out.println("Insert Order By Merchant==>> "+sql);


			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for insertOrder");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for insertOrder"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertOrder -- END");
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

		PaymentModel paymentModel = null;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {

			String sql = "select om.ORDER_ID, om.AMOUNT, om.MERCHANT_ID, om.SUCCESS_URL, om.FAILURE_URL, om.ORDER_TRANSACTION_ID, om.CUTOMER_DETAILS,"
					+ " om.TOKEN, om.STATUS, om.CREATED_DATE, om.CREATED_BY, tm.TRANSACTION_ID "
					+ "FROM ORDER_MASTER om LEFT OUTER JOIN TRANSACTION_MASTER tm ON om.ORDER_ID = tm.ORDER_ID WHERE om.TOKEN =:TOKEN";

			System.out.println("Fetch Order By Token ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("TOKEN", token);
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				paymentModel = new PaymentModel();
				paymentModel.setOrderID(rs.getLong(1));
				paymentModel.setAmount(rs.getDouble(2));
				MerchantModel merchantModel = new MerchantModel();
				merchantModel.setMerchantId(rs.getLong(3));
				paymentModel.setMerchantModel(merchantModel);
				paymentModel.setSuccessURL(rs.getString(4));
				paymentModel.setFailureURL(rs.getString(5));
				paymentModel.setOrderTransactionID(rs.getString(6));
				paymentModel.setCustomerDetails(rs.getString(7));
				paymentModel.setToken(rs.getString(8));
				if(rs.getInt(9) == 0)
					paymentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(9) == 1)
					paymentModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(9) == 2)
					paymentModel.setStatus(Status.DELETE);
				else
					paymentModel.setStatus(Status.ACTIVATED);

				paymentModel.setCreatedDate(rs.getString(10));
				paymentModel.setCreatedBy(rs.getLong(11));

				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setTransactionId(rs.getLong(12));
				paymentModel.setTransactionModel(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByToken -- END");
		}

		return paymentModel;
	}

	/**
	 * This method is used for Fetching Order By Order Transaction ID.
	 * @param orderTransactionId
	 * @return PaymentModel
	 * @throws Exception
	 */
	public PaymentModel fetchOrderByOrderTransactionId(String orderTransactionId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByOrderTransactionId -- START");
		}

		PaymentModel paymentModel = null;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;

		try {

			String sql = "select om.ORDER_ID, om.AMOUNT, om.MERCHANT_ID, om.SUCCESS_URL, om.FAILURE_URL, om.ORDER_TRANSACTION_ID, om.CUTOMER_DETAILS,"
					+ " om.TOKEN, om.STATUS, om.CREATED_DATE, om.CREATED_BY, tm.TRANSACTION_ID "
					+ "FROM ORDER_MASTER om LEFT OUTER JOIN TRANSACTION_MASTER tm ON om.ORDER_ID = tm.ORDER_ID WHERE om.ORDER_TRANSACTION_ID =:ORDER_TRANSACTION_ID";

			System.out.println("Fetch Order By OrderTransactionId ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ORDER_TRANSACTION_ID", orderTransactionId);
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				paymentModel = new PaymentModel();
				paymentModel.setOrderID(rs.getLong(1));
				paymentModel.setAmount(rs.getDouble(2));
				MerchantModel merchantModel = new MerchantModel();
				merchantModel.setMerchantId(rs.getLong(3));
				paymentModel.setMerchantModel(merchantModel);
				paymentModel.setSuccessURL(rs.getString(4));
				paymentModel.setFailureURL(rs.getString(5));
				paymentModel.setOrderTransactionID(rs.getString(6));
				paymentModel.setCustomerDetails(rs.getString(7));
				paymentModel.setToken(rs.getString(8));
				if(rs.getInt(9) == 0)
					paymentModel.setStatus(Status.ACTIVE);
				else if(rs.getInt(9) == 1)
					paymentModel.setStatus(Status.INACTIVE);
				else if(rs.getInt(9) == 2)
					paymentModel.setStatus(Status.DELETE);
				else
					paymentModel.setStatus(Status.ACTIVATED);

				paymentModel.setCreatedDate(rs.getString(10));
				paymentModel.setCreatedBy(rs.getLong(11));

				TransactionModel transactionModel = new TransactionModel();
				transactionModel.setTransactionId(rs.getLong(12));
				paymentModel.setTransactionModel(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchOrderByOrderTransactionId -- END");
		}

		return paymentModel;
	}

	/**
	 * This method is used for Updating Order.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrder(PaymentModel paymentModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateOrder -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update ORDER_MASTER set STATUS =:STATUS,MODIFIED_DATE = localtimestamp(0),MODIFIED_BY =:MODIFIED_BY where TOKEN =:TOKEN";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", 0); // STATUS
			pst.setLongAtName("MODIFIED_BY", paymentModel.getMerchantModel().getMerchantId()); // MODIFIED_BY
			pst.setStringAtName("TOKEN", paymentModel.getToken()); // TOKEN

			System.out.println("Update Order By Merchant==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1; 
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateOrder -- END");
		}

		return result;
	}

	/**
	 * This method is used for Updating the Customer Details in the Order Table.
	 * @param paymentModel
	 * @return int
	 * @throws Exception
	 */
	public int updateOrderCustomerDetails(PaymentModel paymentModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update ORDER_MASTER set CUTOMER_DETAILS =:CUTOMER_DETAILS where ORDER_ID =:ORDER_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("CUTOMER_DETAILS", paymentModel.getCustomerDetails()); // CUTOMER_DETAILS
			pst.setLongAtName("ORDER_ID", paymentModel.getOrderID()); // ORDER_ID

			System.out.println("Update Order By Merchant==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1; 
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateOrderCustomerDetails");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateOrderCustomerDetails"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- END");
		}

		return result;
	}

	/**
	 * This method is used for fetching Transaction by TXNID. 3rd API also use this method
	 * @param accessKey
	 * @param orderTransactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey(String accessKey, String orderTransactionId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByAccessKey -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = null;
		PaymentModel paymentModel = null;
		try {
			String sql = "select " 
					+ "NVL(tm.GROSS_AMOUNT,tm.AMOUNT) AS AMOUNT," // 1
					+ "tm.TXN_ID," // 2
					+ "tm.RESPONSE_MESSAGE," // 3
					+ "tm.RESPONSE_CODE," // 4
					+ "tm.STATUS," // 5
					+ "TO_CHAR(tm.CREATED_DATE,'YYYY-MM-DD HH24:MI:SS')," // 6
					+ "om.ORDER_TRANSACTION_ID," // 7
					+ "om.CUTOMER_DETAILS" // 8
					+ " from  MERCHANT_MASTER m, ORDER_MASTER om LEFT OUTER JOIN TRANSACTION_MASTER tm ON tm.ORDER_ID = om.ORDER_ID "
					+ "where om.MERCHANT_ID = m.MERCHANT_ID and m.ACCESS_KEY=:ACCESS_KEY and om.ORDER_TRANSACTION_ID=:ORDER_TRANSACTION_ID";



			String sql1 = "select " 
					+ "NVL(tm.GROSS_AMOUNT,tm.AMOUNT)," // 1
					+ "tm.TXN_ID," // 2
					+ "tm.RESPONSE_MESSAGE," // 3
					+ "tm.RESPONSE_CODE," // 4
					+ "tm.STATUS," // 5
					+ "tm.CREATED_DATE," // 6
					+ "om.ORDER_TRANSACTION_ID," // 7
					+ "om.CUTOMER_DETAILS" // 8
					+ " from  MERCHANT_MASTER m, ORDER_MASTER om LEFT OUTER JOIN TRANSACTION_MASTER tm ON tm.ORDER_ID = om.ORDER_ID "
					+ "where om.MERCHANT_ID = m.MERCHANT_ID and m.ACCESS_KEY='"+accessKey+"' and om.ORDER_TRANSACTION_ID='"+orderTransactionId+"'";

			System.out.println("Fetch Transaction By Access Key & Order Transaction ID ==>> "+sql1);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ACCESS_KEY", accessKey); // ACCESS_KEY
			pst.setStringAtName("ORDER_TRANSACTION_ID", orderTransactionId); // ORDER_TRANSACTION_ID
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				transactionModel = new TransactionModel();

				paymentModel = new PaymentModel();
				paymentModel.setAmount(rs.getDouble(1));
				paymentModel.setOrderTransactionID(rs.getString(7));
				paymentModel.setCustomerDetails(rs.getString(8));

				//String date_s = rs.getString(6); 
				//SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				//paymentModel.setCreatedDate(dt.format(date_s));
				paymentModel.setTransactionDate(rs.getString(6));

				transactionModel.setTxnId(rs.getString(2));
				paymentModel.setResponseMessage(rs.getString(3));
				paymentModel.setResponseCode(rs.getString(4));

				int status = rs.getInt(5);

				if(status == 0 || status == 1 || status == 2) {
					paymentModel.setTransactionStatus("Success");
				}
				else if(status == 3 || status == 5) {
					paymentModel.setTransactionStatus("Cancelled");
				}
				else if(status == 4) {
					paymentModel.setTransactionStatus("Incomplete");
				}

				PaymentModel paymentModel3 = new PaymentModel();
				Gson gson = new Gson();
				paymentModel3 = gson.fromJson(paymentModel.getCustomerDetails(),PaymentModel.class);

				if(Objects.nonNull(paymentModel3)) {
					paymentModel.setName(paymentModel3.getName());
					paymentModel.setEmailId(paymentModel3.getEmailId());
					paymentModel.setMobileNumber(paymentModel3.getMobileNumber());
				}

				paymentModel.setCustomerDetails(null);

				//paymentModel.setTransactionModel(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByAccessKey -- END");
		}

		return paymentModel;
	}


	//WASIF NEW method 20190110
	/**
	 * This method is used for fetching Transaction by TXNID Specially new 3rd API also use this method
	 * @param accessKey
	 * @param orderTransactionId
	 * @return TransactionModel
	 * @throws Exception
	 */
	public PaymentModel fetchTransactionByAccessKey_V2(String accessKey, String orderTransactionId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByAccessKey_V2 -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		TransactionModel transactionModel = null;
		PaymentModel paymentModel = null;
		try {
			//TODO needs work here 

			String sql = "select " 
					+ "NVL(tm.GROSS_AMOUNT,tm.AMOUNT) AS AMOUNT," // 1
					+ "tm.TXN_ID," // 2
					+ "tm.RESPONSE_MESSAGE," // 3
					+ "tm.RESPONSE_CODE," // 4
					+ "tm.STATUS," // 5
					+ "TO_CHAR(tm.CREATED_DATE,'YYYY-MM-DD HH24:MI:SS')," // 6
					+ "om.ORDER_TRANSACTION_ID," // 7
					+ "om.CUTOMER_DETAILS," // 8
					//new fields
					+ "tm.card_brand," //9
					+ "tm.provided_card_number," //10
					+ "tm.bank_merchant_id," //11
					+ "tm.transaction_type," //12
					+ "tm.enrollment_status," //13 bkash sender
					+ "tm.customer_firstname," //14 
					+ "tm.customer_lastname," //15
					+ "tm.device_ipaddress," //16
					+ "cm.transaction_type," //17 
					+ "cm.pan, " //18 CITYBANK CARD MASKING
					+ "tm.bank " //19 name of Bank
					+ "from MERCHANT_MASTER m,CITYBANK_TRANSACTION_MASTER cm, ORDER_MASTER om,TRANSACTION_MASTER tm "
					+ "where tm.ORDER_ID = om.ORDER_ID and tm.transaction_id=cm.transaction_master_id(+) and om.MERCHANT_ID = m.MERCHANT_ID and m.ACCESS_KEY=:ACCESS_KEY and om.ORDER_TRANSACTION_ID=:ORDER_TRANSACTION_ID";


			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ACCESS_KEY", accessKey); // ACCESS_KEY
			pst.setStringAtName("ORDER_TRANSACTION_ID", orderTransactionId); // ORDER_TRANSACTION_ID

			ResultSet rs =  pst.executeQuery();
			String tmpCardNumber;
			//TODO remove below
			while(rs.next()){
				
				transactionModel = new TransactionModel();
				paymentModel = new PaymentModel();

				int status = rs.getInt(5);

				if(status == 0 || status == 1 || status == 2) {
					paymentModel.setTransactionStatus("Success");
				}
				else if(status == 3 || status == 5) {
					paymentModel.setTransactionStatus("Cancelled");
				}
				else if(status == 4) {
					paymentModel.setTransactionStatus("Incomplete");
				}
				
				paymentModel.setAmount(rs.getDouble(1));
				transactionModel.setTxnId(rs.getString(2));
				paymentModel.setResponseMessage(rs.getString(3));
				paymentModel.setResponseCode(rs.getString(4));
				paymentModel.setTransactionDate(rs.getString(6));
				paymentModel.setOrderTransactionID(rs.getString(7));
				paymentModel.setCustomerDetails(rs.getString(8));					
				//WASIF 20190110
				if(status == 0 || status == 1 || status == 2) {
					paymentModel.setBank(rs.getString(19));
					if(rs.getString(13)== null || rs.getString(13).contains("ENROLLED")  ) {
						paymentModel.setCard_brand(rs.getString(9));
						paymentModel.setBank_merchant_id(rs.getString(11));
						paymentModel.setBilling_name(rs.getString(14)+" "+rs.getString(15));
						paymentModel.setDevice_ipaddress(rs.getString(16));						
						if(rs.getString(18)!=null && !rs.getString(18).isEmpty()) {//CITYBANK
							System.out.println("CITYBANK Card found");	
							//TODO
							tmpCardNumber=rs.getString(18);
							tmpCardNumber = tmpCardNumber.substring(0,4)+'x'+'x'+tmpCardNumber.substring(6);
							paymentModel.setProvided_card_number(tmpCardNumber);
							paymentModel.setTransaction_type(rs.getString(17));
						}else {
							tmpCardNumber=rs.getString(10);
							//TODO String purse
							tmpCardNumber = tmpCardNumber.substring(0,4)+'x'+'x'+tmpCardNumber.substring(6);
							paymentModel.setProvided_card_number(tmpCardNumber);						
							paymentModel.setTransaction_type(rs.getString(12));
						}
					}else{ //If bkash respond this //TODO needs to change 
						paymentModel.setBkash_customer(rs.getString(13));
					}

				}

				//TODO
				if (logger.isInfoEnabled()) { 
					logger.info("fetchTrxAccessKey_V2 0,1,2 -S, 3,5-cancel, 4 incomplete status: "+rs.getString(5));
				}

				PaymentModel paymentModel3 = new PaymentModel();
				Gson gson = new Gson();
				paymentModel3 = gson.fromJson(paymentModel.getCustomerDetails(),PaymentModel.class);

				if(Objects.nonNull(paymentModel3)) {
					paymentModel.setName(paymentModel3.getName());
					paymentModel.setEmailId(paymentModel3.getEmailId());
					paymentModel.setMobileNumber(paymentModel3.getMobileNumber());
				}

				paymentModel.setCustomerDetails(null);

				//paymentModel.setTransactionModel(transactionModel);
			}				
		} //TODO added by Wasif
		catch(Exception e) {
			//TODO NEED to remove
			if (logger.isInfoEnabled()) { 
				logger.info(" transactionDaoImpl -fetchTransactionByAccessKey_V2 responseCode: "+ e+"\n"+e.getStackTrace());			
			}
			e.printStackTrace();
		} //TODO added by Wasif 
		finally {
			try{
				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{
				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchTransactionByAccessKey_V2 -- END");
		}

		return paymentModel;
	}


	@Override
	public List<TransactionModel> fetchAllTransactionByBKashId(String bKashId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByBKashId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement  pst = null;
		List<TransactionModel> transactionModels = new ArrayList<TransactionModel>();

		try {
			String sql = "select tm.TRANSACTION_ID," // 1
					+ "tm.AMOUNT," // 2
					+ "tm.BALANCE," // 3
					+ "tm.TXN_ID," // 4
					+ "tm.MERCHANT_ID," // 5
					+ "tm.LOYALTY_POINT," // 6
					+ "tm.RESPONSE_MESSAGE," // 7
					+ "tm.RESPONSE_CODE," // 8
					+ "tm.STATUS," // 9
					+ "tm.CREATED_DATE," // 10
					+ "tm.CREATED_BY," // 11
					+ "tm.MODIFIED_DATE," // 12
					+ "tm.MODIFIED_BY  " // 13						
					+ "from TRANSACTION_MASTER tm "
					+ "where tm.BKASHID = :BKASHID and tm.status != 4 order by tm.CREATED_DATE desc";

			System.out.println("Fetch All Transaction By BKashId ==>> "+sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("BKASHID", bKashId); // bKashId
			ResultSet rs =  pst.executeQuery();
			while(rs.next()){

				TransactionModel transactionModel = new TransactionModel();

				transactionModel.setTransactionId(rs.getLong(1));
				transactionModel.setAmount(rs.getDouble(2));
				transactionModel.setBalance(rs.getDouble(3));
				transactionModel.setTxnId(rs.getString(4));
				transactionModel.setLoyaltyPoint(Math.round(rs.getDouble(6) * 100.0) / 100.0);
				transactionModel.setResponseMessage(rs.getString(7));
				transactionModel.setResponseCode(rs.getString(8));

				transactionModel.setTransactionStatus(rs.getInt(9));
				String date_s = rs.getString(10); 
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss. S"); 
				Date date = dt.parse(date_s); 
				SimpleDateFormat dt1 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a");
				String createdDate = dt1.format(date);
				transactionModel.setCreatedDate(createdDate);

				transactionModel.setCreatedBy(rs.getLong(11));
				transactionModel.setModifiedDate(rs.getString(12));
				transactionModel.setModifiedBy(rs.getLong(13));

				transactionModels.add(transactionModel);
			}

		} finally {
			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			}catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(connection != null)
					if(!connection.isClosed())
						connection.close();

			}catch(Exception e){
				e.printStackTrace();
			}      
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllTransactionByBKashId -- END");
		}

		return transactionModels;
	}


	/**
	 * This method is used by the Merchant after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int updateTransactionAfterCityPayment(TransactionModel transactionModel) throws Exception {


		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterCityPayment -- START");
		}

		int result = 0;
		int result1 = 0;


		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		OraclePreparedStatement pst1 = null;
		try {
			String sql = "update TRANSACTION_MASTER set BANK =:BANK, TXN_ID =:TXN_ID, BALANCE =:BALANCE, LOYALTY_POINT =:LOYALTY_POINT, STATUS =:STATUS, "
					+ "AUTHORIZATION_RESPONSE_DATE=:AUTHORIZATION_RESPONSE_DATE,ACQUIRER_MESSAGE=:ACQUIRER_MESSAGE," // 5
					+ "BANK_MERCHANT_ID=:BANK_MERCHANT_ID,TOTAL_AUTHORIZED_AMOUNT=:TOTAL_AUTHORIZED_AMOUNT," // 5
					+ "ORDER_STATUS=:ORDER_STATUS," // 7
					+ "CARD_BRAND=:CARD_BRAND," // 8
					+ "TRANSACTION_TYPE=:TRANSACTION_TYPE," // 8
					+ "RESPONSE_CODE=:RESPONSE_CODE," // 7
					+ "MODIFIED_DATE=CURRENT_TIMESTAMP, RESPONSE_GATEWAY_CODE=:RESPONSE_GATEWAY_CODE, DESCRIPTION=:DESCRIPTION, NAME_ON_CARD=:NAME_ON_CARD,"
					+ "CUSTOMER_FIRSTNAME=:CUSTOMER_FIRSTNAME, " // 7
					+ "GROSS_AMOUNT=:GROSS_AMOUNT,AMOUNT=:AMOUNT, " // Amount & Gross Amount Update
					// Changed by wasif
					+ "PROVIDED_CARD_NUMBER=:PROVIDED_CARD_NUMBER "
					+ "where MERCHANT_ID =:MERCHANT_ID and TRANSACTION_ID =:TRANSACTION_ID";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("BANK", transactionModel.getBank());
			pst.setStringAtName("TXN_ID", transactionModel.getTxnId()); // TXN_ID
			pst.setDoubleAtName("BALANCE", transactionModel.getBalance()); // BALANCE
			pst.setDoubleAtName("LOYALTY_POINT", transactionModel.getLoyaltyPoint()); // LOYALTY_POINT
			pst.setLongAtName("STATUS", 0); // STATUS
			pst.setStringAtName("AUTHORIZATION_RESPONSE_DATE", transactionModel.getAuthorizationResponse_date());
			pst.setStringAtName("ACQUIRER_MESSAGE", transactionModel.getAcquirerMessage());
			pst.setStringAtName("BANK_MERCHANT_ID", transactionModel.getMerchantId());
			pst.setStringAtName("TOTAL_AUTHORIZED_AMOUNT", transactionModel.getTotalAuthorizedAmount());
			pst.setStringAtName("RESPONSE_GATEWAY_CODE", transactionModel.getResponse_gatewayCode());
			pst.setStringAtName("ORDER_STATUS", transactionModel.getOrder_status());
			pst.setStringAtName("CARD_BRAND", transactionModel.getCard_brand());
			pst.setStringAtName("TRANSACTION_TYPE", transactionModel.getTransaction_type());
			pst.setStringAtName("RESPONSE_CODE", transactionModel.getResponseCode());
			pst.setStringAtName("DESCRIPTION", transactionModel.getDescription());
			pst.setStringAtName("NAME_ON_CARD", transactionModel.getNameOnCard());
			pst.setStringAtName("CUSTOMER_FIRSTNAME", transactionModel.getCustomer_firstName());

			pst.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setLongAtName("TRANSACTION_ID", transactionModel.getTransactionId()); // TRANSACTION_ID
			pst.setDoubleAtName("GROSS_AMOUNT", transactionModel.getGrossAmount());
			pst.setDoubleAtName("AMOUNT", transactionModel.getAmount());
			pst.setStringAtName("PROVIDED_CARD_NUMBER", transactionModel.getProvided_card_number());

			System.out.println("Update City Transaction By Merchant After Successful Transaction==>> "+sql);

			result = pst.executeUpdate();

			if (result > 0) {

				String sql1 = "update MERCHANT_MASTER set TRANSACTION_AMOUNT =:TRANSACTION_AMOUNT, LOYALTY_POINT =:LOYALTY_POINT where MERCHANT_ID =: MERCHANT_ID";
				pst1 = (OraclePreparedStatement) connection.prepareStatement(sql1);
				pst1.setDoubleAtName("TRANSACTION_AMOUNT", transactionModel.getBalance()); // TRANSACTION_AMOUNT
				pst1.setDoubleAtName("LOYALTY_POINT", transactionModel.getLoyaltyPoint()); // LOYALTY_POINT
				pst1.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
				System.out.println("Merchant Balance + Loyalty Point Complete => "+sql1);
				result1 = pst1.executeUpdate();
				if (result1 > 0)
					result = 1; 
				else
					result = 0; 

			}
			connection.commit();

		} finally {

			try{

				if(pst1 != null)
					if(!pst1.isClosed())
						pst1.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransactionAfterCityPayment");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransactionAfterCityPayment"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateTransactionAfterCityPayment -- END");
		}

		return result;
	}

	/**
	 * This method is used by the Merchant after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int updateCancelledTransactionAfterCityPayment(TransactionModel transactionModel, int status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateCancelled/DeclinedTransactionAfterCityPayment -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		OraclePreparedStatement pst1 = null;
		try {
			String sql = "update TRANSACTION_MASTER set BANK =:BANK, TXN_ID =:TXN_ID, BALANCE =:BALANCE, LOYALTY_POINT =:LOYALTY_POINT, STATUS =:STATUS, "
					+ "AUTHORIZATION_RESPONSE_DATE=:AUTHORIZATION_RESPONSE_DATE,ACQUIRER_MESSAGE=:ACQUIRER_MESSAGE," // 5
					+ "BANK_MERCHANT_ID=:BANK_MERCHANT_ID,TOTAL_AUTHORIZED_AMOUNT=:TOTAL_AUTHORIZED_AMOUNT," // 5
					+ "ORDER_STATUS=:ORDER_STATUS," // 7
					+ "CARD_BRAND=:CARD_BRAND," // 8
					+ "TRANSACTION_TYPE=:TRANSACTION_TYPE," // 8
					+ "RESPONSE_CODE=:RESPONSE_CODE," // 7
					+ "MODIFIED_DATE=CURRENT_TIMESTAMP, RESPONSE_GATEWAY_CODE=:RESPONSE_GATEWAY_CODE, DESCRIPTION=:DESCRIPTION,NAME_ON_CARD=:NAME_ON_CARD," 
					+ "CUSTOMER_FIRSTNAME=:CUSTOMER_FIRSTNAME " // 7
					+ "where MERCHANT_ID =:MERCHANT_ID and TRANSACTION_ID =:TRANSACTION_ID";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("BANK", transactionModel.getBank());
			pst.setStringAtName("TXN_ID", transactionModel.getTxnId()); // TXN_ID
			pst.setDoubleAtName("BALANCE", transactionModel.getBalance()); // BALANCE
			pst.setDoubleAtName("LOYALTY_POINT", transactionModel.getLoyaltyPoint()); // LOYALTY_POINT
			pst.setLongAtName("STATUS", status); // STATUS
			pst.setStringAtName("AUTHORIZATION_RESPONSE_DATE", transactionModel.getAuthorizationResponse_date());
			pst.setStringAtName("ACQUIRER_MESSAGE", transactionModel.getAcquirerMessage());
			pst.setStringAtName("BANK_MERCHANT_ID", transactionModel.getMerchantId());
			pst.setStringAtName("TOTAL_AUTHORIZED_AMOUNT", transactionModel.getTotalAuthorizedAmount());
			pst.setStringAtName("RESPONSE_GATEWAY_CODE", transactionModel.getResponse_gatewayCode());
			pst.setStringAtName("ORDER_STATUS", transactionModel.getOrder_status());
			pst.setStringAtName("CARD_BRAND", transactionModel.getCard_brand());
			pst.setStringAtName("TRANSACTION_TYPE", transactionModel.getTransaction_type());
			pst.setStringAtName("RESPONSE_CODE", transactionModel.getResponseCode());
			pst.setStringAtName("DESCRIPTION", transactionModel.getDescription());
			pst.setStringAtName("NAME_ON_CARD", transactionModel.getNameOnCard());
			pst.setStringAtName("CUSTOMER_FIRSTNAME", transactionModel.getCustomer_firstName());

			pst.setLongAtName("MERCHANT_ID", transactionModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setLongAtName("TRANSACTION_ID", transactionModel.getTransactionId()); // TRANSACTION_ID

			System.out.println("Update City Transaction By Merchant After Cancelled/Declined Transaction==>> "+sql);

			result = pst.executeUpdate();


			connection.commit();

		} finally {

			try{

				if(pst1 != null)
					if(!pst1.isClosed())
						pst1.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransactionAfterCityPayment");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransactionAfterCityPayment"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateCancelled/DeclinedTransactionAfterCityPayment -- END");
		}

		return result;
	}

	/**
	 * This method is used save CityBank Transaction.
	 * @param cityTransactionModel
	 * @return int
	 * @throws Exception
	 */

	public int saveCityBankTransaction(CityBankTransactionModel cityTransactionModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into CITYBANK_TRANSACTION_MASTER (CITYBANK_TRANSACTION_ID,CITYBANK_ORDER_ID,TRANSACTION_TYPE,PAN,"
					+ "PURCHASE_AMOUNT,CURRENCY,CURRENCY_CODE,TRANDATETIME,RESPONSE_CODE,RESPONSE_DESCRIPTION,BRAND,ORDER_STATUS,"
					+ "APPROVAL_CODE,ACQFEE,MERCHANT_TRANSACTION_ID,ORDER_DESCRIPTION,APPROVAL_CODE_SCR,PURCHASE_AMOUNT_SCR,"
					+ "CURRENCY_SCR,ORDER_STATUS_SCR,THREE_DS_VERIFICAION,THREE_DS_STATUS,CREATED_DATE,CREATED_BY,"
					+ "TRANSACTION_MASTER_ID) values("
					+ "CITY_TRANSACTION_MASTER_SEQ.nextval,"
					+ ":CITYBANK_ORDER_ID,:TRANSACTION_TYPE,:PAN,:PURCHASE_AMOUNT,:CURRENCY,:CURRENCY_CODE,:TRANDATETIME,:RESPONSE_CODE,"
					+ ":RESPONSE_DESCRIPTION,:BRAND,:ORDER_STATUS,:APPROVAL_CODE,:ACQFEE,:MERCHANT_TRANSACTION_ID,:ORDER_DESCRIPTION,"
					+ ":APPROVAL_CODE_SCR,:PURCHASE_AMOUNT_SCR,:CURRENCY_SCR,:ORDER_STATUS_SCR,:THREE_DS_VERIFICAION,:THREE_DS_STATUS,"
					+ "systimestamp(0),:CREATED_BY,:TRANSACTION_MASTER_ID)";

			String pk[] = {"CITYBANK_TRANSACTION_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setStringAtName("CITYBANK_ORDER_ID", cityTransactionModel.getCitybankOrderId()); 
			pst.setStringAtName("TRANSACTION_TYPE", cityTransactionModel.getTransactionType()); 
			pst.setStringAtName("PAN", cityTransactionModel.getPan()); 
			pst.setDoubleAtName("PURCHASE_AMOUNT", cityTransactionModel.getAmount()); 
			pst.setStringAtName("CURRENCY", cityTransactionModel.getCurrency()); 
			pst.setStringAtName("CURRENCY_CODE", cityTransactionModel.getCurrencyCode());
			pst.setStringAtName("TRANDATETIME", cityTransactionModel.getTranDateTime()); 

			pst.setStringAtName("RESPONSE_CODE", cityTransactionModel.getResponseCode()); 
			pst.setStringAtName("RESPONSE_DESCRIPTION", cityTransactionModel.getResponseDescription()); 

			pst.setStringAtName("BRAND", cityTransactionModel.getBrand()); 
			pst.setStringAtName("ORDER_STATUS", cityTransactionModel.getOrderStatus()); 
			pst.setStringAtName("APPROVAL_CODE", cityTransactionModel.getApprovalCode()); 
			pst.setStringAtName("ACQFEE", cityTransactionModel.getAcqFee()); 

			pst.setStringAtName("MERCHANT_TRANSACTION_ID", cityTransactionModel.getMerchantTransactionId()); 
			pst.setStringAtName("ORDER_DESCRIPTION", cityTransactionModel.getOrderDescription()); 
			pst.setStringAtName("APPROVAL_CODE_SCR", cityTransactionModel.getApprovalCodeScr()); 
			pst.setStringAtName("PURCHASE_AMOUNT_SCR", cityTransactionModel.getPurchaseAmountScr()); 
			pst.setStringAtName("CURRENCY_SCR", cityTransactionModel.getCurrencyScr()); 

			pst.setStringAtName("ORDER_STATUS_SCR", cityTransactionModel.getOrderStatusScr()); 
			pst.setStringAtName("THREE_DS_VERIFICAION", cityTransactionModel.getThreeDsVerificaion()); 
			pst.setStringAtName("THREE_DS_STATUS", cityTransactionModel.getThreeDsStatus()); 
			pst.setStringAtName("TRANSACTION_MASTER_ID", cityTransactionModel.getTransactionMasterId()); 

			pst.setLongAtName("CREATED_BY", cityTransactionModel.getCreatedBy()); 

			System.out.println("Insert City bank successful By Merchant==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateOrderCustomerDetails -- END");
		}

		return result;

	}


	/**
	 * This method is used to save in EBL_Transaction_Master Table after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int insertEblTransactionAfterPayment(TransactionModel transactionModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("insertEblTransactionAfterPayment -- Start");
		}
		int result = 0;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into EBL_TRANSACTION_MASTER (EBL_TRANSACTION_ID,AMOUNT,BALANCE,RESPONSE_MESSAGE,"
					+ "AUTHORIZATION_RESPONSE_DATE,FUNDING_METHOD,ACQUIRER_MESSAGE,FINANCIAL_NETWORK_CODE,TRANSACTION_IDENTIFIER,"
					+ "NAME_ON_CARD, CARD_EXPIRY_YEAR,AUTHORIZATION_RESPONSE_TIME,SECURE_ID,ACQUIRER_CODE,AUTHORIZATION_RESPONSE_STAN,"
					+ "MERCHANT_EBL_ID,TOTAL_AUTHORIZED_AMOUNT,PROVIDED_CARD_NUMBER,CARD_SECURITY_CODE,AUTHENTICATION_TOKEN,"
					+ "TRANSACTION_RECEIPT,RESPONSE_GATEWAY_CODE,ORDER_STATUS,ACQUIRER_DATE,VERSION,COMMERCIAL_CARD_INDICATOR,CARD_BRAND,"
					+ "SOURCE_OF_FUNDS_TYPE,CUSTOMER_FIRSTNAME,DEVICE_BROWSER,DEVICE_IPADDRESS,ACSECI_VALUE,ACQUIRER_ID,SETTLEMENT_DATE,"
					+ "TRANSACTION_SOURCE,RESULT,CREATION_TIME,CUSTOMER_LASTNAME,TOTAL_REFUNDED_AMOUNT,ACQUIRER_BATCH,DESCRIPTION,"
					+ "TRANSACTION_TYPE,FINANCIAL_NETWORK_DATE,RESPONSE_CODE,TRANSACTION_FREQUENCY,TRANSACTION_TERMINAL,AUTHORIZATION_CODE,"
					+ "AUTHENTICATION_STATUS,PROCESSING_CODE,EXPIRY_MONTH,SECURE_XID,ENROLLMENT_STATUS,CARD_SECURITY_CODE_ERROR,"
					+ "TIME_ZONE,GATEWAY_ENTRY_POINT,STATUS,CREATED_DATE,CREATED_BY,"
					+ "TRANSACTION_MASTER_ID) values("
					+ "EBL_TRANSACTION_MASTER_SEQ.nextval,"
					+ ":AMOUNT,:BALANCE,:RESPONSE_MESSAGE, :AUTHORIZATION_RESPONSE_DATE, :FUNDING_METHOD,"
					+ ":ACQUIRER_MESSAGE, :FINANCIAL_NETWORK_CODE, :TRANSACTION_IDENTIFIER, :NAME_ON_CARD, :CARD_EXPIRY_YEAR,"
					+ ":AUTHORIZATION_RESPONSE_TIME, :SECURE_ID, :ACQUIRER_CODE, :AUTHORIZATION_RESPONSE_STAN, :MERCHANT_EBL_ID,"
					+ ":TOTAL_AUTHORIZED_AMOUNT, :PROVIDED_CARD_NUMBER, :CARD_SECURITY_CODE, :AUTHENTICATION_TOKEN, :TRANSACTION_RECEIPT,"
					+ ":RESPONSE_GATEWAY_CODE,:ORDER_STATUS,:ACQUIRER_DATE,:VERSION,:COMMERCIAL_CARD_INDICATOR,:CARD_BRAND,:SOURCE_OF_FUNDS_TYPE,"
					+ ":CUSTOMER_FIRSTNAME,:DEVICE_BROWSER,:DEVICE_IPADDRESS,:ACSECI_VALUE,:ACQUIRER_ID,:SETTLEMENT_DATE,:TRANSACTION_SOURCE,"
					+ ":RESULT,:CREATION_TIME,:CUSTOMER_LASTNAME,:TOTAL_REFUNDED_AMOUNT,:ACQUIRER_BATCH,:DESCRIPTION,:TRANSACTION_TYPE,"
					+ ":FINANCIAL_NETWORK_DATE,:RESPONSE_CODE,:TRANSACTION_FREQUENCY,:TRANSACTION_TERMINAL,:AUTHORIZATION_CODE,"
					+ ":AUTHENTICATION_STATUS,:PROCESSING_CODE,:EXPIRY_MONTH,:SECURE_XID,:ENROLLMENT_STATUS,:CARD_SECURITY_CODE_ERROR,"
					+ ":TIME_ZONE,:GATEWAY_ENTRY_POINT,:STATUS,systimestamp(0),:CREATED_BY,:TRANSACTION_MASTER_ID)";

			String pk[] = {"EBL_TRANSACTION_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setDoubleAtName("AMOUNT", transactionModel.getAmount()); 
			pst.setDoubleAtName("BALANCE", transactionModel.getBalance()); 
			pst.setStringAtName("RESPONSE_MESSAGE", transactionModel.getResponseMessage()); 
			pst.setStringAtName("AUTHORIZATION_RESPONSE_DATE", transactionModel.getAuthorizationResponse_date()); 
			pst.setStringAtName("FUNDING_METHOD", transactionModel.getFundingMethod());
			pst.setStringAtName("ACQUIRER_MESSAGE", transactionModel.getAcquirerMessage()); 
			pst.setStringAtName("FINANCIAL_NETWORK_CODE", transactionModel.getFinancialNetworkCode()); 
			pst.setStringAtName("TRANSACTION_IDENTIFIER", transactionModel.getTransactionIdentifier());
			pst.setStringAtName("NAME_ON_CARD", transactionModel.getNameOnCard());
			pst.setStringAtName("CARD_EXPIRY_YEAR", transactionModel.getCard_expiry_year());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_TIME", transactionModel.getAuthorizationResponse_time());
			pst.setStringAtName("SECURE_ID", transactionModel.getSecureId());
			pst.setStringAtName("ACQUIRER_CODE", transactionModel.getAcquirerCode());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_STAN", transactionModel.getAuthorizationResponse_stan());
			pst.setStringAtName("MERCHANT_EBL_ID", transactionModel.getMerchantId());
			pst.setStringAtName("TOTAL_AUTHORIZED_AMOUNT", transactionModel.getTotalAuthorizedAmount());
			pst.setStringAtName("PROVIDED_CARD_NUMBER", transactionModel.getProvided_card_number());
			pst.setStringAtName("CARD_SECURITY_CODE", transactionModel.getCardSecurityCode_gatewayCode());
			pst.setStringAtName("AUTHENTICATION_TOKEN", transactionModel.getAuthenticationToken());
			pst.setStringAtName("TRANSACTION_RECEIPT", transactionModel.getTransaction_receipt());
			pst.setStringAtName("RESPONSE_GATEWAY_CODE", transactionModel.getResponse_gatewayCode());
			pst.setStringAtName("ORDER_STATUS", transactionModel.getOrder_status());
			pst.setStringAtName("ACQUIRER_DATE", transactionModel.getAcquirerCode());
			pst.setStringAtName("VERSION", transactionModel.getVersion());
			pst.setStringAtName("COMMERCIAL_CARD_INDICATOR", transactionModel.getCommercialCardIndicator());
			pst.setStringAtName("CARD_BRAND", transactionModel.getCard_brand());
			pst.setStringAtName("SOURCE_OF_FUNDS_TYPE", transactionModel.getSourceOfFunds_type());
			pst.setStringAtName("CUSTOMER_FIRSTNAME", transactionModel.getCustomer_firstName());
			pst.setStringAtName("DEVICE_BROWSER", transactionModel.getDevice_browser());
			pst.setStringAtName("DEVICE_IPADDRESS", transactionModel.getDevice_ipAddress());
			pst.setStringAtName("ACSECI_VALUE", transactionModel.getAcsEci_value());
			pst.setStringAtName("ACQUIRER_ID", transactionModel.getAcquirer_id());
			pst.setStringAtName("SETTLEMENT_DATE", transactionModel.getSettlementDate());
			pst.setStringAtName("TRANSACTION_SOURCE", transactionModel.getTransaction_source());
			pst.setStringAtName("RESULT", transactionModel.getResult());
			pst.setStringAtName("CREATION_TIME", transactionModel.getCreationTime());
			pst.setStringAtName("CUSTOMER_LASTNAME", transactionModel.getCustomer_lastName());
			pst.setStringAtName("TOTAL_REFUNDED_AMOUNT", transactionModel.getTotalRefundedAmount());
			pst.setStringAtName("ACQUIRER_BATCH", transactionModel.getAcquirer_batch());
			pst.setStringAtName("DESCRIPTION", transactionModel.getDescription());
			pst.setStringAtName("TRANSACTION_TYPE", transactionModel.getTransaction_type());
			pst.setStringAtName("FINANCIAL_NETWORK_DATE", transactionModel.getFinancialNetworkDate());
			pst.setStringAtName("RESPONSE_CODE", transactionModel.getResponseCode());
			pst.setStringAtName("TRANSACTION_FREQUENCY", transactionModel.getTransaction_frequency());
			pst.setStringAtName("TRANSACTION_TERMINAL", transactionModel.getTransaction_terminal());
			pst.setStringAtName("AUTHORIZATION_CODE", transactionModel.getAuthorizationCode());
			pst.setStringAtName("AUTHENTICATION_STATUS", transactionModel.getAuthenticationStatus());
			pst.setStringAtName("PROCESSING_CODE", transactionModel.getProcessingCode());
			pst.setStringAtName("EXPIRY_MONTH", transactionModel.getExpiry_month());
			pst.setStringAtName("SECURE_XID", transactionModel.getSecure_xid());
			pst.setStringAtName("ENROLLMENT_STATUS", transactionModel.getEnrollmentStatus());
			pst.setStringAtName("CARD_SECURITY_CODE_ERROR", transactionModel.getCardSecurityCodeError());
			pst.setStringAtName("TIME_ZONE", transactionModel.getTimeZone());
			pst.setStringAtName("GATEWAY_ENTRY_POINT", transactionModel.getGatewayEntryPoint());



			pst.setIntAtName("STATUS", 0); // STATUS
			pst.setLongAtName("CREATED_BY", transactionModel.getMerchantModel().getMerchantId()); 
			pst.setLongAtName("TRANSACTION_MASTER_ID", transactionModel.getTransactionId()); 

			System.out.println("Insert EBL Transaction By Merchant==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("EBL Transaction GENERATED KEY => " + result);
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for insertTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for insertTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertEblTransactionAfterPayment -- END");
		}

		return result;
	}



	/**
	 * This method is used to save in SEBL_Transaction_Master Table after successful transaction.
	 * 0 in status means Transaction Done Successfully. 
	 * @param transactionModel
	 * @return int
	 * @throws Exception
	 */
	public int insertSEblTransactionAfterPayment(TransactionModel transactionModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("insertSeblTransactionAfterPayment -- Start");
		}
		int result = 0;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "insert into SEBL_TRANSACTION_MASTER (SEBL_TRANSACTION_ID,AMOUNT,BALANCE,RESPONSE_MESSAGE,"
					+ "AUTHORIZATION_RESPONSE_DATE,FUNDING_METHOD,ACQUIRER_MESSAGE,FINANCIAL_NETWORK_CODE,TRANSACTION_IDENTIFIER,"
					+ "NAME_ON_CARD, CARD_EXPIRY_YEAR,AUTHORIZATION_RESPONSE_TIME,SECURE_ID,ACQUIRER_CODE,AUTHORIZATION_RESPONSE_STAN,"
					+ "MERCHANT_SEBL_ID,TOTAL_AUTHORIZED_AMOUNT,PROVIDED_CARD_NUMBER,CARD_SECURITY_CODE,AUTHENTICATION_TOKEN,"
					+ "TRANSACTION_RECEIPT,RESPONSE_GATEWAY_CODE,ORDER_STATUS,ACQUIRER_DATE,VERSION,COMMERCIAL_CARD_INDICATOR,CARD_BRAND,"
					+ "SOURCE_OF_FUNDS_TYPE,CUSTOMER_FIRSTNAME,DEVICE_BROWSER,DEVICE_IPADDRESS,ACSECI_VALUE,ACQUIRER_ID,SETTLEMENT_DATE,"
					+ "TRANSACTION_SOURCE,RESULT,CREATION_TIME,CUSTOMER_LASTNAME,TOTAL_REFUNDED_AMOUNT,ACQUIRER_BATCH,DESCRIPTION,"
					+ "TRANSACTION_TYPE,FINANCIAL_NETWORK_DATE,RESPONSE_CODE,TRANSACTION_FREQUENCY,TRANSACTION_TERMINAL,AUTHORIZATION_CODE,"
					+ "AUTHENTICATION_STATUS,PROCESSING_CODE,EXPIRY_MONTH,SECURE_XID,ENROLLMENT_STATUS,CARD_SECURITY_CODE_ERROR,"
					+ "TIME_ZONE,GATEWAY_ENTRY_POINT,STATUS,CREATED_DATE,CREATED_BY,"
					+ "TRANSACTION_MASTER_ID) values("
					+ "SEBL_TRANSACTION_MASTER_SEQ.nextval,"
					+ ":AMOUNT,:BALANCE,:RESPONSE_MESSAGE, :AUTHORIZATION_RESPONSE_DATE, :FUNDING_METHOD,"
					+ ":ACQUIRER_MESSAGE, :FINANCIAL_NETWORK_CODE, :TRANSACTION_IDENTIFIER, :NAME_ON_CARD, :CARD_EXPIRY_YEAR,"
					+ ":AUTHORIZATION_RESPONSE_TIME, :SECURE_ID, :ACQUIRER_CODE, :AUTHORIZATION_RESPONSE_STAN, :MERCHANT_SEBL_ID,"
					+ ":TOTAL_AUTHORIZED_AMOUNT, :PROVIDED_CARD_NUMBER, :CARD_SECURITY_CODE, :AUTHENTICATION_TOKEN, :TRANSACTION_RECEIPT,"
					+ ":RESPONSE_GATEWAY_CODE,:ORDER_STATUS,:ACQUIRER_DATE,:VERSION,:COMMERCIAL_CARD_INDICATOR,:CARD_BRAND,:SOURCE_OF_FUNDS_TYPE,"
					+ ":CUSTOMER_FIRSTNAME,:DEVICE_BROWSER,:DEVICE_IPADDRESS,:ACSECI_VALUE,:ACQUIRER_ID,:SETTLEMENT_DATE,:TRANSACTION_SOURCE,"
					+ ":RESULT,:CREATION_TIME,:CUSTOMER_LASTNAME,:TOTAL_REFUNDED_AMOUNT,:ACQUIRER_BATCH,:DESCRIPTION,:TRANSACTION_TYPE,"
					+ ":FINANCIAL_NETWORK_DATE,:RESPONSE_CODE,:TRANSACTION_FREQUENCY,:TRANSACTION_TERMINAL,:AUTHORIZATION_CODE,"
					+ ":AUTHENTICATION_STATUS,:PROCESSING_CODE,:EXPIRY_MONTH,:SECURE_XID,:ENROLLMENT_STATUS,:CARD_SECURITY_CODE_ERROR,"
					+ ":TIME_ZONE,:GATEWAY_ENTRY_POINT,:STATUS,systimestamp(0),:CREATED_BY,:TRANSACTION_MASTER_ID)";

			String pk[] = {"SEBL_TRANSACTION_ID"};
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);
			pst.setDoubleAtName("AMOUNT", transactionModel.getAmount()); 
			pst.setDoubleAtName("BALANCE", transactionModel.getBalance()); 
			pst.setStringAtName("RESPONSE_MESSAGE", transactionModel.getResponseMessage()); 
			pst.setStringAtName("AUTHORIZATION_RESPONSE_DATE", transactionModel.getAuthorizationResponse_date()); 
			pst.setStringAtName("FUNDING_METHOD", transactionModel.getFundingMethod());
			pst.setStringAtName("ACQUIRER_MESSAGE", transactionModel.getAcquirerMessage()); 
			pst.setStringAtName("FINANCIAL_NETWORK_CODE", transactionModel.getFinancialNetworkCode()); 
			pst.setStringAtName("TRANSACTION_IDENTIFIER", transactionModel.getTransactionIdentifier());
			pst.setStringAtName("NAME_ON_CARD", transactionModel.getNameOnCard());
			pst.setStringAtName("CARD_EXPIRY_YEAR", transactionModel.getCard_expiry_year());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_TIME", transactionModel.getAuthorizationResponse_time());
			pst.setStringAtName("SECURE_ID", transactionModel.getSecureId());
			pst.setStringAtName("ACQUIRER_CODE", transactionModel.getAcquirerCode());
			pst.setStringAtName("AUTHORIZATION_RESPONSE_STAN", transactionModel.getAuthorizationResponse_stan());
			pst.setStringAtName("MERCHANT_SEBL_ID", transactionModel.getMerchantId());
			pst.setStringAtName("TOTAL_AUTHORIZED_AMOUNT", transactionModel.getTotalAuthorizedAmount());
			pst.setStringAtName("PROVIDED_CARD_NUMBER", transactionModel.getProvided_card_number());
			pst.setStringAtName("CARD_SECURITY_CODE", transactionModel.getCardSecurityCode_gatewayCode());
			pst.setStringAtName("AUTHENTICATION_TOKEN", transactionModel.getAuthenticationToken());
			pst.setStringAtName("TRANSACTION_RECEIPT", transactionModel.getTransaction_receipt());
			pst.setStringAtName("RESPONSE_GATEWAY_CODE", transactionModel.getResponse_gatewayCode());
			pst.setStringAtName("ORDER_STATUS", transactionModel.getOrder_status());
			pst.setStringAtName("ACQUIRER_DATE", transactionModel.getAcquirerCode());
			pst.setStringAtName("VERSION", transactionModel.getVersion());
			pst.setStringAtName("COMMERCIAL_CARD_INDICATOR", transactionModel.getCommercialCardIndicator());
			pst.setStringAtName("CARD_BRAND", transactionModel.getCard_brand());
			pst.setStringAtName("SOURCE_OF_FUNDS_TYPE", transactionModel.getSourceOfFunds_type());
			pst.setStringAtName("CUSTOMER_FIRSTNAME", transactionModel.getCustomer_firstName());
			pst.setStringAtName("DEVICE_BROWSER", transactionModel.getDevice_browser());
			pst.setStringAtName("DEVICE_IPADDRESS", transactionModel.getDevice_ipAddress());
			pst.setStringAtName("ACSECI_VALUE", transactionModel.getAcsEci_value());
			pst.setStringAtName("ACQUIRER_ID", transactionModel.getAcquirer_id());
			pst.setStringAtName("SETTLEMENT_DATE", transactionModel.getSettlementDate());
			pst.setStringAtName("TRANSACTION_SOURCE", transactionModel.getTransaction_source());
			pst.setStringAtName("RESULT", transactionModel.getResult());
			pst.setStringAtName("CREATION_TIME", transactionModel.getCreationTime());
			pst.setStringAtName("CUSTOMER_LASTNAME", transactionModel.getCustomer_lastName());
			pst.setStringAtName("TOTAL_REFUNDED_AMOUNT", transactionModel.getTotalRefundedAmount());
			pst.setStringAtName("ACQUIRER_BATCH", transactionModel.getAcquirer_batch());
			pst.setStringAtName("DESCRIPTION", transactionModel.getDescription());
			pst.setStringAtName("TRANSACTION_TYPE", transactionModel.getTransaction_type());
			pst.setStringAtName("FINANCIAL_NETWORK_DATE", transactionModel.getFinancialNetworkDate());
			pst.setStringAtName("RESPONSE_CODE", transactionModel.getResponseCode());
			pst.setStringAtName("TRANSACTION_FREQUENCY", transactionModel.getTransaction_frequency());
			pst.setStringAtName("TRANSACTION_TERMINAL", transactionModel.getTransaction_terminal());
			pst.setStringAtName("AUTHORIZATION_CODE", transactionModel.getAuthorizationCode());
			pst.setStringAtName("AUTHENTICATION_STATUS", transactionModel.getAuthenticationStatus());
			pst.setStringAtName("PROCESSING_CODE", transactionModel.getProcessingCode());
			pst.setStringAtName("EXPIRY_MONTH", transactionModel.getExpiry_month());
			pst.setStringAtName("SECURE_XID", transactionModel.getSecure_xid());
			pst.setStringAtName("ENROLLMENT_STATUS", transactionModel.getEnrollmentStatus());
			pst.setStringAtName("CARD_SECURITY_CODE_ERROR", transactionModel.getCardSecurityCodeError());
			pst.setStringAtName("TIME_ZONE", transactionModel.getTimeZone());
			pst.setStringAtName("GATEWAY_ENTRY_POINT", transactionModel.getGatewayEntryPoint());



			pst.setIntAtName("STATUS", 0); // STATUS
			pst.setLongAtName("CREATED_BY", transactionModel.getMerchantModel().getMerchantId()); 
			pst.setLongAtName("TRANSACTION_MASTER_ID", transactionModel.getTransactionId()); 

			System.out.println("Insert SEBL Transaction By Merchant==>> "+sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("SEBL Transaction GENERATED KEY => " + result);
			}
			connection.commit();

		} finally {

			try{

				if(pst != null)
					if(!pst.isClosed())
						pst.close();

			} catch(Exception e){
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for insertTransaction");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for insertTransaction"+ e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertSEblTransactionAfterPayment -- END");
		}

		return result;
	}
}
