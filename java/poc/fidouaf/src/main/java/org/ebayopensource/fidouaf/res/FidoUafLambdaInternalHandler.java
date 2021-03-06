package org.ebayopensource.fidouaf.res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;
import org.ebayopensource.fidouaf.res.util.NotaryImpl;
import org.ebayopensource.fidouaf.res.util.StorageImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.Base64;

public class FidoUafLambdaInternalHandler implements RequestStreamHandler{
	// Initialize the Log4j logger.
	static final Logger logger = LogManager.getLogger(FidoUafLambdaInternalHandler.class);
	protected Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private final StorageInterface storage = StorageImpl.getInstance();
	private final Notary notary = NotaryImpl.getInstance();
	private final FidoUafResource fidoUafResource;

	//static final String UserTableName = System.getenv("DB_USER_TABLE_NAME");
	//static final String DefaultUser = System.getenv("DEFAULT_USER_IDENTITY"); // b342ff9c-9924-4421-9071-32763f907d9b
	
	JSONParser parser = new JSONParser();

	public FidoUafLambdaInternalHandler(){
		logger.debug("Created FIDO UAF Server Lambda Handler");
		fidoUafResource = new FidoUafResource(storage, notary);
		logger.debug("Created FidoUafResource object with real implementation of notary and storage");
	}


	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		//This lambda will exercise all the different components utilised by the CID hub to ensure that their health is OK.
		//Returns 200 with output of each check
		//Returns 500 with output of each check if it fails
        //LambdaLogger logger = context.getLogger(); //basic cloudwatch logger
        logger.info("Loading Java Lambda handler");

        //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String strEvent = IOUtils.toString(inputStream);
		logger.info("Successfully read inputStream into String");
		logger.info(strEvent);
        JSONArray event = null;
        try {
            event = (JSONArray)parser.parse(strEvent);
			logger.info("Successfully parsed the input stream to a JSONObject");
			logger.info(event.toJSONString());
			AuthenticatorRecord[] ar_response = fidoUafResource.processAuthResponse(event.toJSONString());
			//Simplification - only return the first authenticator record
			String json_body = gson.toJson(ar_response[0]);
			outputStream.write(json_body.getBytes());
            
        } catch(Exception pex) {
	        //check if keep-alive event
            JSONObject event2 = null;
            try {
				event2 = (JSONObject)parser.parse(strEvent);
		        if (event2.containsKey("Records"))
		        {
		        	logger.info("Received a keep-alive event");
		            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		        	logger.info("Updating secret key");
		            NotaryImpl.getInstance().rotateSecret();
		        	logger.info("Secret key updated");
		            writer.write("{ \"response\" : \"OK\"}");  
		            writer.close();
		            return;
		        }
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.info("Failed to parse the input stream to a JSONObject");
			}
        }
	}
}