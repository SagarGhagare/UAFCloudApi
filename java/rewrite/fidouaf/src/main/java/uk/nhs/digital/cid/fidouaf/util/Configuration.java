package uk.nhs.digital.cid.fidouaf.util;

import uk.nhs.digital.cid.fidouaf.logging.Level;

public interface Configuration {

    String getAwsRegionName();

    String get_dynamoEndpoint();

    String get_stage();

    String get_cognitoAdminUserPoolId();

    String get_cognitoAdminClientId();

    String get_cognitoSmartcardUserPassword();
    
    String get_ssoJwtIssuer();
    
    String getAllowedOrigin();
    
    Level getLogLevel();
    
    String getFidoRegistrationsTable();
    
    public String getFidoSignatureTable();
	
	public String getFidoSecretKeyName();
	
	public String getFidoExpiry();

}
