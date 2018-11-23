package uk.nhs.digital.cid.fidouaf.services;

import java.util.HashMap;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.google.inject.Inject;

import uk.nhs.digital.cid.fidouaf.logging.Logger;
import uk.nhs.digital.cid.fidouaf.util.Configuration;

public class SecretHelper implements ISecretHelper {

	private Logger logger;
	private Configuration config;

	private static final String VERSIONSTAGECURRENT = "AWSCURRENT";
	private static final String VERSIONSTAGEPREVIOUS = "AWSPREVIOUS";
	private static HashMap<String, String> currentSecret = new HashMap<String, String>();
	private static HashMap<String, String> previousSecret = new HashMap<String, String>();

	@Inject
	public SecretHelper(Configuration config, Logger logger) {
		this.config = config;
		this.logger = logger;
	}

	public void updateSecrets(String secretName) {
		currentSecret.put(secretName, getSecret(secretName, VERSIONSTAGECURRENT));
		previousSecret.put(secretName, getSecret(secretName, VERSIONSTAGEPREVIOUS));
	}

	public String getCurrent(String secretName) {
		secretName = config.getFidoSecretKeyName();
		logger.info("entered getCurrent for key "+ secretName);
		if (!currentSecret.containsKey(secretName)) {
			logger.info("currentSecret is null for key " + secretName + ", so updating");
			updateSecrets(secretName);
		}
		return currentSecret.get(secretName);
	}

	public String getPrevious(String secretName) {
		logger.info("entered getPrevious for key "+ secretName);
		if (!previousSecret.containsKey(secretName)) {
			logger.info("previousSecret is null for key " + secretName + ", so updating");
			updateSecrets(secretName);
		}
		return previousSecret.get(secretName);
	}

	private String getSecret(String secretName, String versionStage) {
		logger.info("entered getSecret for secretName " + secretName + " and versionStage " + versionStage);
		// Create a Secrets Manager client
		AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(config.getAwsRegionName())
				.build();

		// In this sample we only handle the specific exceptions for the
		// 'GetSecretValue' API.
		// See
		// https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
		// We rethrow the exception by default.

		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)
				.withVersionStage(versionStage);
		GetSecretValueResult getSecretValueResult = null;

		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		} catch (DecryptionFailureException e) {
			// Secrets Manager can't decrypt the protected secret text using the provided
			// KMS key.
			// Deal with the exception here, and/or rethrow at your discretion.
			logger.error("Caught DecryptionFailureException ", e);
		} catch (InternalServiceErrorException e) {
			// An error occurred on the server side.
			// Deal with the exception here, and/or rethrow at your discretion.
			logger.error("Caught InternalServiceErrorException ", e);
		} catch (InvalidParameterException e) {
			// You provided an invalid value for a parameter.
			// Deal with the exception here, and/or rethrow at your discretion.
			logger.error("Caught InvalidParameterException ", e);
		} catch (InvalidRequestException e) {
			// You provided a parameter value that is not valid for the current state of the
			// resource.
			// Deal with the exception here, and/or rethrow at your discretion.
			logger.error("Caught InvalidRequestException ", e);
		} catch (ResourceNotFoundException e) {
			// We can't find the resource that you asked for.
			// Deal with the exception here, and/or rethrow at your discretion.
			logger.error("Caught ResourceNotFoundException ", e);
		}

		// Decrypts secret using the associated KMS CMK.
		// Depending on whether the secret is a string or binary, one of these fields
		// will be populated.
		if (getSecretValueResult != null) {
			if (getSecretValueResult.getSecretString() != null) {
				logger.info("The secretValueResult is ", getSecretValueResult);
				return processSecretValueResultString(getSecretValueResult.getSecretString());
			} else {
				logger.error("Failed to retrieve secret for secretName " + secretName + " and versionStage "
						+ versionStage + " as string value was null");
				return new String();
			}
		} else {
			logger.error("Failed to retrieve secret for secretName " + secretName + " and versionStage " + versionStage);
			return new String();
		}
	}

	private String processSecretValueResultString(String secretValueResultString) {
		logger.info("Entered processSecretValueResultString with secretValueResultString "+ secretValueResultString);
		// string is in format {"[[[secretName]]]":"[[[secretValue]]]"} - this function
		// returns [[[secretValue]]]
		try {
			int colonPosition = secretValueResultString.indexOf(":");
			int firstQuotePosition = secretValueResultString.indexOf("\"", colonPosition);
			int lastQuotePosition = secretValueResultString.lastIndexOf("\"");
			String secretValue = secretValueResultString.substring(firstQuotePosition + 1, lastQuotePosition);
			return secretValue;
		} catch (Exception e) {
			logger.error("Caught error processing secret value result string ", e);
			return new String();
		}
	}
}
