package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
import org.ebayopensource.fido.uaf.msg.RegistrationResponse;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

	@Mock
	public Logger logger;
	
	@Mock
	private StorageInterface storage;
	
	@Mock
	private Notary notary;
	
	@Mock
	private IProcessResponse processResponse;
	
	@Mock
	private IDeregRequestProcessor deregRequestProcessor;

	@Mock
	private Configuration configuration;
	
	@Before
	public void intialise() {
		MockitoAnnotations.initMocks(this);
	}

	private RegistrationService getSut() {
		return new RegistrationService(storage, notary, processResponse, deregRequestProcessor, configuration);
	}
	
	@Test
	public void test_GetRecordByUserAndStatus_success() throws JsonProcessingException {
		String userName= "1d6d2dd3-9b83-4b52-96b6-be21302704c7";

		RegistrationRequest[] registrationRequestResponse = getSut().regReqPublic(userName);
		
		RegistrationRequest[] response = new Gson().fromJson(getExpectedStringResponse(), RegistrationRequest[].class);
		Assert.assertNotNull(registrationRequestResponse);
		Assert.assertEquals(response[0].username, registrationRequestResponse[0].username);
		
	}
	
	@Test
	public void test_processRegResponse_whenRegistrationResponseIsNull() {
		RegistrationResponse[] registrationResponse = null;
		
		RegistrationRecord[] registrationRecord = getSut().processRegResponse(registrationResponse);

		Assert.assertNotNull(registrationRecord);
		Assert.assertEquals("Error: payload could not be empty", registrationRecord[0].status);
	}
	
	private String getExpectedStringResponse() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"header\": {\r\n" + 
				"      \"upv\": {\r\n" + 
				"        \"major\": 1,\r\n" + 
				"        \"minor\": 0\r\n" + 
				"      },\r\n" + 
				"      \"op\": \"Reg\",\r\n" + 
				"      \"appID\": \"\",\r\n" + 
				"      \"serverData\": \"bnVsbC5NVFUwTWpJNU5UQXlNVGs0T0EuTVdRMlpESmtaRE10T1dJNE15MDBZalV5TFRrMllqWXRZbVV5TVRNd01qY3dOR00zLlNrUkthRXBFUlhkS1NHeEpZWHBzTms5R1JrUlhSWEIwVmtaS1dXTkdSWHBYUnpFMllVTTA\"\r\n" + 
				"    },\r\n" + 
				"    \"challenge\": \"JDJhJDEwJHlIazl6OFFDWEptVFJYcFEzWG16aC4\",\r\n" + 
				"    \"username\": \"1d6d2dd3-9b83-4b52-96b6-be21302704c7\",\r\n" + 
				"    \"policy\": {\r\n" + 
				"      \"accepted\": [\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"EBA0#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0015#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0012#0002\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0010#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"4e4e#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"5143#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0011#0701\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0013#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0014#0000\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"0014#0001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"53EC#C002\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"DAB8#8001\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"DAB8#0011\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"DAB8#8011\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"5143#0111\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"5143#0120\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"4746#F816\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ],\r\n" + 
				"        [\r\n" + 
				"          {\r\n" + 
				"            \"aaid\": [\r\n" + 
				"              \"53EC#3801\"\r\n" + 
				"            ]\r\n" + 
				"          }\r\n" + 
				"        ]\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"]";
	}
}
