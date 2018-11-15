package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.msg.AuthenticationRequest;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;

import uk.nhs.digital.cid.fidouaf.facets.Facets;

public interface IAuthenticationService {

	public AuthenticationRequest[] getAuthReq();
	
	public AuthenticatorRecord[] processAuthResponse(AuthenticationResponse[] authenticationResponse);
	
	public Facets facets();
}
