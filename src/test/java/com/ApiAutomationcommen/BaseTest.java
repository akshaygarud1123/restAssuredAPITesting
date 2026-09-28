package com.ApiAutomationcommen;

import java.io.PrintWriter;
import java.io.StringWriter;
//import java.util.logging.LogManager;
//import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.restassured.RestAssured;

public class BaseTest {
	
	private static final Logger logger=LogManager.getLogger(BaseTest.class);
	
	@BeforeMethod
	public void baseTest() {
		//this methods is give and compare the actual json responce and responce we validetd (expected responce)
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
		
		if(result.getStatus()==ITestResult.FAILURE) {
			Throwable t=result.getThrowable();
			
			StringWriter error= new StringWriter();
			t.printStackTrace(new PrintWriter(error));
			
		logger.info(error.toString());
			
					
		}
	}

}
