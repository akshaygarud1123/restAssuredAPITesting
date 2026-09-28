package com.ApAUtomation;

import java.io.File;
import java.io.IOException;
//import java.util.logging.LogManager;
//import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.BaseTest;
import com.ApiAutomationcommen.FileNameConstant;
import com.ApiRestssuredListners.RestAssuredListners;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


@Epic("Epic-01")
@Feature("create update delete booking")

//Epic, feature, story, Description ,Sevarity this all thing we get from test manegment tools like jera
public class AllureReportGeneration extends BaseTest {
	
	private static final Logger logger = LogManager.getLogger(AllureReportGeneration.class);

	@Story("story-01")
	@Test(description = "End to End API testing")
	@Description("End to End API testing")
	@Severity(SeverityLevel.CRITICAL)
	 public void patchcApiRequest() {
		logger.info("test is starting...........");

	        try {

	            String postAPiRequestBOdy = FileUtils.readFileToString(
	                    new File(FileNameConstant.POST_API_REQUEST_BODY),
	                    "UTF-8");

	            String tokenApiRequestBody = FileUtils.readFileToString(
	                    new File(FileNameConstant.TOKEN_API_REQUEST_BODY),
	                    "UTF-8");
	            
	           String putAPiRequestBOdy = FileUtils.readFileToString(
	                    new File(FileNameConstant.PUT_API_REQUEST_BODY),
	                    "UTF-8");
	           
	           String patchAPiRequestBOdy = FileUtils.readFileToString(
	                    new File(FileNameConstant.PATCH_API_REQUEST_BODY),
	                    "UTF-8");
	           // System.out.println(postAPiRequestBOdy);
	         
	            //post api request
	            Response responce=
	            RestAssured
	                  .given()
	                   .filter(new AllureRestAssured())
	                   .filter(new RestAssuredListners())
	                   .contentType(ContentType.JSON)
	                   .body(postAPiRequestBOdy)
	                   .baseUri("https://restful-booker.herokuapp.com/booking")
	                   
	                  .when()
	                   .post()
	                   
	                  .then()
	                   .assertThat()
	                   .statusCode(200)
	                  .extract()
	                   .response();
	            
	            JsonPath jsonpath=responce.jsonPath();
	            String firstname=jsonpath.getString("booking.firstname");
	            String lastname=jsonpath.getString("booking.lastname");
	      //     String checkin= jsonpath.getString("booking.bookingdates.checkin");
	             System.out.println(firstname);
	             System.out.println(lastname);
	      //      System.out.println(checkin);
	  //     JSONArray jsonarray=  com.jayway.jsonpath.JsonPath.read(responce.body().asString(), "$.booking.firstname");
	                 // System.out.println(jsonarray.get(0));
	            //String firstname=(String) jsonarray.get(0);
	            
	            Assert.assertEquals(firstname, "Rohan");
	           Assert.assertEquals(lastname, "Garud");
	        //.assertEquals(checkin, "2018-01-01");
	          
	            //find the booking id for the get() method
	            int bookingid=jsonpath.getInt("bookingid");
	            
	            System.out.println(bookingid);
	            
	   //now i creating the get request by using post booking id
	            
	            //get api request
	            RestAssured
	                  .given()
	                   .filter(new AllureRestAssured())
	                   .filter(new RestAssuredListners())
	                   .contentType(ContentType.JSON)
	                   .baseUri("https://restful-booker.herokuapp.com/booking")
	                  
	                 .when()
	                   .get("/{boookingid}",bookingid)//we use path param method also in given () 
	                  
	                  .then()
	                   .assertThat()
	                   .statusCode(200);
	              
	            //token genararation
	             Response tokenapiresponce=
	            RestAssured
	                  .given()
	                   .contentType(ContentType.JSON)
	                   .body(tokenApiRequestBody)
	                   .baseUri("https://restful-booker.herokuapp.com/auth")
	                   
	                  .when()
	                   .post()
	                   
	                  .then()
	                   .assertThat()
	                  // .statusCode(200)
	            
	                  .extract()
	                   .response();
	             
	        //    JsonPath tokanjsonpath= tokenapiresponce.jsonPath();
	            
	        //   String token=tokanjsonpath.getString("token");
	             
	          //   System.out.println(token);
	            System.out.println(tokenapiresponce.body().asString()); 
	            String token = tokenapiresponce.jsonPath().getString("token");
	            System.out.println(token);
	             
	             //put api request
	             RestAssured
	                 .given()
	                   .filter(new AllureRestAssured())
	                   .contentType(ContentType.JSON)
	                   .body(putAPiRequestBOdy)
	                   .header("cookie","token="+token)
	                   .baseUri("https://restful-booker.herokuapp.com/booking")
	                  
	                  .when()
	                   .put("/{bookingid}",bookingid)
	                   
	                  .then()
	                   .assertThat()
	                   .statusCode(200)
	                   .body("firstname", Matchers.equalTo("Specflow"))
	                   .body("lastname", Matchers.equalTo("Selenium C#"));
	             
	        //patch API request
	             
	             RestAssured
	                   .given()
	                    .filter(new AllureRestAssured())
	                    .filter(new RestAssuredListners())
	                    .contentType(ContentType.JSON)
	                    .body(patchAPiRequestBOdy)
	                    .header("cookie", "token="+token)
	                    .baseUri("https://restful-booker.herokuapp.com/booking")
	                    
	                   .when()
	                    .patch("{bookingid}",bookingid)
	                    
	                   .then()
	                    .assertThat()
	                    .statusCode(200)
	                    .body("firstname", Matchers.equalTo("Akshay"));
	             
	           //delete API request
	             
	              RestAssured 
	                   .given()
	                    .contentType(ContentType.JSON)
	                    .header("cookie","token="+token)
	                    .baseUri("https://restful-booker.herokuapp.com/booking")
	                    
	                   .when()
	                    .delete("/{bookingid}",bookingid)
	                    
	                   .then()
	                    .assertThat()
	                    .statusCode(201);
	             
	             
	             
	             

	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        logger.info("test is ending.....");
	    }
}
