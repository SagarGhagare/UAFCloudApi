package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;

public class AuthenticationResponseHandler extends ApiGatewayHandlerBase {

	@Inject
	private IAuthenticationService authenticationService;

	// Instantiated by AWS Lambda at runtime
	public AuthenticationResponseHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public AuthenticationResponseHandler(IAuthenticationService authenticationService, ApiGatewayResponseBuilder builder,
				Logger logger) {
			this.authenticationService = authenticationService;
			this.builder = builder;
			this.logger = logger;
		}

	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		this.logger.info("Received AuthenticationResponseHandler request", request);

		AuthenticationResponse[] authenticationResponse;
		try {
			this.logger.debug("Parsing body content from request object.");
			authenticationResponse = request.getObjectBody(AuthenticationResponse[].class);
		} catch (IOException e) {
			this.logger.error("Problem parsing body content from request object", e);
			return buildResponse("Problem parsing body content from request object", HttpStatus.SC_BAD_REQUEST);
		}
		AuthenticatorRecord[] authenticatorRecord = authenticationService.processAuthResponse(authenticationResponse);
		return buildResponse(HttpStatus.SC_OK, authenticatorRecord);
	}

}
