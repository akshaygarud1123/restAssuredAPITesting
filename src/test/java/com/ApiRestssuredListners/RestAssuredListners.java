package com.ApiRestssuredListners;



import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestAssuredListners implements io.restassured.filter.Filter{
	
	private static final Logger logger =LogManager.getLogger(RestAssuredListners.class);

	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
	
		
	Response responce=ctx.next(requestSpec, responseSpec);
	
	//if(responce.getStatusCode()!=200 & responce.getStatusCode()!=201) {
		
		logger.info("\n Method=>"+requestSpec.getMethod()+
		"\n URI=>"+requestSpec.getURI()+
		"\n Request body=>"+requestSpec.getBody()+
		"\n Responce body=>"+responce.getBody().prettyPrint()
		 
				);
		
	//}
		return responce;
	}
	
	

}
