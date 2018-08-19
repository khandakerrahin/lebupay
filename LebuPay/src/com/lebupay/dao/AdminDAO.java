/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.AdminModel;
import com.lebupay.model.CardTypeModel;
import com.lebupay.model.MerchantCardPercentageModel;

/**
 * This Interface is used to declare all Admin related database operations.
 * @author Java-Team
 *
 */
public interface AdminDAO {

	
	/**
	 * This method is used for login purpose of the Admin.
	 * @param userName
	 * @param password
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel login(String userName, String password) throws Exception;
	
	/**
	 * This method is used for checking whether the email is valid or not.
	 * @param emailId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel emailCheckCreate(String emailId) throws Exception;
	
	/**
	 * This method is used for checking whether the Mobile Number is valid or not.
	 * @param mobileNo
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel mobileCheckCreate(String mobileNo) throws Exception;
	
	/**
	 * This method is used for checking whether the email is valid or not during update.
	 * @param emailId
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel emailCheckUpdate(String emailId, long adminId) throws Exception ;
	
	/**
	 * This method is used for checking whether the Mobile Number is valid or not during update.
	 * @param mobileNo
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel mobileCheckUpdate(String mobileNo, long adminId) throws Exception;
	
	/**
	 * This method is used for forgot password.
	 * @param adminId
	 * @param password
	 * @return int
	 * @throws Exception
	 */
	public int forgotPassword(long adminId, String password) throws Exception ;
	
	/**
	 * This method is for Admin Profile Update.
	 * @param adminModel	
	 * @return long
	 * @throws Exception
	 */
	public long update(AdminModel adminModel) throws Exception ;
	
	/**
	 * This method is used to change Admin's password.
	 * @param adminId
	 * @param oldPassword
	 * @param newPassword
	 * @return int
	 * @throws Exception
	 */
	public int changePassword(long adminId, String oldPassword, String newPassword) throws Exception ;
	
	/**
	 * This method is used to fetch the Admin w.r.t AdminId.
	 * @param adminId
	 * @return AdminModel
	 * @throws Exception
	 */
	public AdminModel fetchAdminById(long adminId) throws Exception ;
	
	/**
	 * This method is used to fetch CardType List
	 * @return List
	 * @throws Exception
	 */
	public List<CardTypeModel> fetchCardTypeList() throws Exception;
	
	/**
	 * This method is used to get List of Merchant Card Percentage by Merchant.
	 * @param merchantId
	 * @return List
	 * @throws Exception
	 */
	public List<MerchantCardPercentageModel> fetchCardPercentageByMerchantId(String merchantId) throws Exception;
	
	/**
	 * This method is used to insert card Percentage as per Merchant.
	 * @param merchantCardPercentageModel
	 * @return long
	 * @throws Exception
	 */
	public long addCardPercenatge(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception ;
	
	/**
	 * This method is used to update card Percentage as per Merchant.
	 * @param merchantCardPercentageModel
	 * @return long
	 * @throws Exception
	 */
	public long updateCardPercenatge(MerchantCardPercentageModel merchantCardPercentageModel) throws Exception ;

}
