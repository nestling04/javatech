import java.awt.List;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

import org.json.*;
import com.google.gson.*;
import java.util.StringTokenizer;

public class FileManager {
	private static Connection conn = null;

	public static void CsvReader(File fname, FurnitureTM ftm) {
		String id = "", name = "", material = "", type = "", width = "", length = "", height = "", fabricdate = "",
				s = "";

		try {
			FileInputStream f = new FileInputStream(fname);
			LineNumberReader in = new LineNumberReader(new InputStreamReader(f));

			s = in.readLine();
			s = in.readLine();
			while (s != null) {
				String[] splitS = s.split(";");
				id = splitS[0];
				name = splitS[1];
				material = splitS[2];
				type = splitS[3];
				width = splitS[4];
				length = splitS[5];
				height = splitS[6];
				fabricdate = splitS[7];

				ftm.addRow(new Object[] { new Boolean(false), BC.StoI(id), name, material, type, BC.StoI(width),
						BC.StoI(length), BC.StoI(height), fabricdate });

				s = in.readLine();
			}
			in.close();
			BC.showMD("Data successfully read!", 1);
		} catch (IOException ioe) {
			BC.showMD("CsvReader: " + ioe.getMessage(), 0);
		}
	}

	public static void CsvWriter(String fname, FurnitureTM ftm) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fname));
			out.println("ID;Name;Material;Type;Width;Length;Width;Fabrication date");
			int rcnt = ftm.getRowCount();
			int ccnt = ftm.getColumnCount();
			for (int i = 0; i < rcnt; i++) {
				for (int j = 1; j < ccnt - 1; j++) {
					out.print("" + ftm.getValueAt(i, j) + ";");
				}
				out.println("" + ftm.getValueAt(i, ccnt - 1));
			}
			out.close();
			BC.showMD("Data successfully written!", 1);
		} catch (IOException ioe) {
			BC.showMD("CsvWriter: " + ioe.getMessage(), 0);
		}
	}

	public static void DBReader(File fname, FurnitureTM ftm) {
		if (!Reg() || !Connect(fname)) {
			return;
		}

		String name = "", material = "", type = "", fabricdate = "";
		int id = 0, width = 0, length = 0, height = 0;
		Statement st = null;
		ResultSet rs = null;

		String sqlp = "select * from furniture;";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sqlp);
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				material = rs.getString("material");
				type = rs.getString("type");
				width = rs.getInt("width");
				length = rs.getInt("length");
				height = rs.getInt("height");
				fabricdate = rs.getString("fab_date"); // if mismatches, change the variable type to Date!

				ftm.addRow(new Object[] { new Boolean(false), id, name, material, type, width, length, height,
						fabricdate });
			}
			rs.close();
			st.close();
			disConnect();
			BC.showMD("Data successfully read!", 1);
		} catch (SQLException e) {
			BC.showMD(e.getMessage(), 0);
		}
	}

	public static void DBWriter(File fname, FurnitureTM ftm) {
		try {
			Reg();
			Connect(fname);

			Statement st = null;
			ResultSet rs = null;
			ArrayList<Integer> ids = new ArrayList<Integer>();
			try {
				st = conn.createStatement();
				rs = st.executeQuery("select * from furniture;");
				while (rs.next()) {
					ids.add(rs.getInt("id"));
				}
			} catch (SQLException ex) {
				BC.showMD(ex.getMessage(), 0);
			}
			int rcnt = ftm.getRowCount();
			int ccnt = ftm.getColumnCount();
			for (int i = 0; i < rcnt; i++) {
				st = conn.createStatement();
				if (ids.contains(Integer.parseInt(ftm.getValueAt(i, 1).toString()))) {
					try {
						st.executeQuery("delete from furniture where id=" + ftm.getValueAt(i, 1).toString() + ";");
					} catch (Exception e) {
					}
				}
				String sqlp = "insert into furniture values(" + BC.StoI(ftm.getValueAt(i, 1).toString());
				for (int j = 2; j < ccnt; j++) {
					try {
						sqlp += "," + BC.StoI(ftm.getValueAt(i, j).toString());
					} catch (Exception e) {
						sqlp += ",'" + ftm.getValueAt(i, j).toString() + "'";
					}
				}
				sqlp += ");";
				Insert(sqlp);
			}
			rs.close();
			st.close();
			disConnect();
			BC.showMD("Data successfully written!", 1);
		} catch (Exception ioe) {
			BC.showMD("DBWriter: " + ioe.getMessage(), 0);
		}
	}

	public static void XMLReader(File fname, FurnitureTM ftm) {
		String id, name, material, type, width, length, height, fabricdate;
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(fname);
			NodeList nodeList = dom.getElementsByTagName("furniture");

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element element = (Element) node;
				id = element.getElementsByTagName("id").item(0).getTextContent();
				name = element.getElementsByTagName("name").item(0).getTextContent();
				material = element.getElementsByTagName("material").item(0).getTextContent();
				type = element.getElementsByTagName("type").item(0).getTextContent();
				width = element.getElementsByTagName("width").item(0).getTextContent();
				length = element.getElementsByTagName("length").item(0).getTextContent();
				height = element.getElementsByTagName("height").item(0).getTextContent();
				fabricdate = element.getElementsByTagName("fabricdate").item(0).getTextContent();

				ftm.addRow(new Object[] { new Boolean(false), id, name, material, type, width, length, height,
						fabricdate });
			}

			BC.showMD("Data successfully read!", 1);
		} catch (ParserConfigurationException pce) {
			BC.showMD("XmlReader: " + pce.getMessage(), 0);
		} catch (SAXException se) {
			BC.showMD("XmlReader: " + se.getMessage(), 0);
		} catch (IOException ioe) {
			BC.showMD("XmlReader: " + ioe.getMessage(), 0);
		}
	}

	public static void XMLWriter(File fname, FurnitureTM ftm) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
			Element rooroot = dom.createElement("furnitures");
			int rcnt = ftm.getRowCount();

			for (int i = 0; i < rcnt; i++) {
				Element rootEle = dom.createElement("furniture");
				Element e = null;
				e = dom.createElement("id");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 1).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("name");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 2).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("material");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 3).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("type");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 4).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("width");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 5).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("length");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 6).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("height");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 7).toString()));
				rootEle.appendChild(e);
				e = dom.createElement("fabricdate");
				e.appendChild(dom.createTextNode(ftm.getValueAt(i, 8).toString()));
				rootEle.appendChild(e);
				rooroot.appendChild(rootEle);
			}
			dom.appendChild(rooroot);
			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-2");
				tr.setOutputProperty("{http://xml.apache.org/xslt} indent-amount", "4");
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fname.getName())));
				BC.showMD("Data successfully written!", 1);
			} catch (TransformerException te) {
				BC.showMD("XmlWriter: " + te.getMessage(), 0);
			} catch (IOException ioe) {
				BC.showMD("XmlWriter: " + ioe.getMessage(), 0);
			}
		} catch (ParserConfigurationException pce) {
			BC.showMD("XmlWriter: " + pce.getMessage(), 0);
		}
	}

	public static void JSONReader(File fname, FurnitureTM ftm) {
		StringBuilder jsonData = new StringBuilder();
		String name, material, type, fabricdate;
		int id, width, length, height;
		try {
			FileInputStream f = new FileInputStream(fname.getName());
			LineNumberReader in = new LineNumberReader(new InputStreamReader(f));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				jsonData.append(inputLine);
			in.close();
		} catch (IOException e) {
			BC.showMD(e.getMessage(), 0);
		}
		BC.showMD("Data successfully read!", 1);
		try {
			JSONObject jRootObj = new JSONObject(jsonData.toString());
			JSONObject jObj = jRootObj.getJSONObject("furnitures");
			JSONArray jArr = jObj.getJSONArray("furniture");
			for (int i = 0; i < jArr.length(); ++i) {
				JSONObject rec = jArr.getJSONObject(i);
				id = rec.getInt("id");
				name = rec.getString("name");
				material = rec.getString("material");
				type = rec.getString("type");
				width = rec.getInt("width");
				length = rec.getInt("length");
				height = rec.getInt("height");
				fabricdate = rec.getString("fabricdate");

				ftm.addRow(new Object[] { new Boolean(false), id, name, material, type, width, length, height,
						fabricdate });
			}
			BC.showMD("Data succesfullye processed!", 1);
		} catch (Exception e) {
			BC.showMD(e.getMessage(), 0);
		}
	}

	public static void JSONWriter(File fname, FurnitureTM ftm) {
		Furniture[] k = new Furniture[ftm.getRowCount()];

		for (int i = 0; i < ftm.getRowCount(); i++) {
			k[i] = new Furniture();
			k[i].setId(BC.StoI(ftm.getValueAt(i, 1).toString()));
			k[i].setName(ftm.getValueAt(i, 2).toString());
			k[i].setMaterial(ftm.getValueAt(i, 3).toString());
			k[i].setType(ftm.getValueAt(i, 4).toString());
			k[i].setSize(new int[] { BC.StoI(ftm.getValueAt(i, 5).toString()), BC.StoI(ftm.getValueAt(i, 6).toString()),
					BC.StoI(ftm.getValueAt(i, 7).toString()) });
			k[i].setFabricdate(BC.StoD(ftm.getValueAt(i, 8).toString()));
		}

		JSONObject jRootObj = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			for (int i = 0; i < k.length; i++) {
				JSONObject jo = new JSONObject();
				jo.put("id", k[i].getId());
				jo.put("name", k[i].getName());
				jo.put("material", k[i].getMaterial());
				jo.put("type", k[i].getType());
				jo.put("width", k[i].getSize()[0]);
				jo.put("length", k[i].getSize()[1]);
				jo.put("height", k[i].getSize()[2]);
				jo.put("fabricdate", BC.sdf.format(k[i].getFabricdate()));
				jArray.put(i, jo);
			}
			obj.put("furniture", jArray);
			jRootObj.put("furnitures", obj);
			BC.showMD("Data successfully processed!", 1);
		} catch (Exception e) {
			BC.showMD(e.getMessage(), 0);
		}
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jRootObj.toString());
			String prettyJS = gson.toJson(je);
			PrintStream out = new PrintStream(new FileOutputStream(fname.getName()));
			String[] arrSplit = prettyJS.split("\n");
			for (String x : arrSplit)
				out.println(x);
			out.close();
			BC.showMD("Data successfully written!", 1);
		} catch (Exception e) {
			BC.showMD(e.getMessage(), 0);
		}

	}

	private static boolean Reg() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			BC.showMD("Error while registering driver!\n" + e.getMessage(), 0);
			return false;
		}
		return true;
	}

	private static boolean Connect(File fname) {
		try {
			String url = "jdbc:sqlite:";
			url += fname;
			conn = DriverManager.getConnection(url);
			BC.showMD("Connection OK!", 1);
		} catch (SQLException e) {
			BC.showMD("JDBC Connect: " + e.getMessage(), 0);
			return false;
		}
		return true;
	}

	private static void Insert(String sqlp) {
		Statement st = null;
		try {
			st = conn.createStatement();
			st.execute(sqlp);
		} catch (SQLException e) {
			BC.showMD("JDBC insert: " + e.getMessage(), 0);
		}
	}

	private static void disConnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			BC.showMD(e.getMessage(), 1);
		}
	}
}
