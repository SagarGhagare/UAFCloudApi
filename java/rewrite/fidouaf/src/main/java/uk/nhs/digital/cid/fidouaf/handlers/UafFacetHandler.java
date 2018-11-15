package uk.nhs.digital.cid.fidouaf.handlers;

import org.apache.http.HttpStatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.facets.Facets;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;

public class UafFacetHandler extends ApiGatewayHandlerBase{

	@Inject
	private IAuthenticationService authenticationService;

	// Instantiated by AWS Lambda at runtime
	public UafFacetHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public UafFacetHandler(IAuthenticationService authenticationService, ApiGatewayResponseBuilder builder,
			Logger logger) {
		this.authenticationService = authenticationService;
		this.builder = builder;
		this.logger = logger;
	}
	
	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		this.logger.info("Received UafFacetHandler request", request);
		Facets facets;
		try {
			facets = authenticationService.facets();
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			return buildResponse(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		return buildResponse(HttpStatus.SC_OK, facets);
	}

}
