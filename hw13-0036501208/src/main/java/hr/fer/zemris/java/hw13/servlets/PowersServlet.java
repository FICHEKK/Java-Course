package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Dynamic generation of an Excel (.xls) file.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class PowersServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String paramA = req.getParameter("a");
		String paramB = req.getParameter("b");
		String paramN = req.getParameter("n");
		
		int a = 0, b = 0, n = 0;

		try {
			a = Integer.parseInt(paramA);
		} catch(NumberFormatException e) {
			sendError(req, resp, "Could not parse parameter 'a' to integer.");
		}
		
		try {
			b = Integer.parseInt(paramB);
		} catch(NumberFormatException e) {
			sendError(req, resp, "Could not parse 'b' to integer.");
		}
		
		try {
			n = Integer.parseInt(paramN);
		} catch(NumberFormatException e) {
			sendError(req, resp, "Could not parse 'n' to integer.");
		}
		
		if(a > 100 || a < -100) sendError(req, resp, "Parameter 'a' is out of range [-100, 100] for value '" + a + "'.");
		if(b > 100 || b < -100) sendError(req, resp, "Parameter 'b' is out of range [-100, 100] for value '" + b + "'.");
		if(n < 1   || n > 5   ) sendError(req, resp, "Parameter 'n' is out of range [1, 5] for value '" + n + "'.");
			
		resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=table.xls");
        
        HSSFWorkbook excelFile = generateExcelFile(a, b, n);
        excelFile.write(resp.getOutputStream());
	}
	
	/**
	 * Generates a model of an Excel (.xls) file specified by parameters:
	 * <br> - a: The starting integer value
	 * <br> - b: The ending integer value
	 * <br> - n: The number of sheets.
	 * 
	 * <p> Each consecutive sheet will increase the exponent by 1.
	 * For example, the first sheet will have the exponent of 1, the
	 * second of 2, third of 3 and so on.
	 * 
	 * <p>If a = 5 and b = 10, the first column of the first sheet will
	 * contain numbers from 5 to 10, and in the second column will
	 * be 5^1, 6^1, 7^1, 8^1, 9^1 and 10^1. The second sheet will also
	 * have in the first column numbers from 5 to 10, but on the second
	 * column, those values will be squared: 5^2, 6^2, 7^2, 8^2, 9^2 and
	 * 10^2. Third sheet will have cubes in its second column: 5^3, 6^3,
	 * 7^3, 8^3, 9^3 and 10^3. And so on...
	 *
	 * @param a the starting integer value
	 * @param b the ending integer value
	 * @param n the number of sheets
	 * @return a generated model of an Excel file defined as above
	 */
	private HSSFWorkbook generateExcelFile(int a, int b, int n) {
		HSSFWorkbook excelFile = new HSSFWorkbook();
		
		for(int i = 1; i <= n; i++) {
			HSSFSheet sheet = excelFile.createSheet("Powers of "+ i);

			for(int j = 0, rows = b-a; j <= rows; j++) {
				HSSFRow row = sheet.createRow(j);
				row.createCell(0).setCellValue(j + a);
				row.createCell(1).setCellValue(Math.pow(j + a, i));
			}
		}
		
		return excelFile;
	}
	
	/**
	 * A helper method that redirects the user to the error page if the user
	 * input was incorrect (number out of range, parsing problem etc.)
	 *
	 * @param req the request object
	 * @param resp the response object
	 * @param message the message to be displayed to the user
	 * @throws ServletException if an error with forwarding occurs
	 * @throws IOException if an IO error occurs
	 */
	private void sendError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
		req.setAttribute("error_msg", message);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
}
