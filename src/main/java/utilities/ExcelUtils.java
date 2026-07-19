package utilities;

// Apache POI classes for reading Excel files
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Java classes for file handling
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    /**
     * Reads test data from an Excel worksheet
     * and returns it as a two-dimensional Object array.
     *
     * This method is mainly used for
     * TestNG DataProvider (Data-Driven Testing).
     *
     * @param filePath  Path of the Excel file
     * @param sheetName Name of the worksheet
     * @param colCount  Number of columns to read
     * @return Object[][] containing Excel data
     */

    public static Object[][] getTestData(String filePath,
                                         String sheetName,
                                         int colCount) {

        // Object array used to store Excel data
        Object[][] data = null;

        // Open the Excel file and workbook
        // try-with-resources automatically closes the file
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the worksheet by its name
            Sheet sheet = workbook.getSheet(sheetName);

            // Verify whether the worksheet exists
            if (sheet == null) {

                throw new RuntimeException(
                        "Excel Error: Sheet '" + sheetName + "' not found.");
            }

            // Get the last row number
            // (Header row is not included)
            int rowCount = sheet.getLastRowNum();

            // Verify whether the worksheet contains test data
            if (rowCount < 1) {

                throw new RuntimeException(
                        "Excel Error: No test data available in sheet '"
                                + sheetName + "'.");
            }

            // Create a two-dimensional array
            // Rows = Test Data Rows
            // Columns = Number of fields to read
            data = new Object[rowCount][colCount];

            // Read all data rows
            // Row 0 contains column headers
            // Therefore reading starts from Row 1
            for (int i = 0; i < rowCount; i++) {

                // Read current data row
                Row row = sheet.getRow(i + 1);

                // Skip empty rows
                if (row == null) {
                    continue;
                }

                // Read every column in the current row
                for (int j = 0; j < colCount; j++) {

                    // Get the current cell
                    Cell cell = row.getCell(j);

                    // If the cell is empty,
                    // store an empty string
                    // Otherwise store the trimmed cell value
                    data[i][j] =
                            (cell == null)
                                    ? ""
                                    : cell.toString().trim();
                }
            }

        } catch (IOException e) {

            // Handle file-related exceptions
            // such as file not found or access issues
            e.printStackTrace();
        }

        // Return the Excel data
        // Used by TestNG DataProvider
        return data;
    }
}
