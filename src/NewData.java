import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;

public class NewData extends JDialog {
	private JTextField id;
	private JTextField name;
	private JTextField material;
	private JTextField type;
	private JTextField w;

	private Furniture data;
	private int exit = 0;
	private JTextField l;
	private JTextField h;
	private JTextField fabricdate;

	/**
	 * Create the dialog.
	 */
	public NewData(JFrame f, int maxID) {
		super(f, true);
		setTitle("Upload new furniture");
		getContentPane().setBackground(Color.YELLOW);
		setBackground(new Color(175, 238, 238));
		setBounds(100, 100, 324, 255);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(10, 11, 87, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblNv = new JLabel("Name:");
		lblNv.setBounds(10, 36, 87, 14);
		getContentPane().add(lblNv);

		JLabel lblSzletsiId = new JLabel("Material:");
		lblSzletsiId.setBounds(10, 61, 87, 14);
		getContentPane().add(lblSzletsiId);

		JLabel lblLakhely = new JLabel("Type:");
		lblLakhely.setBounds(10, 86, 87, 14);
		getContentPane().add(lblLakhely);

		JLabel lblIq = new JLabel("Size:");
		lblIq.setBounds(10, 111, 87, 14);
		getContentPane().add(lblIq);

		id = new JTextField();
		id.setEditable(false);
		id.setBounds(107, 5, 47, 20);
		getContentPane().add(id);
		id.setColumns(10);

		name = new JTextField();
		name.setColumns(10);
		name.setBounds(107, 30, 180, 20);
		getContentPane().add(name);

		material = new JTextField();
		material.setColumns(10);
		material.setBounds(107, 55, 180, 20);
		getContentPane().add(material);

		type = new JTextField();
		type.setColumns(10);
		type.setBounds(107, 80, 182, 20);
		getContentPane().add(type);

		w = new JTextField();
		w.setColumns(10);
		w.setBounds(130, 108, 34, 20);
		getContentPane().add(w);

		JButton btnGet = new JButton("Get");
		btnGet.setBounds(164, 4, 89, 23);
		getContentPane().add(btnGet);
		btnGet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				id.setText("" + (maxID + 1));
			}
		});

		JButton btnInsrt = new JButton("Insert");
		btnInsrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!BC.filled(id))
					id.setText("" + (maxID + 1));
				if (!BC.filled(name))
					BC.showMD("Name field is empty!", 0);
				else if (!BC.filled(material))
					BC.showMD("Material field is empty!", 0);
				else if (!BC.goodDate(fabricdate))
					BC.showMD("Format of Fabrication date is wrong!", 0);
				else if (!BC.filled(type))
					BC.showMD("Type field is empty", 0);
				else if (!BC.filled(w))
					BC.showMD("Width field is empty!", 0);
				else if (!BC.goodInt(w))
					BC.showMD("Width field contains wrong data!", 0);
				else if (!BC.filled(l))
					BC.showMD("Length field is empty!", 0);
				else if (!BC.goodInt(l))
					BC.showMD("Length field contains wrong data!", 0);
				else if (!BC.filled(h))
					BC.showMD("Height field is empty!", 0);
				else if (!BC.goodInt(h))
					BC.showMD("Height field contains wrong data!", 0);
				else {
					int[] tmp = new int[3];
					tmp[0] = Integer.parseInt(w.getText());
					tmp[1] = Integer.parseInt(l.getText());
					tmp[2] = Integer.parseInt(h.getText());
					data = new Furniture(Integer.parseInt(id.getText()), name.getText(), material.getText(),
							type.getText(), tmp, BC.StoD(fabricdate.getText()));
					BC.showMD("Data Inserted!", 1);
					exit = 1;
					dispose();
					setVisible(false);
				}
			}
		});
		btnInsrt.setBounds(42, 172, 89, 23);
		getContentPane().add(btnInsrt);

		JButton btnCls = new JButton("Close");
		btnCls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});
		btnCls.setBounds(198, 172, 89, 23);
		getContentPane().add(btnCls);

		l = new JTextField();
		l.setColumns(10);
		l.setBounds(190, 108, 34, 20);
		getContentPane().add(l);

		h = new JTextField();
		h.setColumns(10);
		h.setBounds(253, 108, 34, 20);
		getContentPane().add(h);

		JLabel lblH = new JLabel("H:");
		lblH.setBounds(236, 111, 23, 14);
		getContentPane().add(lblH);

		JLabel lblE = new JLabel("L:");
		lblE.setBounds(172, 110, 23, 14);
		getContentPane().add(lblE);

		JLabel lblW = new JLabel("W:");
		lblW.setBounds(109, 110, 23, 14);
		getContentPane().add(lblW);

		JLabel lblFabricationDate = new JLabel("Fabrication date:");
		lblFabricationDate.setBounds(10, 138, 105, 14);
		getContentPane().add(lblFabricationDate);

		fabricdate = new JTextField();
		fabricdate.setColumns(10);
		fabricdate.setBounds(107, 135, 105, 20);
		getContentPane().add(fabricdate);

		JLabel lblYyyymmdd = new JLabel("yyyy.mm.dd");
		lblYyyymmdd.setBounds(219, 138, 68, 14);
		getContentPane().add(lblYyyymmdd);

	}

	public Furniture getFurniture() {
		return data;
	}

	public int Quit() {
		return exit;
	}
}
