package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    
	// Reads test data from the specified sheet and required columns.
    public static Object[][] getTestData(String filePath, String sheetName, int colCount) {
        Object[][] data = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
        	// Finds the sheet by its name.
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                throw new RuntimeException("Excel Error: '" + sheetName + "' എന്ന പേരിൽ ഷീറ്റ് കണ്ടെത്താനായില്ല!");
            }
            
            int rowCount = sheet.getLastRowNum();
            if (rowCount < 1) {
                throw new RuntimeException("Excel Error: '" + sheetName + "' ഷീറ്റിൽ ഡാറ്റ ഇല്ല!");
            }
            
            data = new Object[rowCount][colCount];

            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i + 1); // ഹെഡർ റോ ഒഴിവാക്കുന്നു
                if (row == null) continue;
                
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i][j] = (cell == null) ? "" : cell.toString().trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
