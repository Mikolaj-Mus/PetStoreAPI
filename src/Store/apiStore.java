package Store;


import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class apiStore {

	private Store store;
	
	@BeforeTest
    public void init() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }
	
	@Test(priority=1)
	public void placeOrderForPetAndCheckData200()
	{
		store = new Store();
		
		store.setId(11);
		store.setPetId(5);
		store.setQuantity(1);
		store.setStatus(Store.Status.PLACED);;
		store.setComplete(true);
		
		Response response = given()
                .header("Content-Type", "application/json")
                .body(store)
                .when()
                .post("/store/order").then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        Assert.assertEquals(jsonPath.getInt("id"), store.getId());
        Assert.assertEquals(jsonPath.getInt("petId"), store.getPetId());
        Assert.assertEquals(jsonPath.getInt("quantity"), store.getQuantity());
        Assert.assertEquals(jsonPath.getString("status"), store.getStatus());
        Assert.assertEquals(jsonPath.getBoolean("complete"), store.getComplete());
	}
	
	@Test(priority=2)
	public void placeNewOrderForPet405()
	{	
		String invalidId = "invalidId";
		String jsonBody = "{\r\n"
        		+ "  \"id\": "+ invalidId +",\r\n"
        		+ "  \"petId\": "+store.getPetId()+",\r\n"
        		+ "  \"quantity\": "+store.getQuantity()+",\r\n"
        		+ "  \"shipDate\": \"" + store.getShipDate().toString()+ "\",\r\n"
        		+ "  \"status\": \""+store.getStatus()+"\",\r\n"
        		+ "  \"complete\": "+store.getComplete()+"\r\n"
        		+ "}";
		given()
		.header("Content-Type", "application/json")
		.body(jsonBody)
		.when()
		.post("/store/order")
		.then()
		.assertThat()
		.statusCode(400)
		.body("message", equalTo("bad input"));
	}
	
	@Test(priority=3)
	public void findOrderById200()
	{
		given()
	    .when()
	    .get("/store/order/" + store.getId())
	    .then()
	    .assertThat()
	    .statusCode(200)
	    .body("id", equalTo((int) store.getId())); // rzutowanie na typ int
	}
	
	@Test(priority=4)
	public void findOrderById404()
	{
		int invalidId = 123456789;
		given()
        .when()
        .get("/store/order/" + invalidId)
		.then()
		.assertThat()
        .statusCode(404)
        .body("message", equalTo("Order not found"));

	}
	
	@Test(priority=5)
	public void deleteOrderById200()
	{
		given()
		.when()
		.delete("/store/order/" + store.getId())
		.then()
		.assertThat()
		.statusCode(200)
		.body("message", equalTo(Long.toString(store.getId())));
	}
	
	@Test(priority=6)
	public void deleteOrderById404()
	{
		int invalidId = 123456789;
		given()
		.when()
		.delete("/store/order/" + invalidId)
		.then()
		.assertThat()
		.statusCode(404)
		.body("message", equalTo("Order Not Found"));

	}
	
	@Test(priority=7)
    public void checkIfOrderIsDeleted404() 
	{	
        given()
        .when()
        .get("/store/order/" + store.getId())
		.then()
        .assertThat()
        .statusCode(404)
        .body("message", equalTo("Order not found"));

    }
	
	@Test(priority=8)
	public void returnsPetInventoryByStatus200()
	{
		given()
		.when()
		.get("/store/inventory")
		.then()
		.assertThat()
		.statusCode(200);
	}
	
}