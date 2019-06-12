package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw14.database.PollOption;

/**
 * A servlet that allows the user to download the
 * voting results as a form of an Excel (.xls) file.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeXlsServlet extends HttpServlet {
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=voting_results.xls");
        
        List<PollOption> pollOptionsSorted = (List<PollOption>) req.getSession().getAttribute("pollOptionsSorted");
        HSSFWorkbook excelFile = generateExcelFile(pollOptionsSorted);
        excelFile.write(resp.getOutputStream());
	}

	/**
	 * Generates the band voting results Excel file.
	 *
	 * @param bands the list of bands with their votes
	 * @return the band voting results Excel file
	 */
	private HSSFWorkbook generateExcelFile(List<PollOption> bands) {
		HSSFWorkbook excelFile = new HSSFWorkbook();
		HSSFSheet sheet = excelFile.createSheet("Voting results");
		
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Poll option");
		header.createCell(1).setCellValue("Number of votes");
		
		for(int i = 0; i < bands.size(); i++) {
			HSSFRow row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(bands.get(i).title);
			row.createCell(1).setCellValue(bands.get(i).getVotes());
		}
		
		return excelFile;
	}
}
