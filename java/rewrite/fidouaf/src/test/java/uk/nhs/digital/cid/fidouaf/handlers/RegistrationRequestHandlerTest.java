package uk.nhs.digital.cid.fidouaf.handlers;

import java.util.Map;

import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
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

public class RegistrationRequestHandlerTest {

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

	private RegistrationRequestHandler getSut() {
		return new RegistrationRequestHandler(mockRegistrationService, new ApiGatewayResponseBuilder("*"), logger);
	}
	
	private Context createContext() {
		return new TestContext();
	}

	@Test
	public void test_HandlerSuccess() {
		// Arrange
		Context ctx = createContext();
		String userName = "1d6d2dd3-9b83-4b52-96b6-be21302704c7";
		RegistrationRequest[] registrationRequestResponse = new RegistrationRequest[1];
		Mockito.when(req.getHeaders()).thenReturn(headers);
		Mockito.when(headers.containsKey("Authorization")).thenReturn(true);
		Mockito.when(headers.get("Authorization")).thenReturn("eyJzdWIiOiIxZDZkMmRkMy05YjgzLTRiNTItOTZiNi1iZTIxMzAyNzA0YzciLCJhdWQiOiJuaHMtb25saW5lIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLmRldjMuc2lnbmluLm5ocy51ayIsInR5cCI6IkpXVCIsImV4cCI6MTU0MjEyMTA2NCwiaWF0IjoxNTQyMTE3NDY0LCJhbGciOiJSUzUxMiIsImp0aSI6IjhmMjZhZDY1LTI4NzAtNDM5Mi1hZjYxLWE4YTE3N2VhMWIxYyJ9.eyJzdWIiOiIxZDZkMmRkMy05YjgzLTRiNTItOTZiNi1iZTIxMzAyNzA0YzciLCJhdWQiOiJuaHMtb25saW5lIiwibmhzX251bWJlciI6Ijc2MCAxOTQgNzkxMiIsInRva2VuX3VzZSI6ImFjY2VzcyIsImF1dGhfdGltZSI6MTU0MjExNTkxOSwic2NvcGUiOiJvcGVuaWQiLCJpc3MiOiJodHRwczovL2F1dGguZGV2My5zaWduaW4ubmhzLnVrIiwiZXhwIjoxNTQyMTIxMDY0LCJpYXQiOjE1NDIxMTc0NjQsInZlcnNpb24iOjAsImNsaWVudF9pZCI6Im5ocy1vbmxpbmUifQ.DM4QrCATeiZIMDzDLZstFBjgZ11nXv_-4g8OKHrRxYpyrkr2lTGgzzwRSj8c8G5bECIjjUS8SUzUHr6XNg3U6l9e14sS0AlA6geKUOPzUveDOAhD9A1qrwDBimhlTjbQ6Z1hJMKNaARzbZkk5xFXUXBqPE0q7HBVvYDZd_I1POoenkN-iB3Mp_nElanueAHq65rMvFFiPmHLsvpInJ6UN99esETnTfPz0-boouJdG-umMSOu80iDvSsOsXnRWekXheXHcpyIw8JDbzZuqmwM41voTD4w0zUxmPtmzJ3R2_RT5B-gmQGrBfPnQTxWTv6lXtdLvyLsLxV7NnQpeJgTpA");
		Mockito.when(mockRegistrationService.regReqPublic(userName)).thenReturn(registrationRequestResponse);
		
		// Act
		ApiGatewayResponse apiGatewayResponse = getSut().doHandleRequest(req, ctx);

		Mockito.verify(mockRegistrationService).regReqPublic(userName);
		Assert.assertThat("Response Status Code should be 200 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(200));
	}
	
	@Test
	public void test_handler_returnsHttp500_whenAuthorizationHeaderIsNotPresent() {
		Context ctx = createContext();
		Mockito.when(req.getHeaders()).thenReturn(headers);
		Mockito.when(headers.containsKey("Authorization")).thenReturn(false);

		ApiGatewayResponse apiGatewayResponse =  getSut().doHandleRequest(req, ctx);

		Assert.assertThat("Response Status Code should be 500 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(500));
	}
	
	@Test
	public void test_handler_returnsHttp500_whenInvalidJwtExceptionThrown() {
		Context ctx = createContext();
		Mockito.when(req.getHeaders()).thenReturn(headers);
		Mockito.when(headers.containsKey("Authorization")).thenReturn(true);
		Mockito.when(headers.get("Authorization")).thenReturn("eyJzdWIiOiIxZMmRkMy05YjgzLTRiNTItOTZiNi1iZTIxMzAyNzA0YzciLCJhdWQiOiJuaHMtb25saW5lIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLmRldjMuc2lnbmluLm5ocy51ayIsInR5cCI6IkpXVCIsImV4cCI6MTU0MjEyMTA2NCwiaWF0IjoxNTQyMTE3NDY0LCJhbGciOiJSUzUxMiIsImp0aSI6IjhmMjZhZDY1LTI4NzAtNDM5Mi1hZjYxLWE4YTE3N2VhMWIxYyJ9.eyJzdWIiOiIxZDZkMmRkMy05YjgzLTRiNTItOTZiNi1iZTIxMzAyNzA0YzciLCJhdWQiOiJuaHMtb25saW5lIiwibmhzX251bWJlciI6Ijc2MCAxOTQgNzkxMiIsInRva2VuX3VzZSI6ImFjY2VzcyIsImF1dGhfdGltZSI6MTU0MjExNTkxOSwic2NvcGUiOiJvcGVuaWQiLCJpc3MiOiJodHRwczovL2F1dGguZGV2My5zaWduaW4ubmhzLnVrIiwiZXhwIjoxNTQyMTIxMDY0LCJpYXQiOjE1NDIxMTc0NjQsInZlcnNpb24iOjAsImNsaWVudF9pZCI6Im5ocy1vbmxpbmUifQ.DM4QrCATeiZIMDzDLZstFBjgZ11nXv_-4g8OKHrRxYpyrkr2lTGgzzwRSj8c8G5bECIjjUS8SUzUHr6XNg3U6l9e14sS0AlA6geKUOPzUveDOAhD9A1qrwDBimhlTjbQ6Z1hJMKNaARzbZkk5xFXUXBqPE0q7HBVvYDZd_I1POoenkN-iB3Mp_nElanueAHq65rMvFFiPmHLsvpInJ6UN99esETnTfPz0-boouJdG-umMSOu80iDvSsOsXnRWekXheXHcpyIw8JDbzZuqmwM41voTD4w0zUxmPtmzJ3R2_RT5B-gmQGrBfPnQTxWTv6lXtdLvyLsLxV7NnQpeJgTpA");
		ApiGatewayResponse apiGatewayResponse =  getSut().doHandleRequest(req, ctx);

		Assert.assertThat("Response Status Code should be 500 ", apiGatewayResponse.getStatusCode(),
				CoreMatchers.is(500));
	}
}
