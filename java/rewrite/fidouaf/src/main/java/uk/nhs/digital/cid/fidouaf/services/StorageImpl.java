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

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.fido.uaf.storage.DuplicateKeyException;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;
import org.ebayopensource.fido.uaf.storage.StorageInterface;
import org.ebayopensource.fido.uaf.storage.SystemErrorException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.Configuration;

public class StorageImpl implements StorageInterface {

	private Configuration config;
	private Logger logger;

	private Map<String, RegistrationRecord> db = new HashMap<String, RegistrationRecord>();
	private Map<String, String> db_names = new HashMap<String, String>();

	protected Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	@Inject
	public StorageImpl(Configuration config, Logger logger) {
		this.config = config;
		this.logger = logger;
	}

	public void storeServerDataString(String username, String serverDataString) {
		logger.info("Entered storeServerDataString with username " + username + " and serverDataString "
				+ serverDataString);
		if (db_names.containsKey(serverDataString)) {
			db_names.remove(serverDataString);
		}
		db_names.put(serverDataString, username);
	}

	public String getUsername(String serverDataString) {
		logger.info("Entered getUsername with serverDataString ", serverDataString);
		if (db_names.containsKey(serverDataString)) {
			return db_names.get(serverDataString);
		}
		return null;
	}

	public void store(RegistrationRecord[] records) throws DuplicateKeyException, SystemErrorException {
		logger.info("store received records ", records);
		if (records != null && records.length > 0) {
			for (int i = 0; i < records.length; i++) {
				if (db.containsKey(records[i].authenticator.toString())) {
					throw new DuplicateKeyException();
				}
				records[i].authenticator.username = records[i].username;
				db.put(records[i].authenticator.toString(), records[i]);
			}
			storeAWS(records);
		}
		logger.info("stored records into inmemory db ", db);
	}

	private void storeAWS(RegistrationRecord[] records) {
		logger.info("Entered storeAWS to store ... " + records.length + " items");
		// registrationsTable
		Table table = getRegistrationsTable();
		if (records != null && records.length > 0) {
			for (int i = 0; i < records.length; i++) {
				// check duplicate key
				// if (db.containsKey(records[i].authenticator.toString())) {
				// throw new DuplicateKeyException();
				// }
				records[i].authenticator.username = records[i].username;
				Item regItem = new Item().withPrimaryKey("authenticator_string", records[i].authenticator.toString())
						.withString("record", gson.toJson(records[i]));
				PutItemSpec putSpec = new PutItemSpec().withItem(regItem);
				table.putItem(putSpec);
				logger.info("Successfull put item ... " + i + " with key " + records[i].authenticator.toString());
			}

		}
	}

	public RegistrationRecord readRegistrationRecord(String key) {
		logger.info("Got request for Registration Record with key ", key);
		RegistrationRecord rr = db.get(key);
		rr = readRegistrationRecordAWS(key);
		if (rr != null) {
			logger.info("Registration Record username details are ", rr.username);
		}
		return rr;
	}

	private RegistrationRecord readRegistrationRecordAWS(String key) {
		logger.info("Entered readRegistrationRecordAWS with key ", key);
		Table table = getRegistrationsTable();
		GetItemSpec spec = new GetItemSpec().withPrimaryKey("authenticator_string", key);
		try {
			logger.info("Attempting to read the item with key... ", key);
			logger.info("Attempting to read the item...");
			Item outcome = table.getItem(spec);
			logger.info("GetItem succeeded: " + outcome.toJSONPretty());
			return gson.fromJson(outcome.getString("record"), RegistrationRecord.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public void update(RegistrationRecord[] records) {
		// TODO Auto-generated method stub
	}

	public void deleteRegistrationRecord(String key) {
		logger.info("deleteRegistrationRecord key ", key);
		logger.info("deleteRegistrationRecord db ", db);
		if (db != null && db.containsKey(key)) {
			logger.info("Deleting object associated with key=", key);
			db.remove(key);
		}
		deleteRegistrationRecordAWS(key);
	}

	private void deleteRegistrationRecordAWS(String key) {
		logger.info("deleteRegistrationRecordAWS key ", key);
		Table table = getRegistrationsTable();
		DeleteItemSpec spec = new DeleteItemSpec().withPrimaryKey("authenticator_string", key);
		try {
			// logger.log("Attempting to read the item...");
			DeleteItemOutcome outcome = table.deleteItem(spec);
			logger.info("Deleted item from DynamoDB with key ", key);
			logger.info("GetItem succeeded: ", outcome);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Map<String, RegistrationRecord> dbDump() {
		logger.info("Entered dbDump");
		// TODO - return from DynamoDB
		return db;
	}
	
	private Table getRegistrationsTable() {
		AmazonDynamoDB ddb = AmazonDynamoDBClient.builder().withRegion(config.getAwsRegionName()).build();
		DynamoDB dynamoDB = new DynamoDB(ddb);
		Table registrationsTable = dynamoDB.getTable(config.getFidoRegistrationsTable());
		return registrationsTable;
	}
}
