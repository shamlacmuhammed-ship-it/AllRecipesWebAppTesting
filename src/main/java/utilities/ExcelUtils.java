package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    /**
     * Reads test data from an Excel sheet and returns it as a 2D Object array.
     *
     * @param filePath   Path of the Excel file
     * @param sheetName  Name of the worksheet
     * @param colCount   Number of columns to read
     * @return Object[][] containing test data
     */

    public static Object[][] getTestData(String filePath,
                                         String sheetName,
                                         int colCount) {

        Object[][] data = null;

        // Open the Excel file and workbook
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the required worksheet by name
            Sheet sheet = workbook.getSheet(sheetName);

            // Check whether the sheet exists
            if (sheet == null) {
                throw new RuntimeException(
                        "Excel Error: Sheet '" + sheetName + "' not found.");
            }

            // Get total number of data rows
            int rowCount = sheet.getLastRowNum();

            // Check whether the sheet contains data
            if (rowCount < 1) {
                throw new RuntimeException(
                        "Excel Error: No test data available in sheet '" + sheetName + "'.");
            }

            // Create a two-dimensional array
            data = new Object[rowCount][colCount];

            // Read all data rows (skip header row)
            for (int i = 0; i < rowCount; i++) {

                // Start reading from row index 1 because row 0 contains headers
                Row row = sheet.getRow(i + 1);

                if (row == null) {
                    continue;
                }

                // Read each column value
                for (int j = 0; j < colCount; j++) {

                    Cell cell = row.getCell(j);

                    // Store empty string if the cell is blank
                    data[i][j] =
                            (cell == null)
                                    ? ""
                                    : cell.toString().trim();
                }
            }

        } catch (IOException e) {

            // Handle file reading errors
            e.printStackTrace();
        }

        // Return the Excel data
        return data;
    }
}
