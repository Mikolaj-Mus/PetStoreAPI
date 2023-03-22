package Store;

import java.time.LocalDateTime;

public class Store {
	
	private long id;
	private long petId;
	private int quantity;
	private String shipDate = LocalDateTime.now().toString();
	private Boolean complete;
	private Status status;
	
	public enum Status {
	    PLACED("placed"), APPROVED("approved"), DELIVERED("delivered");
	    
	    private String value;
	    
	    private Status(String value) {
	        this.value = value;
	    }
	    
	    public String getValue() {
	        return value;
	    }
	}
	
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPetId() {
		return petId;
	}
	public void setPetId(long petId) {
		this.petId = petId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Boolean getComplete() {
		return complete;
	}
	public void setComplete(Boolean complete) {
		this.complete = complete;
	}
	
	public String getStatus() {
		return status.getValue();
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
}
