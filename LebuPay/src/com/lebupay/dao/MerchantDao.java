/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.CardTypePercentageModel;
import com.lebupay.model.CompanyModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.MerchantModel;
import com.lebupay.model.Status;

/**
 * This interface is used to declare methods for Merchant Database Operations.
 * 
 * @author Java Team
 *
 */
public interface MerchantDao {

	/**
	 * This method is used to check whether the Mobile Number is already present in
	 * Database or not during sign up.
	 * 
	 * @param mobileNo
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel createPhoneNoCheck(String mobileNo) throws Exception;

	/**
	 * This method is used for checking whether the Mobile No is associated with
	 * other Merchant or not during update operation.
	 * 
	 * @param mobileNo
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel updatePhoneNoCheck(String mobileNo, Long merchantId) throws Exception;

	/**
	 * This method is used to check whether the Email Id is already present in
	 * Database or not during sign up.
	 * 
	 * @param emailId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel createEmailCheck(String emailId) throws Exception;

	/**
	 * This method is used for checking whether the Email Id is associated with
	 * other Merchant or not during update operation.
	 * 
	 * @param emailId
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel updateEmailCheck(String emailId, Long merchantId) throws Exception;

	/**
	 * This method is used for merchant signup. Insertion done into Merchant Master,
	 * Parameter Master & CheckOut Master Tables.
	 * 
	 * @param merchantModel
	 * @return long
	 * @throws Exception
	 */
	public long merchantSignUp(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used for phone verification. If verification is done then the
	 * corresponding phone_verified field is updated
	 * 
	 * @param merchantID
	 * @param phoneCode
	 * @return int
	 * @throws Exception
	 */
	public int phoneVerification(long merchantID, String phoneCode) throws Exception;

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
	public MerchantModel login(String userName, String password) throws Exception;

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
	public MerchantModel userCheck(String userName) throws Exception;

	/**
	 * This method is used for insert the company details for the Merchant first
	 * time.
	 * 
	 * @param companyModel
	 * @return long
	 * @throws Exception
	 */
	public long insertCompany(CompanyModel companyModel) throws Exception;

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ID.
	 * 
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantById(long merchantId) throws Exception;

	/**
	 * This method is used to fetch the Active And Activated By Admin Merchant
	 * Details according to his/her ID.
	 * 
	 * @param merchantId
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveAndActivateMerchantById(long merchantId) throws Exception;

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
	public int changePassword(long merchantID, String oldPassword, String newPassword) throws Exception;

	/**
	 * This method is used for updation of old password if the user forgot password.
	 * 
	 * @param merchantID
	 * @param password
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(long merchantID, String password) throws Exception;

	/**
	 * This method is used to skip the COMPANY DETAILS.
	 * 
	 * @param merchantID
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int skipCompany(long merchantID, int status) throws Exception;

	/**
	 * This method is used for Merchant Update Profile
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfile(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used for re-sending the phone code. Also the latest generated
	 * code is been updated into the database.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int resend(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used for fetching all Active or Activated Merchant By Admin.
	 * 
	 * @param status
	 * @return List<MerchantModel>
	 * @throws Exception
	 */
	public List<MerchantModel> fetchAllActiveMerchants(Status status) throws Exception;

	/**
	 * This method is used for fetch the Merchant List according to the searching
	 * criteria.
	 * 
	 * @param dataTableModel
	 * @param status
	 * @return List<MerchantModel>
	 * @throws Exception
	 */
	public List<MerchantModel> fetchActiveMerchantList(DataTableModel dataTableModel, Status status) throws Exception;

	/**
	 * This method is used to get the total number of Merchants in the Database
	 * according to the AJAX Call.
	 * 
	 * @param dataTableModel
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int getActiveMerchantListCount(DataTableModel dataTableModel, Status status) throws Exception;

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
			Status status) throws Exception;

	/**
	 * This method is used for activating the merchant by Admin.
	 * 
	 * @param merchantId
	 * @param adminId
	 * @param status
	 * @return int
	 * @throws Exception
	 */
	public int activateMerchant(long merchantId, long adminId, Status status) throws Exception;

	/**
	 * This method is used for updating Merchant By Admin.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int updateProfileByAdmin(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used by the Merchant to fetch the Company Details w.r.t
	 * Merchant ID.
	 * 
	 * @param merchantId
	 * @return CompanyModel
	 * @throws Exception
	 */
	public CompanyModel fetchCompanyByMerchantId(long merchantId) throws Exception;

	/**
	 * This method is used to update the Company For Merchant.
	 * 
	 * @param companyModel
	 * @return int
	 * @throws Exception
	 */
	public int updateCompany(CompanyModel companyModel) throws Exception;

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ACCESSKEY.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchActiveMerchantByAccessKey(String accessKey) throws Exception;

	/**
	 * This method is used to fetch the Active But Not Activated By Admin Merchant
	 * Details according to his/her ACCESSKEY.
	 * 
	 * @param accessKey
	 * @return MerchantModel
	 * @throws Exception
	 */
	public MerchantModel fetchMerchantByAccessKey(String accessKey) throws Exception;

	/**
	 * This method is used to save User Id and Password.
	 * 
	 * @param merchantModel
	 * @return int
	 * @throws Exception
	 */
	public int createUserIdAndPassword(MerchantModel merchantModel) throws Exception;

	/**
	 * This method is used to fetch Card Percentage as per Merchant Id.
	 * 
	 * @param merchantId
	 * @return List<CardTypePercentageModel>
	 * @throws Exception
	 */
	public List<CardTypePercentageModel> getAllCardPercentageByMerchantId(Long merchantId) throws Exception;
}
