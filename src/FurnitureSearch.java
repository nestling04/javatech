
public class FurnitureSearch {
	public static boolean KeyCheck(String field, String key) {
		boolean vi = false;
		String fs = "";
		if (field.equals("ID")) {
			if (key.substring(0, 1).equals("="))
				key = key.substring(1, key.length());
			vi = BC.goodStoInt(key);
		} else {
			fs = key.substring(0, 1);
			if (Character.isLetter(key.charAt(0)) || fs.equals(" "))
				vi = true;
		}
		return vi;
	}

	public static FurnitureTM Select(FurnitureTM ftm, String field, String key) {
		Object FurnitureTMn[] = { "Sign", "ID", "Name", "Material", "Type", "Width", "Length", "Width",
				"Fabrication date" };
		FurnitureTM srchtm = new FurnitureTM(FurnitureTMn, 0);
		if (key.substring(0, 1).equals("="))
			key = key.substring(1, key.length());
		for (int i = 0; i < ftm.getRowCount(); i++) {
			if (field.equals("ID") && key.equals(ftm.getValueAt(i, 1).toString()))
				Pack(ftm, srchtm, i);
			if (field.equals("name") && ftm.getValueAt(i, 2).toString().contains(key))
				Pack(ftm, srchtm, i);
			if (field.equals("material") && ftm.getValueAt(i, 3).toString().contains(key))
				Pack(ftm, srchtm, i);
			if (field.equals("type") && ftm.getValueAt(i, 4).toString().contains(key))
				Pack(ftm, srchtm, i);
		}
		return srchtm;
	}

	public static void Pack(FurnitureTM ftm, FurnitureTM srchtm, int row) {
		srchtm.addRow(new Object[] { new Boolean(false), BC.StoI(ftm.getValueAt(row, 1).toString()),
				ftm.getValueAt(row, 2).toString(), ftm.getValueAt(row, 3).toString(), ftm.getValueAt(row, 4).toString(),
				BC.StoI(ftm.getValueAt(row, 5).toString()), BC.StoI(ftm.getValueAt(row, 6).toString()),
				BC.StoI(ftm.getValueAt(row, 7).toString()), ftm.getValueAt(row, 8).toString() });
	}

}
