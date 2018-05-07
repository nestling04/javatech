import java.awt.BorderLayout;
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
import javax.swing.JCheckBox;

public class FurnitureDel extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private FurnitureTM ftm;
	private boolean md = false;

	/**
	 * Create the dialog.
	 */
	public FurnitureDel(JFrame f, FurnitureTM bftm) {
		super(f, "Furniture list:", true);
		setTitle("Delete furniture");
		ftm = bftm;
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
		btnClose.setBounds(412, 227, 89, 23);
		contentPanel.add(btnClose);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 714, 181);
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

		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = 0, sign = 0, x = 0;
				for (x = 0; x < ftm.getRowCount(); x++) {
					if ((Boolean) ftm.getValueAt(x, 0)) {
						count++;
						sign = x;
					}
				}
				if (count == 0)
					BC.showMD("No signed data!", 0);
				if (!md) {
					if (count > 1) {
						ftm.removeRow(sign);
						BC.showMD("Multiple rows selected, please check multiple delete!", 0);
					}
					if(count==1){
						ftm.removeRow(sign);
						BC.showMD("Record deleted", 1);
					}
				} else {
					for (int i = 0; i < ftm.getRowCount(); i++) {
						if ((Boolean) ftm.getValueAt(i, 0)) {
							ftm.removeRow(i);
							i--;
						}
					}
					BC.showMD("Record(s) deleted!", 1);
				}
			}
		});
		delete.setBounds(235, 227, 89, 23);
		contentPanel.add(delete);

		JCheckBox jcb = new JCheckBox("Multiple record delete enabled");
		jcb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				md = jcb.isSelected();
			}
		});
		jcb.setBounds(30, 199, 256, 23);
		contentPanel.add(jcb);
		TableRowSorter<FurnitureTM> trs = (TableRowSorter<FurnitureTM>) table.getRowSorter();
		trs.setSortable(0, false);
	}
}
