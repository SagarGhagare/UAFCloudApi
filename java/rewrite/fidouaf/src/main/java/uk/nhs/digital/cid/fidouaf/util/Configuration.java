package uk.nhs.digital.cid.fidouaf.util;

import uk.nhs.digital.cid.fidouaf.logging.Level;

public interface Configuration {

    String getAwsRegionName();
    
    Level getLogLevel();
    
    String getFidoRegistrationsTable();
    
    String getFidoSignatureTable();

	String getFidoSecretKeyName();

	String getFidoExpiry();

	String getUafBaseUrl();

}
