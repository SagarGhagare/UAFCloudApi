package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.msg.RegistrationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;

public interface IProcessResponse {

	public AuthenticatorRecord[] processAuthResponse(AuthenticationResponse resp);

	public RegistrationRecord[] processRegResponse(RegistrationResponse resp);
}
