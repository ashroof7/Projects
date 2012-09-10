
public class Record {
	
	private String name ;//32 bit
	private String type;// 7 bit
	private String address;//32 bit
	private String scope;//32 bit
	private int length;
	
	public Record(String name,String type,String address,String scope,int length) {
		this.name = name ;
		this.type = type;
		this.address = address;
		this.scope = scope;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	
	
	
}
