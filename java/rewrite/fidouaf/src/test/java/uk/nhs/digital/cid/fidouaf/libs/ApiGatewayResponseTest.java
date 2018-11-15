package uk.nhs.digital.cid.fidouaf.libs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApiGatewayResponseTest {

    private ApiGatewayResponseBuilder CreateBuilderObj() {
        return new ApiGatewayResponseBuilder("*");
    }

    @Test
    public void testApiGatewayResponse_returnsExpectedStatusCode() {

        // Set status code and expect same code to be returned
        ApiGatewayResponse response = CreateBuilderObj().setStatusCode(200).build();
        assertTrue("Response should return expected status code", response.getStatusCode() == 200);
    }

    @Test
    public void testApiGatewayResponse_returnsExpectedCORSHeaders_whenCorsHeadersApplied() {

        // Act
        ApiGatewayResponse response = CreateBuilderObj().addCorsHeaders().build();
        
        // Asssert
        String allowOrigin = response.getHeaders().get("Access-Control-Allow-Origin");
        String allowCredentials = response.getHeaders().get("Access-Control-Allow-Credentials");

        assertEquals("*", allowOrigin);
        assertEquals("true", allowCredentials);
    }

    @Test
    public void testApiGatewayResponse_returnsExpectedCacheHeaders_whenSecurityHeadersApplied() {
        
        // Act
        ApiGatewayResponse response = CreateBuilderObj().addSecurityHeaders().build();

        // Assert
        String cacheControl = response.getHeaders().get("Cache-Control");
        String expires = response.getHeaders().get("Expires");
        String pragma = response.getHeaders().get("Pragma");

        assertEquals("Cache-Control header should have expected value", "no-cache,no-store", cacheControl);
        assertEquals("Expires header should have expected value", "0", expires);
        assertEquals("Pragma header should have expected value", "no-cache", pragma);
    }
}
