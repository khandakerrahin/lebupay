/**
 * @formatter:off
 *
 */
package com.lebupay.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lebupay.common.MessageUtil;
import com.lebupay.dao.MerchantDao;
import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.Status;
import com.lebupay.model.TypeModel;

import oracle.jdbc.OraclePreparedStatement;

/**
 * This is MerchantDaoImpl extends BaseDao and Implements MerchantDao Interface
 * used to perform operation on Merchant Module.
 * 
 * @author Java Team
 *
 */
@Repository
public class MerchantDaoImpl extends BaseDao implements MerchantDao {

	private static Logger logger = Logger.getLogger(MerchantDaoImpl.class);

	@Autowired
	private MessageUtil messageUtil;

	/**
	 * This method is used to check whether the Mobile Number is already present in
	 * Database or not during sign up.
	 * 
	 * @param mobileNo
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel createPhoneNoCheck(String mobileNo) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createPhoneNoCheck -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.MOBILE_NO=:MOBILE_NO and m.STATUS !=:STATUS";

			System.out.println("Merchant Phone No Checking During Create ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("MOBILE_NO", mobileNo); // mobileNo
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				merchantModel.setCreatedDate(rs.getString(43));
				merchantModel.setCreatedBy(rs.getLong(44));
				merchantModel.setModifiedDate(rs.getString(45));
				merchantModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createPhoneNoCheck -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used for checking whether the Mobile No is associated with
	 * other Merchant or not during update operation.
	 * 
	 * @param mobileNo
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel updatePhoneNoCheck(String mobileNo, Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updatePhoneNoCheck -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.MOBILE_NO=:MOBILE_NO and m.STATUS !=:STATUS and m.MERCHANT_ID !=:MERCHANT_ID";

			System.out.println("Merchant Phone No Checking During Update ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("MOBILE_NO", mobileNo); // mobileNo
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				merchantModel.setCreatedDate(rs.getString(43));
				merchantModel.setCreatedBy(rs.getLong(44));
				merchantModel.setModifiedDate(rs.getString(45));
				merchantModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updatePhoneNoCheck -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used to check whether the Email Id is already present in
	 * Database or not during sign up.
	 * 
	 * @param emailId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel createEmailCheck(String emailId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createEmailCheck -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.EMAIL_ID=:EMAIL_ID and m.STATUS !=:STATUS";

			System.out.println("Merchant Email Id Checking During Create ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("EMAIL_ID", emailId); // EMAIL_ID
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				merchantModel.setCreatedDate(rs.getString(43));
				merchantModel.setCreatedBy(rs.getLong(44));
				merchantModel.setModifiedDate(rs.getString(45));
				merchantModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant createEmailCheck -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used for checking whether the Email Id is associated with
	 * other Merchant or not during update operation.
	 * 
	 * @param emailId
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel updateEmailCheck(String emailId, Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateEmailCheck -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.EMAIL_ID=:EMAIL_ID and m.STATUS !=:STATUS and m.MERCHANT_ID !=:MERCHANT_ID";

			System.out.println("Merchant Email Id Checking During Update ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("EMAIL_ID", emailId); // EMAIL_ID
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				merchantModel.setCreatedDate(rs.getString(43));
				merchantModel.setCreatedBy(rs.getLong(44));
				merchantModel.setModifiedDate(rs.getString(45));
				merchantModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant updateEmailCheck -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used for merchant signup. Insertion done into Merchant Master,
	 * Parameter Master & CheckOut Master Tables.
	 * 
	 * @param merchantModel
	 * @return long
	 * @throws Exception
	 */
	public long merchantSignUp(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("merchantSignUp -- START");
		}

		long result = 0;
		long result4 = 0;
		long result5 = 0;

