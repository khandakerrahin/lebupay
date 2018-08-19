/**
 * @formatter:off
 *
 */
package com.lebupay.dao;

import java.util.List;

import com.lebupay.model.BannerModel;
import com.lebupay.model.DataTableModel;

/**
 * This is BannerDAO Interface used to declare methods to operate database operation for Banners. 
 * @author Java-Team
 *
 */
public interface BannerDAO {

	
	/**
	 * This method is used for inserting BANNER.
	 * @param bannerModel
	 * @return long
	 * @throws Exception
	 */
	public long insertBanner(BannerModel bannerModel) throws Exception ;
	
	/**
	 * This method is used for updating BANNER.
	 * @param bannerModel
	 * @return int
	 * @throws Exception
	 */
	public int updateBanner(BannerModel bannerModel) throws Exception ;
	
	/**
	 * This method is used for fetching all active Banners.
	 * @return List
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllActiveBanners()  throws Exception ;
	
	
	/**
	 * This method is used to fetch all the banner details according to the searching citeria for Customer, Merchant and Admin.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllBannersForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	
	/**
	 * This method is used to count the total number of banners in Database during AJAX Call.
	 * @param dataTableModel
	 * @return int
	 * @throws Exception
	 */
	public int getBannersCountForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used during the export of the banner list.
	 * @param dataTableModel
	 * @return List
	 * @throws Exception
	 */
	public List<BannerModel> fetchAllBannersForExportForAdmin(DataTableModel dataTableModel) throws Exception ;
	
	/**
	 * This method is used for fetching active Banners By Id.
	 * @param bannerId
	 * @return BannerModel
	 * @throws Exception
	 */
	public BannerModel fetchActiveBannerByID(long bannerId)  throws Exception ;

}
