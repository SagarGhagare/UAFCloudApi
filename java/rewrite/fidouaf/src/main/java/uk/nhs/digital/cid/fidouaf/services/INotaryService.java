package uk.nhs.digital.cid.fidouaf.services;

public interface INotaryService {

	public String sign(String dataToSign);

	public boolean verify(String dataToSign, String signature);
	
	public void rotateSecret();
}
