package uk.nhs.digital.cid.fidouaf.handlers;

import java.util.Map;

import org.ebayopensource.fido.uaf.msg.AuthenticationRequest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.amazonaws.services.lambda.runtime.Context;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;
import uk.nhs.digital.cid.fidouaf.test.TestContext;

public class AuthenticationRequestHandlerTest {

	@Mock
	private IAuthenticationService mockAuthenticationService;

	@Mock
	public Logger logger;

	@Mock
	private ApiGatewayRequest req;

	@Mock
	private Map<String, String> headers;

	@Before
	public void initialise() {
		MockitoAnnotations.initMocks(this);
	}

	private AuthenticationRequestHandler getSut() {
		return new AuthenticationRequestHandler(mockAuthenticationService, new ApiGatewayResponseBuilder("*"), logger);
	}

	private Context createContext() {
		return new TestContext();
	}

	@Test
	public void test_HandlerSuccess() {
		// Arrange
		Context ctx = createContext();
		AuthenticationRequest[] result = null;
		Mockito.when(mockAuthenticationService.getAuthReq()).thenReturn(result);

		// Act
		ApiGatewayResponse apiGatewayResponse = getSut().doHandleRequest(req, ctx);

		Mockito.verify(mockAuthenticationService).getAuthReq();
		Assert.assertThat("Response Status Code should be 200 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(200));
	}
}
