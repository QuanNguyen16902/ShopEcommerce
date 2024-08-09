package com.ecommerce.admin.category;


import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.ecommerce.admin.user.export.AbstractExporter;
import com.ecommerce.common.entity.Category;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryPdfExporter extends AbstractExporter{

	public void export(List<Category> listcategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "categories_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of categories", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {3.5f, 3.0f});
		
		writeTableHeader(table);
		writeTableData(table, listcategories);
		document.add(table);
		
		document.close();
	}

	private void writeTableData(PdfPTable table, List<Category> listcategories) {
		
		for(Category category : listcategories) {
			table.addCell(String.valueOf(category.getId()));
			table.addCell(category.getName().replace("--", "  "));
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("ID", font));	
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name", font));	
		table.addCell(cell);
		
//		cell.setPhrase(new Phrase("Alias", font));	
//		table.addCell(cell);
//		cell.setPhrase(new Phrase("Enabled", font));	
//		table.addCell(cell);
	}

}
