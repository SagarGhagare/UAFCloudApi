package uk.nhs.digital.cid.fidouaf.services;

public interface ISecretHelper {

	public void updateSecrets(String secretName);

	public String getCurrent(String secretName);

	public String getPrevious(String secretName);
}
