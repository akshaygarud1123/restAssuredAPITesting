package com.ApAUtomation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.FileNameConstant;
import com.ApiRestssuredListners.RestAssuredListners;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import apiAUtomationPOJO.Booking;
import apiAUtomationPOJO.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class DataDrivenTestingUsingCsvTest {

    @Test(dataProvider = "getTestData")
    public void DataDrivenTestingUsingCSV(Map<String, String> testData) {
        System.out.println("firstname: " + testData.get("firstname"));
       int totalprice= Integer.parseInt(testData.get("totalprice"));
        
        try {
			BookingDates bookinDates=new BookingDates("2018-01-25", "2018-01-30");
			Booking booking=new Booking(testData.get("firstname"),testData.get("lastname"), totalprice, "Breakfast", true,bookinDates);
			
			
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
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @DataProvider(name = "getTestData")
    public Object[][] getTestData() {
        Object[][] objArray = null;
        Map<String, String> map = null;
        List<Map<String, String>> testDataList = null;

        try (CSVReader csvReader = new CSVReader(new FileReader(FileNameConstant.CSV_API_BODY))) {

            testDataList = new ArrayList<Map<String, String>>();
            String[] line=null;
            int count=0;

            while ((line = csvReader.readNext()) != null) {
            	
            	if (count==0) {
            		count++;
            		continue;
            	}
                map = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
                map.put("firstname", line[0]);
                map.put("lastname", line[1]);
                map.put("totalprice", line[2]);
                testDataList.add(map);
            }

            objArray = new Object[testDataList.size()][1];
            for (int i = 0; i < testDataList.size(); i++) {
                objArray[i][0] = testDataList.get(i);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objArray;
    }
}