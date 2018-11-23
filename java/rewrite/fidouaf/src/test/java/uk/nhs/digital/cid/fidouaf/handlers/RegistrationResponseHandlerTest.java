package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;
import java.util.Map;

import org.ebayopensource.fido.uaf.msg.RegistrationResponse;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;
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
import uk.nhs.digital.cid.fidouaf.services.IRegistrationService;
import uk.nhs.digital.cid.fidouaf.test.TestContext;

public class RegistrationResponseHandlerTest {

	@Mock
	private IRegistrationService mockRegistrationService;

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

	private RegistrationResponseHandler getSut() {
		return new RegistrationResponseHandler(mockRegistrationService, new ApiGatewayResponseBuilder("*"), logger);
	}

	private Context createContext() {
		return new TestContext();
	}

	@Test
	public void test_HandlerSuccess() {
		// Arrange
		Context ctx = createContext();
		RegistrationResponse[] registrationResponse = null;
		RegistrationRecord[] result = null;
		Mockito.when(mockRegistrationService.processRegResponse(registrationResponse)).thenReturn(result);

		// Act
		ApiGatewayResponse apiGatewayResponse = getSut().doHandleRequest(req, ctx);

		Mockito.verify(mockRegistrationService).processRegResponse(registrationResponse);
		Assert.assertThat("Response Status Code should be 200 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(200));
	}

	@Test
	public void test_handler_returnsHttp400_whenIOExceptionIsThrownByService() throws Exception {
		// Arrange
		Mockito.doThrow(new IOException()).when(req).getObjectBody(RegistrationResponse[].class);

		// Act
		ApiGatewayResponse apiGatewayResponse = getSut().doHandleRequest(req, createContext());

		Assert.assertThat("Response Status Code should be 400 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(400));
	}
}
