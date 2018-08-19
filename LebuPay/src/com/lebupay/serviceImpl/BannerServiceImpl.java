/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.lebupay.common.MessageUtil;
import com.lebupay.dao.BannerDAO;
import com.lebupay.exception.ImageFormatMismatch;
import com.lebupay.model.BannerModel;
import com.lebupay.model.DataTableModel;
import com.lebupay.model.Status;
import com.lebupay.service.BannerService;

/**
 * This is BannerServiceImpl Class implements BannerService interface is used to perform operation on Banner Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class BannerServiceImpl implements BannerService {

	private static Logger logger = Logger.getLogger(BannerServiceImpl.class);
	
	private static final Tika TIKA = new Tika();
	
	@Autowired
	private BannerDAO bannerDao;
	
	@Autowired
	private MessageUtil messageUtil;
	
	/**
	 * This method use to save Banner.
	 * @param bannerModel
	 * @param request
	 * @return long
	 * @throws Exception
	 */
	public long saveBanner(BannerModel bannerModel,HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("saveBanner -- START");
		}
		
		long result = 0;
		
		if(!(bannerModel.getFile() == null || bannerModel.getFile().isEmpty())){
			
			// to do file upload
			
			MultipartFile multipartFile = bannerModel.getFile();
			this.imageFormatValidation(bannerModel);
			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");
			
			String dirStr = appPath + "resources" + File.separator + "banner";
			File dir = new File(dirStr);
			if(!dir.exists()){
				dir.mkdir();
			}

			// construct the complete absolute path of the file
			String fileName = bannerModel.getCreatedBy()+"_"+new Date().getTime()+"_" + multipartFile.getOriginalFilename();
			String fullPath = dirStr + File.separator + fileName;
			
			File file = new File(fullPath);

			bannerModel.setImageName(fileName);
			result = bannerDao.insertBanner(bannerModel);
			
			if(result > 1){
				multipartFile.transferTo(file);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("saveBanner -- END");
		}
		
		return result;
	}
	
	
	/**
	 * This method use to get Banner w.r.t Banner Id.
	 * @param bannerId
	 * @return BannerModel
	 * @throws Exception
	 */
	public BannerModel fetchBannerById(long bannerId) throws Exception {
			
			if (logger.isInfoEnabled()) {
				logger.info("fetchBannerById -- START");
			}
			
			BannerModel bannerModel = bannerDao.fetchActiveBannerByID(bannerId);
					
			if (logger.isInfoEnabled()) {
				logger.info("fetchBannerById -- END");
			}
				
			return bannerModel;
	}
	
	
	/**
	 * This method use to get all Banner details.
	 * @return List<BannerModel>
	 * @throws Exception
	 */
	public List<BannerModel> fetchBanner() throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchBannerModel -- START");
		}
		
		List<BannerModel> bannerModels = bannerDao.fetchAllActiveBanners();
				
		if (logger.isInfoEnabled()) {
			logger.info("fetchBannerModel -- END");
	    }
		
		return bannerModels;
	}
	
	/**
	 * This method is used for Fetching the Banners for the Datatable.
	 * @param dataTableModel
	 * @throws Exception
	 */
	public void fetchBanner(DataTableModel dataTableModel) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchBanners -- START");
		}
		
		List<BannerModel> bannerModels = bannerDao.fetchAllBannersForAdmin(dataTableModel);
		dataTableModel.setData(bannerModels);
		dataTableModel.setRecordsTotal(bannerDao.getBannersCountForAdmin(dataTableModel));
		
		if (logger.isInfoEnabled()) {
			logger.info("fetchBanners -- END");
		}
	}
	
	/**
	 * This method is used to generate the Excel for Banner Datatable.
	 * @param dataTableModel
	 * @param noOfColumns
	 * @return Object[][]
	 * @throws Exception
	 */
	public Object[][] exportBannersForExcel(DataTableModel dataTableModel, int noOfColumns) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("exportBannersForExcel -- START");
		}
		List<BannerModel> bannerModels = bannerDao.fetchAllBannersForExportForAdmin(dataTableModel);
		Object[][] objects = new Object[bannerModels.size()+1][noOfColumns];
		
		
		String[] columns = dataTableModel.getColumnNames().split(",");
		objects[0] = columns;
		
		for (int i = 0; i < objects.length-1;) {
			Object[] objects2 = new Object[noOfColumns];
			int j=0;
			if(dataTableModel.getAutoIncrementColumnIndex()!=null){
				objects2[j++] = i+1;
			}
			/*objects2[j++] = bannerModels.get(i).getBannerId();*/
			objects2[j++] = bannerModels.get(i).getImageName();
			objects2[j++] = bannerModels.get(i).getTypeModel().getTypeName();
			objects2[j] = bannerModels.get(i).getStatus().name();
			
			objects[++i] = objects2;
		}
		
				
		if (logger.isInfoEnabled()) {
			logger.info("exportBannersForExcel -- END");
		}
		return objects;
	}
	
	
	
	/**
	 * This method is use to update banner details
	 * @param bannerModel
	 * @param request
	 * @return int
	 * @throws Exception
	 */
	public int updateBanner(BannerModel bannerModel,HttpServletRequest request) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("updateBanner -- START");
		}
		
		
		if(!(bannerModel.getFile() == null || bannerModel.getFile().isEmpty())){
			
			// to do file upload
			
			MultipartFile multipartFile = bannerModel.getFile();
			
			ServletContext context = request.getServletContext();
			String appPath = context.getRealPath("");
			
			String dirStr = appPath + "resources" + File.separator + "banner";
			File dir = new File(dirStr);
			if(!dir.exists()){
				dir.mkdir();
			}

			// construct the complete absolute path of the file
			String fileName = bannerModel.getCreatedBy()+"_"+new Date().getTime()+"_" + multipartFile.getOriginalFilename();
			String fullPath = dirStr + File.separator + fileName;
			
			File file = new File(fullPath);

			multipartFile.transferTo(file);
			
			bannerModel.setImageName(fileName);
		}
		
		int result = bannerDao.updateBanner(bannerModel);
				
		if (logger.isInfoEnabled()) {
			logger.info("updateBanner -- END");
		}
		
		return result;
		
	}
	
	
	/**
	 * This method use to delete Banner details
	 * @param bannerId
	 * @param adminId
	 * @return int
	 * @throws Exception
	 */
	public int deleteBanner(long bannerId,long adminId) throws Exception {
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteBanner -- START");
		}
		
		BannerModel bannerModel = bannerDao.fetchActiveBannerByID(bannerId);
		bannerModel.setaModifiedBy(adminId);
		bannerModel.setStatus(Status.DELETE);
		
		int result = bannerDao.updateBanner(bannerModel);
		
		if (logger.isInfoEnabled()) {
			logger.info("deleteBanner -- END");
		}
		return result;
	}
	
	public void imageFormatValidation(BannerModel bannerModel) throws ImageFormatMismatch, IOException {
		
		String imageType = TIKA.detect(bannerModel.getFile().getBytes());
		if (StringUtils.equals(imageType, "image/jpeg")
				|| StringUtils.equals(imageType, "image/jpg")
				|| StringUtils.equals(imageType, "image/jif")
				|| StringUtils.equals(imageType, "image/png")
				|| StringUtils.equals(imageType, "image/gif")
				|| StringUtils.equals(imageType, "image/bmp")) {

		} else {
			throw new ImageFormatMismatch(messageUtil.getBundle("image.format.mismatch"));
		}
	}
}
