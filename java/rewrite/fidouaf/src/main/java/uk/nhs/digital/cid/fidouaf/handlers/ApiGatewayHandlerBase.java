package uk.nhs.digital.cid.fidouaf.handlers;

import org.apache.http.HttpStatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.libs.Response;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.InjectorModule;
import uk.nhs.digital.cid.fidouaf.util.LambdaStatsCollector;

public abstract class ApiGatewayHandlerBase implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

	protected Injector injector = Guice.createInjector(new InjectorModule());

	@Inject
	protected Logger logger;

	@Inject
	protected ApiGatewayResponseBuilder builder;

	public ApiGatewayHandlerBase() {
	}

	protected ApiGatewayResponse buildResponse(int statusCode, Object body) {
		return builder.setStatusCode(statusCode).setObjectBody(body).addCorsHeaders().addSecurityHeaders().build();
	}

	protected ApiGatewayResponse buildResponse(String messageString, int statusCode) {
		Response responseBody = new Response(messageString, null);
		return buildResponse(statusCode, responseBody);
	}

	public abstract ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context);

	public ApiGatewayResponse handleRequest(ApiGatewayRequest request, Context context) {

		if(request.getHeaders() != null && request.getHeaders().get("X-Lambda-Keepwarm") != null){
			this.logger.info("Lambda keepwarm invocation");
			return buildResponse(HttpStatus.SC_OK, new LambdaStatsCollector().collect(request.getHeaders()));
		}

		if(request != null && request.getRequestContext() != null && !StringUtils.isNullOrEmpty(request.getRequestContext().getRequestId())) {
			this.logger.setCorrelationId(request.getRequestContext().getRequestId());
		}
		return doHandleRequest(request, context);
	}

	protected String getSourceIp(ApiGatewayRequest request) {
		if(request == null || request.getRequestContext() == null || request.getRequestContext().getIdentity() == null ) {
			return "";
		}

		return request.getRequestContext().getIdentity().getSourceIp();
	}

	protected String getSmartcardUsername(ApiGatewayRequest request) {
		if(request == null || request.getHeaders() == null || !request.getHeaders().containsKey("Authorization")) {
			return "";
		}

		String authToken = request.getHeaders().get("Authorization");
		DecodedJWT jwt = JWT.decode(authToken);

		if(jwt.getClaims().containsKey("cognito:username")) {
        	return jwt.getClaims().get("cognito:username").asString();
		}

		return "";
	}
}
