package com.systemone.API.EndpointTesting;

import static io.restassured.RestAssured.expect;
import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.systemone.utils.UITestSeleniumHelper;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIPortalTests {
	
	UITestSeleniumHelper helper = new UITestSeleniumHelper();
	
	private static final String AUTHENTICATE_API = "https://restful-booker.herokuapp.com/auth";
	private static final String BASE_API = "https://restful-booker.herokuapp.com/booking";
	private String userName = "admin";
	private String password = "password123";
	private int expectedGetStatusCode = 200;
	private int expectedDeleteStatusCode = 201;
	private String token;
	private int building_ID;
	
	Logger logger = LoggerFactory.getLogger(APIPortalTests.class);
	
	@Test(enabled = true, priority = 1)
	public void authenticate() {
		logger.info("To authenticate " + new Throwable().getStackTrace()[0].getMethodName());
		
		Response res = expect().given().contentType("application/json").body(new JSONObject().put("username", userName).put
				("password", password).toString()).log().everything().when().post(AUTHENTICATE_API);
		
		logger.info("Validate status code");
		assertEquals(res.statusCode(), expectedGetStatusCode);
		JsonPath json = new JsonPath(res.asString());
		
		logger.info("To get the authentication token");
		token = json.get("token");
		logger.info("token : " + token);
	}
	
	@Test(enabled = true, dependsOnMethods = {"authenticate"})
	public void getBookingIds(ITestContext ctx) {
		logger.info("To get the booking Ids " + new Throwable().getStackTrace()[0].getMethodName());
		
		Response res = expect().given().log().everything().when().get(BASE_API);
		
		ctx.setAttribute("Response", res);
		
		logger.info("Validate status code");
		System.out.println(res.asString());
		assertEquals(res.statusCode(), expectedGetStatusCode);
	}
	
	@Test(enabled = true, dependsOnMethods = {"getBookingIds"})
	public void getBooking(ITestContext ctx) {
		logger.info("To get a specific booking based upon the booking id " + new Throwable().getStackTrace()[0].getMethodName());
		
		Response response = (Response) ctx.getAttribute("Response");
		
		logger.info("To get the building id");
		JsonPath jsonPath = response.jsonPath();
		building_ID = jsonPath.get("bookingid[0]");
		System.out.println("building_ID : " + building_ID);
		
		response = expect().given().log().everything().when().get(BASE_API + "/" + building_ID);
		response.then().log().all();
		
		logger.info("Validate status code");
		assertEquals(response.statusCode(), expectedGetStatusCode);
	}
	
	@Test(enabled = true, dependsOnMethods = {"authenticate"}, priority = 3)
	public void createBooking() {
		logger.info("To Create a new booking " + new Throwable().getStackTrace()[0].getMethodName());
		
		Response res = expect().given().contentType("application/json").accept("application/json").body(new JSONObject().put("firstname", "Danish")
				.put("lastname", "Kaushal").put("totalprice", 1000).put("depositpaid", true).put("bookingdates", new JSONObject()
				.put("checkin", helper.date()).put("checkout", helper.date())).put("additionalneeds", "Brunch").toString())
				.log().everything().when().post(BASE_API);

		int statusCode = res.statusCode();
		System.out.println("statusCode : " + statusCode);
		System.out.println(res.asString());
		
		logger.info("Validate status code");
		assertEquals(statusCode, expectedGetStatusCode);
		
		logger.info("To get the building id");
		JsonPath jsonPath = res.jsonPath();
		building_ID = jsonPath.get("bookingid");
		System.out.println("building_ID : " + building_ID);
	}
	
	@Test(enabled = true, dependsOnMethods = {"createBooking"})
	public void deleteBooking() {
		logger.info("To delete the above created booking " + new Throwable().getStackTrace()[0].getMethodName());
		
		Response res = expect().given().contentType("application/json").cookie("token", token).log().everything().when()
				.delete(BASE_API + "/" + building_ID);
		
		logger.info("Validate status code");
		assertEquals(res.statusCode(), expectedDeleteStatusCode);
		
		assertEquals(res.asString(), "Created");
	}
}
