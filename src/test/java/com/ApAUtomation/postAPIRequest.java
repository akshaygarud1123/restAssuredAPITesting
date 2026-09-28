package com.ApAUtomation;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class postAPIRequest extends BaseTest{

	@Test
	public void createBooking() {
		//RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();//this method is gives and compare the actual and expected responce
		
	JSONObject booking = new JSONObject();
	JSONObject bookingDAtes = new JSONObject(); 
	
	booking.put("firstname", "Akshay");
	booking.put("lastname", "Garud");
	booking.put("totalprice", 1000);
	booking.put("depositpaid", true);
	booking.put("additionalneeds", "Ambat god");
	 
	booking.put("bookingdates", bookingDAtes);
	bookingDAtes.put("checkin", "2018-01-25");
	bookingDAtes.put("checkout", "2019-01-30");
	
	Response response=
	RestAssured
         .given()  
	         .contentType(ContentType.JSON)
	         .body(booking.toString())            //convert json to string y this method
	         .baseUri("https://restful-booker.herokuapp.com")
	         //.log().all()      //this method get the  which responce you send when it will take in given() method
	     .when()
	         .post("/booking")
	     .then()
	      
	         .assertThat()
	         .statusCode(200)
	         //.log().all() // this method means how will get reasponse from the server . when it will take in  then() 
	         //.log().ifValidationFails() //if any validation fails it will give
	         .body("booking.firstname",Matchers.equalTo("Akshay"))
	         .body("booking.lastname", Matchers.equalTo("Garud"))
	         .body("booking.bookingdates.checkin", Matchers.equalTo("2018-01-25")) 
	         .extract()
	         .response();
	          
	        int bookingid= response.path("bookingid");
	        
	  RestAssured 
	      .given()
	       .contentType(ContentType.JSON)
	       .pathParam("bookingid", bookingid)
	       .baseUri("https://restful-booker.herokuapp.com/booking")
	      
	      .when()
	       .get("{bookingid}")
	      
	      .then()
	       .assertThat()
	       .statusCode(200)
	       .body("firstname", Matchers.equalTo("Akshay"));
	       
	  
	  
	  
	  
	  
	  
	  
	   
	
	         
	         
	         
	
	
	}
}
