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
import javax.swing.SwingConstants;

public class FurnitureSearchP extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private FurnitureTM ftm;
	private JTextField type;
	private JTextField material;
	private JTextField name;
	private JTextField ID;

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { FurnitureMod frame = new
	 * FurnitureMod(null); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the dialog.
	 */
	public FurnitureSearchP(JFrame f, FurnitureTM bftm, String field, String key) {
		super(f, "Furniture list:", true);
		setTitle("Search result");
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
		btnClose.setBounds(295, 227, 89, 23);
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

		type = new JTextField(key);
		type.setEditable(false);
		type.setColumns(10);
		type.setBounds(380, 202, 47, 20);
		contentPanel.add(type);

		material = new JTextField(key);
		material.setEditable(false);
		material.setColumns(10);
		material.setBounds(317, 202, 61, 20);
		contentPanel.add(material);

		name = new JTextField(key);
		name.setEditable(false);
		name.setColumns(10);
		name.setBounds(102, 202, 208, 20);
		contentPanel.add(name);

		ID = new JTextField(key);
		ID.setEditable(false);
		ID.setHorizontalAlignment(SwingConstants.RIGHT);
		ID.setColumns(10);
		ID.setBounds(62, 202, 35, 20);
		contentPanel.add(ID);
		TableRowSorter<FurnitureTM> trs = (TableRowSorter<FurnitureTM>) table.getRowSorter();
		trs.setSortable(0, false);

		ID.setVisible(false);
		name.setVisible(false);
		material.setVisible(false);
		type.setVisible(false);
		if (field.equals("ID"))
			ID.setVisible(true);
		if (field.equals("name"))
			name.setVisible(true);
		if (field.equals("material"))
			material.setVisible(true);
		if (field.equals("type"))
			type.setVisible(true);
	}
}
