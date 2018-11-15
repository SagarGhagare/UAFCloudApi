package uk.nhs.digital.cid.fidouaf.handlers;

import org.apache.http.HttpStatus;
import org.ebayopensource.fido.uaf.msg.AuthenticationRequest;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;

public class AuthenticationRequestHandler extends ApiGatewayHandlerBase {

	@Inject
	private IAuthenticationService authenticationService;

	// Instantiated by AWS Lambda at runtime
	public AuthenticationRequestHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public AuthenticationRequestHandler(IAuthenticationService authenticationService, ApiGatewayResponseBuilder builder,
			Logger logger) {
		this.authenticationService = authenticationService;
		this.builder = builder;
		this.logger = logger;
	}

	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		this.logger.info("Received AuthenticationRequestHandler request", request);

		AuthenticationRequest[] authReq = authenticationService.getAuthReq();

		return buildResponse(HttpStatus.SC_OK, authReq);
	}

}
