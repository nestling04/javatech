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

public class FurnitureList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private FurnitureTM ftm;

	/**
	 * Create the dialog.
	 */
	public FurnitureList(JFrame f, FurnitureTM bftm) {
		super(f, "Furniture list:", true);
		setTitle("Furniture list");
		ftm=bftm;
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
		btnClose.setBounds(324, 227, 89, 23);
		contentPanel.add(btnClose);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 714, 210);
		contentPanel.add(scrollPane);
		
		table = new JTable(ftm);
		scrollPane.setViewportView(table);
		
		TableColumn tc = null;
		for (int i = 0; i < 9; i++) {
			tc = table.getColumnModel().getColumn(i);
			switch(i) {
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
		TableRowSorter<FurnitureTM> trs = (TableRowSorter<FurnitureTM>)table.getRowSorter();
		trs.setSortable(0, false);
	}
}
