/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lebupay.exception.ImageFormatMismatch;
import com.lebupay.model.BannerModel;
import com.lebupay.model.DataTableModel;

/**
 * This is BannerService Interface is used to perform operations on Banner Module.
 * @author Java Team
 *
 */
public interface BannerService {

	/**
	 * This method use to save Banner.
	 * @param bannerModel
	 * @param request
	 * @return long
	 * @throws Exception
	 */
	public long saveBanner(BannerModel bannerModel,HttpServletRequest request) throws Exception ;
	
	
	/**
	 * This method use to get Banner w.r.t Banner Id.
	 * @param bannerId
	 * @return BannerModel
	 * @throws Exception
	 */
	public BannerModel fetchBannerById(long bannerId) throws Exception ;
	
	
	/**
	 * This method use to get all Banner details.
	 * @return List<BannerModel>
	 * @throws Exception
	 */
	public List<BannerModel> fetchBanner() throws Exception ;
	
	/**
	 * This method is used for Fetching the Banners for the Datatable.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchBanner(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used to generate the Excel for Banner Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportBannersForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception ;
	
	/**
	 * This method is use to update banner details
	 * @param bannerModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int updateBanner(BannerModel bannerModel,HttpServletRequest request) throws Exception ;
	
	
	/**
	 * This method use to delete Banner details
	 * @param bannerId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteBanner(long bannerId,long adminId) throws Exception ;
	
	/**
	 * This method is used to ImageFormat Validation.
	 * @param bannerModel
	 * @throws ImageFormatMismatch
	 * @throws IOException
	 */
	public void imageFormatValidation(BannerModel bannerModel) throws ImageFormatMismatch, IOException ;
}
