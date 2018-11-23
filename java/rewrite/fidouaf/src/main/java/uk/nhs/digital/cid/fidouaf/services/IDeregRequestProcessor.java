package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.msg.DeregistrationRequest;

public interface IDeregRequestProcessor {
	
	public String process(DeregistrationRequest[] deRegistrationRequests);

}
