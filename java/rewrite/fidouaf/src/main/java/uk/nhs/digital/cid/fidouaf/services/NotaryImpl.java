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

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.ebayopensource.fido.uaf.crypto.HMAC;
import org.ebayopensource.fido.uaf.crypto.Notary;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.Configuration;

/**
 * This is just an example implementation. You should implement this class based
 * on your operational environment.
 */
public class NotaryImpl implements Notary {

	private Logger logger;
	private Configuration config;
	private ISecretHelper secretHelper;

	@Inject
	public NotaryImpl(Configuration config, Logger logger, ISecretHelper secretHelper) {
		this.config = config;
		this.logger = logger;
		this.secretHelper = secretHelper;
	}

	public void rotateSecret() {
		logger.info("Entered rotateSecret");
		secretHelper.updateSecrets(config.getFidoSecretKeyName());
		logger.info("Exiting rotateSecret - secret values have been updated from Secrets Manager");
	}

	public String sign(String signData) {
		try {
			String signature = Base64
					.encodeBase64URLSafeString(HMAC.sign(signData, secretHelper.getCurrent(config.getFidoSecretKeyName())));
			storeAWS(signature);
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		return null;
	}

	public boolean verify(String signData, String signature) {
		logger.info("Entered verify of NotaryImpl");
		String secretName = config.getFidoSecretKeyName();
		if (!verifyAWS(signature)) {
			logger.warn("The signature failed the replay check");
			return false;
		}
		try {
			boolean result = false;
			result = MessageDigest.isEqual(Base64.decodeBase64(signature),
					HMAC.sign(signData, secretHelper.getCurrent(secretName)));
			if (!result) {
				logger.warn("Verification of signature failed using current secret - trying previous version");
				// try the previous version of the secret
				result = MessageDigest.isEqual(Base64.decodeBase64(signature),
						HMAC.sign(signData, secretHelper.getPrevious(secretName)));
				if (!result) {
					logger.warn(
							"Verification of signature failed using previous secret - updating secrets in case they have been rotated");
					// signatures do not match on current and previous. Check that secrets have not
					// been rotated and check current again
					rotateSecret();
					result = MessageDigest.isEqual(Base64.decodeBase64(signature),
							HMAC.sign(signData, secretHelper.getCurrent(secretName)));
				}
			}
			logger.info("Result if signature verification is ", result);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	private void storeAWS(String signature) {
		logger.info("Entered storeAWS to store ... ", signature);
		// signaturessTable
		Table table = getSignaturesTable();
		int SERVER_DATA_EXPIRY_IN_MS = Integer.parseInt(config.getFidoExpiry());
		Item signatureItem = new Item().withPrimaryKey("signature", signature).withNumber("expiry_time",
				((System.currentTimeMillis() + SERVER_DATA_EXPIRY_IN_MS) / 1000L));
		PutItemSpec putSpec = new PutItemSpec().withItem(signatureItem);
		table.putItem(putSpec);
		logger.info("Successfully stored item in DDB ... ");
	}

	private boolean verifyAWS(String signature) {
		logger.info("Entered verifyAWS to check signature ... ", signature);
		Table table = getSignaturesTable();
		GetItemSpec spec = new GetItemSpec().withPrimaryKey("signature", signature);
		try {
			logger.info("Attempting to read the item with key... ", signature);
			logger.info("Attempting to read the item...");
			Item outcome = table.getItem(spec);
			if (outcome == null) {
				logger.info("Item was not present - return false");
				return false;
			} else {
				logger.info("GetItem succeeded: " + outcome.toJSONPretty());
				logger.info("Deleting item to prevent replay attacks");
				DeleteItemSpec delSpec = new DeleteItemSpec().withPrimaryKey("signature", signature);
				try {
					logger.info("Attempting to delete the signature...");
					DeleteItemOutcome delOutcome = table.deleteItem(delSpec);
					logger.info("Deleted signature from DynamoDB");
					logger.info(delOutcome.getDeleteItemResult().toString());
					return true;
				} catch (Exception dex) {
					logger.error("DeleteItem failed: ", dex);
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("GetItem failed: ", e);
			return false;
		}
	}
	
	private Table getSignaturesTable() {
		String DDB_REGION = config.getAwsRegionName();
		String SIGNATURES_TABLE_NAME = config.getFidoSignatureTable();
		AmazonDynamoDB ddb = AmazonDynamoDBClient.builder().withRegion(DDB_REGION).build();
		DynamoDB dynamoDB = new DynamoDB(ddb);
		Table signaturesTable = dynamoDB.getTable(SIGNATURES_TABLE_NAME);
		return signaturesTable;
	}
}