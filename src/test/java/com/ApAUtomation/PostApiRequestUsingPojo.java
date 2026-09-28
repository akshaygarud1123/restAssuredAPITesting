package com.ApAUtomation;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.FileNameConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import apiAUtomationPOJO.Booking;
import apiAUtomationPOJO.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PostApiRequestUsingPojo {
	
	@Test
	public void postAPIRequest() throws IOException  {
		
		String jsonsceama=FileUtils.readFileToString(new File(FileNameConstant.JSON_SCEAMA_VALIDATION),"UTF-8");
	
	BookingDates bookinDates=new BookingDates("2018-01-25", "2018-01-30");
 
	
	Booking booking=new Booking("Rohan", "Garud", 1000, "Breakfast", true,bookinDates);
	//booking class content the pojo object
	
	//serelization(java class object into json)
	ObjectMapper objectMapper= new ObjectMapper();
	try {
		String requestBody=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
		//requwst body content the json object
		System.out.println(requestBody);
		
	//deserelization(json object into the java class object)
		Booking bookingdetails=objectMapper.readValue(requestBody, Booking.class);
		System.out.println(bookingdetails.getFirstname());
		System.out.println(bookingdetails.getLastname());
		System.out.println(bookingdetails.getTotalprice());
		System.out.println(bookingdetails.getAdditionalneeds());

		System.out.println(bookingdetails.getBookingdates().getCheckin());
		System.out.println(bookingdetails.getBookingdates().getCheckout());
		
		Response responce=
		RestAssured
		        .given()
		          .contentType(ContentType.JSON)
		          .body(requestBody)
		          .baseUri("https://restful-booker.herokuapp.com/booking")
		        
		        .when()
		          .post()
		          
		        .then()
		          .assertThat()
		          .statusCode(200)
		          
		        .extract()
		        .response();
		        
		       int bookingId= responce.path("bookingid");
		       
		    //   System.out.println(jsonsceama);
		       
		RestAssured
		        .given()
		         .contentType(ContentType.JSON)
		   //      .pathParam("bookingid",bookingId)
		         .baseUri("https://restful-booker.herokuapp.com/booking")
		         
		        .when()
		         .get("/{bookingId}",bookingId)
		         
		        .then()
		         .assertThat()
		         .statusCode(200)
		         .body(JsonSchemaValidator.matchesJsonSchema(jsonsceama));
		         
		
		
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	

}
