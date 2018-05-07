import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font.*;
import java.io.*;
import java.awt.Desktop;

public class BC {
	private static String mes = "Furniture program message";
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

	public static String RF(JTextField a) {
		return a.getText().toString();
	}

	public static boolean filled(JTextField a) {
		String s = RF(a);
		if (s.length() > 0)
			return true;
		else
			return false;
	}

	public static boolean goodDate(JTextField a) {
		String s = RF(a);
		Date testDate = null;
		try {
			testDate = sdf.parse(s);
		} catch (ParseException e) {
			return false;
		}
		if (sdf.format(testDate).equals(s))
			return true;
		else
			return false;
	}

	public static boolean goodInt(JTextField a) {
		String s = RF(a);
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean goodSize(JTextField a) {
		String s = RF(a);
		String[] sa = s.split(";");
		if (sa.length != 3) {
			return false;
		}
		try {
			for (int i = 0; i < sa.length; i++) {
				if (sa[i].trim().equals("")) {
					continue;
				}
				Integer.parseInt(sa[i]);
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static void showMD(String s, int i) {
		JOptionPane.showMessageDialog(null, s, mes, i);
	}

	public static void SMD(String s) {
		JOptionPane.showMessageDialog(null, s, mes, 0);
	}

	public static Date StoD(String s) {
		Date testDate = null;
		try {
			testDate = sdf.parse(s);
			;
		} catch (ParseException e) {
			return testDate;
		}
		if (sdf.format(testDate).equals(s))
			return testDate;
		else
			return testDate;
	}

	public static int StoI(String s) {
		int x = -55;
		x = Integer.parseInt(s);
		return x;
	}

	public static void DF(JTextField a) {
		a.setText("");
	}

	public static boolean goodStoInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void PDFWriter(File fname, FurnitureTM ftm) {
		try {
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font fnt = new Font(bf, 16, Font.BOLD);
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fname));
			document.open();
			Paragraph par = new Paragraph("List of furnitures", fnt);
			document.add(par);

			float[] widths = { 30, 180, 50, 50, 45, 40, 40, 115 };
			PdfPTable table = new PdfPTable(widths);
			table.setWidthPercentage(100);
			Font f1 = new Font(bf, 10, Font.BOLD);
			Font f2 = new Font(bf, 10);

			PdfPCell c = new PdfPCell(new Paragraph("ID", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Name", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Material", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Type", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Length", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Width", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Height", f1));
			table.addCell(c);
			c = new PdfPCell(new Paragraph("Fabricdate", f1));
			table.addCell(c);

			for (int i = 0; i < ftm.getRowCount(); i++) {
				for (int j = 1; j < ftm.getColumnCount(); j++) {
					c = new PdfPCell(new Paragraph(ftm.getValueAt(i, j).toString(), f2));
					table.addCell(c);
				}
			}
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			document.add(table);

			document.close();
			showMD("The document has been completed, name is: " + fname.getName(), 1);
		} catch (DocumentException de) {
			System.out.println("PdfMaker: " + de.getMessage());
		} catch (IOException ex) {
			System.out.println("PdfMaker: " + ex.getMessage());
		}
		try {
			File fpdf = new File(fname.getName());
			Desktop.getDesktop().open(fpdf);
		} catch (IOException exx) {
			showMD(exx.getMessage(), 0);
		}
	}
}
