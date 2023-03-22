package Pet;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;


public class apiPet {
    private Category category;
    private Tag tags;
    private Pet pet;
    
    
    @BeforeTest
    public void init() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test(priority=1)
    public void addNewPet200() 
    {
    	
    	category = new Category();
        category.setId(1717);
        category.setName("Bulldog");

        tags = new Tag();
        tags.setId(1717);
        tags.setName("Show");

        pet = new Pet();
        pet.setId(1717);
        pet.setCategory(category);
        pet.setName("Max");
        pet.setPhotoUrls(Arrays.asList("C:\\Users\\mikis\\Desktop\\bulldog.jpg")); //nalezy podac wlasna sciezke do pliku
        pet.setTags(Arrays.asList(tags));
        pet.setStatus(Pet.Status.AVAILABLE);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.getInt("id"), pet.getId());
        Assert.assertEquals(jsonPath.getString("category.id"), String.valueOf(category.getId()));
        Assert.assertEquals(jsonPath.getString("category.name"), category.getName());
        Assert.assertEquals(jsonPath.getString("name"), pet.getName());
        Assert.assertEquals(jsonPath.getList("photoUrls"), pet.getPhotoUrls());
        Assert.assertEquals(jsonPath.getInt("tags[0].id"), tags.getId());
        Assert.assertEquals(jsonPath.getString("tags[0].name"), tags.getName());
        Assert.assertEquals(jsonPath.getString("status"), pet.getStatus());
    }
	
   
	
	@Test(priority=2)
	public void addNewPet405()
	{
		category = new Category();
        category.setId(1717);
        category.setName("Bulldog");

        tags = new Tag();
        tags.setId(1717);
        tags.setName("Show");

        pet = new Pet();
        pet.setId(1717);
        pet.setCategory(category);
        pet.setName("Max");
        pet.setPhotoUrls(Arrays.asList("C:\\Users\\mikis\\Desktop\\bulldog.jpg"));
        pet.setTags(Arrays.asList(tags));
        pet.setStatus(Pet.Status.AVAILABLE);
        
		given()
		.header("Content-Type", "application/json")
		.body(pet)
		.when()
		.get("/pet")
		.then()
		.assertThat()
		.statusCode(405);
	}
	
	@Test(priority=3)
	public void uploadImage200() {
	    File file = new File(pet.getPhotoUrls().get(0));
	    String additionalMetadata = "metadata test";

	    Response response = RestAssured.given()
		            .headers("Content-Type", "multipart/form-data", "Accept", "application/json")
		            .multiPart("petId", pet.getId())
		            .multiPart("additionalMetadata", additionalMetadata)
		            .multiPart("file", file)
		            .when()
		            .post("/pet/"+pet.getId()+"/uploadImage")
		            .then()
	        		.assertThat()
	        		.statusCode(200)
	        		.extract().response();
	    
	    JsonPath jsonPath = response.jsonPath();
	    Assert.assertEquals(jsonPath.getString("message"), String.format("additionalMetadata: %s\nFile uploaded to ./%s, %d bytes", additionalMetadata, file.getName(), file.length()));
	}
	
	
	@Test(priority = 4)
	public void updateExistingPet200() {
	    pet.setName("Doggie");
	    String photoUrl = pet.getPhotoUrls().get(0).replace("\\", "\\\\");

	    String jsonBody = "{\n"
	            + "  \"id\": " + pet.getId() + ",\n"
	            + "  \"category\": {\n"
	            + "    \"id\": " + category.getId() + ",\n"
	            + "    \"name\": \"" + category.getName() + "\"\n"
	            + "  },\n"
	            + "  \"name\": \"" + pet.getName() + "\",\n"
	            + "  \"photoUrls\": [\n"
	            + "    \"" + photoUrl + "\"\n"
	            + "  ],\n"
	            + "  \"tags\": [\n"
	            + "    {\n"
	            + "      \"id\": " + tags.getId() + ",\n"
	            + "      \"name\": \"" + tags.getName() + "\"\n"
	            + "    }\n"
	            + "  ],\n"
	            + "  \"status\": \"" + pet.getStatus() + "\"\n"
	            + "}";

	    given()
        .header("Content-Type", "application/json")
        .body(jsonBody)
        .put("/pet")
        .then()
        .assertThat()
        .statusCode(200)
        .body("name", equalTo(pet.getName()));
	}
	
	
	@Test(priority=5)
	public void updateExistingPet400()
	{
		pet.setName("Doggie");
		String invalidId = "test";
		String photoUrl = pet.getPhotoUrls().get(0).replace("\\", "\\\\");
		String jsonBody = "{\n"
	            + "  \"id\": " + invalidId + ",\n"
	            + "  \"category\": {\n"
	            + "    \"id\": " + category.getId() + ",\n"
	            + "    \"name\": \"" + category.getName() + "\"\n"
	            + "  },\n"
	            + "  \"name\": \"" + pet.getName() + "\",\n"
	            + "  \"photoUrls\": [\n"
	            + "    \"" + photoUrl + "\"\n"
	            + "  ],\n"
	            + "  \"tags\": [\n"
	            + "    {\n"
	            + "      \"id\": " + tags.getId() + ",\n"
	            + "      \"name\": \"" + tags.getName() + "\"\n"
	            + "    }\n"
	            + "  ],\n"
	            + "  \"status\": \"" + pet.getStatus() + "\"\n"
	            + "}";

	    given()
        .header("Content-Type", "application/json")
        .body(jsonBody)
		.when()
		.put("/pet")
		.then()
		.assertThat()
		.statusCode(400)
		.body("message", equalTo("bad input"));
	}
	
	@Test(priority = 4)
	public void updateExistingPet404() {
		pet.setName("Doggie");
	    String photoUrl = pet.getPhotoUrls().get(0).replace("\\", "\\\\");

	    String jsonBody = "{\n"
	            + "  \"id\": " + pet.getId() + ",\n"
	            + "  \"category\": {\n"
	            + "    \"id\": " + category.getId() + ",\n"
	            + "    \"name\": \"" + category.getName() + "\"\n"
	            + "  },\n"
	            + "  \"name\": \"" + pet.getName() + "\",\n"
	            + "  \"photoUrls\": [\n"
	            + "    \"" + photoUrl + "\"\n"
	            + "  ],\n"
	            + "  \"tags\": [\n"
	            + "    {\n"
	            + "      \"id\": " + tags.getId() + ",\n"
	            + "      \"name\": \"" + tags.getName() + "\"\n"
	            + "    }\n"
	            + "  ],\n"
	            + "  \"status\": \"" + pet.getStatus() + "\"\n"
	            + "}";

	    given()
        .header("Content-Type", "application/json")
        .when()
        .body(jsonBody)
        .put("/pets")
        .then()
        .assertThat()
        .statusCode(404);

	}
	
	@Test(priority=7)
	public void updateExistingPet405()
	{
		pet.setName("Doggie");
	    String photoUrl = pet.getPhotoUrls().get(0).replace("\\", "\\\\");

	    String jsonBody = "{\n"
	            + "  \"id\": " + pet.getId() + ",\n"
	            + "  \"category\": {\n"
	            + "    \"id\": " + category.getId() + ",\n"
	            + "    \"name\": \"" + category.getName() + "\"\n"
	            + "  },\n"
	            + "  \"name\": \"" + pet.getName() + "\",\n"
	            + "  \"photoUrls\": [\n"
	            + "    \"" + photoUrl + "\"\n"
	            + "  ],\n"
	            + "  \"tags\": [\n"
	            + "    {\n"
	            + "      \"id\": " + tags.getId() + ",\n"
	            + "      \"name\": \"" + tags.getName() + "\"\n"
	            + "    }\n"
	            + "  ],\n"
	            + "  \"status\": \"" + pet.getStatus() + "\"\n"
	            + "}";
	    
	    given()
        .header("Content-Type", "application/json")
        .when()
        .body(jsonBody)
		.get("/pet")
		.then()
		.assertThat()
		.statusCode(405);
	}
	
	
	@Test(priority=8)
	public void findsPetsByStatus200()
	{	
		given()
        .queryParam("status", pet.getStatus())
        .when()
        .get("/pet/findByStatus")
        .then()
        .assertThat()
        .statusCode(200);
	}
	
	@Test(priority=8)
	public void findsPetsByStatus405()
	{	
		given()
        .queryParam("status", pet.getStatus())
        .when()
        .post("/pet/findByStatus")
        .then()
        .assertThat()
        .statusCode(405);
	}
	
	
	@Test(priority=9)
	public void findPetsByID200()
	{
		Response response = given()
        .when()
        .get("/pet/" + pet.getId())
        .then()
        .assertThat()
        .statusCode(200)
        .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.getInt("id"), pet.getId());
        Assert.assertEquals(jsonPath.getString("name"), pet.getName());
        Assert.assertEquals(jsonPath.getString("status"), pet.getStatus());
        
	}
	
	@Test(priority=10)
	public void findPetsByID_petNotFound404()
	{	
		String invalidId = "invalidId";
		given()
        .when()
        .get("/pet/" + invalidId)
        .then()
        .assertThat()
        .statusCode(404);
	}
	
	
	@Test(priority=11)
	public void updatePetFromData200()
	{
		pet.setName("Bruno");
		pet.setStatus(Pet.Status.PENDING);
		
		given()
		.when()
		.contentType(ContentType.URLENC)
        .formParam("name", pet.getName())
        .formParam("status", pet.getStatus())
        .post("/pet/" + pet.getId())
		.then()
        .assertThat()
        .statusCode(200)
        .body("message", equalTo(Long.toString(pet.getId())));
	}
	
	

	@Test(priority = 12)
    public void checkUpdatedPetFromData200() {

        Response response = RestAssured.given()
                .when()
                .get("/pet/" + pet.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.getString("name"), pet.getName());
        Assert.assertEquals(jsonPath.getString("status"), pet.getStatus());
    }
	
	@Test(priority=13)
	public void updatePetFromData404()
	{
		int invalidId = 123456789;
		given()
		.when()
		.contentType(ContentType.URLENC)
        .formParam("name", pet.getName())
        .formParam("status", pet.getStatus())
        .post("/pet/" + invalidId)
		.then()
        .assertThat()
        .statusCode(404)
        .body("message", equalTo("not found"));
	}
	
	@Test(priority=11)
	public void updatePetFromData405()
	{	
		given()
		.when()
		.contentType(ContentType.URLENC)
        .formParam("name", pet.getName())
        .formParam("status", pet.getStatus())
        .put("/pet/" + pet.getId())
		.then()
        .assertThat()
        .statusCode(405);
	}
	
	@Test(priority=15)
	public void deletePet200()
	{
		given()
		.when()
		.header("api_key", "special-key")
        .delete("/pet/" + pet.getId())
		.then()
        .assertThat()
        .statusCode(200)
        .body("message", equalTo(Long.toString(pet.getId())));
	}
	
	@Test(priority = 16)
    public void checkIfPetIsDeleted404() 
	{	
        given()
        .when()
        .get("/pet/" + pet.getId())
		.then()
        .assertThat()
        .statusCode(404)
        .body("message", equalTo("Pet not found"));

    }
}
