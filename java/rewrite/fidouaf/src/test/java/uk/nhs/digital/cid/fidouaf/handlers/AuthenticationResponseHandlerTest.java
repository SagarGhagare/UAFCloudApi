/*package uk.nhs.digital.cid.fidouaf.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.util.IOUtils;

import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayRequest;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponse;
import uk.nhs.digital.cid.fidouaf.libs.ApiGatewayResponseBuilder;
import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.services.IAuthenticationService;
import uk.nhs.digital.cid.fidouaf.test.TestContext;

public class AuthenticationResponseHandlerTest {

	@Mock
	private IAuthenticationService mockAuthenticationService;

	@Mock
	public Logger logger;

	@Mock
	private ApiGatewayRequest req;

	@Mock
	private Map<String, String> headers;

	@Mock
	private InputStream inputStream;

	private OutputStream outputStream;

	@Before
	public void initialise() {
		MockitoAnnotations.initMocks(this);
	}

	private AuthenticationResponseHandler getSut() {
		return new AuthenticationResponseHandler(mockAuthenticationService, new ApiGatewayResponseBuilder("*"), logger);
	}

	private Context createContext() {
		return new TestContext();
	}

	@Test
	public void test_HandlerSuccess() throws IOException {
		// Arrange
		Context ctx = createContext();
		AuthenticatorRecord[] result = null;
		AuthenticationResponse[] authenticationResponse = null;
		Mockito.when(IOUtils.toString(inputStream)).thenReturn("[{\\\"assertions\\\":[{\\\"assertion\\\":\\\"Aj4kAQQ-1QALLgkARUJBMCMwMDAxDi4FAAAAAQIADy5AADNiZjM5NDk4ZjlkMjI1MzExYTQ1NzNlNTM1M2ZlNGY0ODFkNGM0MjI3NDZhYzk4ZDc1ZjlhZWMxNWVmNjFkZWQKLiAAmTI7OMuslfxJ-HcrZ7vIc7yEh5QClx327xOiOgALwG0QLgAACS5HAFpXSmhlUzEwWlhOMExXdGxlUzFLUkVwb1NrUkZkMHBHWnpSUFJYQlhVVEphU21WWVRrVlZSRUpxVm14amRWRnNWbmxPU0ZVDS4EAAAAAQAGLkcAMEUCIEdwYlIjZs_WZ-1XXUM9mQbpuxxoeaBsrNJMi6ma4UhmAiEAvYjanaZvRiDqaDhTOa2f6-1OvwBan1Kze0rg4Tpm_PQ\\\",\\\"assertionScheme\\\":\\\"UAFV1TLV\\\"}],\\\"fcParams\\\":\\\"eyJhcHBJRCI6ImFuZHJvaWQ6YXBrLWtleS1oYXNoOldySkNLZStIZFZuT3dxd29qUVJJdXpjZ1VTMCIsImNoYWxsZW5nZSI6IkpESmhKREV3SkVwSVUycEhNRk5MVW1SNU1HTjRVV2RRZVdkNk9DNCIsImZhY2V0SUQiOiIifQ\\\",\\\"header\\\":{\\\"appID\\\":\\\"android:apk-key-hash:WrJCKe+HdVnOwqwojQRIuzcgUS0\\\",\\\"op\\\":\\\"Auth\\\",\\\"serverData\\\":\\\"eVZrUTktRWxxczZfNEh2cF9HQWxjZkRUaUJEYjJvX1RXTFRCLUZqNnV1Yy5NVFUwTWprM01EVTVPRE0zT0EuU2tSS2FFcEVSWGRLUlhCSlZUSndTRTFHVGt4VmJWSTFUVWRPTkZWWFpGRmxWMlEyVDBNMA\\\",\\\"upv\\\":{\\\"major\\\":1,\\\"minor\\\":0}}}]");
		Mockito.when(mockAuthenticationService.processAuthResponse(authenticationResponse)).thenReturn(result);

		// Act
		getSut().handleRequest(inputStream, outputStream, ctx);

		Mockito.verify(mockAuthenticationService).processAuthResponse(authenticationResponse);
	}
	
	@Test
	public void test_handler_returnsHttp400_whenIOExceptionIsThrownByService() throws Exception {
		// Arrange
		Mockito.doThrow(new IOException()).when(req).getObjectBody(AuthenticationResponse[].class);

		// Act
		getSut().handleRequest(inputStream, outputStream, createContext());

	}
}
*/