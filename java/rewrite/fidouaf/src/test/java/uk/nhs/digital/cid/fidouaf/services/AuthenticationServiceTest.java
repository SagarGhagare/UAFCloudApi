package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.AuthenticationRequest;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;

import uk.nhs.digital.cid.fidouaf.facets.Facets;
import uk.nhs.digital.cid.fidouaf.logging.Logger;

public class AuthenticationServiceTest {

	@Mock
	public Logger logger;
	
	@Mock
	private StorageInterface storage;
	
	@Mock
	private Notary notary;
	
	@Mock
	private IProcessResponse processResponse;
	
	@Before
	public void intialise() {
		MockitoAnnotations.initMocks(this);
	}

	private AuthenticationService getSut() {
		return new AuthenticationService(storage, notary, processResponse, logger);
	}
	
	@Test
	public void test_getAuthReq_success() {
		AuthenticationRequest[] authenticationRequestResponse = getSut().getAuthReq();

		AuthenticationRequest[] expected = new Gson().fromJson(getAuthReqResponseString(), AuthenticationRequest[].class);
		Assert.assertNotNull(authenticationRequestResponse);
		Assert.assertEquals(expected[0].header.appID, authenticationRequestResponse[0].header.appID);
		Assert.assertEquals(expected[0].header.upv.major, authenticationRequestResponse[0].header.upv.major);
		Assert.assertEquals(expected[0].header.upv.minor, authenticationRequestResponse[0].header.upv.minor);
		Assert.assertEquals(expected[0].header.op.name(), authenticationRequestResponse[0].header.op.name());
		Assert.assertEquals(expected[0].header.op.ordinal(), authenticationRequestResponse[0].header.op.ordinal());
	}
	
	@Test
	public void test_processAuthResponse_success() {
		AuthenticationResponse[] authenticationResponse = null;
		
		AuthenticatorRecord[] AuthenticatorRecord = getSut().processAuthResponse(authenticationResponse);
		
		Assert.assertNotNull(AuthenticatorRecord);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void test_facets_success() {
		String facetsString = "{\r\n" + 
				"  \"trustedFacets\": [\r\n" + 
				"    {\r\n" + 
				"      \"version\": {\r\n" + 
				"        \"major\": 1,\r\n" + 
				"        \"minor\": 0\r\n" + 
				"      },\r\n" + 
				"      \"ids\": [\r\n" + 
				"        \"https://www.head2toes.org\",\r\n" + 
				"        \"android:apk-key-hash:Df+2X53Z0UscvUu6obxC3rIfFyk\",\r\n" + 
				"        \"android:apk-key-hash:bE0f1WtRJrZv/C0y9CM73bAUqiI\",\r\n" + 
				"        \"android:apk-key-hash:Lir5oIjf552K/XN4bTul0VS3GfM\",\r\n" + 
				"        \"https://openidconnect.ebay.com\",\r\n" + 
				"        \"android:apk-key-hash:CxHdfRYR5KEkAfDMe4jOHGt6RKg\"\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		Facets facets = getSut().facets();
		
		Facets expected = new Gson().fromJson(facetsString, Facets.class);
		Assert.assertNotNull(facets);
		Assert.assertEquals(expected.trustedFacets[0].ids, facets.trustedFacets[0].ids);
	}
	
	private String getAuthReqResponseString() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"header\": {\r\n" + 
				"      \"upv\": {\r\n" + 
				"        \"major\": 1,\r\n" + 
				"        \"minor\": 0\r\n" + 
				"      },\r\n" + 
				"      \"op\": \"Auth\",\r\n" + 
				"      \"appID\": \"\",\r\n" + 
				"      \"serverData\": \"bnVsbC5NVFUwTWpJNU56YzRORGN4TVEuU2tSS2FFcEVSWGRLUjFaelRXcGFXbGRIT0RKaldFa3dZMjVXZWxaSWNFZFNSRTV4VmxkVg\"\r\n" + 
				"    },\r\n" + 
				"    \"challenge\": \"JDJhJDEwJGVsMjZZWG82cXI0cnVzVHpGRDNqVWU\",\r\n" + 
				"    \"transaction\": null,\r\n" + 
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
