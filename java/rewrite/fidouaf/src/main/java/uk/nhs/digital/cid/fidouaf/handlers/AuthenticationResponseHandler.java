package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;
import uk.nhs.digital.cid.fidouaf.util.InjectorModule;

public class AuthenticationResponseHandler implements RequestStreamHandler {

	protected Injector injector = Guice.createInjector(new InjectorModule());

	@Inject
	private IAuthenticationService authenticationService;

	@Inject
	protected Logger logger;

	@Inject
	protected Notary notary;

	@Inject
	protected ApiGatewayResponseBuilder builder;

	protected Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	// Instantiated by AWS Lambda at runtime
	public AuthenticationResponseHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public AuthenticationResponseHandler(IAuthenticationService authenticationService,
			ApiGatewayResponseBuilder builder, Logger logger) {
		this.authenticationService = authenticationService;
		this.builder = builder;
		this.logger = logger;
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		// This lambda will exercise all the different components utilised by the CID
		// hub to ensure that their health is OK.
		// Returns 200 with output of each check
		// Returns 500 with output of each check if it fails
		// LambdaLogger logger = context.getLogger(); //basic cloudwatch logger
		logger.info("Loading Java Lambda handler");

		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(inputStream));
		String strEvent = IOUtils.toString(inputStream);
		logger.info("Successfully read inputStream into String", strEvent);
		JSONArray event = null;
		JSONParser parser = new JSONParser();
		try {
			event = (JSONArray) parser.parse(strEvent);
			logger.info("Successfully parsed the input stream to a JSONObject", event);
			AuthenticationResponse[] authResp = gson.fromJson(event.toJSONString(), AuthenticationResponse[].class);
			AuthenticatorRecord[] authenticatorRecordResponse = authenticationService.processAuthResponse(authResp);
			// Simplification - only return the first authenticator record
			String jsonResponseBody = gson.toJson(authenticatorRecordResponse[0]);
			outputStream.write(jsonResponseBody.getBytes());

		} catch (Exception pex) {
			logger.error("Exception ", pex);
			// check if keep-alive event
			JSONObject jsonResponseObject = null;
			try {
				jsonResponseObject = (JSONObject) parser.parse(strEvent);
				if (jsonResponseObject.containsKey("Records")) {
					logger.info("Received a keep-alive event");
					OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
					logger.info("Updating secret key");
					notary.rotateSecret();
					logger.info("Secret key updated");
					writer.write("{ \"response\" : \"OK\"}");
					writer.close();
					return;
				}
			} catch (ParseException e) {
				logger.error("Failed to parse the input stream to a JSONObject", e);
			}
		}
	}
}
