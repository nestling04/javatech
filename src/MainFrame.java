import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField fname;
	private JTextField fdb;
	private JTextField key;
	private JTextField outname;

	private FurnitureTM ftm;
	private String source = "Choose!";
	private File fin;
	private File fout;
	private String mes = "Furniture program message";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private String target = "Choose!";

	private String srchout = "ID";
	private FurnitureTM srchtm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Furniture");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 522);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnLoad = new JButton("Load");
		btnLoad.setForeground(Color.WHITE);
		btnLoad.setBackground(new Color(0, 0, 255));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (source.equals("Choose!")) {
					JOptionPane.showMessageDialog(null, "Firstly choose a source!", mes, 0);
				}

				if (Integer.parseInt(fdb.getText()) != 0) {
					int result = JOptionPane.showConfirmDialog(null,
							"Would you like to overwrite the data already loaded?", mes,
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						for (int i = 0; i < ftm.getRowCount();) {
							ftm.removeRow(0);
						}
						loadFile();
					} else {
						if (result == JOptionPane.NO_OPTION) {
							loadFile();
						}
					}
				} else {
					loadFile();
				}
				fdb.setText("" + ftm.getRowCount());
			}
		});
		btnLoad.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLoad.setBounds(12, 13, 140, 50);
		contentPane.add(btnLoad);

		JButton btnList = new JButton("List");
		btnList.setForeground(Color.WHITE);
		btnList.setBackground(new Color(0, 0, 255));
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FurnitureList fl = new FurnitureList(MainFrame.this, ftm);
				fl.setVisible(true);
			}
		});
		btnList.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnList.setBounds(12, 76, 140, 50);
		contentPane.add(btnList);

		JButton btnNData = new JButton("New Data");
		btnNData.setForeground(Color.WHITE);
		btnNData.setBackground(new Color(0, 0, 255));
		btnNData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int codev = 0;
				if (ftm.getRowCount() == 0)
					codev = 20;
				else
					codev = (int) ftm.getValueAt(ftm.getRowCount() - 1, 1);
				NewData en = new NewData(MainFrame.this, codev);
				en.setVisible(true);
				int quit = en.Quit();
				if (quit == 1) {
					Furniture newFurniture = en.getFurniture();
					Date d = newFurniture.getFabricdate();
					String ddd = sdf.format(d).toString();
					ftm.addRow(new Object[] { new Boolean(false), newFurniture.getId(), newFurniture.getName(),
							newFurniture.getMaterial(), newFurniture.getType(), newFurniture.getSize()[0],
							newFurniture.getSize()[1], newFurniture.getSize()[2], ddd });
					fdb.setText("" + ftm.getRowCount());
				}
			}
		});
		btnNData.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNData.setBounds(12, 139, 140, 50);
		contentPane.add(btnNData);

		JButton btnModify = new JButton("Modify");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ftm.getRowCount() == 0)
					BC.SMD("No data to modify!");
				else {
					FurnitureMod fm = new FurnitureMod(MainFrame.this, ftm);
					fm.setVisible(true);
				}
			}
		});
		btnModify.setForeground(Color.WHITE);
		btnModify.setBackground(new Color(0, 0, 255));
		btnModify.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModify.setBounds(12, 201, 140, 50);
		contentPane.add(btnModify);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ftm.getRowCount() == 0)
					BC.showMD("No data to delete!", 0);
				else {
					FurnitureDel fd = new FurnitureDel(MainFrame.this, ftm);
					fd.setVisible(true);
					fdb.setText("" + ftm.getRowCount());
				}
			}
		});
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setBackground(new Color(0, 0, 255));
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDelete.setBounds(12, 264, 140, 50);
		contentPane.add(btnDelete);

		JButton btnWrite = new JButton("Write to file");
		btnWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (target.equals("Choose!"))
					BC.SMD("Firstly choose a source!");
				else if (ftm.getRowCount() == 0)
					BC.SMD("No data to write!");
				else if (target.equals("Local .csv file")) {
					FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

					fd.setFile("*.csv");
					fd.setVisible(true);
					if (fd.getFile() != null) {
						fin = new File(fd.getDirectory(), fd.getFile());
						String outfname = fd.getFile();
						outname.setText(outfname);
						FileManager.CsvWriter(outname.getText().toString(), ftm);
					}
				} else if (target.equals("SQLite DB")) {
					FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

					fd.setFile("*.db");
					fd.setVisible(true);
					if (fd.getFile() != null) {
						fout = new File(fd.getDirectory(), fd.getFile());
						String outfname = fd.getFile();
						outname.setText(outfname);
						FileManager.DBWriter(fout, ftm);
					}
				} else if (target.equals("Local .xml file")) {
					FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

					fd.setFile("*.xml");
					fd.setVisible(true);
					if (fd.getFile() != null) {
						fout = new File(fd.getDirectory(), fd.getFile());
						String outfname = fd.getFile();
						outname.setText(outfname);
						FileManager.XMLWriter(fout, ftm);
					}
				} else if (target.equals("Local .json file")) {
					FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

					fd.setFile("*.json");
					fd.setVisible(true);
					if (fd.getFile() != null) {
						fout = new File(fd.getDirectory(), fd.getFile());
						String outfname = fd.getFile();
						outname.setText(outfname);
						FileManager.JSONWriter(fout, ftm);
					}
				} else if (target.equals("Local .pdf file")) {
					FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

					fd.setFile("*.pdf");
					fd.setVisible(true);
					if (fd.getFile() != null) {
						fout = new File(fd.getDirectory(), fd.getFile());
						String outfname = fd.getFile();
						outname.setText(outfname);
						BC.PDFWriter(fout, ftm);
					}
				} else if (target.equals(">>> Source") && source.equals("Local .csv file")) {
					String outfname = fname.getText();
					outname.setText(outfname);
					FileManager.CsvWriter(outfname, ftm);
				} else if (target.equals(">>> Source") && source.equals("SQLite DB")) {
					String outfname = fname.getText();
					outname.setText(outfname);
					FileManager.DBWriter(fin, ftm);
				} else if (target.equals(">>> Source") && source.equals("Local .xml file")) {
					String outfname = fname.getText();
					outname.setText(outfname);
					FileManager.XMLWriter(fin, ftm);
				} else if (target.equals(">>> Source") && source.equals("Local .json file")) {
					String outfname = fname.getText();
					outname.setText(outfname);
					FileManager.JSONWriter(fin, ftm);
				}
			}
		});
		btnWrite.setForeground(Color.WHITE);
		btnWrite.setBackground(new Color(0, 0, 255));
		btnWrite.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnWrite.setBounds(12, 329, 140, 50);
		contentPane.add(btnWrite);

		JButton btnStat = new JButton("Size statistics");
		btnStat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double w_avg = 0;
				double l_avg = 0;
				double h_avg = 0;

				for (int i = 1; i < ftm.getRowCount(); i++) {
					w_avg += Double.parseDouble(ftm.getValueAt(i, 5).toString());
					l_avg += Double.parseDouble(ftm.getValueAt(i, 6).toString());
					h_avg += Double.parseDouble(ftm.getValueAt(i, 7).toString());
				}

				w_avg /= ftm.getRowCount();
				l_avg /= ftm.getRowCount();
				h_avg /= ftm.getRowCount();

				BC.showMD("Avarge width: " + w_avg + "\nAvarge length: " + l_avg + "\nAvarge height: " + h_avg, 1);
			}
		});
		btnStat.setForeground(Color.WHITE);
		btnStat.setBackground(new Color(0, 0, 255));
		btnStat.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStat.setBounds(12, 392, 171, 50);
		contentPane.add(btnStat);

		JLabel lblForrs = new JLabel("Source:");
		lblForrs.setForeground(Color.WHITE);
		lblForrs.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblForrs.setBounds(164, 30, 75, 16);
		contentPane.add(lblForrs);

		String element[] = { "Choose!", "Local .xml file", "Local .csv file", "SQLite DB", "Local .json file" };

		JComboBox jcbf = new JComboBox();
		jcbf.setForeground(Color.WHITE);
		jcbf.setBackground(new Color(0, 0, 255));
		jcbf.setFont(new Font("Tahoma", Font.PLAIN, 20));
		for (String s : element)
			jcbf.addItem(s);
		jcbf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				source = (String) jcbf.getSelectedItem();
				fname.setText(source);
			}
		});
		jcbf.setBounds(239, 13, 155, 48);
		contentPane.add(jcbf);

		fname = new JTextField();
		fname.setFont(new Font("Tahoma", Font.PLAIN, 20));
		fname.setBounds(406, 13, 364, 50);
		contentPane.add(fname);
		fname.setColumns(10);

		JLabel lblDataNum = new JLabel("Data Number:");
		lblDataNum.setForeground(Color.WHITE);
		lblDataNum.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDataNum.setBounds(164, 96, 132, 16);
		contentPane.add(lblDataNum);

		fdb = new JTextField();
		fdb.setHorizontalAlignment(SwingConstants.RIGHT);
		fdb.setText("0");
		fdb.setFont(new Font("Tahoma", Font.PLAIN, 20));
		fdb.setColumns(10);
		fdb.setBounds(306, 77, 88, 50);
		contentPane.add(fdb);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(164, 139, 606, 175);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblSrch = new JLabel("Search");
		lblSrch.setBounds(12, 13, 70, 25);
		lblSrch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(lblSrch);

		JRadioButton jrbID = new JRadioButton("ID");
		jrbID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jrbID.isSelected()) {
					srchout = "ID";
				}
			}
		});
		buttonGroup.add(jrbID);
		jrbID.setFont(new Font("Tahoma", Font.PLAIN, 20));
		jrbID.setBackground(Color.WHITE);
		jrbID.setBounds(22, 47, 62, 25);
		panel.add(jrbID);

		JRadioButton jrbName = new JRadioButton("Name");
		jrbName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jrbName.isSelected()) {
					srchout = "name";
				}
			}
		});
		buttonGroup.add(jrbName);
		jrbName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		jrbName.setBackground(Color.WHITE);
		jrbName.setBounds(177, 47, 88, 25);
		panel.add(jrbName);

		JRadioButton jrbMaterial = new JRadioButton("Material");
		jrbMaterial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jrbMaterial.isSelected()) {
					srchout = "material";
				}
			}
		});
		buttonGroup.add(jrbMaterial);
		jrbMaterial.setFont(new Font("Tahoma", Font.PLAIN, 20));
		jrbMaterial.setBackground(Color.WHITE);
		jrbMaterial.setBounds(319, 47, 107, 25);
		panel.add(jrbMaterial);

		JRadioButton jrbType = new JRadioButton("Type");
		jrbType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jrbType.isSelected()) {
					srchout = "type";
				}
			}
		});
		buttonGroup.add(jrbType);
		jrbType.setFont(new Font("Tahoma", Font.PLAIN, 20));
		jrbType.setBackground(Color.WHITE);
		jrbType.setBounds(478, 47, 77, 25);
		panel.add(jrbType);

		JLabel lblIDx = new JLabel("ID:=X");
		lblIDx.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblIDx.setBounds(32, 81, 70, 25);
		panel.add(lblIDx);

		JLabel lblX = new JLabel("X=");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX.setBounds(407, 81, 27, 25);
		panel.add(lblX);

		key = new JTextField();
		key.setBackground(Color.LIGHT_GRAY);
		key.setBounds(443, 75, 132, 38);
		panel.add(key);
		key.setFont(new Font("Tahoma", Font.PLAIN, 20));
		key.setColumns(10);

		JButton search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (BC.RF(fdb).equals("0"))
					BC.showMD("No data loaded!", 0);
				else if (!BC.filled(key))
					BC.showMD("The search key (X=) is not defined!", 0);
				else if (!FurnitureSearch.KeyCheck(srchout, BC.RF(key)))
					BC.showMD("The search key format is wrong!", 0);
				else {
					srchtm = FurnitureSearch.Select(ftm, srchout, BC.RF(key));
					FurnitureSearchP ek = new FurnitureSearchP(MainFrame.this, srchtm, srchout, BC.RF(key));
					ek.setVisible(true);
				}
			}
		});
		search.setFont(new Font("Tahoma", Font.PLAIN, 20));
		search.setBounds(44, 125, 140, 37);
		panel.add(search);

		JLabel lblTo = new JLabel("To:");
		lblTo.setForeground(Color.WHITE);
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTo.setBounds(164, 342, 41, 25);
		contentPane.add(lblTo);

		String element2[] = { "Choose!", ">>> Source", "Local .xml file", "Local .csv file", "Local .json file",
				"Local .pdf file", "SQLite DB" };
		JComboBox jcbc = new JComboBox();
		jcbc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				target = (String) jcbc.getSelectedItem();
				if (target.equals(">>> Source") && fname.getText().equals(""))
					BC.SMD("No source given!");
				if (target.equals(">>> Source") && !fname.getText().equals(""))
					outname.setText(fname.getText());
			}
		});
		jcbc.setForeground(Color.WHITE);
		jcbc.setBackground(new Color(0, 0, 255));
		jcbc.setFont(new Font("Tahoma", Font.PLAIN, 20));
		jcbc.setBounds(206, 332, 155, 48);
		for (String s : element2)
			jcbc.addItem(s);
		contentPane.add(jcbc);

		outname = new JTextField();
		outname.setFont(new Font("Tahoma", Font.PLAIN, 20));
		outname.setColumns(10);
		outname.setBounds(373, 330, 397, 50);
		contentPane.add(outname);

		JButton btnClose = new JButton("Close");
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(new Color(178, 34, 34));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnClose.setBounds(630, 392, 140, 50);
		contentPane.add(btnClose);

		Object furnituretmn[] = { "Sign", "ID", "Name", "Material", "Type", "Width", "Length", "Width",
				"Fabrication date" };
		ftm = new FurnitureTM(furnituretmn, 0);
	}

	public void loadFile() {
		if (source.equals("Local .csv file")) {
			FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

			fd.setFile("*.csv");
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fin = new File(fd.getDirectory(), fd.getFile());
				String inname = fd.getFile();
				fname.setText(inname);
				FileManager.CsvReader(fin, ftm);
			}
		} else if (source.equals("SQLite DB")) {
			FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

			fd.setFile("*.db");
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fin = new File(fd.getDirectory(), fd.getFile());
				String inname = fd.getFile();
				fname.setText(inname);
				FileManager.DBReader(fin, ftm);
			}
		} else if (source.equals("Local .xml file")) {
			FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

			fd.setFile("*.xml");
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fin = new File(fd.getDirectory(), fd.getFile());
				String inname = fd.getFile();
				fname.setText(inname);
				FileManager.XMLReader(fin, ftm);
			}
		} else if (source.equals("Local .json file")) {
			FileDialog fd = new FileDialog(new Frame(), "", FileDialog.LOAD);

			fd.setFile("*.json");
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fin = new File(fd.getDirectory(), fd.getFile());
				String inname = fd.getFile();
				fname.setText(inname);
				FileManager.JSONReader(fin, ftm);
			}
		}
	}
}
