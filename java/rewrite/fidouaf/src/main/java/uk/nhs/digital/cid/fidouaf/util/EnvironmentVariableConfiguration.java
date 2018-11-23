package uk.nhs.digital.cid.fidouaf.util;

import java.util.Map;

import com.amazonaws.util.StringUtils;

import uk.nhs.digital.cid.fidouaf.logging.Level;

public class EnvironmentVariableConfiguration implements Configuration {

	private final String AWS_REGION_NAME;
	private final String FIDO_REGISTRATIONS_TABLE;
	private String LOG_LEVEL;
	private final String SIGNATURES_TABLE_NAME;
	private final String FIDO_EXPIRY_MSECS;
	private final String SECRET_KEY_NAME;
	private final String UAF_BASE_URL;

	private final Map<String, String> vars;

	public EnvironmentVariableConfiguration() {

		vars = System.getenv();

		AWS_REGION_NAME = vars.get("AWS_REGION_NAME");
		LOG_LEVEL = vars.get("LOG_LEVEL");
		FIDO_REGISTRATIONS_TABLE = vars.get("FIDO_REGISTRATIONS_TABLE");
		SIGNATURES_TABLE_NAME = vars.get("SIGNATURES_TABLE_NAME");
		FIDO_EXPIRY_MSECS = vars.get("FIDO_EXPIRY_MSECS");
		SECRET_KEY_NAME = vars.get("SECRET_KEY_NAME");
		UAF_BASE_URL = vars.get("UAF_BASE_URL");

		if (StringUtils.isNullOrEmpty(LOG_LEVEL)) {
			LOG_LEVEL = "DEBUG";
		}
	}

	private String getVar(String name) {

		if (!vars.containsKey(name)) {
			throw new RuntimeException("Missing environment variable " + name);
		}

		return vars.get(name);
	}

	public String getAwsRegionName() {
		return AWS_REGION_NAME;
	}

	@Override
	public Level getLogLevel() {
		return Level.valueOf(this.LOG_LEVEL);
	}
	
	public String getFidoRegistrationsTable() {
		return FIDO_REGISTRATIONS_TABLE;
	}
	
	public String getFidoSignatureTable() {
		return SIGNATURES_TABLE_NAME;
	}
	
	public String getFidoSecretKeyName() {
		return SECRET_KEY_NAME;
	}
	
	public String getFidoExpiry() {
		return FIDO_EXPIRY_MSECS;
	}

	@Override
	public String getUafBaseUrl() {
		return null;
	}

}