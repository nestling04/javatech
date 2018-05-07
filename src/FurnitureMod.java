import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class FurnitureMod extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private FurnitureTM ftm;
	private JTextField fdate;
	private JTextField size;
	private JTextField type;
	private JTextField material;
	private JTextField name;

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { FurnitureMod frame = new
	 * FurnitureMod(null); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the dialog.
	 */
	public FurnitureMod(JFrame f, FurnitureTM bftm) {
		super(f, "Furniture list:", true);
		setTitle("Modify furniture");
		ftm = bftm;
		Object furnituretmn[] = { "Sign", "ID", "Name", "Material", "Type", "Width", "Length", "Width",
				"Fabrication date" };
		setBounds(100, 100, 750, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});
		btnClose.setBounds(404, 227, 89, 23);
		contentPanel.add(btnClose);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 714, 180);
		contentPanel.add(scrollPane);

		table = new JTable(ftm);
		scrollPane.setViewportView(table);

		TableColumn tc = null;
		for (int i = 0; i < 9; i++) {
			tc = table.getColumnModel().getColumn(i);
			switch (i) {
			case 0:
				tc.setPreferredWidth(35);
				break;
			case 1:
				tc.setPreferredWidth(30);
				break;
			case 2:
				tc.setPreferredWidth(200);
				break;
			case 3:
				tc.setPreferredWidth(50);
				break;
			case 4:
				tc.setPreferredWidth(50);
				break;
			case 5:
				tc.setPreferredWidth(40);
				break;
			case 6:
				tc.setPreferredWidth(40);
				break;
			case 7:
				tc.setPreferredWidth(40);
				break;
			case 8:
				tc.setPreferredWidth(115);
				break;
			}
		}

		table.setAutoCreateRowSorter(true);

		fdate = new JTextField();
		fdate.setBounds(623, 202, 101, 20);
		contentPanel.add(fdate);
		fdate.setColumns(10);

		size = new JTextField();
		size.setColumns(10);
		size.setBounds(440, 202, 166, 20);
		contentPanel.add(size);

		type = new JTextField();
		type.setColumns(10);
		type.setBounds(380, 202, 47, 20);
		contentPanel.add(type);

		material = new JTextField();
		material.setColumns(10);
		material.setBounds(317, 202, 61, 20);
		contentPanel.add(material);

		name = new JTextField();
		name.setColumns(10);
		name.setBounds(102, 202, 208, 20);
		contentPanel.add(name);

		JButton btnModify = new JButton("Modify");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!BC.filled(name) && !BC.filled(material) && !BC.filled(type) && !BC.filled(fdate)
						&& !BC.filled(size)) {
					BC.showMD("No modifying data inserted!", 0);
				} else if (BC.filled(fdate) && !BC.goodDate(fdate))
					BC.showMD("Wrong date format!", 0);
				else if (BC.filled(size) && !BC.goodSize(size))
					BC.showMD("Please give the size parameters like this: WIDTH;LENGTH;HEIGHT\nSeparated with ';' !!!", 0);
				else {
					int count = 0, sign = 0, x = 0;
					for (x = 0; x < ftm.getRowCount(); x++) {
						if ((Boolean) ftm.getValueAt(x, 0)) {
							count++;
							sign = x;
						}
					}
					if (count == 0)
						BC.showMD("No signed data!", 0);
					if (count > 1)
						BC.showMD("Multiple rows selected", 0);
					if (count == 1) {
						if (BC.filled(name))
							ftm.setValueAt(BC.RF(name), sign, 2);
						if (BC.filled(material))
							ftm.setValueAt(BC.RF(material), sign, 3);
						if (BC.filled(type))
							ftm.setValueAt(BC.RF(type), sign, 4);
						if (BC.filled(size)) {
							String[] sa = BC.RF(size).split(";");
							if (!sa[0].trim().equals(""))
								ftm.setValueAt(sa[0], sign, 5);
							if (!sa[1].trim().equals(""))
								ftm.setValueAt(sa[1], sign, 6);
							if (!sa[2].trim().equals(""))
								ftm.setValueAt(sa[2], sign, 7);
						}
						if (BC.filled(fdate))
							ftm.setValueAt(BC.RF(fdate), sign, 8);
						BC.showMD("Record modified", 1);
						BC.DF(name);
						BC.DF(material);
						BC.DF(type);
						BC.DF(size);
						BC.DF(fdate);
						ftm.setValueAt(new Boolean(false), sign, 0);
					}
				}
			}
		});
		btnModify.setBounds(221, 227, 89, 23);
		contentPanel.add(btnModify);
		TableRowSorter<FurnitureTM> trs = (TableRowSorter<FurnitureTM>) table.getRowSorter();
		trs.setSortable(0, false);
	}
}
