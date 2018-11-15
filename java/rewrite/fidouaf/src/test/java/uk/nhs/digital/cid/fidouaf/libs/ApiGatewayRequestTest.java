package uk.nhs.digital.cid.fidouaf.libs;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class ApiGatewayRequestTest {

    @Test
    public void testApiGatewayRequest_objectSerialisationSuccessful() throws IOException {

        // Populate pojo with some test data
        RequestContext obj = new RequestContext() {
            {
                setAccountId("12345");
                setApiId("5678");
                setHttpMethod("POST");
                setRequestId("7890");
                setResourceId("5432");
                setResourcePath("test_path");
                setStage("test");
            }
        };

        ApiGatewayRequest req = new ApiGatewayRequest();

        // Call the function to perform the serialisation
        req.setObjectBody(obj);

        // Check serialised string in the body is what we expect
        String expectedBody = "{\"accountId\":\"12345\",\"resourceId\":\"5432\",\"stage\":\"test\",\"requestId\":\"7890\",\"identity\":null,\"resourcePath\":\"test_path\",\"httpMethod\":\"POST\",\"apiId\":\"5678\"}";
        assertTrue("Returned serialised string should match input object", req.getBody().equals(expectedBody));        
    }

}