		String typeName = "Merchant";
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		OraclePreparedStatement pst1 = null;
		OraclePreparedStatement pst2 = null;
		try {

			String sql1 = "insert into MERCHANT_MASTER (MERCHANT_ID, TYPE_ID, FIRST_NAME, LAST_NAME, EMAIL_ID, MOBILE_NO, PASSWORD, UNIQUE_KEY, ACCESS_KEY, SECRET_KEY, PHONE_CODE, PHONE_VERIFIED, STATUS, CREATED_BY, CREATED_DATE) "
					+ "values(MERCHANT_MASTER_SEQ.nextval," + "(select TYPE_ID from type_master where type_name = '"
					+ typeName + "')," + ":first_name," + ":last_name," + ":email_id," + ":mobile_no,"
					+ "( select standard_hash(:password, 'MD5') md5 from DUAL)," + ":UNIQUE_KEY," + ":ACCESS_KEY,"
					+ ":SECRET_KEY," + ":phone_code," + ":phone_verified," + ":status," + ":CREATED_BY,"
					+ "localtimestamp(0)) ";

			String pk[] = { "MERCHANT_ID" };
			pst = (OraclePreparedStatement) connection.prepareStatement(sql1, pk);

			pst.setStringAtName("first_name", merchantModel.getFirstName()); // first_name
			pst.setStringAtName("last_name", merchantModel.getLastName()); // last_name
			pst.setStringAtName("email_id", merchantModel.getEmailId()); // email_id
			pst.setStringAtName("mobile_no", merchantModel.getMobileNo()); // mobile_no
			pst.setStringAtName("password", merchantModel.getPassword()); // password
			pst.setStringAtName("UNIQUE_KEY", merchantModel.getUniqueKey()); // UNIQUE_KEY
			pst.setStringAtName("ACCESS_KEY", merchantModel.getAccessKey()); // ACCESS_KEY
			pst.setStringAtName("SECRET_KEY", merchantModel.getSecretKey()); // SECRET_KEY
			pst.setIntAtName("phone_code", merchantModel.getPhoneCode()); // phone_code
			pst.setIntAtName("phone_verified", Status.INACTIVE.ordinal()); // phone_verified
			pst.setIntAtName("status", Status.INACTIVE.ordinal()); // status
			pst.setIntAtName("CREATED_BY", 0); // CREATED_BY

			System.out.println("Merchant Sign Up => " + sql1);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getLong(1);
				System.out.println("Merchant GENERATED KEY => " + result);

				List<String> parameterNames = new ArrayList<String>();
				parameterNames.add(messageUtil.getBundle("merchant.checkout.parameter1"));
				parameterNames.add(messageUtil.getBundle("merchant.checkout.parameter2"));
				parameterNames.add(messageUtil.getBundle("merchant.checkout.parameter3"));
				parameterNames.add(messageUtil.getBundle("merchant.checkout.parameter4"));
				parameterNames.add(messageUtil.getBundle("merchant.checkout.parameter5"));

				for (String name : parameterNames) {

					String sql2 = "insert into PARAMETER_MASTER (PARAMETER_ID,PARAMETER_NAME,PARAMETER_TYPE,VISIBLE,PERSISTENT,MANDATORY,MERCHANT_ID,IS_DELETABLE,STATUS,CREATED_BY,CREATED_DATE) values(PARAMETER_MASTER_SEQ.nextval,"
							+ ":PARAMETER_NAME," + ":PARAMETER_TYPE," + ":VISIBLE," + ":PERSISTENT," + ":MANDATORY,"
							+ ":MERCHANT_ID," + ":IS_DELETABLE," + ":status," + ":CREATED_BY," + "localtimestamp(0)) ";

					String pk2[] = { "PARAMETER_ID" };
					pst = null;
					pst1 = (OraclePreparedStatement) connection.prepareStatement(sql2, pk2);

					pst1.setStringAtName("PARAMETER_NAME", name); // PARAMETER_NAME
					pst1.setStringAtName("PARAMETER_TYPE", Status.ACTIVE.toString()); // PARAMETER_TYPE
					pst1.setStringAtName("VISIBLE", Status.ACTIVE.toString()); // VISIBLE
					pst1.setStringAtName("PERSISTENT", Status.ACTIVE.toString()); // PERSISTENT
					pst1.setStringAtName("MANDATORY", Status.ACTIVE.toString()); // MANDATORY
					pst1.setLongAtName("MERCHANT_ID", result); // Merchant Id
					pst1.setIntAtName("IS_DELETABLE", Status.ACTIVE.ordinal()); // IS_DELETABLE
					pst1.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
					pst1.setLongAtName("CREATED_BY", result); // CREATED_BY

					System.out.println("PARAMETER MASTER Insert => " + sql2);
					boolean result2 = pst1.execute();

					if (!result2) {

						ResultSet rs1 = pst1.getGeneratedKeys();
						rs1.next();
						result4 = rs1.getLong(1);
						System.out.println("PARAMETER_MASTER GENERATED KEY => " + result4);

					}
				}

				String sql3 = "insert into CHECKOUT_MASTER (CHECKOUT_ID,BACKGROUND_COLOUR,MERCHANT_ID,STATUS,CREATED_BY,CREATED_DATE) values(CHECKOUT_MASTER_SEQ.nextval,"
						+ ":BACKGROUND_COLOUR," + ":MERCHANT_ID," + ":status," + ":CREATED_BY," + "localtimestamp(0)) ";

				String pk3[] = { "CHECKOUT_ID" };
				pst2 = (OraclePreparedStatement) connection.prepareStatement(sql3, pk3);

				pst2.setStringAtName("BACKGROUND_COLOUR", messageUtil.getBundle("merchant.checkout.bgcolour")); // PARAMETER_NAME
				pst2.setLongAtName("MERCHANT_ID", result); // Merchant Id
				pst2.setIntAtName("status", Status.ACTIVE.ordinal()); // Status
				pst2.setLongAtName("CREATED_BY", result); // CREATED_BY

				System.out.println("Checkout MASTER Insert => " + sql3);
				boolean result3 = pst2.execute();

				if (!result3) {

					ResultSet rs2 = pst2.getGeneratedKeys();
					rs2.next();
					result5 = rs2.getLong(1);
					System.out.println("CHECKOUT_MASTER GENERATED KEY => " + result5);

				}

				connection.commit();
			}

		} finally {

			try {
				if (pst2 != null)
					if (!pst2.isClosed())
						pst2.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (pst1 != null)
					if (!pst1.isClosed())
						pst1.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for merchantSignUp");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for merchantSignUp" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("merchantSignUp -- END");
		}

		return result;
	}

	/**
	 * This method is used for phone verification. If verification is done then the
	 * corresponding phone_verified field is updated
	 * 
	 * @param merchantID
	 * @param phoneCode
	 * @return int
	 * @throws Exception
	 */
	public int phoneVerification(long merchantID, String phoneCode) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("phoneVerification -- START");
		}

		int result = 0; // Checking failed for phone verification

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {
			String sql = "select phone_code from MERCHANT_MASTER where MERCHANT_ID=:merchantID and phone_code=:phoneCode and phone_verified =:phone_verified";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("merchantID", merchantID);
			pst.setStringAtName("phoneCode", phoneCode);
			pst.setIntAtName("phone_verified", Status.INACTIVE.ordinal()); // phone_verified
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				Connection connection1 = oracleConnection.Connect();
				OraclePreparedStatement pst1 = null;
				try {
					String sql1 = "update MERCHANT_MASTER set phone_verified =:phone_verified where MERCHANT_ID =: merchantId";
					pst1 = (OraclePreparedStatement) connection1.prepareStatement(sql1);
					pst1.setIntAtName("phone_verified", Status.ACTIVE.ordinal()); // phone_verified
					pst1.setLongAtName("merchantId", merchantID);
					System.out.println("Phone Validation Complete => " + sql1);
					result = pst1.executeUpdate();

					System.out.println("Update result-->" + result);

					if (result > 0)
						result = 1; // Phone Verification Completed
					else
						result = 2; // Phone verified not done
					connection1.commit();

				} finally {

					try {

						if (pst1 != null)
							if (!pst1.isClosed())
								pst1.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

					try { // Closing Connection Object
						if (connection1 != null) {

							if (!connection1.isClosed())
								connection1.close();

							if (logger.isDebugEnabled()) {
								logger.debug("Connection Closed for phoneValidation");
							}
						}
					} catch (Exception e) {

						if (logger.isDebugEnabled()) {
							logger.debug("Connection not closed for phoneValidation" + e.getMessage());
						}

					}
				}
			}

		} finally {

			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("phoneVerification -- END");
		}

		return result;
	}

	/**
	 * This method is used for Login Check Of the Merchant. Status active means only
	 * login and company profile can be add by Merchant. Status activate means Admin
	 * has given the permission to the Merchant for Performing Transaction.
	 * 
	 * @param userName
	 * @param password
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel login(String userName, String password) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant login -- START");
		}
		System.out.println("in dao : " + password);
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;
		String sql = "";

		try {

			if (userName.contains("@")) { // User has provided Email ID for login

				sql = "select m.MERCHANT_ID," // 1
						+ "m.TYPE_ID," // 2
						+ "m.FIRST_NAME," // 3
						+ "m.LAST_NAME," // 4
						+ "m.EMAIL_ID," // 5
						+ "m.MOBILE_NO," // 6
						+ "m.PASSWORD," // 7
						+ "m.UNIQUE_KEY," // 8
						+ "m.ACCESS_KEY," // 9
						+ "m.SECRET_KEY," // 10
						+ "m.PHONE_CODE," // 11
						+ "m.PHONE_VERIFIED," // 12
						+ "m.TRANSACTION_AMOUNT," // 13
						+ "m.LOYALTY_POINT," // 14
						+ "m.STATUS," // 15
						+ "m.CREATED_DATE," // 16
						+ "m.CREATED_BY," // 17
						+ "m.MODIFIED_DATE," // 18
						+ "m.MODIFIED_BY," // 19
						+ "m.A_MODIFIED_DATE," // 20
						+ "m.A_MODIFIED_BY," // 21
						+ "c.COMPANY_ID," // 22
						+ "c.MERCHANT_ID," // 23
						+ "c.COMPANY_NAME," // 24
						+ "c.DBA," // 25
						+ "c.SERVICE," // 26
						+ "c.YEARS_IN_BUSINESS," // 27
						+ "c.PHONE," // 28
						+ "c.FAX," // 29
						+ "c.WEBSITE," // 30
						+ "c.IP," // 31
						+ "c.ADDRESS," // 32
						+ "c.NET_WORTH," // 33
						+ "c.OTHER_BANK," // 34
						+ "c.PROJECT_NO_OF_TPM," // 35
						+ "c.PROJECT_VOL_OF_TPM," // 36
						+ "c.MAX_AMT_ST," // 37
						+ "c.MAX_NO_TPD," // 38
						+ "c.MAX_VOL_TPD," // 39
						+ "c.PAN_NO," // 40
						+ "c.NID," // 41
						+ "c.STATUS," // 42
						+ "c.CREATED_DATE," // 43
						+ "c.CREATED_BY," // 44
						+ "c.MODIFIED_DATE," // 45
						+ "c.MODIFIED_BY," // 46
						+ "t.TYPE_NAME " // 47
						+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
						+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
						+ "where m.EMAIL_ID=:EMAIL_ID and m.PASSWORD =( select standard_hash(:PASSWORD, 'MD5') md5 from DUAL) and (m.STATUS =:STATUS1 OR m.STATUS =:STATUS2)";// and
																																												// m.PHONE_VERIFIED
																																												// =:PHONE_VERIFIED";

				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("EMAIL_ID", userName); // mobileNo

			} else { // User has provided Mobile Number for login

				sql = "select m.MERCHANT_ID," // 1
						+ "m.TYPE_ID," // 2
						+ "m.FIRST_NAME," // 3
						+ "m.LAST_NAME," // 4
						+ "m.EMAIL_ID," // 5
						+ "m.MOBILE_NO," // 6
						+ "m.PASSWORD," // 7
						+ "m.UNIQUE_KEY," // 8
						+ "m.ACCESS_KEY," // 9
						+ "m.SECRET_KEY," // 10
						+ "m.PHONE_CODE," // 11
						+ "m.PHONE_VERIFIED," // 12
						+ "m.TRANSACTION_AMOUNT," // 13
						+ "m.LOYALTY_POINT," // 14
						+ "m.STATUS," // 15
						+ "m.CREATED_DATE," // 16
						+ "m.CREATED_BY," // 17
						+ "m.MODIFIED_DATE," // 18
						+ "m.MODIFIED_BY," // 19
						+ "m.A_MODIFIED_DATE," // 20
						+ "m.A_MODIFIED_BY," // 21
						+ "c.COMPANY_ID," // 22
						+ "c.MERCHANT_ID," // 23
						+ "c.COMPANY_NAME," // 24
						+ "c.DBA," // 25
						+ "c.SERVICE," // 26
						+ "c.YEARS_IN_BUSINESS," // 27
						+ "c.PHONE," // 28
						+ "c.FAX," // 29
						+ "c.WEBSITE," // 30
						+ "c.IP," // 31
						+ "c.ADDRESS," // 32
						+ "c.NET_WORTH," // 33
						+ "c.OTHER_BANK," // 34
						+ "c.PROJECT_NO_OF_TPM," // 35
						+ "c.PROJECT_VOL_OF_TPM," // 36
						+ "c.MAX_AMT_ST," // 37
						+ "c.MAX_NO_TPD," // 38
						+ "c.MAX_VOL_TPD," // 39
						+ "c.PAN_NO," // 40
						+ "c.NID," // 41
						+ "c.STATUS," // 42
						+ "c.CREATED_DATE," // 43
						+ "c.CREATED_BY," // 44
						+ "c.MODIFIED_DATE," // 45
						+ "c.MODIFIED_BY," // 46
						+ "t.TYPE_NAME " // 47
						+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
						+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
						+ "where m.MOBILE_NO =:MOBILE_NO and m.PASSWORD =:( select standard_hash(:PASSWORD, 'MD5') md5 from DUAL) and (m.STATUS =:STATUS1 OR m.STATUS =:STATUS2)";// and
																																													// m.PHONE_VERIFIED
																																													// =:PHONE_VERIFIED";

				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("MOBILE_NO", userName); // mobileNo
			}

			System.out.println("Merchant Login Check ==>> " + sql);

			pst.setStringAtName("PASSWORD", password); // PASSWORD
			pst.setIntAtName("STATUS1", Status.ACTIVE.ordinal()); // STATUS1
			pst.setIntAtName("STATUS2", Status.ACTIVATED.ordinal()); // STATUS2
			// pst.setIntAtName("PHONE_VERIFIED", Status.ACTIVE.ordinal()); //
			// phone_verified
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("rs found");

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant login -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is use to check if user is registered or not. Status active means
	 * only login and company profile can be add by Merchant. Status activate means
	 * Admin has given the permission to the Merchant for Performing Transaction.
	 * 
	 * @param userName
	 * @param password
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel userCheck(String userName) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant userCheck in MerchantDaoImp -- START");
		}
		String password = "";

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;
		String sql = "";

		try {

			if (userName.contains("@")) { // User has provided Email ID for login

				sql = "select m.MERCHANT_ID " // 1
						+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
						+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
						+ "where m.EMAIL_ID=:EMAIL_ID";// and
														// m.PHONE_VERIFIED
														// =:PHONE_VERIFIED";

				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("EMAIL_ID", userName); // mobileNo

			} else { // User has provided Mobile Number for login

				sql = "select m.MERCHANT_ID " // 1
						+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
						+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
						+ "where m.MOBILE_NO =:MOBILE_NO";// and
															// m.PHONE_VERIFIED
															// =:PHONE_VERIFIED";

				pst = (OraclePreparedStatement) connection.prepareStatement(sql);
				pst.setStringAtName("MOBILE_NO", userName); // mobileNo
			}

			System.out.println("Merchant userCheck in MerchantDaoImp ==>> " + sql);
			// pst.setIntAtName("PHONE_VERIFIED", Status.ACTIVE.ordinal()); //
			// phone_verified
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				merchantModel = new MerchantModel();
				merchantModel.setMerchantId(rs.getLong(1));
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant userCheck in MerchantDaoImp -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used for insert the company details for the Merchant first
	 * time.
	 * 
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long insertCompany(CompanyModel companyModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("insertCompany -- START");
		}

		long result = 0;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "insert into COMPANY_MASTER (COMPANY_ID,MERCHANT_ID,COMPANY_NAME,DBA,SERVICE,YEARS_IN_BUSINESS,PHONE,FAX,WEBSITE,IP,ADDRESS,NET_WORTH,OTHER_BANK,"
					+ "PROJECT_NO_OF_TPM,PROJECT_VOL_OF_TPM,MAX_AMT_ST,MAX_NO_TPD,MAX_VOL_TPD,PAN_NO,NID,STATUS,CREATED_BY,CREATED_DATE,MECHANDISING,CONTACTPERSON,DESIGNATION,MOBILE,EMAIL,CODESEC) "
					+ "values(COMPANY_MASTER_SEQ.nextval," + ":MERCHANT_ID," + ":COMPANY_NAME," + ":DBA," + ":SERVICE,"
					+ ":YEARS_IN_BUSINESS," + ":PHONE," + ":FAX," + ":WEBSITE," + ":IP," + ":ADDRESS," + ":NET_WORTH,"
					+ ":OTHER_BANK," + ":PROJECT_NO_OF_TPM," + ":PROJECT_VOL_OF_TPM," + ":MAX_AMT_ST," + ":MAX_NO_TPD,"
					+ ":MAX_VOL_TPD," + ":PAN_NO," + ":NID," + ":STATUS," + ":CREATED_BY," + "localtimestamp(0),"
					+ ":MECHANDISING," + ":CONTACTPERSON," + ":DESIGNATION," + ":MOBILE," + ":EMAIL,:CODESEC)";

			String pk[] = { "COMPANY_ID" };
			pst = (OraclePreparedStatement) connection.prepareStatement(sql, pk);

			pst.setLongAtName("MERCHANT_ID", companyModel.getMerchantModel().getMerchantId()); // MERCHANT_ID
			pst.setStringAtName("COMPANY_NAME", companyModel.getCompanyName()); // COMPANY_NAME
			pst.setStringAtName("DBA", companyModel.getDba()); // DBA
			pst.setStringAtName("SERVICE", companyModel.getService()); // SERVICE
			pst.setIntAtName("YEARS_IN_BUSINESS", companyModel.getYearsInBusiness()); // YEARS_IN_BUSINESS
			pst.setStringAtName("PHONE", companyModel.getPhone()); // PHONE
			pst.setStringAtName("FAX", companyModel.getFax()); // FAX
			pst.setStringAtName("WEBSITE", companyModel.getWebsite()); // WEBSITE
			pst.setStringAtName("IP", companyModel.getIp()); // IP
			pst.setStringAtName("ADDRESS", companyModel.getAddress()); // ADDRESS
			pst.setDoubleAtName("NET_WORTH", companyModel.getNetWorth()); // NET_WORTH
			pst.setStringAtName("OTHER_BANK", companyModel.getOtherBank()); // OTHER_BANK
			pst.setLongAtName("PROJECT_NO_OF_TPM", companyModel.getProjectNoOfTpm()); // PROJECT_NO_OF_TPM
			pst.setLongAtName("PROJECT_VOL_OF_TPM", companyModel.getProjectVolOfTpm()); // PROJECT_VOL_OF_TPM
			pst.setLongAtName("MAX_AMT_ST", companyModel.getMaxAmtSt()); // MAX_AMT_ST
			pst.setLongAtName("MAX_NO_TPD", companyModel.getMaxNoTpd()); // MAX_NO_TPD
			pst.setLongAtName("MAX_VOL_TPD", companyModel.getMaxVolTpd()); // MAX_VOL_TPD
			pst.setStringAtName("PAN_NO", companyModel.getPanNo()); // PAN_NO
			pst.setStringAtName("NID", companyModel.getNid()); // NID
			pst.setIntAtName("STATUS", Status.ACTIVE.ordinal()); // STATUS
			pst.setLongAtName("CREATED_BY", companyModel.getMerchantModel().getMerchantId()); // CREATED_BY
			pst.setStringAtName("MECHANDISING", companyModel.getMechandising()); // MECHANDISING
			pst.setStringAtName("CONTACTPERSON", companyModel.getContactPerson()); // CONTACTPERSON
			pst.setStringAtName("DESIGNATION", companyModel.getDesignation()); // DESIGNATION
			pst.setStringAtName("MOBILE", companyModel.getMobile()); // MOBILE
			pst.setStringAtName("EMAIL", companyModel.getEmail()); // EMAIL
			pst.setStringAtName("CODESEC", companyModel.getCodeSec()); // CODESEC

			System.out.println("Insert Company => " + sql);

			boolean result1 = pst.execute();
			if (!result1) {

				ResultSet rs = pst.getGeneratedKeys();
				rs.next();
				result = rs.getInt(1);
				System.out.println("Company GENERATED KEY => " + result);
				connection.commit();

			}

		} finally {

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for insertCompany");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for insertCompany" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("insertCompany -- END");
		}

		return result;
	}

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ID.
	 * 
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantById(long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantById -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.MERCHANT_ID=:MERCHANT_ID and m.STATUS !=:STATUS";

			System.out.println("Fetch Active MerchantById ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantById -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used to fetch the Active And Activated By Admin Merchant
	 * Details according to his/her ID.
	 * 
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveAndActivateMerchantById(long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveAndActivateMerchantById -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME," // 47
					+ "m.EBL_USER_NAME,"// 48
					+ "m.EBL_PASSWORD,"// 49
					+ "m.EBL_ID,"// 50
					+ "m.CITYBANK_MERCHANT_ID,"// 51
					+ "m.SEBL_USER_NAME,"// 52
					+ "m.SEBL_PASSWORD,"// 53
					+ "m.SEBL_ID"// 54
					+ " from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.MERCHANT_ID=:MERCHANT_ID";// m.STATUS =:STATUS1 OR m.STATUS =:STATUS2

			System.out.println("Fetch Active And Activate MerchantById ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
			// pst.setIntAtName("STATUS1", Status.ACTIVE.ordinal()); // STATUS1
			// pst.setIntAtName("STATUS2", Status.ACTIVATED.ordinal()); // STATUS2
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setEblUserName(rs.getString(48));
				merchantModel.setEblPassword(rs.getString(49));
				merchantModel.setEblId(rs.getString(50));
				merchantModel.setCityMerchantId(rs.getString(51));
				merchantModel.setSeblUserName(rs.getString(52));
				merchantModel.setSeblPassword(rs.getString(53));
				merchantModel.setSeblId(rs.getString(54));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveAndActivateMerchantById -- END");
		}

		return merchantModel;
	}

	/**
	 * This method is used for changing the old password to new one. First checking
	 * is done whether old password is valid or not, if valid then the new password
	 * is being updated to the password field.
	 * 
	 * @param merchantID
	 * @param oldPassword
	 * @param newPassword
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(long merchantID, String oldPassword, String newPassword) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- START");
		}

		int result = 0; // Checking failed for old password failed

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {
			String sql = "select password from MERCHANT_MASTER where MERCHANT_ID=:MERCHANT_ID and password=( select standard_hash(:oldPassword, 'MD5') md5 from DUAL)";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantID);
			pst.setStringAtName("oldPassword", oldPassword);
			System.out.println("CHANGE PASSWORD 1 => " + sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				OraclePreparedStatement pst1 = null;
				try {
					String sql1 = "update MERCHANT_MASTER set password =( select standard_hash(:newPassword, 'MD5') md5 from DUAL) where MERCHANT_ID =:MERCHANT_ID";
					pst1 = (OraclePreparedStatement) connection.prepareStatement(sql1);
					pst1.setStringAtName("newPassword", newPassword);
					pst1.setLongAtName("MERCHANT_ID", merchantID);

					System.out.println("CHANGE PASSWORD 2 => " + sql1);
					result = pst1.executeUpdate();
					if (result > 0)
						result = 1; // Password Changed Successfully
					else
						result = 2; // Password Not Changed

					connection.commit();

				} finally {

					try {

						if (pst1 != null)
							if (!pst1.isClosed())
								pst1.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("changePassword -- END");
		}

		return result;
	}

	/**
	 * This method is used for updation of old password if the user forgot password.
	 * 
	 * @param merchantID
	 * @param password
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(long merchantID, String password) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update MERCHANT_MASTER set password =( select standard_hash(:password, 'MD5') md5 from DUAL) where MERCHANT_ID =: MERCHANT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("password", password);
			pst.setLongAtName("MERCHANT_ID", merchantID);

			System.out.println("Forgot Password ==>> " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;
			connection.commit();

		} finally {

			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for forgotPassword");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for forgotPassword" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("forgotPassword -- END");
		}

		return result;
	}

	/**
	 * This method is used to skip the COMPANY DETAILS.
	 * 
	 * @param merchantID
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int skipCompany(long merchantID, int status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update MERCHANT_MASTER set CREATED_BY =:CREATED_BY where MERCHANT_ID =: MERCHANT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("CREATED_BY", status);
			pst.setLongAtName("MERCHANT_ID", merchantID);

			System.out.println("Skip Company Details ==>> " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;
			connection.commit();

		} finally {

			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for skipCompany");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for skipCompany" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("skipCompany -- END");
		}

		return result;
	}

	/**
	 * This method is used for Merchant Update Profile
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfile(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "update MERCHANT_MASTER set first_name =:first_name," + "last_name =:last_name,"
					+ "email_id =:email_id," + "mobile_no =:mobile_no," + "MODIFIED_DATE = localtimestamp(0),"
					+ "MODIFIED_BY =:MODIFIED_BY where MERCHANT_ID =:MERCHANT_ID ";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);

			pst.setStringAtName("first_name", merchantModel.getFirstName()); // first_name
			pst.setStringAtName("last_name", merchantModel.getLastName()); // last_name
			pst.setStringAtName("email_id", merchantModel.getEmailId()); // email_id
			pst.setStringAtName("mobile_no", merchantModel.getMobileNo()); // mobile_no
			pst.setLongAtName("MODIFIED_BY", merchantModel.getMerchantId()); // MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", merchantModel.getMerchantId()); // MERCHANT_ID

			System.out.println("Merchant Profile Update => " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;

			connection.commit();

		} finally {

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateProfile");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateProfile" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateProfile -- END");
		}

		return result;
	}

	/**
	 * This method is used for resending the phone code. Also the latest generated
	 * code is been updated into the database.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int resend(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant resend -- START");
		}

		int result = 0;
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "update MERCHANT_MASTER set PHONE_CODE =:PHONE_CODE where MERCHANT_ID =:MERCHANT_ID";
			pst = (OraclePreparedStatement) connection.prepareStatement(sql);

			pst.setIntAtName("PHONE_CODE", merchantModel.getPhoneCode()); // Phone Code
			pst.setLongAtName("MERCHANT_ID", merchantModel.getMerchantId()); // merchantId

			System.out.println("Merchant Phone Code Resend => " + sql);

			result = pst.executeUpdate();
			if (result > 0)
				result = 1;
			connection.commit();

		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant resend -- END");
		}

		return result;
	}

	/**
	 * This method is used for fetching all Active or Activated Merchant By Admin.
	 * 
	 * @param status
	 * @return List<MerchantModel>
	 * @throws Exception
	 */
	public List<MerchantModel> fetchAllActiveMerchants(Status status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchants -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<MerchantModel> merchantModels = new ArrayList<MerchantModel>();

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.STATUS !=:STATUS order by m.CREATED_DATE desc";

			System.out.println("Fetch All Merchants By Status ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setIntAtName("STATUS", status.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {

				MerchantModel merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);

				merchantModels.add(merchantModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchants -- END");
		}

		return merchantModels;
	}

	/**
	 * This method is used internally to generate the searching query for merchant.
	 * 
	 * @param dataTableModel
	 * @param isCount
	 * @return String
	 */
	private String getSearchQueryForActiveMerchant(DataTableModel dataTableModel, boolean isCount) {

		if (logger.isInfoEnabled()) {
			logger.info("getSearchQueryForActiveMerchant -- START");
		}

		String[] searchColumns = { "FIRST_NAME", "LAST_NAME", "EMAIL_ID", "MOBILE_NO" };

		String sql = "from MERCHANT_MASTER";
		if (searchColumns.length > 0) {
			sql += " where (";
			for (int i = 0; i < searchColumns.length; i++) {
				if (i != 0) {
					sql += " OR";
				}
				sql += " " + searchColumns[i] + " like :searchstr";
			}
			sql += ")";
		}

		if (!isCount) {
			sql = "select MERCHANT_ID,FIRST_NAME,LAST_NAME,EMAIL_ID,MOBILE_NO,STATUS,CREATED_DATE,CREATED_BY,MODIFIED_DATE,MODIFIED_BY "
					+ sql;
		} else {
			sql = "select COUNT(*)as total " + sql;
		}

		if (logger.isInfoEnabled()) {
			logger.info("getSearchQueryForActiveMerchant -- END");
		}

		return sql;
	}

	/**
	 * This method is used for fetch the Merchant List according to the searching
	 * criteria.
	 * 
	 * @param dataTableModel
	 * @param status
	 * @return List<MerchantModel>
	 * @throws Exception
	 */
	public List<MerchantModel> fetchActiveMerchantList(DataTableModel dataTableModel, Status status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantList -- START");
		}

		List<MerchantModel> merchantModels = new ArrayList<MerchantModel>();
		String typeName = "Merchant";
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = getSearchQueryForActiveMerchant(dataTableModel, false)
					+ " AND TYPE_ID=(select TYPE_ID from type_master where type_name = '" + typeName
					+ "') AND STATUS !=:STATUS order by " + dataTableModel.getOrderColumn() + " "
					+ dataTableModel.getOrderBy() + " OFFSET " + dataTableModel.getStart() + " ROWS FETCH NEXT "
					+ dataTableModel.getLength() + " ROWS ONLY";

			System.out.println("Fetch Merchant: " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr", "%" + dataTableModel.getSearchString() + "%");
			pst.setIntAtName("STATUS", status.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {

				MerchantModel merchantModel = new MerchantModel();

				merchantModel.setMerchantId(rs.getLong(1));
				merchantModel.setFirstName(rs.getString(2));
				merchantModel.setLastName(rs.getString(3));
				merchantModel.setEmailId(rs.getString(4));
				merchantModel.setMobileNo(rs.getString(5));

				if (rs.getInt(6) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(6) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(6) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(7));
				merchantModel.setCreatedBy(rs.getLong(8));
				merchantModel.setModifiedDate(rs.getString(9));
				merchantModel.setModifiedBy(rs.getLong(10));

				merchantModels.add(merchantModel);
			}
		}

		finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchActiveMerchantList -- END");
		}

		return merchantModels;

	}

	/**
	 * This method is used to get the total number of Merchants in the Database
	 * according to the AJAX Call.
	 * 
	 * @param dataTableModel
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int getActiveMerchantListCount(DataTableModel dataTableModel, Status status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("getActiveMerchantListCount -- START");
		}

		int result = 0;
		String typeName = "Merchant";
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = getSearchQueryForActiveMerchant(dataTableModel, true)
					+ " AND  TYPE_ID=(select TYPE_ID from type_master where type_name = '" + typeName
					+ "') AND STATUS !=:STATUS";

			System.out.println("Merchant Count: " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr", "%" + dataTableModel.getSearchString() + "%");
			pst.setIntAtName("STATUS", status.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			rs.next();
			result = rs.getInt(1);
		}

		finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("getActiveMerchantListCount -- END");
		}

		return result;

	}

	/**
	 * This method is used to fetch the List Of Merchants for Export.
	 * 
	 * @param userType
	 * @param dataTableModel
	 * @param status
	 * @return List<MerchantModel>
	 * @throws Exception
	 */
	public List<MerchantModel> fetchAllActiveMerchantListForExport(String userType, DataTableModel dataTableModel,
			Status status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchantListForExport -- START");
		}

		List<MerchantModel> merchantModels = new ArrayList<MerchantModel>();
		String typeName = "Merchant";
		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = getSearchQueryForActiveMerchant(dataTableModel, false)
					+ "  AND  TYPE_ID=(select TYPE_ID from type_master where type_name = '" + typeName
					+ "') AND STATUS !=:STATUS order by " + dataTableModel.getOrderColumn() + " "
					+ dataTableModel.getOrderBy();

			System.out.println("Fetch Merchant For Export: " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("searchstr", "%" + dataTableModel.getSearchString() + "%");
			pst.setIntAtName("STATUS", status.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {

				MerchantModel merchantModel = new MerchantModel();

				merchantModel.setMerchantId(rs.getLong(1));
				merchantModel.setFirstName(rs.getString(2));
				merchantModel.setLastName(rs.getString(3));
				merchantModel.setEmailId(rs.getString(4));
				merchantModel.setMobileNo(rs.getString(5));

				if (rs.getInt(6) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(6) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(6) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(7));
				merchantModel.setCreatedBy(rs.getLong(8));
				merchantModel.setModifiedDate(rs.getString(9));
				merchantModel.setModifiedBy(rs.getLong(10));

				merchantModels.add(merchantModel);
			}
		}

		finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("fetchAllActiveMerchantListForExport -- END");
		}

		return merchantModels;

	}

	/**
	 * This method is used for activating the merchant by Admin.
	 * 
	 * @param merchantId
	 * @param adminId
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int activateMerchant(long merchantId, long adminId, Status status) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "update MERCHANT_MASTER set STATUS =:STATUS," + "A_MODIFIED_DATE = localtimestamp(0),"
					+ "A_MODIFIED_BY =:A_MODIFIED_BY where MERCHANT_ID = :MERCHANT_ID ";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);

			pst.setIntAtName("STATUS", status.ordinal()); // STATUS
			pst.setLongAtName("A_MODIFIED_BY", adminId); // A_MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID

			System.out.println("Activate Merchant Profile => " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;

			connection.commit();

		} finally {

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for activateMerchant");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for activateMerchant" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("activateMerchant -- END");
		}

		return result;
	}

	/**
	 * This method is used for updating Merchant By Admin.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfileByAdmin(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateProfileByAdmin -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "update MERCHANT_MASTER set first_name =:first_name," + "last_name =:last_name,"
					+ "email_id =:email_id," + "mobile_no =:mobile_no," + "STATUS =:STATUS,"
					+ "A_MODIFIED_DATE = localtimestamp(0),"
					+ "A_MODIFIED_BY =:A_MODIFIED_BY where MERCHANT_ID =:MERCHANT_ID ";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);

			pst.setStringAtName("first_name", merchantModel.getFirstName()); // first_name
			pst.setStringAtName("last_name", merchantModel.getLastName()); // last_name
			pst.setStringAtName("email_id", merchantModel.getEmailId()); // email_id
			pst.setStringAtName("mobile_no", merchantModel.getMobileNo()); // mobile_no
			pst.setIntAtName("STATUS", merchantModel.getStatus().ordinal()); // STATUS
			pst.setLongAtName("A_MODIFIED_BY", merchantModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", merchantModel.getMerchantId()); // MERCHANT_ID

			System.out.println("Merchant Profile Update By Admin => " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;

			connection.commit();

		} finally {

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateProfileByAdmin");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateProfileByAdmin" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateProfileByAdmin -- END");
		}

		return result;
	}

	/**
	 * This method is used by the Merchant to fetch the Company Details w.r.t
	 * Merchant ID.
	 * 
	 * @param merchantId
	 * @return CompanyModel
	 * @throws Exception
	 */
	public CompanyModel fetchCompanyByMerchantId(long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchCompanyByMerchantId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		CompanyModel companyModel = null;

		try {
			String sql = "select c.COMPANY_ID," // 1
					+ "c.COMPANY_NAME," // 2
					+ "c.DBA," // 3
					+ "c.SERVICE," // 4
					+ "c.YEARS_IN_BUSINESS," // 5
					+ "c.PHONE," // 6
					+ "c.FAX," // 7
					+ "c.WEBSITE," // 8
					+ "c.IP," // 9
					+ "c.ADDRESS," // 10
					+ "c.NET_WORTH," // 11
					+ "c.OTHER_BANK," // 12
					+ "c.PROJECT_NO_OF_TPM," // 13
					+ "c.PROJECT_VOL_OF_TPM," // 14
					+ "c.MAX_AMT_ST," // 15
					+ "c.MAX_NO_TPD," // 16
					+ "c.MAX_VOL_TPD," // 17
					+ "c.PAN_NO," // 18
					+ "c.NID," // 19
					+ "c.STATUS," // 20
					+ "c.CREATED_DATE," // 21
					+ "c.CREATED_BY," // 22
					+ "c.MODIFIED_DATE," // 23
					+ "c.MODIFIED_BY," // 24
					+ "c.MECHANDISING," // 25
					+ "c.CONTACTPERSON," // 26
					+ "c.DESIGNATION," // 27
					+ "c.MOBILE," // 28
					+ "c.EMAIL," // 29
					+ "c.codeSec " // 29
					+ "from COMPANY_MASTER c " + "where c.MERCHANT_ID=:MERCHANT_ID and c.STATUS !=:STATUS";

			System.out.println("Fetch Active Company By MerchantId ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // MERCHANT_ID
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				companyModel = new CompanyModel();

				companyModel.setCompanyId(rs.getLong(1));
				companyModel.setCompanyName(rs.getString(2));
				companyModel.setDba(rs.getString(3));
				companyModel.setService(rs.getString(4));
				companyModel.setYearsInBusiness(rs.getInt(5));
				companyModel.setPhone(rs.getString(6));
				companyModel.setFax(rs.getString(7));
				companyModel.setWebsite(rs.getString(8));
				companyModel.setIp(rs.getString(9));
				companyModel.setAddress(rs.getString(10));
				companyModel.setNetWorth(rs.getDouble(11));
				companyModel.setOtherBank(rs.getString(12));
				companyModel.setProjectNoOfTpm(rs.getLong(13));
				companyModel.setProjectVolOfTpm(rs.getLong(14));
				companyModel.setMaxAmtSt(rs.getLong(15));
				companyModel.setMaxNoTpd(rs.getLong(16));
				companyModel.setMaxVolTpd(rs.getLong(17));
				companyModel.setPanNo(rs.getString(18));
				companyModel.setNid(rs.getString(19));

				if (rs.getInt(20) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(20) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(20) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(21));
				companyModel.setCreatedBy(rs.getLong(22));
				companyModel.setModifiedDate(rs.getString(23));
				companyModel.setModifiedBy(rs.getLong(24));
				companyModel.setMechandising(rs.getString(25));
				companyModel.setContactPerson(rs.getString(26));
				companyModel.setDesignation(rs.getString(27));
				companyModel.setMobile(rs.getString(28));
				companyModel.setEmail(rs.getString(29));
				companyModel.setCodeSec(rs.getString(30));

			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchCompanyByMerchantId -- END");
		}

		return companyModel;
	}

	/**
	 * This method is used to update the Company For Merchant.
	 * 
	 * @param companyModel
	 * @return int
	 * @throws Exception
	 */
	public int updateCompany(CompanyModel companyModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("updateCompany -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		try {
			String sql = "update COMPANY_MASTER set COMPANY_NAME=:COMPANY_NAME," + "DBA=:DBA," + "SERVICE=:SERVICE,"
					+ "YEARS_IN_BUSINESS=:YEARS_IN_BUSINESS," + "PHONE=:PHONE," + "FAX=:FAX," + "WEBSITE=:WEBSITE,"
					+ "IP=:IP," + "ADDRESS=:ADDRESS," + "NET_WORTH=:NET_WORTH," + "OTHER_BANK=:OTHER_BANK,"
					+ "PROJECT_NO_OF_TPM=:PROJECT_NO_OF_TPM," + "PROJECT_VOL_OF_TPM=:PROJECT_VOL_OF_TPM,"
					+ "MAX_AMT_ST=:MAX_AMT_ST," + "MAX_NO_TPD=:MAX_NO_TPD," + "MAX_VOL_TPD=:MAX_VOL_TPD,"
					+ "PAN_NO=:PAN_NO," + "NID=:NID," + "MODIFIED_BY=:MODIFIED_BY," + "MODIFIED_DATE=localtimestamp(0),"
					+ "MECHANDISING=:MECHANDISING," + "CONTACTPERSON=:CONTACTPERSON," + "DESIGNATION=:DESIGNATION,"
					+ "MOBILE=:MOBILE," + "EMAIL=:EMAIL," + "CODESEC=:CODESEC where COMPANY_ID =:COMPANY_ID";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("COMPANY_NAME", companyModel.getCompanyName()); // COMPANY_NAME
			pst.setStringAtName("DBA", companyModel.getDba()); // DBA
			pst.setStringAtName("SERVICE", companyModel.getService()); // SERVICE
			pst.setIntAtName("YEARS_IN_BUSINESS", companyModel.getYearsInBusiness()); // YEARS_IN_BUSINESS
			pst.setStringAtName("PHONE", companyModel.getPhone()); // PHONE
			pst.setStringAtName("FAX", companyModel.getFax()); // FAX
			pst.setStringAtName("WEBSITE", companyModel.getWebsite()); // WEBSITE
			pst.setStringAtName("IP", companyModel.getIp()); // IP
			pst.setStringAtName("ADDRESS", companyModel.getAddress()); // ADDRESS
			pst.setDoubleAtName("NET_WORTH", companyModel.getNetWorth()); // NET_WORTH
			pst.setStringAtName("OTHER_BANK", companyModel.getOtherBank()); // OTHER_BANK
			pst.setLongAtName("PROJECT_NO_OF_TPM", companyModel.getProjectNoOfTpm()); // PROJECT_NO_OF_TPM
			pst.setLongAtName("PROJECT_VOL_OF_TPM", companyModel.getProjectVolOfTpm()); // PROJECT_VOL_OF_TPM
			pst.setLongAtName("MAX_AMT_ST", companyModel.getMaxAmtSt()); // MAX_AMT_ST
			pst.setLongAtName("MAX_NO_TPD", companyModel.getMaxNoTpd()); // MAX_NO_TPD
			pst.setLongAtName("MAX_VOL_TPD", companyModel.getMaxVolTpd()); // MAX_VOL_TPD
			pst.setStringAtName("PAN_NO", companyModel.getPanNo()); // PAN_NO
			pst.setStringAtName("NID", companyModel.getNid()); // NID
			pst.setLongAtName("MODIFIED_BY", companyModel.getModifiedBy()); // CREATED_BY
			pst.setStringAtName("MECHANDISING", companyModel.getMechandising()); // MECHANDISING
			pst.setStringAtName("CONTACTPERSON", companyModel.getContactPerson()); // CONTACTPERSON
			pst.setStringAtName("DESIGNATION", companyModel.getDesignation()); // DESIGNATION
			pst.setStringAtName("MOBILE", companyModel.getMobile()); // MOBILE
			pst.setStringAtName("EMAIL", companyModel.getEmail()); // EMAIL
			pst.setStringAtName("CODESEC", companyModel.getCodeSec()); // CODESEC
			pst.setLongAtName("COMPANY_ID", companyModel.getCompanyId());

			System.out.println("Update Company ==>> " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;
			connection.commit();

		} finally {

			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for updateCompany");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug("Connection not closed for updateCompany" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("updateCompany -- END");
		}

		return result;
	}

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ACCESSKEY.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantByAccessKey(String accessKey) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantByAccessKey -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.ACCESS_KEY=:ACCESS_KEY and m.STATUS !=:STATUS";
			// TODO query optimization
			System.out.println("Fetch Active MerchantById ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ACCESS_KEY", accessKey); // ACCESS_KEY
			pst.setIntAtName("STATUS", Status.DELETE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantByAccessKey -- END");
		}

		return merchantModel;
	}

	@Override
	public int createUserIdAndPassword(MerchantModel merchantModel) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("createUserIdAndPassword -- START");
		}

		int result = 0;

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;

		try {

			String sql = "update MERCHANT_MASTER set ebl_user_name =:ebl_user_name," + "ebl_password =:ebl_password,"
					+ "ebl_id =:ebl_id, " + "citybank_merchant_id =:citybank_merchant_id, "
					+ "sebl_user_name =:sebl_user_name," + "sebl_password =:sebl_password," + "sebl_id =:sebl_id, "
					+ "A_MODIFIED_DATE = localtimestamp(0),"
					+ "A_MODIFIED_BY =:A_MODIFIED_BY where MERCHANT_ID =:MERCHANT_ID ";

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);

			pst.setStringAtName("ebl_user_name", merchantModel.getEblUserName()); // ebl_user_name
			pst.setStringAtName("ebl_password", merchantModel.getEblPassword()); // ebl_password
			pst.setStringAtName("ebl_id", merchantModel.getEblId()); // ebl_id
			pst.setStringAtName("citybank_merchant_id", merchantModel.getCityMerchantId()); // city_merchant_id
			pst.setStringAtName("sebl_user_name", merchantModel.getSeblUserName()); // sebl_user_name
			pst.setStringAtName("sebl_password", merchantModel.getSeblPassword()); // sebl_password
			pst.setStringAtName("sebl_id", merchantModel.getSeblId()); // sebl_id
			pst.setLongAtName("A_MODIFIED_BY", merchantModel.getaModifiedBy()); // MODIFIED_BY
			pst.setLongAtName("MERCHANT_ID", merchantModel.getMerchantId()); // MERCHANT_ID

			System.out.println("Merchant create UserId And Password By Admin => " + sql);

			result = pst.executeUpdate();

			if (result > 0)
				result = 1;

			connection.commit();

		} finally {

			try {
				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try { // Closing Connection Object
				if (connection != null) {

					if (!connection.isClosed())
						connection.close();

					if (logger.isDebugEnabled()) {
						logger.debug("Connection Closed for Merchant create UserId And Password By Admin");
					}
				}
			} catch (Exception e) {

				if (logger.isDebugEnabled()) {
					logger.debug(
							"Connection not closed for Merchant create UserId And Password By Admin" + e.getMessage());
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("createUserIdAndPassword -- END");
		}

		return result;
	}

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ACCESSKEY.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchMerchantByAccessKey(String accessKey) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchMerchantByAccessKey -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		MerchantModel merchantModel = null;

		try {
			String sql = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID "
					+ "where m.ACCESS_KEY=:ACCESS_KEY and m.STATUS =:STATUS";

			String sql1 = "select m.MERCHANT_ID," // 1
					+ "m.TYPE_ID," // 2
					+ "m.FIRST_NAME," // 3
					+ "m.LAST_NAME," // 4
					+ "m.EMAIL_ID," // 5
					+ "m.MOBILE_NO," // 6
					+ "m.PASSWORD," // 7
					+ "m.UNIQUE_KEY," // 8
					+ "m.ACCESS_KEY," // 9
					+ "m.SECRET_KEY," // 10
					+ "m.PHONE_CODE," // 11
					+ "m.PHONE_VERIFIED," // 12
					+ "m.TRANSACTION_AMOUNT," // 13
					+ "m.LOYALTY_POINT," // 14
					+ "m.STATUS," // 15
					+ "m.CREATED_DATE," // 16
					+ "m.CREATED_BY," // 17
					+ "m.MODIFIED_DATE," // 18
					+ "m.MODIFIED_BY," // 19
					+ "m.A_MODIFIED_DATE," // 20
					+ "m.A_MODIFIED_BY," // 21
					+ "c.COMPANY_ID," // 22
					+ "c.MERCHANT_ID," // 23
					+ "c.COMPANY_NAME," // 24
					+ "c.DBA," // 25
					+ "c.SERVICE," // 26
					+ "c.YEARS_IN_BUSINESS," // 27
					+ "c.PHONE," // 28
					+ "c.FAX," // 29
					+ "c.WEBSITE," // 30
					+ "c.IP," // 31
					+ "c.ADDRESS," // 32
					+ "c.NET_WORTH," // 33
					+ "c.OTHER_BANK," // 34
					+ "c.PROJECT_NO_OF_TPM," // 35
					+ "c.PROJECT_VOL_OF_TPM," // 36
					+ "c.MAX_AMT_ST," // 37
					+ "c.MAX_NO_TPD," // 38
					+ "c.MAX_VOL_TPD," // 39
					+ "c.PAN_NO," // 40
					+ "c.NID," // 41
					+ "c.STATUS," // 42
					+ "c.CREATED_DATE," // 43
					+ "c.CREATED_BY," // 44
					+ "c.MODIFIED_DATE," // 45
					+ "c.MODIFIED_BY," // 46
					+ "t.TYPE_NAME " // 47
					+ "from MERCHANT_MASTER m left outer join TYPE_MASTER t on m.TYPE_ID = t.TYPE_ID "
					+ "left outer join COMPANY_MASTER c on m.MERCHANT_ID = c.MERCHANT_ID " + "where m.ACCESS_KEY='"
					+ accessKey + "' and m.STATUS ='" + Status.ACTIVE.ordinal() + "'";

			System.out.println("Fetch Active MerchantById ==>> " + sql1);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setStringAtName("ACCESS_KEY", accessKey); // ACCESS_KEY
			pst.setIntAtName("STATUS", Status.ACTIVE.ordinal()); // status
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				merchantModel = new MerchantModel();
				TypeModel typeModel = new TypeModel();
				CompanyModel companyModel = new CompanyModel();

				merchantModel.setMerchantId(rs.getLong(1));
				typeModel.setTypeId(rs.getLong(2));
				merchantModel.setFirstName(rs.getString(3));
				merchantModel.setLastName(rs.getString(4));
				merchantModel.setEmailId(rs.getString(5));
				merchantModel.setMobileNo(rs.getString(6));
				merchantModel.setPassword(rs.getString(7));
				merchantModel.setUniqueKey(rs.getString(8));
				merchantModel.setAccessKey(rs.getString(9));
				merchantModel.setSecretKey(rs.getString(10));
				merchantModel.setPhoneCode(rs.getInt(11));
				merchantModel.setPhoneVerified(rs.getString(12));
				merchantModel.setTransactionAmount(rs.getDouble(13));
				merchantModel.setLoyaltyPoint(rs.getDouble(14));

				if (rs.getInt(15) == 0)
					merchantModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(15) == 1)
					merchantModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(15) == 2)
					merchantModel.setStatus(Status.DELETE);
				else
					merchantModel.setStatus(Status.ACTIVATED);

				merchantModel.setCreatedDate(rs.getString(16));
				merchantModel.setCreatedBy(rs.getLong(17));
				merchantModel.setModifiedDate(rs.getString(18));
				merchantModel.setModifiedBy(rs.getLong(19));
				merchantModel.setaModifiedDate(rs.getString(20));
				merchantModel.setaModifiedBy(rs.getLong(21));

				companyModel.setCompanyId(rs.getLong(22));
				companyModel.setCompanyName(rs.getString(24));
				companyModel.setDba(rs.getString(25));
				companyModel.setService(rs.getString(26));
				companyModel.setYearsInBusiness(rs.getInt(27));
				companyModel.setPhone(rs.getString(28));
				companyModel.setFax(rs.getString(29));
				companyModel.setWebsite(rs.getString(30));
				companyModel.setIp(rs.getString(31));
				companyModel.setAddress(rs.getString(32));
				companyModel.setNetWorth(rs.getDouble(33));
				companyModel.setOtherBank(rs.getString(34));
				companyModel.setProjectNoOfTpm(rs.getLong(35));
				companyModel.setProjectVolOfTpm(rs.getLong(36));
				companyModel.setMaxAmtSt(rs.getLong(37));
				companyModel.setMaxNoTpd(rs.getLong(38));
				companyModel.setMaxVolTpd(rs.getLong(39));
				companyModel.setPanNo(rs.getString(40));
				companyModel.setNid(rs.getString(41));

				if (rs.getInt(42) == 0)
					companyModel.setStatus(Status.ACTIVE);
				else if (rs.getInt(42) == 1)
					companyModel.setStatus(Status.INACTIVE);
				else if (rs.getInt(42) == 2)
					companyModel.setStatus(Status.DELETE);

				companyModel.setCreatedDate(rs.getString(43));
				companyModel.setCreatedBy(rs.getLong(44));
				companyModel.setModifiedDate(rs.getString(45));
				companyModel.setModifiedBy(rs.getLong(46));

				typeModel.setTypeName(rs.getString(47));

				merchantModel.setCompanyModel(companyModel);
				merchantModel.setTypeModel(typeModel);
			}
		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Merchant fetchActiveMerchantByAccessKey -- END");
		}

		return merchantModel;
	}

	@Override
	public List<CardTypePercentageModel> getAllCardPercentageByMerchantId(Long merchantId) throws Exception {

		if (logger.isInfoEnabled()) {
			logger.info("getAllCardPercentageByMerchantId -- START");
		}

		Connection connection = oracleConnection.Connect();
		OraclePreparedStatement pst = null;
		List<CardTypePercentageModel> cardTypePercentageModels = new ArrayList<CardTypePercentageModel>();

		try {
			String sql = "select ctm.CARD_TYPE,mcp.PERCENTAGE,mcp.FIXED,mcp.MERCHANT_ID from CARD_TYPE_MASTER ctm,MERCHANT_CARD_PERCENTAGE mcp "
					+ " WHERE ctm.CARD_TYPE_ID = mcp.CARD_TYPE_ID AND mcp.MERCHANT_ID =:MERCHANT_ID";

			System.out.println("Fetch All Transaction By Merchant Id ==>> " + sql);

			pst = (OraclePreparedStatement) connection.prepareStatement(sql);
			pst.setLongAtName("MERCHANT_ID", merchantId); // merchantId
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {

				CardTypePercentageModel cardTypePercentageModel = new CardTypePercentageModel();

				cardTypePercentageModel.setCardType(rs.getString(1));
				cardTypePercentageModel.setCardPercentage(rs.getDouble(2));
				cardTypePercentageModel.setFixed(rs.getDouble(3));
				cardTypePercentageModel.setMerchantId(rs.getLong(4));
				cardTypePercentageModels.add(cardTypePercentageModel);
			}

		} finally {
			try {

				if (pst != null)
					if (!pst.isClosed())
						pst.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (connection != null)
					if (!connection.isClosed())
						connection.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("getAllCardPercentageByMerchantId -- END");
		}

		return cardTypePercentageModels;
	}
}
