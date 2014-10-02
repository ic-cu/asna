package xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import sql.DB;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DB db = new DB();
		Properties config = new Properties();
		String query = null;
		try {
			config.load(new FileReader("query.prop"));
			query = config.getProperty("query.inventariali");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResultSet rs = db.executeQuery(query);
		ResultSetMetaData md = null;
		int colNum = 0;
		int count = 1;
		try {
			md = rs.getMetaData();
			colNum = md.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(config.getProperty("xml.output.filename"));
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			PrintWriter pw = new PrintWriter(osw);
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<table name=\"DatiInventariali\" version=\"4.0.0.2\" code=\"NAPOLI\">");
			
			while(rs.next())
			{
				System.out.println("Record numero " + count++);
				pw.println("<record>");
				for(int i = 1; i <= colNum; i++)
				{
					String val = rs.getString(i);
					String field = "<field id=\"" + i + "\"";
					field += " null=\""; 
					if(val == null || val.trim() == "")
					{
						field += "1\"/>";
						pw.println(field);
					}
					else
					{
//						val = val.replaceAll("Vicer.. ", "Vicerè ");
//						val = val.replaceAll("Universit. ", "Università ");
						field += "0\">";
						field += val.trim();
						field += "</field>";
						pw.println(field);
					}
				}
				pw.println("</record>");
			}
			pw.println("</table>");
			pw.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
