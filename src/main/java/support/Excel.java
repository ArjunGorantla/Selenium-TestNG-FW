package support;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.SkipException;

/**
 * @author Arjun
 */

public class Excel extends DriverManager {
	
	private static final Logger LOGGER = LogManager.getLogger(Excel.class.getName());
	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFCell cell;
	public static String xlFile;

	/**
	 * @param xlFilepath
	 */
	public Excel(String xlFilepath) {
		this.xlFile = xlFilepath;
	}
	
	public synchronized String getCellData(String xlsheet, String columnName, String value) {
		DataFormatter formatter = new DataFormatter();
		ws = null;
		row = null;
		boolean fnstatus=false;
		try {
			int columnNum = -1;
			fi = new FileInputStream(xlFile);
			wb = new XSSFWorkbook(fi);
			ws = wb.getSheet(xlsheet);
			row = ws.getRow(0);
			int rowcount = ws.getLastRowNum() - ws.getFirstRowNum();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(columnName)) {
					columnNum = i;
					break;
				}
			}
			for (int i = 1; i <= rowcount; i++) {
				String tcid = ws.getRow(i).getCell(columnNum).getStringCellValue();
				if (tcid.equalsIgnoreCase(value)) {
					row = ws.getRow(i);
					cell = row.getCell(columnNum);
					fnstatus=true;
					break;
				}
			}
			wb.close();
			fi.close();
		} catch (Exception e) {
			LOGGER.info(e);
		}
		if(!fnstatus)
			throw new SkipException("Please set tc name.... "+getTestCaseName());
		
		if (formatter.formatCellValue(cell).isEmpty()) {
			return null;
		} else {
			return formatter.formatCellValue(cell);
		}

	}
	
	public synchronized void updateCellData(String xlsheet, String columnName, String value)
			throws Exception {
		try {
			fi = new FileInputStream(xlFile);
			wb = new XSSFWorkbook(fi);
			ws = wb.getSheet(xlsheet);
			int columnNum = -1;
			row = ws.getRow(0);
			int rowcount = ws.getLastRowNum() - ws.getFirstRowNum();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(columnName)) {
					columnNum = i;
					break;
				}
			}
			for (int i = 1; i <= rowcount; i++) {
				String tcid = ws.getRow(i).getCell(1).getStringCellValue();
				if (tcid.equalsIgnoreCase(getTestCaseName())) {
					row = ws.getRow(i);
					cell = row.createCell(columnNum);
					cell.setCellValue(value);
					fo = new FileOutputStream(xlFile);
					wb.write(fo);
					break;
				}
			}
			wb.close();
			fi.close();
			fo.close();
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found", e.getMessage());
			throw new Exception("File not found" + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Test data not found", e.getMessage());
			throw new Exception("Test data not found" + e.getMessage());
		}
	}

}
