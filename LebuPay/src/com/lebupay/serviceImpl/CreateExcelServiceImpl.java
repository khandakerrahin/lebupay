/**
 * @formatter:off
 *
 */
package com.lebupay.serviceImpl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lebupay.service.CreateExcelService;

/**
 * This is CreateExcelServiceImpl Class implements CreateExcelService interface is used to perform operation on Create Excel Module.
 * @author Java Team
 *
 */
@Transactional
@Service
public class CreateExcelServiceImpl implements CreateExcelService {
	
	/**
	 * This method is used to generate Excel.
	 * @param bookData
	 * @return XSSFWorkbook
	 */
	public XSSFWorkbook getExcel(Object[][] bookData){
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        
        XSSFCellStyle my_style = workbook.createCellStyle();
        XSSFFont my_font=workbook.createFont();
        my_font.setBold(true);
        my_style.setFont(my_font);
        
        int rowCount = 0;
        
        for (Object[] aBook : bookData) {
            Row row = sheet.createRow(++rowCount);
             
            int columnCount = 0;
            for (Object field : aBook) {
                Cell cell = row.createCell(++columnCount);
                if(rowCount==1)
                	cell.setCellStyle(my_style);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
             
        }
        
        return workbook;
	}
}
