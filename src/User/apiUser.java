package User;


import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;


public class apiUser {

	private User user;
	
	@BeforeTest
    public void init() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }
	
	@Test(priority=1)
	public void createUser200()
	{
		user = new User();
		
		user.setId(20);
		user.setUsername("user20");
		user.setFirstName("John");
		user.setLastName("Smith");
		user.setEmail("johnsmith@gmail.com");
		user.setPassword("password");
		user.setPhone("123456789");
		user.setUserStatus(1);
		
		given()
        .header("Content-Type", "application/json")
        .body(user)
        .when()
        .post("/user")
        .then()
        .assertThat()
        .statusCode(200)
        .body("message", equalTo(Long.toString(user.getId())));
	}
	
	@Test(priority=2)
	public void logsOutUserSession200() 
	{	
		given()
		.when()
		.get("/user/logout")
		.then()
		.assertThat()
		.statusCode(200)
		.body("message", equalTo("ok"));
	}
	
	@Test(priority=3)
	public void logsUserIntoSystem200()
	{
		given()
		.queryParam("username", user.getUsername())
		.queryParam("password", user.getPassword())
		.when()
		.get("/user/login")
		.then()
		.assertThat()
		.statusCode(200);
	}
	
	@Test(priority=4)
	public void getUserByName200()
	{
		Response response = given()
				.when()
				.get("/user/" + user.getUsername())
				.then()
				.assertThat()
				.statusCode(200)
				.extract().response();
		
        JsonPath jsonPath = response.jsonPath();
        
        Assert.assertEquals(jsonPath.getInt("id"), user.getId());
        Assert.assertEquals(jsonPath.getString("username"), user.getUsername());
        Assert.assertEquals(jsonPath.getString("firstName"), user.getFirstName());
        Assert.assertEquals(jsonPath.getString("lastName"), user.getLastName());
      	Assert.assertEquals(jsonPath.getString("email"), user.getEmail());
      	Assert.assertEquals(jsonPath.getString("password"), user.getPassword());
      	Assert.assertEquals(jsonPath.getString("phone"), user.getPhone());
      	Assert.assertEquals(jsonPath.getInt("userStatus"), user.getUserStatus());
	}
	
	@Test(priority=5)
	public void getUserByName404()
	{
		given()
		.when()
		.get("/user/invalidUser")
		.then()
		.assertThat()
		.statusCode(404)
		.body("message", equalTo("User not found"));
	}
	
	@Test(priority=6)
	public void updateUser200()
	{
		user.setUsername("user2023");
		
		String requestBody = "{\r\n"
				+ "  \"id\": "+user.getId()+",\r\n"
				+ "  \"username\": \""+user.getUsername()+"\",\r\n"
				+ "  \"firstName\": \""+user.getFirstName()+"\",\r\n"
				+ "  \"lastName\": \""+user.getLastName()+"\",\r\n"
				+ "  \"email\": \""+user.getEmail()+"\",\r\n"
				+ "  \"password\": \""+user.getPassword()+"\",\r\n"
				+ "  \"phone\": \""+user.getPhone()+"\",\r\n"
				+ "  \"userStatus\": "+user.getUserStatus()+"\r\n"
				+ "}";
		
				given()
				.header("Content-Type", "application/json")
		        .body(requestBody)
				.when()
				.put("/user/" + user.getUsername())
				.then()
				.assertThat()
				.statusCode(200)
				.body("message", equalTo(Long.toString(user.getId())));
	}
	
	@Test(priority=7)
	public void updateUser404()
	{
		user.setUsername("user2023");
		
		String requestBody = "{\r\n"
				+ "  \"id\": "+user.getId()+",\r\n"
				+ "  \"username\": \""+user.getUsername()+"\",\r\n"
				+ "  \"firstName\": \""+user.getFirstName()+"\",\r\n"
				+ "  \"lastName\": \""+user.getLastName()+"\",\r\n"
				+ "  \"email\": \""+user.getEmail()+"\",\r\n"
				+ "  \"password\": \""+user.getPassword()+"\",\r\n"
				+ "  \"phone\": \""+user.getPhone()+"\",\r\n"
				+ "  \"userStatus\": "+user.getUserStatus()+"\r\n"
				+ "}";
		
				given()
				.header("Content-Type", "application/json")
		        .body(requestBody)
				.when()
				.put("/users/" + user.getUsername())
				.then()
				.assertThat()
				.statusCode(404);
	}
	
	@Test(priority=8)
	public void getUserByNameNew200()
	{
		Response response = given()
				.when()
				.get("/user/" + user.getUsername())
				.then()
				.assertThat()
				.statusCode(200)
				.extract().response();
		
        JsonPath jsonPath = response.jsonPath();
        
        Assert.assertEquals(jsonPath.getInt("id"), user.getId());
        Assert.assertEquals(jsonPath.getString("username"), user.getUsername());
        Assert.assertEquals(jsonPath.getString("firstName"), user.getFirstName());
        Assert.assertEquals(jsonPath.getString("lastName"), user.getLastName());
      	Assert.assertEquals(jsonPath.getString("email"), user.getEmail());
      	Assert.assertEquals(jsonPath.getString("password"), user.getPassword());
      	Assert.assertEquals(jsonPath.getString("phone"), user.getPhone());
      	Assert.assertEquals(jsonPath.getInt("userStatus"), user.getUserStatus());
	}
	
	@Test(priority=9)
	public void deleteUserByUsername200()
	{
		given()
		.when()
		.delete("/user/" + user.getUsername())
		.then()
		.assertThat()
		.statusCode(200)
		.body("message", equalTo(user.getUsername()));
	}
	
	@Test(priority=10)
	public void deleteUserByUsername404()
	{
		String invalidUsername = "user123456789";
		given()
		.when()
		.delete("/user/" + invalidUsername)
		.then()
		.assertThat()
		.statusCode(404);
	}
	
	@Test(priority=11)
	public void createListOfUsersWithGivenInputArray200()
	{
		String requestBody = "[{\r\n"
				+ "  \"id\": "+user.getId()+",\r\n"
				+ "  \"username\": \""+user.getUsername()+"\",\r\n"
				+ "  \"firstName\": \""+user.getFirstName()+"\",\r\n"
				+ "  \"lastName\": \""+user.getLastName()+"\",\r\n"
				+ "  \"email\": \""+user.getEmail()+"\",\r\n"
				+ "  \"password\": \""+user.getPassword()+"\",\r\n"
				+ "  \"phone\": \""+user.getPhone()+"\",\r\n"
				+ "  \"userStatus\": "+user.getUserStatus()+"\r\n"
				+ "}]";
		
		given()
		.header("Content-Type", "application/json")
		.body(requestBody)
		.when()
		.post("/user/createWithList")
		.then()
		.assertThat()
		.statusCode(200)
		.body("message", equalTo("ok"));
	}
	
}
