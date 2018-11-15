package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.ebayopensource.fido.uaf.msg.DeregistrationRequest;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;

public class DeRegistrationRequestHandler extends ApiGatewayHandlerBase {

	@Inject
	private IRegistrationService registrationService;

	// Instantiated by AWS Lambda at runtime
	public DeRegistrationRequestHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public DeRegistrationRequestHandler(IRegistrationService registrationService, ApiGatewayResponseBuilder builder,
			Logger logger) {
		this.registrationService = registrationService;
		this.builder = builder;
		this.logger = logger;
	}

	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		this.logger.info("Received DeRegistrationRequestHandler request", request);

		DeregistrationRequest[] deRegistrationRequest;
		try {
			this.logger.debug("Parsing body content from request object.");
			deRegistrationRequest = request.getObjectBody(DeregistrationRequest[].class);
		} catch (IOException e) {
			this.logger.error("Problem parsing body content from request object", e);
			return buildResponse("Problem parsing body content from request object", HttpStatus.SC_BAD_REQUEST);
		}
		String deRegistrationResult = registrationService.deregRequestPublic(deRegistrationRequest);
		return buildResponse(HttpStatus.SC_OK, deRegistrationResult);
	}

}
