/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lebupay.model.ParameterModel;

/**
 * This interface is used to declare methods for Parameter Database Operations.
 * @author Java Team
 *
 */
@Repository
public interface ParameterDAO { 

	/**
	 * This method is used to insert the parameters w.r.t Merchant Id.
	 * @param parameterModel
	 * @return long
	 * @throws Exception
	 */
	public long insertParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This method is used for deleting the parameter for Merchant.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int deleteParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This method is used to update the parameters for the Merchant.
	 * @param parameterModel
	 * @return int
	 * @throws Exception
	 */
	public int updateParameter(ParameterModel parameterModel) throws Exception ;
	
	/**
	 * This method is used internally for fetching the Parameter List as per Merchant during merchant login.
	 * @param merchantId
	 * @return List<ParameterModel>
	 * @throws Exception
	 */
	public List<ParameterModel> fetchParametersById(long merchantId) throws Exception;
	
	/**
	 * This method is used for fetching Parameter w.r.t ID.
	 * @param parameterId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(long parameterId) throws Exception;
	
	/**
	 * This method is used to fetch the Parameters w.r.t Parameter Name.
	 * @param parameterName
	 * @param merchantId
	 * @return ParameterModel
	 * @throws Exception
	 */
	public ParameterModel fetchParameterById(String parameterName, long merchantId) throws Exception;

}
