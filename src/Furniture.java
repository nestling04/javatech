import java.util.Date;

public class Furniture {
	private int id;
	private String name;
	private String material;
	private String type;
	private int[] size;
	private Date fabricdate;

	public Furniture(int id, String name, String material, String type, int[] size, Date fabricdate) {
		this.id = id;
		this.name = name;
		this.material = material;
		this.type = type;
		this.size = size.clone();
		this.fabricdate = fabricdate;
	}

	public Furniture() {
		this.id = 0;
		this.name = null;
		this.material = null;
		this.type = null;
		this.size = null;
		this.fabricdate = null;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSize(int[] size) {
		this.size = size;
	}

	public void setFabricdate(Date fabricdate) {
		this.fabricdate = fabricdate;
	}

	public Date getFabricdate() {
		return fabricdate;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMaterial() {
		return material;
	}

	public String getType() {
		return type;
	}

	public int[] getSize() {
		return size;
	}
}
