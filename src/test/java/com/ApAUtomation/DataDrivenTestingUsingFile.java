package com.ApAUtomation;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.FileNameConstant;
import com.ApiRestssuredListners.RestAssuredListners;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import apiAUtomationPOJO.Booking;
import apiAUtomationPOJO.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class DataDrivenTestingUsingFile {
	
	@Test (dataProvider = "getTestData")
	public void DataDrivenTestingUsingJson(LinkedHashMap <String,String> testdata ) throws JsonProcessingException {
		
		BookingDates bookinDates=new BookingDates("2018-01-25", "2018-01-30");
		Booking booking=new Booking(testdata.get("firstname"),testdata.get("lastname"), 1000, "Breakfast", true,bookinDates);
		
		
		//serelization(java class object into json)
		ObjectMapper objectMapper= new ObjectMapper();
			String requestBody=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
		//	Response responce=
			RestAssured
			        .given()
			          .filter(new RestAssuredListners())
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
			
			
		
	}
	
		
		@DataProvider(name="getTestData")
		 public Object[] getTestDataUsingJson() {
			 Object[] obj =null;
			 
			 try {
				String jsonTestData=FileUtils.readFileToString(new File(FileNameConstant.JSON_API_BODY),"UTF-8");
				JSONArray jsonArray=com.jayway.jsonpath.JsonPath.read(jsonTestData, "$");
				
				obj=new Object[jsonArray.size()];
				
				for(int i=0;i<jsonArray.size();i++) {
					
					obj[i]=jsonArray.get(i);
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return obj;
			
		}
		

}
