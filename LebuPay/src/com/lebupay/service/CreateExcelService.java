/**
 * @formatter:off
 *
 */
package com.lebupay.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This is CreateExcelService Interface is used to perform operations on CreateExcel Module.
 * @author Java Team
 *
 */
public interface CreateExcelService {
	
	/**
	 * This method is used to getExcel. 
	 * @param bookData
	 * @return XSSFWorkbook
	 */
	public XSSFWorkbook getExcel(Object[][] bookData);
}
