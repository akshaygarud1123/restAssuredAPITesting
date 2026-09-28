package com.ApAUtomation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ApiAutomationcommen.FileNameConstant;
import com.ApiRestssuredListners.RestAssuredListners;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset; 
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import apiAUtomationPOJO.Booking;
import apiAUtomationPOJO.BookingDates;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class dataDrivenTestingUsingexel {

    @Test(dataProvider = "ExelTestData")
    public void dataDrivnTesting(Map<String, String> testData) {

        System.out.println(testData.get("firstname"));

        int totalprice = Integer.parseInt(testData.get("totalprice"));

        try {

            BookingDates bookinDates =
                    new BookingDates("2018-01-25", "2018-01-30");

            Booking booking =
                    new Booking(
                            testData.get("firstname"),
                            testData.get("lastname"),
                            totalprice,
                            "Breakfast",
                            true,
                            bookinDates
                    );

            // Serialization (Java object into JSON)
            ObjectMapper objectMapper = new ObjectMapper();

            String requestBody =
                    objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(booking);

            System.out.println("Request Body:");
            System.out.println(requestBody);

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

            e.printStackTrace();
        }
    }

    @DataProvider(name = "ExelTestData")
    public Object[][] getTestData() {

        String query = "SELECT * FROM Sheet1 WHERE Run='yes'";

        Object[][] objArray = null;

        Map<String, String> testdata = null;

        List<Map<String, String>> testDataList = null;

        Fillo fillo = new Fillo();

        com.codoid.products.fillo.Connection connection = null;

        Recordset recordset = null;

        try {

            // ============================================
            // DEBUG: CHECK EXCEL FILE PATH
            // ============================================

            System.out.println("=================================");
            System.out.println(
                    "Excel Path = "
                    + FileNameConstant.EXEL_API_BODY
            );

            java.io.File file =
                    new java.io.File(
                            FileNameConstant.EXEL_API_BODY
                    );

            System.out.println(
                    "Absolute Path = "
                    + file.getAbsolutePath()
            );

            System.out.println(
                    "File Exists = "
                    + file.exists()
            );

            System.out.println(
                    "Is File = "
                    + file.isFile()
            );

            System.out.println("=================================");

            // ============================================
            // CONNECT TO EXCEL
            // ============================================

            connection =
                    fillo.getConnection(
                            FileNameConstant.EXEL_API_BODY
                    );

            recordset =
                    connection.executeQuery(query);

            testDataList =
                    new ArrayList<Map<String, String>>();

            while (recordset.next()) {

                testdata =
                        new TreeMap<String, String>(
                                String.CASE_INSENSITIVE_ORDER
                        );

                for (String field : recordset.getFieldNames()) {

                    testdata.put(
                            field,
                            recordset.getField(field)
                    );
                }

                testDataList.add(testdata);
            }

            System.out.println(
                    "Records found = "
                    + testDataList.size()
            );

            objArray =
                    new Object[testDataList.size()][1];

            for (int i = 0;
                            i < testDataList.size();
                 i++) {

                objArray[i][0] =
                        testDataList.get(i);
            }

        } catch (FilloException e) {

            e.printStackTrace();
        }

        return objArray;
    }
}