package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
import org.ebayopensource.fido.uaf.storage.StorageInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import uk.nhs.digital.cid.fidouaf.logging.Logger;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

	@Mock
	public Logger logger;
	
	@Mock
	private StorageInterface storage;
	
	@Mock
	private Notary notary;
	
	@Before
	public void intialise() {
		MockitoAnnotations.initMocks(this);
	}

	private RegistrationService getSut() {
		return new RegistrationService(storage, notary);
	}
	
	@Test
	public void test_GetRecordByUserAndStatus_With_CorrectRequest() {
		String userName= "1d6d2dd3-9b83-4b52-96b6-be21302704c7";

		RegistrationRequest[] registrationRequestResponse = getSut().regReqPublic(userName);

		Assert.assertNotNull(registrationRequestResponse);
	}
}
