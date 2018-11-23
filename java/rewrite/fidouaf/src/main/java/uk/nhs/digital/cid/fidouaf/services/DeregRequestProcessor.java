/*
 * Copyright 2015 eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.nhs.digital.cid.fidouaf.services;

import org.ebayopensource.fido.uaf.msg.DeregisterAuthenticator;
import org.ebayopensource.fido.uaf.msg.DeregistrationRequest;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;

import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.stats.Dash;

public class DeregRequestProcessor implements IDeregRequestProcessor {

	private StorageInterface storage;
	private Logger logger;
	
	@Inject
	public DeregRequestProcessor(StorageInterface storage, Logger logger) {
		this.storage = storage;
		this.logger = logger;
	}
	
	public String process(DeregistrationRequest[] deRegistrationRequests) {
		logger.info("Received deRegistrationRequests ", deRegistrationRequests);
		if (deRegistrationRequests != null) {
			try {
				DeregistrationRequest deRegistrationRequest = deRegistrationRequests[0];
				Dash.getInstance().stats.put(Dash.LAST_DEREG_REQ, deRegistrationRequests);
				AuthenticatorRecord authRecord = new AuthenticatorRecord();
				for (DeregisterAuthenticator authenticator : deRegistrationRequest.authenticators) {
					authRecord.AAID = authenticator.aaid;
					authRecord.KeyID = authenticator.keyID;
					try {
						String Key = authRecord.toString();
						storage.deleteRegistrationRecord(Key);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						return "Failure: Problem in deleting record from local DB";
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return "Failure: problem processing deregistration request";
			}
			return "Success";
		}
		return "Failure: problem processing deregistration request";
	}
}
