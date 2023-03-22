package Pet;

import java.util.List;

public class Pet {

	private long id;
	private Category category;
	private String name;
	private List<String> photoUrls;
	private List<Tag> tags;
	private Status status;
	private String InvalidId;
	
	public enum Status {
	    AVAILABLE("available"), PENDING("pending"), SOLD("sold");
	    
	    private String value;
	    
	    private Status(String value) {
	        this.value = value;
	    }
	    
	    public String getValue() {
	        return value;
	    }
	}
	
	public String getInvalidId() {
        return InvalidId;
    }

    public void setInvalidId(String InvalidId) {
        this.InvalidId = InvalidId;
    }
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPhotoUrls() {
		return photoUrls;
	}
	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public String getStatus() {
		return status.getValue();
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
