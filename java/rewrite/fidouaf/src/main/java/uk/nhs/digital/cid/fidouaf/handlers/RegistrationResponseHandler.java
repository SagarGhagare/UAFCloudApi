package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.ebayopensource.fido.uaf.msg.RegistrationResponse;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;

public class RegistrationResponseHandler extends ApiGatewayHandlerBase {

	@Inject
	private IRegistrationService registrationService;

	// Instantiated by AWS Lambda at runtime
	public RegistrationResponseHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public RegistrationResponseHandler(IRegistrationService registrationService, ApiGatewayResponseBuilder builder,
			Logger logger) {
		this.registrationService = registrationService;
		this.builder = builder;
		this.logger = logger;
	}

	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		this.logger.info("Received RegistrationResponseHandler request", request);

		RegistrationResponse[] registrationResponse;
		try {
			this.logger.debug("Parsing body content from request object.");
			registrationResponse = request.getObjectBody(RegistrationResponse[].class);
		} catch (IOException e) {
			this.logger.error("Problem parsing body content from request object", e);
			return buildResponse("Problem parsing body content from request object", HttpStatus.SC_BAD_REQUEST);
		}
		RegistrationRecord[] registrationRecord = registrationService.processRegResponse(registrationResponse);
		return buildResponse(HttpStatus.SC_OK, registrationRecord);
	}
}
