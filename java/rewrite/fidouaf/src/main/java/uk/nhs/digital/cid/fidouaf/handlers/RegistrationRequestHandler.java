package uk.nhs.digital.cid.fidouaf.handlers;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;

public class RegistrationRequestHandler extends ApiGatewayHandlerBase {

	@Inject
	private IRegistrationService registrationService;

	// Instantiated by AWS Lambda at runtime
	public RegistrationRequestHandler() {
		injector.injectMembers(this);
	}

	// Used at test time
	public RegistrationRequestHandler(IRegistrationService registrationService, ApiGatewayResponseBuilder builder,
			Logger logger) {
		this.registrationService = registrationService;
		this.builder = builder;
		this.logger = logger;
	}

	@Override
	public ApiGatewayResponse doHandleRequest(ApiGatewayRequest request, Context context) {
		logger.info("Processing Registration Request", request);
		Map<String, String> headers = request.getHeaders();
		logger.info("Got Headers object", headers);
		String userName = "";
		if (headers != null && headers.containsKey("Authorization")) {
			logger.info("Authorization key present in headers");
			String accessTokenJwt = headers.get("Authorization").toString();
			logger.info("Access Token is " + accessTokenJwt);
			try {
				JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder().setSkipAllValidators()
						.setDisableRequireSignature().setSkipSignatureVerification().build();
				JwtContext jwtContext = firstPassJwtConsumer.process(accessTokenJwt);
				userName = jwtContext.getJwtClaims().getSubject();
			} catch (InvalidJwtException | MalformedClaimException e) {
				logger.info("Exception", e);
				return buildResponse(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.error("Authorization key not present in headers");
			return buildResponse("Authorization key not present in headers", HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		logger.info("Username for registration is " + userName);
		RegistrationRequest[] registrationRequestResponse = registrationService.regReqPublic(userName);
		return buildResponse(HttpStatus.SC_OK, registrationRequestResponse);
	}
}
