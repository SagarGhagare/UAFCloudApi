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

import org.ebayopensource.fido.uaf.crypto.FinalChallengeParamsValidator;
import org.ebayopensource.fido.uaf.crypto.FinalChallengeParamsValidatorImpl;
import org.ebayopensource.fido.uaf.crypto.Notary;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.msg.RegistrationResponse;
import org.ebayopensource.fido.uaf.ops.AuthenticationResponseProcessing;
import org.ebayopensource.fido.uaf.ops.RegistrationResponseProcessing;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;

import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.Configuration;

public class ProcessResponse implements IProcessResponse {

	private Logger logger;
	private static int SERVER_DATA_EXPIRY_IN_MS;

	private Notary notary = null;
	private StorageInterface storage = null;
	private FinalChallengeParamsValidator finalChallengeParamsValidator = null;

	@Inject
	public ProcessResponse(Configuration config, Logger logger, Notary notary, StorageInterface storage) {
		this.logger = logger;
		SERVER_DATA_EXPIRY_IN_MS = Integer.parseInt(config.getFidoExpiry());
		this.notary = notary;
		this.storage = storage;
		this.finalChallengeParamsValidator = new FinalChallengeParamsValidatorImpl();
	}

	public ProcessResponse(Notary notary, StorageInterface storage, int data_expiry) {
		this.notary = notary;
		this.storage = storage;
		SERVER_DATA_EXPIRY_IN_MS = data_expiry;
		this.finalChallengeParamsValidator = new FinalChallengeParamsValidatorImpl();
	}

	public ProcessResponse(Notary notary, StorageInterface storage,
			FinalChallengeParamsValidator finalChallengeParamsValidator, int data_expiry) {
		this.notary = notary;
		this.storage = storage;
		SERVER_DATA_EXPIRY_IN_MS = data_expiry;
		this.finalChallengeParamsValidator = finalChallengeParamsValidator;
	}

	public ProcessResponse(Notary notary, StorageInterface storage) {
		this.notary = notary;
		this.storage = storage;
		this.finalChallengeParamsValidator = new FinalChallengeParamsValidatorImpl();
	}

	public ProcessResponse(Notary notary, StorageInterface storage,
			FinalChallengeParamsValidator finalChallengeParamsValidator) {
		this.notary = notary;
		this.storage = storage;
		this.finalChallengeParamsValidator = finalChallengeParamsValidator;
	}

	public AuthenticatorRecord[] processAuthResponse(AuthenticationResponse resp) {
		logger.info("processAuthResponse received AuthenticationResponse ", resp);
		AuthenticatorRecord[] result = null;
		try {
			result = new AuthenticationResponseProcessing(SERVER_DATA_EXPIRY_IN_MS, notary,
					finalChallengeParamsValidator).verify(resp, storage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new AuthenticatorRecord[1];
			result[0] = new AuthenticatorRecord();
			result[0].status = e.getMessage();
		}
		logger.info("processAuthResponse returning AuthenticatorRecord[] ", result);
		return result;
	}

	public RegistrationRecord[] processRegResponse(RegistrationResponse resp) {
		logger.info("processRegResponse received  RegistrationResponse ", resp);
		RegistrationRecord[] result = null;
		try {
			result = new RegistrationResponseProcessing(SERVER_DATA_EXPIRY_IN_MS, notary, finalChallengeParamsValidator)
					.processResponse(resp);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new RegistrationRecord[1];
			result[0] = new RegistrationRecord();
			result[0].status = e.getMessage();
		}
		logger.info("processRegResponse returning RegistrationRecord[] ", result);
		return result;
	}
}