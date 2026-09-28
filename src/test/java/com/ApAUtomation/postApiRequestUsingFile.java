package com.ApAUtomation;import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.BaseTest;
import com.ApiAutomationcommen.FileNameConstant;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class postApiRequestUsingFile extends BaseTest{

    @Test
    public void postApiRequest() {

        try {

            String postAPiRequestBOdy = FileUtils.readFileToString(
                    new File(FileNameConstant.POST_API_REQUEST_BODY),
                    "UTF-8");

           // System.out.println(postAPiRequestBOdy);
            
            Response responce=
            RestAssured
                  .given()
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
           String checkin= jsonpath.getString("booking.bookingdates.checkin");
           System.out.println(firstname);
           System.out.println(lastname);
            System.out.println(checkin);
  //     JSONArray jsonarray=  com.jayway.jsonpath.JsonPath.read(responce.body().asString(), "$.booking.firstname");
                 // System.out.println(jsonarray.get(0));
            //String firstname=(String) jsonarray.get(0);
            
            Assert.assertEquals(firstname, "Rohan");
            Assert.assertEquals(lastname, "Garud");
            Assert.assertEquals(checkin, "2018-01-01");
          
            //find the booking id for the get() method
            int bookingid=jsonpath.getInt("bookingid");
            System.out.println(bookingid);
            
   //now i creating the get request by using post booking id
            
            RestAssured
                  .given()
                   .contentType(ContentType.JSON)
                   .baseUri("https://restful-booker.herokuapp.com/booking")
                  
                 .when()
                   .get("/{boookingid}",bookingid)//we use path param method also in given () 
                  
                  .then()
                   .assertThat()
                   .statusCode(200);
                  
                
           

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}